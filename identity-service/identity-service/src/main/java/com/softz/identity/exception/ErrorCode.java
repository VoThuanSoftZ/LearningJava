package com.softz.identity.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import lombok.Getter;

@Getter
public enum ErrorCode {
	UNCATEGORIZED_EXCEPTION(9999, HttpStatus.INTERNAL_SERVER_ERROR),
	METHODARGUMENT_NOTVALIDEXCEPTION(9999, HttpStatus.INTERNAL_SERVER_ERROR),
	USER_NOT_FOUND(404100, HttpStatus.NOT_FOUND),
	USER_EXISTED(409100, HttpStatus.CONFLICT),
	INVALID_USERNAME(100100, HttpStatus.BAD_REQUEST),
	INVALID_FIELD(100106, HttpStatus.BAD_REQUEST),
	INVALID_NOTNULL(100106, HttpStatus.BAD_REQUEST),
	MISSING_MESSAGE_KEY(100101, HttpStatus.BAD_REQUEST),
	INVALID_DATE_OF_BIRTH(100103, HttpStatus.BAD_REQUEST),
	INVALID_INPUT(9000, HttpStatus.BAD_REQUEST),
	USER_ID_NOT_FOUND(404102, HttpStatus.NOT_FOUND),
	INVALID_EMAIL(404103, HttpStatus.NOT_FOUND),
	EMAIL_EXISTED(404105, HttpStatus.NOT_FOUND),
	INVALID_ROLE(404104, HttpStatus.NOT_FOUND),
	DUPLICATE_IDS(404106, HttpStatus.NOT_FOUND),
	INVALID_IDS(404107, HttpStatus.BAD_REQUEST),
	DELETE_USER_FAILED(404109, HttpStatus.BAD_REQUEST),
	INVALID_PERMISSIONS(404108, HttpStatus.BAD_REQUEST),
	FIELD_REQUIRED(100106, HttpStatus.BAD_REQUEST),
	UNAUTHORIZED(9403, HttpStatus.FORBIDDEN),
	UNAUTHENTICATED(9401, HttpStatus.UNAUTHORIZED);
	;

	ErrorCode(int code, HttpStatusCode statusCode) {
		this.code = code;
		this.statusCode = statusCode;
	}

	private final int code;
	private final HttpStatusCode statusCode;
}
