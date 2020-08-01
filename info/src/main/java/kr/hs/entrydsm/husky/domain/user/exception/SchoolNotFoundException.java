package kr.hs.entrydsm.husky.domain.user.exception;

import kr.hs.entrydsm.husky.error.exception.BusinessException;
import kr.hs.entrydsm.husky.error.exception.ErrorCode;

public class SchoolNotFoundException extends BusinessException {
    public SchoolNotFoundException() { super(ErrorCode.SCHOOL_NOT_FOUND); }
}
