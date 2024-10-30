package com.softz.identity.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import lombok.Getter;

@Getter
public enum ErrorCode {
        UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error",
                        HttpStatus.INTERNAL_SERVER_ERROR),
        INVALID_INPUT(9000, "Invalid input",
                        HttpStatus.BAD_REQUEST),
        UNAUTHENTICATED(9401, "Unauthenticated",
                        HttpStatus.UNAUTHORIZED),
        USER_NOT_FOUND(404100, "User not found", HttpStatus.NOT_FOUND),
        USER_EXISTED(409101, "User existed", HttpStatus.CONFLICT),
        USER_ID_NOT_FOUND(404102, "User id %s not found", HttpStatus.NOT_FOUND),
        PERMISSION_NOT_FOUND(404103, "Permission not found", HttpStatus.NOT_FOUND),

        //
        MISSING_MESSAGE_KEY(100100, "Invalid message", HttpStatus.BAD_REQUEST),
        INVALID_USERNAME(100101, "Username's length must be between {min} and {max}", HttpStatus.BAD_REQUEST),
        INVALID_DATE_OF_BIRTH(100102, "User's age must be equal or greater than {min}", HttpStatus.BAD_REQUEST),
        INVALID_EMAIL_ADDRESS(100103, "Invalid email address", HttpStatus.BAD_REQUEST),
        ROLE_IS_EMPTY(100104, "Role is empty", HttpStatus.BAD_REQUEST),
        ROLE_CONTAINS_DUPLICATED_ITEM(100105, "Role contains duplicated item(s)", HttpStatus.BAD_REQUEST),
        ROLE_CONTAINS_INVALID_ITEM(100105, "Role contains invalid item(s)", HttpStatus.BAD_REQUEST),
        FIELD_EXISTED(100105, "Field existed %s with value %s", HttpStatus.BAD_REQUEST),
        FIELD_IS_REQUIRED(100106, "Field is required", HttpStatus.BAD_REQUEST)
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
