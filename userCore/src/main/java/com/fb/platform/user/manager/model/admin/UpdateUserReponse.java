package com.fb.platform.user.manager.model.admin;

import java.io.Serializable;



public class UpdateUserReponse implements Serializable {
	
	private UpdateUserStatusEnum status = null;
	private String sessionToken = null;
	/**
	 * @return the status
	 */
	public UpdateUserStatusEnum getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(UpdateUserStatusEnum status) {
		this.status = status;
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
