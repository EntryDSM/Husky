package kr.hs.entrydsm.husky.global.error.exception;

public class BadRequestException extends BusinessException {
    public BadRequestException() { super(ErrorCode.BAD_REQUEST); }
}
