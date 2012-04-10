package com.fb.platform.user.manager.model.address;

public class AddAddressResponse {
	
	private String sessionToken = null;
	private long addressId;
	private AddAddressStatusEnum addAddressStatus;
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
	 * @return the addressId
	 */
	public long getAddressId() {
		return addressId;
	}
	/**
	 * @param addressId the addressId to set
	 */
	public void setAddressId(long addressId) {
		this.addressId = addressId;
	}
	/**
	 * @return the addAddressStatus
	 */
	public AddAddressStatusEnum getAddAddressStatus() {
		return addAddressStatus;
	}
	/**
	 * @param addAddressStatus the addAddressStatus to set
	 */
	public void setAddAddressStatus(AddAddressStatusEnum addAddressStatus) {
		this.addAddressStatus = addAddressStatus;
	}
	
	

}
