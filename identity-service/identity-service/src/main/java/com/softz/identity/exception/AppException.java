package com.softz.identity.exception;

import lombok.Getter;

@Getter
public class AppException extends RuntimeException {
    private final String[] params;
    private final ErrorCode errorCode;

    public AppException(ErrorCode errorCode, String... params) {
        super();
        this.errorCode = errorCode;
        this.params = params;
    }

}
