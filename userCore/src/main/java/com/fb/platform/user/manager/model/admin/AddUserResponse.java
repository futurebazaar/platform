package com.fb.platform.user.manager.model.admin;

import java.io.Serializable;



public class AddUserResponse implements Serializable {

	private AddUserStatusEnum status = null;
	private String sessionToken = null;
	private Integer userId;
	/**
	 * @return the status
	 */
	public AddUserStatusEnum getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(AddUserStatusEnum status) {
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
	/**
	 * @return the userId
	 */
	public Integer getUserId() {
		return userId;
	}
	/**
	 * @param userId the userid to set
	 */
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
}
