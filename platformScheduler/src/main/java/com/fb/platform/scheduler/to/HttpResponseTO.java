package com.fb.platform.scheduler.to;

public class HttpResponseTO {
	
	private int statusCode;
	private String message;
	
	public int getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	@Override
	public String toString() {
		return "HttpResponseTO [statusCode=" + statusCode + ", message="
				+ message + "]";
	}

}
