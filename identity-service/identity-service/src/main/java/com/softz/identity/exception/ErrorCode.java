package com.softz.identity.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import lombok.Getter;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", 
            HttpStatus.INTERNAL_SERVER_ERROR),
    USER_NOT_FOUND(404100, "User not found", HttpStatus.NOT_FOUND),
    USER_EXISTED(409100, "User existed", HttpStatus.CONFLICT)
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
