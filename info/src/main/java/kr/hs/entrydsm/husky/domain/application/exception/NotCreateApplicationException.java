package kr.hs.entrydsm.husky.domain.application.exception;

import kr.hs.entrydsm.husky.error.exception.BusinessException;
import kr.hs.entrydsm.husky.error.exception.ErrorCode;

public class NotCreateApplicationException extends BusinessException {
    public NotCreateApplicationException() { super(ErrorCode.NOT_CREATE_APPLICATION); }
}
