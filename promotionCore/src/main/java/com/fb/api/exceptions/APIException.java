package com.fb.api.exceptions;


public class APIException extends Exception {

	private static final long serialVersionUID = 1L;
	private String exceptionCode;
	private HTTPStatusCode status = HTTPStatusCode.SERVER_ERROR;

	public APIException(String message) {
		this(message, null, APIExceptionCode.GENERAL);
	}

	public APIException(String message, Throwable t) {
		this(message, t, APIExceptionCode.GENERAL);
	}

	public APIException(String message, String exceptionCode) {
		this(message, null, exceptionCode);
	}

	public APIException(String message, Throwable t, String exceptionCode) {
		super(message, t);
		this.exceptionCode = exceptionCode;
	}

	public String getExceptionCode() {
		return exceptionCode;
	}

	public HTTPStatusCode getStatus() {
		return status;
	}

	public void setStatus(HTTPStatusCode status) {
		this.status = status;
	}
}
