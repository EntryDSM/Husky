package kr.hs.entrydsm.husky.domain.pdf.exception;

import kr.hs.entrydsm.husky.error.exception.BusinessException;
import kr.hs.entrydsm.husky.error.exception.ErrorCode;

public class FinalSubmitRequiredException extends BusinessException {
    public FinalSubmitRequiredException() { super(ErrorCode.FINAL_SUBMIT_REQUIRED); }
}
