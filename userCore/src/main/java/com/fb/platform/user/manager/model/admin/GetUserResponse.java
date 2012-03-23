package com.fb.platform.user.manager.model.admin;

import java.io.Serializable;


public class GetUserResponse implements Serializable {
	
	private String sessionToken = null;
	private GetUserStatusEnum status = null ;
	private String userName;
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
	public GetUserStatusEnum getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(GetUserStatusEnum status) {
		this.status = status;
	}
	/**
	 * @return the username
	 */
	public String getUserName() {
		return userName;
	}
	/**
	 * @param username the username to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	
	

}
