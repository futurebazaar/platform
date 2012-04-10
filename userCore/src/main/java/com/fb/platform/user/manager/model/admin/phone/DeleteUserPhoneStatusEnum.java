package com.fb.platform.user.manager.model.admin.phone;

public enum DeleteUserPhoneStatusEnum {
	
	SUCCESS("SUCCESS"),
	INVALID_REQUEST ("INVALID REQUEST"),
	NO_PHONE("THIS PHONE NOT FOR THIS USER"),
	ERROR_DELETING_PHONE ("ERROR DELETEING PHONE"),
	NO_SESSION("NO_SESSION");
	
	private String deleteUserPhoneStatus = null;
	
	private DeleteUserPhoneStatusEnum(String deleteUserPhoneStatus) {
		this.deleteUserPhoneStatus = deleteUserPhoneStatus;
	}

	@Override
	public String toString() {
		return this.deleteUserPhoneStatus;
	}

}
