package kr.hs.entrydsm.husky.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "VE1010")
public class InvalidAuthEmailException extends RuntimeException {
    public InvalidAuthEmailException() {
        super();
    }
}
