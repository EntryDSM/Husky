package kr.hs.entrydsm.husky.domain.user.exception;

import kr.hs.entrydsm.husky.error.exception.BusinessException;
import kr.hs.entrydsm.husky.error.exception.ErrorCode;

public class BadRequestException extends BusinessException {

    public BadRequestException() { super(ErrorCode.BAD_REQUEST); }
}
