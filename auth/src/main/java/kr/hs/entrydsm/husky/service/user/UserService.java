package kr.hs.entrydsm.husky.service.user;

import kr.hs.entrydsm.husky.domains.request.AccountRequest;
import kr.hs.entrydsm.husky.domains.request.AuthCodeRequest;
import kr.hs.entrydsm.husky.domains.request.ChangePasswordRequest;

public interface UserService {
    void signUp(AccountRequest accountRequest);
    void sendEmail(String email);
    void authEmail(AuthCodeRequest authCodeRequest);
    void changePassword(String token, ChangePasswordRequest changePasswordRequest);
}
