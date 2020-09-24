package kr.hs.entrydsm.husky.domain.auth.service.user;

import kr.hs.entrydsm.husky.domain.auth.dto.request.AccountRequest;
import kr.hs.entrydsm.husky.domain.auth.dto.request.ChangePasswordRequest;
import kr.hs.entrydsm.husky.domain.auth.dto.request.EmailRequest;
import kr.hs.entrydsm.husky.domain.auth.dto.request.VerifyCodeRequest;

public interface UserService {
    void signUp(AccountRequest accountRequest);
    void sendSignUpEmail(EmailRequest emailRequest);
    void authEmail(VerifyCodeRequest verifyCodeRequest);
    void sendPasswordChangeEmail(EmailRequest emailRequest);
    void changePassword(ChangePasswordRequest changePasswordRequest);
}
