package com.fb.platform.user.manager.model.admin.email;

public class DeleteUserEmailResponse { 
	
	private DeleteUserEmailStatusEnum deleteUserEmailStatus;
	private String sessionToken;
	/**
	 * @return the deleteUserEmailStatusEnum
	 */
	public DeleteUserEmailStatusEnum getDeleteUserEmailStatus() {
		return deleteUserEmailStatus;
	}
	/**
	 * @param deleteUserEmailStatusEnum the deleteUserEmailStatusEnum to set
	 */
	public void setDeleteUserEmailStatus(
			DeleteUserEmailStatusEnum deleteUserEmailStatus) {
		this.deleteUserEmailStatus = deleteUserEmailStatus;
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
