package kr.hs.entrydsm.husky.domain.school.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public class AppError {

    HttpStatus status;
    String message;

}
