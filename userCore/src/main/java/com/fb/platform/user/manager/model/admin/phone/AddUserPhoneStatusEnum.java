package com.fb.platform.user.manager.model.admin.phone;

public enum AddUserPhoneStatusEnum {
	
	SUCCESS("SUCCESS"),
	ALREADY_PRESENT("ALREADY ADDED PHONE"),
	INVALID_PHONE ("INVALID PHONE"),
	ERROR_ADDING_PHONE ("ERROR ADDING PHONE"),
	NO_SESSION("NO_SESSION");
	
	private String addUserPhoneStatus = null;
	
	private AddUserPhoneStatusEnum(String addUserPhoneStatus) {
		this.addUserPhoneStatus = addUserPhoneStatus;
	}

	@Override
	public String toString() {
		return this.addUserPhoneStatus;
	}

}
