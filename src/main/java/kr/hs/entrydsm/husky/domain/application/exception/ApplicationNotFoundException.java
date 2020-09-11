package kr.hs.entrydsm.husky.domain.application.exception;

import kr.hs.entrydsm.husky.global.error.exception.BusinessException;
import kr.hs.entrydsm.husky.global.error.exception.ErrorCode;

public class ApplicationNotFoundException extends BusinessException {
    public ApplicationNotFoundException() { super(ErrorCode.APPLICATION_NOT_FOUND); }
}
