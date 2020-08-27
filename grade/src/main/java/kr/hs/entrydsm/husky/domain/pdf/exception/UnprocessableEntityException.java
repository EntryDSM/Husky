package kr.hs.entrydsm.husky.domain.pdf.exception;

import kr.hs.entrydsm.husky.error.exception.BusinessException;
import kr.hs.entrydsm.husky.error.exception.ErrorCode;

public class UnprocessableEntityException extends BusinessException {
    public UnprocessableEntityException() { super(ErrorCode.UNPROCESSABLE_APPLICATION); }
}
