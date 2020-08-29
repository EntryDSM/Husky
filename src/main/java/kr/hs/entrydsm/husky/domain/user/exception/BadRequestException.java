package kr.hs.entrydsm.husky.domain.user.exception;

import kr.hs.entrydsm.husky.global.error.exception.BusinessException;
import kr.hs.entrydsm.husky.global.error.exception.ErrorCode;

public class BadRequestException extends BusinessException {

    public BadRequestException() { super(ErrorCode.BAD_REQUEST); }
}
