package kr.hs.entrydsm.husky.error.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    //Global
    USER_NOT_FOUND(404, "", "User Not Found."),

    //Auth
    INVALID_AUTH_EMAIL(400, "VE1010", "Invalid Auth Email"),
    INVALID_AUTH_CODE(400, "VE1013", "Invalid Auth Code"),
    EXPIRED_AUTH_CODE(400, "VE1012", "Expired Auth Code"),
    INVALID_TOKEN(401, "", "Invalid Token"),
    EXPIRED_TOKEN(401, "", "Expired Token"),
    UNAUTHORIZED(401, "", "Authentication is required and has failed or has not yet been provided."),
    USER_DUPLICATION(409, "VE1011", "User is Already Exists"),
    PASSWORD_DUPLICATION(409, "", "Password is Already Exists"),

    //Info
    NOT_ADMIN_FORBIDDEN(403, "", "The server understood the request but refuses to authorize it.");

    private final int status;
    private final String code;
    private final String message;

}
