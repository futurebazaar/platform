package com.fb.platform.user.manager.model.address;

import java.util.List;

public class GetAddressResponse {
	
	private String sessionToken = null;
	private GetAddressStatusEnum getAddressStatus;
	private List<UserAddress> userAddress = null;
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
	 * @return the getAddressStatus
	 */
	public GetAddressStatusEnum getGetAddressStatus() {
		return getAddressStatus;
	}
	/**
	 * @param getAddressStatus the getAddressStatus to set
	 */
	public void setGetAddressStatus(GetAddressStatusEnum getAddressStatus) {
		this.getAddressStatus = getAddressStatus;
	}
	/**
	 * @return the userAddress
	 */
	public List<UserAddress> getUserAddress() {
		return userAddress;
	}
	/**
	 * @param userAddress the userAddress to set
	 */
	public void setUserAddress(List<UserAddress> userAddress) {
		this.userAddress = userAddress;
	}
	
	

}
