package com.fb.api.exceptions;


public class BadRequestException extends APIException {

	private static final long serialVersionUID = 1L;

	public BadRequestException(String message) {
		this(message, APIExceptionCode.BAD_REQUEST);
	}

	public BadRequestException(String message, String exceptionCode) {
		super(message, exceptionCode);
		this.setStatus(HTTPStatusCode.BAD_REQUEST);
	}
}
