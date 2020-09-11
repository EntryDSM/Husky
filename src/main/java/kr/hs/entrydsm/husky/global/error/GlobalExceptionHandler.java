package kr.hs.entrydsm.husky.global.error;

import kr.hs.entrydsm.husky.global.error.exception.BusinessException;
import kr.hs.entrydsm.husky.global.error.exception.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleValidException(final MethodArgumentNotValidException e) {
        return new ResponseEntity<>(new ErrorResponse(ErrorCode.BAD_REQUEST.getStatus(), "Invalid Parameter",
                ErrorCode.BAD_REQUEST.getCode()),
                HttpStatus.valueOf(ErrorCode.BAD_REQUEST.getStatus()));
    }

    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<ErrorResponse> handleBusinessException(final BusinessException e) {
        final ErrorCode errorCode = e.getErrorCode();
        return new ResponseEntity<>(new ErrorResponse(errorCode.getStatus(), e.getMessage(), errorCode.getCode()),
                HttpStatus.valueOf(errorCode.getStatus()));
    }

}
