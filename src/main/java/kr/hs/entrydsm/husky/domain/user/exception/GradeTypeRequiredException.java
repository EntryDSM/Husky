package kr.hs.entrydsm.husky.domain.user.exception;

import kr.hs.entrydsm.husky.global.error.exception.BusinessException;
import kr.hs.entrydsm.husky.global.error.exception.ErrorCode;

public class GradeTypeRequiredException extends BusinessException {
    public GradeTypeRequiredException() {
        super(ErrorCode.GRADE_TYPE_REQUIRED);
    }
}
