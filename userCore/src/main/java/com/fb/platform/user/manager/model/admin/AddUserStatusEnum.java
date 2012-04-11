package com.fb.platform.user.manager.model.admin;

public enum AddUserStatusEnum {

	SUCCESS("SUCCESS"),
	USER_ALREADY_EXISTS("USER ALREADY EXISTS"),
	INVALID_USER_NAME("INVALID USER NAME"),
	NO_USER_PROVIDED("NO USERNAME PROVIDED"),
	ADD_USER_FAILED("ADD USER FAILED"),
	NO_SESSION("NO_SESSION");

	private String status = null;

	private AddUserStatusEnum(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return this.status;
	}

}
