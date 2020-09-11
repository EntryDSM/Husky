package kr.hs.entrydsm.husky.domain.auth.exceptions;

import kr.hs.entrydsm.husky.global.error.exception.BusinessException;
import kr.hs.entrydsm.husky.global.error.exception.ErrorCode;

public class InvalidAuthEmailException extends BusinessException {
    public InvalidAuthEmailException() {
        super(ErrorCode.INVALID_AUTH_EMAIL);
    }
}
