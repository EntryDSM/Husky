package kr.hs.entrydsm.husky.error.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    //Global
    USER_NOT_FOUND(404, "", "User Not Found."),
    BAD_REQUEST(400, "", "Bad Request(Invalid Parameter)"),

    //Auth
    INVALID_AUTH_EMAIL(400, "VE1010", "Invalid Auth Email"),
    INVALID_AUTH_CODE(400, "VE1013", "Invalid Auth Code"),
    EXPIRED_AUTH_CODE(400, "VE1012", "Expired Auth Code"),
    INVALID_TOKEN(401, "", "Invalid Token"),
    EXPIRED_TOKEN(401, "", "Expired Token"),
    UNAUTHORIZED(401, "", "Authentication is required and has failed or has not yet been provided."),
    USER_DUPLICATION(409, "VE1011", "User is Already Exists"),
    PASSWORD_DUPLICATION(409, "", "Password is Already Exists"),
    FAIL_GENERATE_VERIFY_EMAIL(422, "", "Email Generate Error"),

    //Info
    NOT_ADMIN_FORBIDDEN(403, "", "The server understood the request but refuses to authorize it."),
    APPLICATION_NOT_FOUND(404, "", "Application Not Found"),
    APPLICATION_TYPE_UNMATCHED(403, "", "Application Type is unmatched"),

    //School
    SCHOOL_NOT_FOUND(404, "", "School Not Found."),

    // Grade
    FINAL_SUBMIT_REQUIRED(406, "", "Final Submit Required."),
    UNPROCESSABLE_APPLICATION(422, "", "Cannot Generate Application");

    private final int status;
    private final String code;
    private final String message;

}
