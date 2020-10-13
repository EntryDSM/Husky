package kr.hs.entrydsm.husky.domain.user.exception;

import kr.hs.entrydsm.husky.global.error.exception.BusinessException;
import kr.hs.entrydsm.husky.global.error.exception.ErrorCode;

public class NotStartedScheduleException extends BusinessException {
    public NotStartedScheduleException() { super(ErrorCode.NOT_STARTED_SCHEDULE); }
}
