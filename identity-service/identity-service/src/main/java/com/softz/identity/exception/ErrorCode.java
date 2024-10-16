package com.softz.identity.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import lombok.Getter;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    USER_NOT_FOUND(404100, "User %s not found", HttpStatus.NOT_FOUND),
    USER_EXISTED(409101, "User %s existed", HttpStatus.CONFLICT),
    USER_ID_NOT_FOUND(409102, "User id %s not found", HttpStatus.CONFLICT),
    INVALID_USERNAME(100100, "{property}'s length must be between {min} and {max}", HttpStatus.BAD_REQUEST),
    MISSING_MESSAGE_KEY(100101, "Invailid message key",  HttpStatus.BAD_REQUEST),
    INVALID_DATE_OF_BIRTH(100102, "{property}'s age minimum must be {min}",  HttpStatus.BAD_REQUEST),
    INVALID_INPUT(9000, "Invalid input", HttpStatus.BAD_REQUEST),
    PERMISSION_NOT_FOUND(404101, "Permission not found", HttpStatus.NOT_FOUND),
    PERMISSION_EXISTED(409101, "Permission existed", HttpStatus.CONFLICT),
    ROLE_NOT_FOUND(404102, "Role not found", HttpStatus.NOT_FOUND),
    ROLE_EXISTED(409103, "Role existed", HttpStatus.CONFLICT)
    ;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;
}
