package kr.hs.entrydsm.husky.exceptions;

import kr.hs.entrydsm.husky.error.exception.BusinessException;
import kr.hs.entrydsm.husky.error.exception.ErrorCode;

public class UserAlreadyExistsException extends BusinessException {
    public UserAlreadyExistsException() {
        super(ErrorCode.USER_DUPLICATION);
    }
}
