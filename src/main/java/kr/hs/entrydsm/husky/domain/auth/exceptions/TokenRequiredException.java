package kr.hs.entrydsm.husky.domain.auth.exceptions;

import kr.hs.entrydsm.husky.global.error.exception.BusinessException;
import kr.hs.entrydsm.husky.global.error.exception.ErrorCode;

public class TokenRequiredException  extends BusinessException {
    public TokenRequiredException() {
        super(ErrorCode.TOKEN_REQUIRED);
    }
}
