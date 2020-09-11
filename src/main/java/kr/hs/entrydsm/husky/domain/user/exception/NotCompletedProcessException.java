package kr.hs.entrydsm.husky.domain.user.exception;

import kr.hs.entrydsm.husky.global.error.exception.BusinessException;
import kr.hs.entrydsm.husky.global.error.exception.ErrorCode;

public class NotCompletedProcessException extends BusinessException {
    public NotCompletedProcessException() {
        super(ErrorCode.NOT_COMPLETED_PROCESS);
    }
}