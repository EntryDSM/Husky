package kr.hs.entrydsm.husky.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "VE1012")
public class ExpiredAuthCodeException extends RuntimeException {
    public ExpiredAuthCodeException() {
        super();
    }
}
