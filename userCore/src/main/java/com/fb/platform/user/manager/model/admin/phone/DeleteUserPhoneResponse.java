package com.fb.platform.user.manager.model.admin.phone;

public class DeleteUserPhoneResponse { 
	
	private DeleteUserPhoneStatusEnum deleteUserPhoneStatus;
	private String sessionToken;
	/**
	 * @return the deleteUserPhoneStatusEnum
	 */
	public DeleteUserPhoneStatusEnum getDeleteUserPhoneStatus() {
		return deleteUserPhoneStatus;
	}
	/**
	 * @param deleteUserPhoneStatusEnum the deleteUserPhoneStatusEnum to set
	 */
	public void setDeleteUserPhoneStatus(
			DeleteUserPhoneStatusEnum deleteUserPhoneStatus) {
		this.deleteUserPhoneStatus = deleteUserPhoneStatus;
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
