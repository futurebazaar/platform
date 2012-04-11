package com.fb.platform.user.manager.model.admin.email;

public enum DeleteUserEmailStatusEnum {
	
	SUCCESS("SUCCESS"),
	INVALID_REQUEST ("INVALID REQUEST"),
	NO_EMAIL_ID("THIS EMAIL ID NOT FOR THIS USER"),
	ERROR_DELETING_EMAIL ("ERROR DELETEING EMAIL"),
	NO_SESSION("NO_SESSION");
	
	private String deleteUserEmailStatus = null;
	
	private DeleteUserEmailStatusEnum(String deleteUserEmailStatus) {
		this.deleteUserEmailStatus = deleteUserEmailStatus;
	}

	@Override
	public String toString() {
		return this.deleteUserEmailStatus;
	}

}
