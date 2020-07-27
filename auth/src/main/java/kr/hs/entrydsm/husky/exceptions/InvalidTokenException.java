package kr.hs.entrydsm.husky.exceptions;

import kr.hs.entrydsm.husky.error.exception.BusinessException;
import kr.hs.entrydsm.husky.error.exception.ErrorCode;

public class InvalidTokenException extends BusinessException {
    public InvalidTokenException() {
        super(ErrorCode.INVALID_TOKEN);
    }
}
