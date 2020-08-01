package kr.hs.entrydsm.husky.domain.application.exception;

import kr.hs.entrydsm.husky.error.exception.BusinessException;
import kr.hs.entrydsm.husky.error.exception.ErrorCode;

public class NotCreateApplication extends BusinessException {
    public NotCreateApplication() { super(ErrorCode.NOT_CREATE_APPLICATION); }
}
