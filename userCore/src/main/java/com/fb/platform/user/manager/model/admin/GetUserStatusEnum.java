package com.fb.platform.user.manager.model.admin;

public enum GetUserStatusEnum {
	
	SUCCESS("SUCCESS"),
	NO_USER_KEY("NO USER KEY PROVIDED"),
	INVALID_USER ("INVALID USER"),
	ERROR_RETRIVING_USER ("ERROR GETTING USER"),
	NO_SESSION("NO_SESSION");

	private String status = null;

	private GetUserStatusEnum(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return this.status;
	}

}
