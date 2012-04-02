package com.fb.platform.user.manager.model.auth;

public class KeepAliveResponse {
	
	private KeepAliveStatusEnum keepAliveStatus;
	private String sessionToken;

	
	/**
	 * @return the keepAliveStatus
	 */
	public KeepAliveStatusEnum getKeepAliveStatus() {
		return keepAliveStatus;
	}
	/**
	 * @param keepAliveStatus the keepAliveStatus to set
	 */
	public void setKeepAliveStatus(KeepAliveStatusEnum keepAliveStatus) {
		this.keepAliveStatus = keepAliveStatus;
	}
	public String getSessionToken() {
		return sessionToken;
	}
	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}

}
