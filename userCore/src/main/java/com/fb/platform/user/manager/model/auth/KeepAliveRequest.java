package com.fb.platform.user.manager.model.auth;

public class KeepAliveRequest {
	
	private String sessionToken = null;

	public String getSessionToken() {
		return sessionToken;
	}

	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}

}
