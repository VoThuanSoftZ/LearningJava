package com.softz.identity.exception;

import lombok.Getter;

@Getter
public class AppException extends RuntimeException {
    String[] params;
    private final ErrorCode errorCode;

    public AppException(ErrorCode errorCode, String... params) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.params = params;
    }
    

    public ErrorCode getErrorCode() {
        return errorCode;
    }

}
