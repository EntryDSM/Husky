package kr.hs.entrydsm.husky.service.user;

import kr.hs.entrydsm.husky.domains.request.AuthCodeRequest;
import kr.hs.entrydsm.husky.domains.request.PasswordRequest;
import kr.hs.entrydsm.husky.domains.request.SignUpRequest;
import kr.hs.entrydsm.husky.entities.users.repositories.UserRepository;
import kr.hs.entrydsm.husky.service.email.EmailServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final EmailServiceImpl emailService;

    @Override
    public void signUp(SignUpRequest signUpRequest) {

    }

    @Override
    public void sendEmail(String email) {
        emailService.sendEmail(email);
    }

    @Override
    public void authEmail(AuthCodeRequest authCodeRequest) {

    }

    @Override
    public void changePassword(String token, Integer userId, PasswordRequest passwordRequest) {

    }

}
