package com.fb.platform.user.manager.model.address;

public class AddAddressRequest {
	
	private String sessionToken = null;
	private int userId;
	private UserAddress userAddress;
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
	 * @return the userAddress
	 */
	public UserAddress getUserAddress() {
		return userAddress;
	}
	/**
	 * @param userAddress the userAddress to set
	 */
	public void setUserAddress(UserAddress userAddress) {
		this.userAddress = userAddress;
	}
	
	

}
