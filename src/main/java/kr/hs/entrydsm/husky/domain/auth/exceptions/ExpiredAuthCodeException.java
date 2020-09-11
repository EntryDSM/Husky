package kr.hs.entrydsm.husky.domain.auth.exceptions;

import kr.hs.entrydsm.husky.global.error.exception.BusinessException;
import kr.hs.entrydsm.husky.global.error.exception.ErrorCode;

public class ExpiredAuthCodeException extends BusinessException {
    public ExpiredAuthCodeException() {
        super(ErrorCode.EXPIRED_AUTH_CODE);
    }
}
