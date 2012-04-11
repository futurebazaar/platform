package com.fb.platform.user.manager.model.admin.phone;

import java.util.List;

public class GetUserPhoneResponse {
	
	private String sessionToken = null;
	private List<UserPhone> userPhone = null;
	private GetUserPhoneStatusEnum getUserPhoneStatus;
	private int userId;
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
	 * @return the userPhone
	 */
	public List<UserPhone> getUserPhone() {
		return userPhone;
	}
	/**
	 * @param userPhone the userPhone to set
	 */
	public void setUserPhone(List<UserPhone> userPhone) {
		this.userPhone = userPhone;
	}
	
	/**
	 * @return the getUserPhoneStatus
	 */
	public GetUserPhoneStatusEnum getGetUserPhoneStatus() {
		return getUserPhoneStatus;
	}
	/**
	 * @param getUserPhoneStatus the getUserPhoneStatus to set
	 */
	public void setGetUserPhoneStatus(GetUserPhoneStatusEnum getUserPhoneStatus) {
		this.getUserPhoneStatus = getUserPhoneStatus;
	}
	/**
	 * @return the userId
	 */
	public int getUserId() {
		return userId;
	}
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(int userId) {
		this.userId = userId;
	}
}
