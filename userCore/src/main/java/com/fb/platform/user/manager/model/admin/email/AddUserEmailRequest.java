package com.fb.platform.user.manager.model.admin.email;

public class AddUserEmailRequest {
	
	private String sessionToken = null;
	private int userId;
	private UserEmail userEmail = null;
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
	public int getUserId() {
		return userId;
	}
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(int userId) {
		this.userId = userId;
	}
	/**
	 * @return the userEmail
	 */
	public UserEmail getUserEmail() {
		return userEmail;
	}
	/**
	 * @param userEmail the userEmail to set
	 */
	public void setUserEmail(UserEmail userEmail) {
		this.userEmail = userEmail;
	}
	
	
	
}
