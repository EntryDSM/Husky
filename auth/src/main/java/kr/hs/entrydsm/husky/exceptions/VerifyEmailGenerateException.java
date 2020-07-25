package kr.hs.entrydsm.husky.exceptions;

import kr.hs.entrydsm.husky.error.exception.BusinessException;
import kr.hs.entrydsm.husky.error.exception.ErrorCode;

public class VerifyEmailGenerateException extends BusinessException {
    public VerifyEmailGenerateException() {
        super(ErrorCode.FAIL_GENERATE_VERIFY_EMAIL);
    }
}
