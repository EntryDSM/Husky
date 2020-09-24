package kr.hs.entrydsm.husky.domain.auth.exceptions;

import kr.hs.entrydsm.husky.global.error.exception.BusinessException;
import kr.hs.entrydsm.husky.global.error.exception.ErrorCode;

public class TooManyEmailRequestException extends BusinessException {
    public TooManyEmailRequestException() {
        super(ErrorCode.TOO_MANY_REQUEST);
    }
}
