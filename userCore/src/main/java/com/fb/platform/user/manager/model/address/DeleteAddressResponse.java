package com.fb.platform.user.manager.model.address;

public class DeleteAddressResponse {
	
	private String sessionToken;
	private DeleteAddressStatusEnum deleteAddressStatus;
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
	 * @return the deleteAddressStatus
	 */
	public DeleteAddressStatusEnum getDeleteAddressStatus() {
		return deleteAddressStatus;
	}
	/**
	 * @param deleteAddressStatus the deleteAddressStatus to set
	 */
	public void setDeleteAddressStatus(DeleteAddressStatusEnum deleteAddressStatus) {
		this.deleteAddressStatus = deleteAddressStatus;
	}
	
	

}
