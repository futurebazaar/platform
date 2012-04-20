package com.fb.platform.user.manager.model.admin.email;

public class VerifyUserEmailResponse {
	private VerifyUserEmailStatusEnum verifyUserEmailStatus;
	private String sessionToken;
	/**
	 * @return the verifyUserEmailStatus
	 */
	public VerifyUserEmailStatusEnum getVerifyUserEmailStatus() {
		return verifyUserEmailStatus;
	}
	/**
	 * @param verifyUserEmailStatus the verifyUserEmailStatus to set
	 */
	public void setVerifyUserEmailStatus(
			VerifyUserEmailStatusEnum verifyUserEmailStatus) {
		this.verifyUserEmailStatus = verifyUserEmailStatus;
	}
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
	
	

}
