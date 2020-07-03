package kr.hs.entrydsm.husky.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY, reason = "email create error")
public class VerifyEmailGenerateException extends RuntimeException {
}
