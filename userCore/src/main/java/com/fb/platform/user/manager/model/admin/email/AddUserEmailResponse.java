package com.fb.platform.user.manager.model.admin.email;



public class AddUserEmailResponse {
	
	private String sessionToken = null;
	private AddUserEmailStatusEnum addUserEmailStatus;
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
	 * @return the addUserEmailStatus
	 */
	public AddUserEmailStatusEnum getAddUserEmailStatus() {
		return addUserEmailStatus;
	}
	/**
	 * @param addUserEmailStatus the addUserEmailStatus to set
	 */
	public void setAddUserEmailStatus(AddUserEmailStatusEnum addUserEmailStatus) {
		this.addUserEmailStatus = addUserEmailStatus;
	}
	
	
	
	

}
