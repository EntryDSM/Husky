package kr.hs.entrydsm.husky.exceptions;

import kr.hs.entrydsm.husky.error.exception.BusinessException;
import kr.hs.entrydsm.husky.error.exception.ErrorCode;

public class ExpiredAuthCodeException extends BusinessException {
    public ExpiredAuthCodeException() {
        super(ErrorCode.EXPIRED_AUTH_CODE);
    }
}
