package com.fb.platform.user.manager.model.address;

public enum AddAddressStatusEnum {
	SUCCESS("SUCCESS"),
    INVALID_USER("INVALID USER"),
    EMPTY_ADDRESS("EMPTY ADDRESS"),
    ERROR_ADDING_ADDRESS("ERROR ADDING ADDRESS"),
    NO_SESSION("NO SESSION");
	
	private String addAddAddressStatus;
	
	private AddAddressStatusEnum(String addAddAddressStatus){
		this.addAddAddressStatus = addAddAddressStatus;
	}
		@Override
	public String toString(){
		return this.addAddAddressStatus;
	}
}
