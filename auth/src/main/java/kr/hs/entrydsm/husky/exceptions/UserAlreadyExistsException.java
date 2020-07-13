package kr.hs.entrydsm.husky.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "VE1011")
public class UserAlreadyExistsException extends RuntimeException {
}
