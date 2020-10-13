package kr.hs.entrydsm.husky.domain.user.exception;

import kr.hs.entrydsm.husky.global.error.exception.BusinessException;
import kr.hs.entrydsm.husky.global.error.exception.ErrorCode;

public class ProcessNotCompletedException extends BusinessException {
    public ProcessNotCompletedException() {
        super(ErrorCode.PROCESS_NOT_COMPLETED);
    }
}
