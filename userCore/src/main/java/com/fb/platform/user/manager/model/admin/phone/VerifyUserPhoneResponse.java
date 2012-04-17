package com.fb.platform.user.manager.model.admin.phone;

public class VerifyUserPhoneResponse {
	private VerifyUserPhoneStatusEnum verifyUserPhoneStatus;
	private String sessionToken;
	/**
	 * @return the verifyUserPhoneStatus
	 */
	public VerifyUserPhoneStatusEnum getVerifyUserPhoneStatus() {
		return verifyUserPhoneStatus;
	}
	/**
	 * @param verifyUserPhoneStatus the verifyUserPhoneStatus to set
	 */
	public void setVerifyUserPhoneStatus(
			VerifyUserPhoneStatusEnum verifyUserPhoneStatus) {
		this.verifyUserPhoneStatus = verifyUserPhoneStatus;
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
