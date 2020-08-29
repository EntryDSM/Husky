package kr.hs.entrydsm.husky.domain.user.exception;

import kr.hs.entrydsm.husky.global.error.exception.BusinessException;
import kr.hs.entrydsm.husky.global.error.exception.ErrorCode;

public class UserNotFoundException extends BusinessException {
    public UserNotFoundException() { super(ErrorCode.USER_NOT_FOUND); }
}
