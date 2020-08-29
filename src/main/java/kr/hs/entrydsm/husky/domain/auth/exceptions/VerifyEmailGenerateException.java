package kr.hs.entrydsm.husky.domain.auth.exceptions;

import kr.hs.entrydsm.husky.global.error.exception.BusinessException;
import kr.hs.entrydsm.husky.global.error.exception.ErrorCode;

public class VerifyEmailGenerateException extends BusinessException {
    public VerifyEmailGenerateException() {
        super(ErrorCode.FAIL_GENERATE_VERIFY_EMAIL);
    }
}
