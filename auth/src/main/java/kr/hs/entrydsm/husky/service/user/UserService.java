package kr.hs.entrydsm.husky.service.user;

import kr.hs.entrydsm.husky.domains.request.AccountRequest;
import kr.hs.entrydsm.husky.domains.request.VerifyCodeRequest;
import kr.hs.entrydsm.husky.domains.request.ChangePasswordRequest;

public interface UserService {
    void signUp(AccountRequest accountRequest);
    void sendEmail(String email);
    void authEmail(VerifyCodeRequest verifyCodeRequest);
    void changePassword(String token, ChangePasswordRequest changePasswordRequest);
}
