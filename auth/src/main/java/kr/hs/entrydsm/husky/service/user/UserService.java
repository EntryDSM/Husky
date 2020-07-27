package kr.hs.entrydsm.husky.service.user;

import kr.hs.entrydsm.husky.domains.request.AccountRequest;
import kr.hs.entrydsm.husky.domains.request.VerifyCodeRequest;
import kr.hs.entrydsm.husky.domains.request.ChangePasswordRequest;

public interface UserService {
    void signUp(AccountRequest accountRequest);
    void sendSignUpEmail(String email);
    void authEmail(VerifyCodeRequest verifyCodeRequest);
    void sendPasswordChangeEmail(String email);
    void changePassword(ChangePasswordRequest changePasswordRequest);
}
