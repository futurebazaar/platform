package com.fb.platform.user.manager.model.address;

public enum GetAddressByIdStatusEnum {
	
	 SUCCESS("SUCCESS"),
     INVALID_ADDRESS_ID("INVALID ADDRESS ID"),
     ERROR_RETRIVING_ADDRESS("ERROR IN GETTING ADDDRESS"),
     NO_SESSION("NO SESSION");
	 
	 private String getAddressStatus = null;
	 
	 private GetAddressByIdStatusEnum(String getAddressStatus) {
		this.getAddressStatus = getAddressStatus;
	 }
	 
	 @Override
	public String toString() {
		return this.getAddressStatus;
	}
	 
}
