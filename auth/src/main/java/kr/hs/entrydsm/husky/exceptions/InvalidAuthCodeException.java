package kr.hs.entrydsm.husky.exceptions;

import kr.hs.entrydsm.husky.error.exception.BusinessException;
import kr.hs.entrydsm.husky.error.exception.ErrorCode;

public class InvalidAuthCodeException extends BusinessException {
    public InvalidAuthCodeException() {
        super(ErrorCode.INVALID_AUTH_CODE);
    }
}
