package kr.hs.entrydsm.husky.domain.auth.service.user;

import kr.hs.entrydsm.husky.domain.auth.domain.emaillimit.EmailLimiter;
import kr.hs.entrydsm.husky.domain.auth.domain.emaillimit.EmailLimiterRepository;
import kr.hs.entrydsm.husky.domain.auth.dto.request.AccountRequest;
import kr.hs.entrydsm.husky.domain.auth.dto.request.ChangePasswordRequest;
import kr.hs.entrydsm.husky.domain.auth.dto.request.EmailRequest;
import kr.hs.entrydsm.husky.domain.auth.dto.request.VerifyCodeRequest;
import kr.hs.entrydsm.husky.domain.auth.domain.verification.EmailVerification;
import kr.hs.entrydsm.husky.domain.auth.domain.verification.EmailVerificationRepository;
import kr.hs.entrydsm.husky.domain.auth.domain.verification.EmailVerificationStatus;
import kr.hs.entrydsm.husky.domain.auth.exceptions.*;
import kr.hs.entrydsm.husky.domain.user.domain.User;
import kr.hs.entrydsm.husky.domain.user.domain.repositories.UserRepository;
import kr.hs.entrydsm.husky.global.error.exception.UserNotFoundException;
import kr.hs.entrydsm.husky.global.config.security.AuthenticationFacade;
import kr.hs.entrydsm.husky.domain.auth.service.email.EmailService;
import kr.hs.entrydsm.husky.global.config.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final EmailVerificationRepository emailVerificationRepository;
    private final EmailLimiterRepository emailLimiterRepository;

    private final EmailService emailService;
    private final JwtTokenProvider jwtTokenProvider;

    private final AuthenticationFacade authenticationFacade;
    private final PasswordEncoder passwordEncoder;

    @Value("${auth.email.limit}")
    private int limit;

    @Override
    public void signUp(AccountRequest accountRequest) {
        String email = accountRequest.getEmail();
        String password = passwordEncoder.encode(accountRequest.getPassword());

        emailVerificationRepository.findById(email)
                .filter(EmailVerification::isVerified)
                .orElseThrow(ExpiredAuthCodeException::new);

        userRepository.save(
            User.builder()
                .email(email)
                .password(password)
                .build()
        );
    }

    @Override
    public void sendSignUpEmail(EmailRequest emailRequest) {
        String email = emailRequest.getEmail();
        userRepository.findByEmail(email)
                .ifPresent(user -> {
                    throw new UserAlreadyExistsException();
                });

        if (!isUnderRequestLimit(email))
            throw new TooManyEmailRequestException();

        String code = randomCode();
        emailService.sendEmail(email, code);
        emailVerificationRepository.save(
            EmailVerification.builder()
                .email(email)
                .authCode(code)
                .status(EmailVerificationStatus.UNVERIFIED)
                .build()
        );
    }

    @Override
    public void authEmail(VerifyCodeRequest verifyCodeRequest) {
        String email = verifyCodeRequest.getEmail();
        String code = verifyCodeRequest.getAuthCode();
        EmailVerification emailVerification = emailVerificationRepository.findById(email)
                .orElseThrow(InvalidAuthEmailException::new);

        if (!emailVerification.getAuthCode().equals(code))
            throw new InvalidAuthCodeException();

        emailVerification.verify();
        emailVerificationRepository.save(emailVerification);
    }

    @Override
    public void sendPasswordChangeEmail(EmailRequest emailRequest) {
        String email = emailRequest.getEmail();
        userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);

        String code = randomCode();
        emailService.sendPasswordChangeEmail(email, code);
        emailVerificationRepository.save(
                EmailVerification.builder()
                        .email(email)
                        .authCode(code)
                        .status(EmailVerificationStatus.UNVERIFIED)
                        .build()
        );
    }

    @Override
    public void changePassword(ChangePasswordRequest changePasswordRequest) {
        String email = changePasswordRequest.getEmail();
        String password = passwordEncoder.encode(changePasswordRequest.getPassword());

        emailVerificationRepository.findById(email)
                .filter(EmailVerification::isVerified)
                .orElseThrow(ExpiredAuthCodeException::new);

        User user = userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);

        user.setPassword(password);
        userRepository.save(user);
    }

    private String randomCode() {
        StringBuilder result = new StringBuilder();
        String[] codes = "QWERTYUIOPASDFGHJKLZXCVBNM0123456789".split("");
        for (int i = 0; i < 6; i++) {
            result.append(codes[new Random().nextInt(codes.length)]);
        }
        return result.toString();
    }

    private boolean isUnderRequestLimit(String email) {
        return emailLimiterRepository.findById(email)
                .or(() -> Optional.of(new EmailLimiter(email, 0L)))
                .map(limiter -> emailLimiterRepository.save(limiter.update(limit)))
                .map(EmailLimiter::isBelowLimit)
                .get();
    }

}
