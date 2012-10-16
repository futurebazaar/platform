package com.fb.platform.wallet.manager.model.access;

public class ResetWalletPasswordResponse {
	
	private String sessionToken;
	ResetWalletPasswordStatusEnum status;
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
	public ResetWalletPasswordStatusEnum getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(ResetWalletPasswordStatusEnum status) {
		this.status = status;
	}
	
	

}
