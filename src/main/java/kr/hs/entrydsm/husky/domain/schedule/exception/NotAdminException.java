package kr.hs.entrydsm.husky.domain.schedule.exception;

import kr.hs.entrydsm.husky.global.error.exception.BusinessException;
import kr.hs.entrydsm.husky.global.error.exception.ErrorCode;

public class NotAdminException extends BusinessException {
    public NotAdminException() { super(ErrorCode.NOT_ADMIN_FORBIDDEN); }
}
