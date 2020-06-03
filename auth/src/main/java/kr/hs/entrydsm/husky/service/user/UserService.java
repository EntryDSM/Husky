package kr.hs.entrydsm.husky.service.user;

import kr.hs.entrydsm.husky.domains.request.AuthCodeRequest;
import kr.hs.entrydsm.husky.domains.request.PasswordRequest;
import kr.hs.entrydsm.husky.domains.request.SignUpRequest;

public interface UserService {
    void signUp(SignUpRequest signUpRequest);
    void sendEmail(String email);
    void authEmail(AuthCodeRequest authCodeRequest);
    void changePassword(String token, Integer userId, PasswordRequest passwordRequest);
}
