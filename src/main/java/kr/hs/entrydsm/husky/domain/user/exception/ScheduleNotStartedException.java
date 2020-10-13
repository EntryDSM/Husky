package kr.hs.entrydsm.husky.domain.user.exception;

import kr.hs.entrydsm.husky.global.error.exception.BusinessException;
import kr.hs.entrydsm.husky.global.error.exception.ErrorCode;

public class ScheduleNotStartedException extends BusinessException {
    public ScheduleNotStartedException() { super(ErrorCode.SCHEDULE_NOT_STARTED); }
}
