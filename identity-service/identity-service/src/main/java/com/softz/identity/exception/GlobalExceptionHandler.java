package com.softz.identity.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.softz.identity.dto.ApiResponse;

@ControllerAdvice
public class GlobalExceptionHandler {
    @SuppressWarnings("rawtypes")
    @ExceptionHandler(value = RuntimeException.class)
    ResponseEntity<ApiResponse> handlingRuntimeException(
        RuntimeException exception) {
        ApiResponse apiResponse = new ApiResponse();

        apiResponse.setCode(4000);
        apiResponse.setMessage(exception.getMessage());

        return ResponseEntity.badRequest()
                .body(apiResponse);
    }

    @SuppressWarnings("rawtypes")
    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse> handlingAppException(
        AppException exception) {
        ApiResponse apiResponse = new ApiResponse();

        var errorCode = exception.getErrorCode();

        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage());

        return ResponseEntity.status(errorCode.getStatusCode())
                .body(apiResponse);
    }

    @SuppressWarnings("rawtypes")
    @ExceptionHandler(value = Exception.class)
    ResponseEntity<ApiResponse> handlingException(
            Exception exception) {
        ErrorCode errorCode = ErrorCode.UNCATEGORIZED_EXCEPTION;
        
        ApiResponse apiResponse = new ApiResponse();

        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage());

        return ResponseEntity.status(errorCode.getStatusCode())
                .body(apiResponse);
    }
}
