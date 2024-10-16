package com.softz.identity.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.softz.identity.dto.ApiResponse;

import jakarta.validation.ConstraintViolation;
import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

	private static final String MIN_ATTRIBUTE = "min";
	private static final String MAX_ATTRIBUTE = "max";
	private static final String PROPERTY = "property";

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
		apiResponse.setMessage(buildMessage(errorCode.getMessage(), exception.getParams()));

		return ResponseEntity.status(errorCode.getStatusCode())
				.body(apiResponse);
	}

	@SuppressWarnings("rawtypes")
	@ExceptionHandler(value = MethodArgumentNotValidException.class)
	ResponseEntity<ApiResponse> handlingMethodArgumentNotValid(MethodArgumentNotValidException exception) {
		log.error("Uncategorized error", exception);

		@SuppressWarnings("null")
		var messageKey = exception.getFieldError().getDefaultMessage();

		var errorCode = ErrorCode.MISSING_MESSAGE_KEY;
		var propertyName = "";
		Map<String, Object> attributes = new HashMap<>();
		try {
			errorCode = ErrorCode.valueOf(messageKey);

			var constraintViolation = exception.getBindingResult()
					.getAllErrors()
					.getFirst()
					.unwrap(ConstraintViolation.class);
			propertyName = constraintViolation.getPropertyPath().toString();
			attributes = constraintViolation.getConstraintDescriptor()
					.getAttributes();

		} catch (IllegalArgumentException e) {
			log.error("Invalid message key: ", e);
		}

		String message = mapAttribute(propertyName, errorCode.getMessage(), attributes);

		ApiResponse apiResponse = ApiResponse.builder()
				.code(errorCode.getCode())
				.message(message)
				.build();

		return ResponseEntity.badRequest()
				.body(apiResponse);
	}

	@SuppressWarnings("rawtypes")
	@ExceptionHandler(value = HttpMessageNotReadableException.class)
    ResponseEntity<ApiResponse> handlingHttpMessageNotReadableException(
            HttpMessageNotReadableException exception) {
        log.error("Http message not readable error: ", exception);
        ErrorCode errorCode = ErrorCode.INVALID_INPUT;

        ApiResponse apiResponse = new ApiResponse();

        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage());

        return ResponseEntity.status(errorCode.getStatusCode())
                .body(apiResponse);
    }

	@SuppressWarnings("rawtypes")
	@ExceptionHandler(value = Exception.class)
	ResponseEntity<ApiResponse> handlingException(
			Exception exception) {
		log.error("Uncategorized error", exception);
		ErrorCode errorCode = ErrorCode.UNCATEGORIZED_EXCEPTION;

		ApiResponse apiResponse = new ApiResponse();

		apiResponse.setCode(errorCode.getCode());
		apiResponse.setMessage(errorCode.getMessage());

		return ResponseEntity.status(errorCode.getStatusCode())
				.body(apiResponse);
	}

	private String buildMessage(String message, String[] params){
		return String.format(message, params);
	}

	private String mapAttribute(String property, String message, Map<String, Object> attributes) {
		String minValue = String.valueOf(attributes.get(MIN_ATTRIBUTE));
		String maxValue = String.valueOf(attributes.get(MAX_ATTRIBUTE));

		return message.replace("{" + PROPERTY + "}", property).replace("{" + MIN_ATTRIBUTE + "}", minValue)
				.replace("{" + MAX_ATTRIBUTE + "}", maxValue);
	}
}
