package kr.hs.entrydsm.husky.global.error.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Getter
@AllArgsConstructor
public enum ErrorCode {
    //Common
    BAD_REQUEST(400, "C400-0", "Bad Request(Invalid Parameter)"),
    USER_NOT_FOUND(404, "C404-0", "User Not Found."),
    INTERNAL_SERVER_ERROR(500, "C500-0", "Internal Server Error"),

    //Auth
    INVALID_AUTH_EMAIL(400, "A400-0", "Invalid Auth Email"),
    INVALID_AUTH_CODE(400, "A400-1", "Invalid Auth Code"),
    EXPIRED_AUTH_CODE(400, "A400-2", "Expired Auth Code"),
    INVALID_TOKEN(401, "A401-0", "Invalid Token"),
    EXPIRED_TOKEN(401, "A401-1", "Expired Token"),
    UNAUTHORIZED(401, "A401-2", "Authentication is required and has failed or has not yet been provided."),
    TOKEN_REQUIRED(404, "A404-0", "Bearer Token Required."),
    USER_DUPLICATION(409, "A409-0", "User is Already Exists"),
    PASSWORD_DUPLICATION(409, "A409-1", "Password is Already Exists"),
    FAIL_GENERATE_VERIFY_EMAIL(422, "A422-0", "Email Generate Error"),

    //Info
    NOT_ADMIN_FORBIDDEN(403, "I403-0", "The server understood the request but refuses to authorize it."),
    APPLICATION_TYPE_UNMATCHED(403, "I403-1", "Application Type is unmatched"),
    GRADE_TYPE_REQUIRED(406, "I406-0", "Grade type is not set."),
    APPLICATION_NOT_FOUND(404, "I404-0", "Application Not Found"),

    //School
    SCHOOL_NOT_FOUND(404, "S404-0", "School Not Found."),

    //Grade
    FINAL_SUBMIT_REQUIRED(406, "G406-0", "Final Submit Required."),
    UNPROCESSABLE_APPLICATION(422, "G422-0", "Cannot Generate Application");

    private final int status;
    private final String code;
    private final String message;

}
