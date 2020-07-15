package kr.hs.entrydsm.husky.exceptions;

import kr.hs.entrydsm.husky.error.exception.BusinessException;
import kr.hs.entrydsm.husky.error.exception.ErrorCode;

public class PasswordDuplicationException extends BusinessException {
    public PasswordDuplicationException() {
        super(ErrorCode.PASSWORD_DUPLICATION);
    }
}
