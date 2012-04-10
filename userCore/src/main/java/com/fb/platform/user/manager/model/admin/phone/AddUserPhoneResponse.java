package com.fb.platform.user.manager.model.admin.phone;



public class AddUserPhoneResponse {
	
	private String sessionToken = null;
	private AddUserPhoneStatusEnum addUserPhoneStatus;
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
	 * @return the addUserPhoneStatus
	 */
	public AddUserPhoneStatusEnum getAddUserPhoneStatus() {
		return addUserPhoneStatus;
	}
	/**
	 * @param addUserPhoneStatus the addUserPhoneStatus to set
	 */
	public void setAddUserPhoneStatus(AddUserPhoneStatusEnum addUserPhoneStatus) {
		this.addUserPhoneStatus = addUserPhoneStatus;
	}
	
	
	
	

}
