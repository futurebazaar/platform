package com.fb.api.exceptions;


public class UnauthorizedException extends APIException{

	private static final long serialVersionUID = 1L;

	public UnauthorizedException(String message) {
		this(message, APIExceptionCode.UNAUTHORIZED);
	}

	public UnauthorizedException(String message, String exceptionCode) {
		super(message, exceptionCode);
		this.setStatus(HTTPStatusCode.UNAUTHORIZED);
	}
}
