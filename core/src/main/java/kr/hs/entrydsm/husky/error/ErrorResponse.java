package kr.hs.entrydsm.husky.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ErrorResponse {

    private int status;
    private String message;
    private String code;

}
