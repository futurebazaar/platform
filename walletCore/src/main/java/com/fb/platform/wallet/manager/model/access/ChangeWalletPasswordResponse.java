package com.fb.platform.wallet.manager.model.access;

public class ChangeWalletPasswordResponse {
	
	private String sessionToken;
	ChangeWalletPasswordStatusEnum status;
	/**
	 * @return the sessionToken
	 */
	public String getSessionToken() {
		return sessionToken;
	}
	/**
	 * @param sessionToken the sessionToken to set
	 */
	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}
	/**
	 * @return the status
	 */
	public ChangeWalletPasswordStatusEnum getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(ChangeWalletPasswordStatusEnum status) {
		this.status = status;
	}
	
	

}
