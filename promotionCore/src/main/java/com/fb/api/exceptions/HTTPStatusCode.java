package com.fb.api.exceptions;

public enum HTTPStatusCode {
	OK (200),
	BAD_REQUEST (400),
	UNAUTHENTICATED (401),
	UNAUTHORIZED (403),
	SERVER_ERROR (500);

	private int code;

	private HTTPStatusCode(int code) {
		this.code = code;
	}

	public int getCode() {
		return this.code;
	}
}
