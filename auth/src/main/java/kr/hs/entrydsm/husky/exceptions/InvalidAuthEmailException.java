package kr.hs.entrydsm.husky.exceptions;

import kr.hs.entrydsm.husky.error.exception.BusinessException;
import kr.hs.entrydsm.husky.error.exception.ErrorCode;

public class InvalidAuthEmailException extends BusinessException {
    public InvalidAuthEmailException() {
        super(ErrorCode.INVALID_AUTH_EMAIL);
    }
}
