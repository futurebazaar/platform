package com.fb.platform.user.manager.model.admin.phone;

public enum GetUserPhoneStatusEnum {
	
	SUCCESS("SUCCESS"),
	INVALID_USER ("INVALID USER"),
	NO_PHONE("NO PHONE FOR THIS USER"),
	ERROR_RETRIVING_PHONE ("ERROR GETTING PHONE"),
	NO_SESSION("NO_SESSION");
	
	private String getUserPhoneStatus = null;
	
	private GetUserPhoneStatusEnum(String getUserPhoneStatus) {
		this.getUserPhoneStatus = getUserPhoneStatus;
	}

	@Override
	public String toString() {
		return this.getUserPhoneStatus;
	}


}
