package com.fb.platform.user.manager.model.address;

public enum UpdateAddressStatusEnum {
	
	SUCCESS("SUCCESS"),
    INVALID_USER("INVALID USER"),
    USER_ADDRESSID_MISMATCH ("THIS ADDRESS DOESNOT BELOG TO THIS USER"),
    ADDRESSID_ABSENT("ADDRESSID ABSENT"),
    ERROR_UPDATING_ADDRESS("ERROR UPDATING ADDRESS"),
    NO_SESSION("NO SESSION");
	
	private String updateAddressStatus = null;
	
	private UpdateAddressStatusEnum(String updateAddressStatus){
		this.updateAddressStatus = updateAddressStatus;
	}
	
	@Override
	public String toString(){
		return this.updateAddressStatus;
	}

}
