package kr.hs.entrydsm.husky.error.exception;

public class BadRequestException extends BusinessException {
    public BadRequestException() { super(ErrorCode.BAD_REQUEST); }
}
