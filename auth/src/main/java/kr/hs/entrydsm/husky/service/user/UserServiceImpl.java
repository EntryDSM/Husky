package kr.hs.entrydsm.husky.service.user;

import kr.hs.entrydsm.husky.domains.request.AccountRequest;
import kr.hs.entrydsm.husky.domains.request.AuthCodeRequest;
import kr.hs.entrydsm.husky.domains.request.ChangePasswordRequest;
import kr.hs.entrydsm.husky.entities.verification.EmailVerification;
import kr.hs.entrydsm.husky.entities.verification.EmailVerificationStatus;
import kr.hs.entrydsm.husky.entities.verification.EmailVerificationRepository;
import kr.hs.entrydsm.husky.entities.users.User;
import kr.hs.entrydsm.husky.entities.users.repositories.UserRepository;
import kr.hs.entrydsm.husky.exceptions.*;
import kr.hs.entrydsm.husky.service.email.EmailServiceImpl;
import kr.hs.entrydsm.husky.service.token.TokenServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final EmailVerificationRepository emailVerificationRepository;

    private final EmailServiceImpl emailService;
    private final TokenServiceImpl tokenService;

    private final PasswordEncoder passwordEncoder;

    @Override
    public void signUp(AccountRequest accountRequest) {
        String email = accountRequest.getEmail();
        String password = passwordEncoder.encode(accountRequest.getPassword());

        userRepository.save(
                User.builder()
                .email(email)
                .password(password)
                .build()
        );
    }

    @Override
    public void sendEmail(String email) {
        userRepository.findById(email).ifPresent(user -> {
            throw new UserAlreadyExistsException();
        });

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
    public void authEmail(AuthCodeRequest authCodeRequest) {
        String email = authCodeRequest.getEmail();
        String code = authCodeRequest.getAuthCode();
        EmailVerification emailVerification = emailVerificationRepository.findById(email).orElseThrow(InvalidAuthEmailException::new);

        if (!emailVerification.getAuthCode().equals(code)) throw new InvalidAuthCodeException();
        if (!emailVerification.isVerify()) throw new ExpiredAuthCodeException();

        emailVerification.verify();
        emailVerificationRepository.save(emailVerification);
    }

    @Override
    public void changePassword(String token, ChangePasswordRequest changePasswordRequest) {
        User user = userRepository.findById(tokenService.parseToken(token)).orElseThrow(UserNotFoundException::new);

        if (passwordEncoder.matches(changePasswordRequest.getPassword(), user.getPassword())) {
            throw new PasswordSameException();
        }

        user.setPassword(passwordEncoder.encode(changePasswordRequest.getPassword()));
        userRepository.save(user);
    }

    private String randomCode() {
        StringBuilder result = new StringBuilder();
        String[] codes = "QWERTYUIOPASDFGHJKLZXCVBNM0123456789".split("");
        for (int i = 0; i < 6; i++) {
            result.append(codes[(int) (Math.random() % codes.length)]);
        }
        return result.toString();
    }

}
