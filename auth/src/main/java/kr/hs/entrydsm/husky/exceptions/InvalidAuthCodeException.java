package kr.hs.entrydsm.husky.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "VE1013")
public class InvalidAuthCodeException extends RuntimeException {
    public InvalidAuthCodeException() {
        super();
    }
}
