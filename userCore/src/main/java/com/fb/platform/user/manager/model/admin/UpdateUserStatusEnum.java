package com.fb.platform.user.manager.model.admin;

public enum UpdateUserStatusEnum {
	
	SUCCESS("SUCCESS"),
	INVALID_USER("INVALID USER"),
	NO_USER_PROVIDED("NO USER TO UPDATE"),
	UPDATE_USER_FAILED("UPDATE USER FAILED"),
	NO_SESSION("NO_SESSION");

	private String status = null;

	private UpdateUserStatusEnum(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return this.status;
	}

}
