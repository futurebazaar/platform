package com.fb.platform.user.manager.model.address;

public enum DeleteAddressStatusEnum {

	SUCCESS("SUCCESS"),
    INVALID_USER("INVALID USER"),
    USER_ADDRESSID_MISMATCH ("THIS ADDRESS DOESNOT BELOG TO THIS USER"),
    ADDRESSID_ABSENT("ADDRESSID ABSENT"),
    ERROR_DELETING_ADDRESS("ERROR DELETING ADDRESS"),
    NO_SESSION("NO SESSION");
	
	private String deleteAddressStatus;
	
	private DeleteAddressStatusEnum(String deleteAddressStatus){
		this.deleteAddressStatus = deleteAddressStatus;
	}
	@Override
	public String toString(){
		return this.deleteAddressStatus;
	}
}
