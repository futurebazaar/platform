package com.fb.platform.user.manager.model.address;

public class UpdateAddressResponse {
	
	private UpdateAddressStatusEnum updateAddressStatus;
	private String sessionToken;
	/**
	 * @return the updateAddressStatus
	 */
	public UpdateAddressStatusEnum getUpdateAddressStatus() {
		return updateAddressStatus;
	}
	/**
	 * @param updateAddressStatus the updateAddressStatus to set
	 */
	public void setUpdateAddressStatus(UpdateAddressStatusEnum updateAddressStatus) {
		this.updateAddressStatus = updateAddressStatus;
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
	
	

}
