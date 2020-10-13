package kr.hs.entrydsm.husky.domain.user.exception;

import kr.hs.entrydsm.husky.global.error.exception.BusinessException;
import kr.hs.entrydsm.husky.global.error.exception.ErrorCode;

public class ScheduleNotFoundException extends BusinessException {
    public ScheduleNotFoundException() { super(ErrorCode.SCHEDULE_NOT_FOUND); }
}
