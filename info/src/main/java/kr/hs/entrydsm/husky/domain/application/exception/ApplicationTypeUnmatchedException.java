package kr.hs.entrydsm.husky.domain.application.exception;

import kr.hs.entrydsm.husky.error.exception.BusinessException;
import kr.hs.entrydsm.husky.error.exception.ErrorCode;

public class ApplicationTypeUnmatchedException extends BusinessException {
    public ApplicationTypeUnmatchedException() { super(ErrorCode.APPLICATION_TYPE_UNMATCHED); }
}
