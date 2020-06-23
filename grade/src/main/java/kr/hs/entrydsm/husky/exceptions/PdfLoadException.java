package kr.hs.entrydsm.husky.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Fail To Load Pdf")
public class PdfLoadException extends RuntimeException {
    public PdfLoadException() {}
}
