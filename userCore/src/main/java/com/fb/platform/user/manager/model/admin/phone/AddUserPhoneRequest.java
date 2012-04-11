package com.fb.platform.user.manager.model.admin.phone;

public class AddUserPhoneRequest {
	
	private String sessionToken = null;
	private int userId;
	private UserPhone userPhone = null;
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
	 * @return the userPhone
	 */
	public UserPhone getUserPhone() {
		return userPhone;
	}
	/**
	 * @param userPhone the userPhone to set
	 */
	public void setUserPhone(UserPhone userPhone) {
		this.userPhone = userPhone;
	}
	
	
	
}
