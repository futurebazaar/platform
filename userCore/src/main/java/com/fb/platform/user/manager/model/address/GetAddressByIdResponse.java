package com.fb.platform.user.manager.model.address;

public class GetAddressByIdResponse {
	
	private String sessionToken = null;
	private GetAddressByIdStatusEnum getAddressStatus;
	private UserAddress userAddress = null;
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
	public GetAddressByIdStatusEnum getGetAddressStatus() {
		return getAddressStatus;
	}
	/**
	 * @param getAddressStatus the getAddressStatus to set
	 */
	public void setGetAddressStatus(GetAddressByIdStatusEnum getAddressStatus) {
		this.getAddressStatus = getAddressStatus;
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
