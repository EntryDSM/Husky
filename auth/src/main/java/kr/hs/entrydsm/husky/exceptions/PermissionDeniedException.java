package kr.hs.entrydsm.husky.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "Permission Denied")
public class PermissionDeniedException extends RuntimeException {
    public PermissionDeniedException() {
        super();
    }
}
