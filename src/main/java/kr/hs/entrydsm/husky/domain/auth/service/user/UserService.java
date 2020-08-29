package kr.hs.entrydsm.husky.domain.auth.service.user;

import kr.hs.entrydsm.husky.domain.auth.dto.request.AccountRequest;
import kr.hs.entrydsm.husky.domain.auth.dto.request.ChangePasswordRequest;
import kr.hs.entrydsm.husky.domain.auth.dto.request.VerifyCodeRequest;

public interface UserService {
    void signUp(AccountRequest accountRequest);
    void sendSignUpEmail(String email);
    void authEmail(VerifyCodeRequest verifyCodeRequest);
    void sendPasswordChangeEmail(String email);
    void changePassword(ChangePasswordRequest changePasswordRequest);
}
