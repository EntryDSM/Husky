package kr.hs.entrydsm.husky.service.user;

import kr.hs.entrydsm.husky.domains.request.AccountRequest;
import kr.hs.entrydsm.husky.domains.request.AuthCodeRequest;
import kr.hs.entrydsm.husky.domains.request.PasswordRequest;
import kr.hs.entrydsm.husky.entities.redis.AuthEmail;
import kr.hs.entrydsm.husky.entities.redis.RedisRepository;
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
    private final RedisRepository redisRepository;

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
        redisRepository.save(
                AuthEmail.builder()
                .email(email)
                .authCode(code)
                .status("UnAuthorized")
                .build()
        );
        // 3분뒤 만료 상태로 변경 해야함. 근데 어떤 방식을 사용해야 잘했다 소문이 날지 모르겠음
    }

    @Override
    public void authEmail(AuthCodeRequest authCodeRequest) {
        String email = authCodeRequest.getEmail();
        String code = authCodeRequest.getAuthCode();
        AuthEmail authEmail = redisRepository.findById(email).orElseThrow(InvalidAuthEmailException::new);

        if (!authEmail.getAuthCode().equals(code)) throw new InvalidAuthCodeException();
        if (authEmail.getStatus().equals("Expired")) throw new ExpiredAuthCodeException();

        authEmail.setStatus("Authorized");
        redisRepository.save(authEmail);
    }

    @Override
    public void changePassword(String token, String userId, PasswordRequest passwordRequest) {
        User user = userRepository.findById(tokenService.parseToken(token)).orElseThrow(UserNotFoundException::new);
        if (!user.getEmail().equals(userId)) throw new PermissionDeniedException();
        if (passwordEncoder.matches(passwordRequest.getPassword(), user.getPassword())) throw new PasswordSameException();

        user.setPassword(passwordEncoder.encode(passwordRequest.getPassword()));
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
