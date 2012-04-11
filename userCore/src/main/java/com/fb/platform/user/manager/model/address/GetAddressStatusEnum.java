package com.fb.platform.user.manager.model.address;

public enum GetAddressStatusEnum {
	
	 SUCCESS("SUCCESS"),
     INVALID_USER("INVALID USER"),
     NO_ADDRESSES("NO ADDRESS FOR THIS USER"),
     ERROR_RETRIVING_ADDRESS("ERROR IN GETTING ADDDRESS"),
     NO_SESSION("NO SESSION");
	 
	 private String getAddressStatus = null;
	 
	 private GetAddressStatusEnum(String getAddressStatus) {
		this.getAddressStatus = getAddressStatus;
	 }
	 
	 @Override
	public String toString() {
		return this.getAddressStatus;
	}
	 
}
