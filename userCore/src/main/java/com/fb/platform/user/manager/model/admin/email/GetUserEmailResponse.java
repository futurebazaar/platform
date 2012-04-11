package com.fb.platform.user.manager.model.admin.email;

import java.util.List;

public class GetUserEmailResponse {
	
	private String sessionToken = null;
	private List<UserEmail> userEmail = null;
	private GetUserEmailStatusEnum getUserEmailStatus;
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
	 * @return the userEmail
	 */
	public List<UserEmail> getUserEmail() {
		return userEmail;
	}
	/**
	 * @param userEmail the userEmail to set
	 */
	public void setUserEmail(List<UserEmail> userEmail) {
		this.userEmail = userEmail;
	}
	
	/**
	 * @return the getUserEmailStatus
	 */
	public GetUserEmailStatusEnum getGetUserEmailStatus() {
		return getUserEmailStatus;
	}
	/**
	 * @param getUserEmailStatus the getUserEmailStatus to set
	 */
	public void setGetUserEmailStatus(GetUserEmailStatusEnum getUserEmailStatus) {
		this.getUserEmailStatus = getUserEmailStatus;
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
