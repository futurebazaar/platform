package com.fb.platform.user.manager.model.admin;

public enum IsValidUserEnum {

	VALID_USER ("VALID USER"),
	INVALID_USER ("INVALID USER"),
	ERROR ("ERROR IN PROCESSING REQUEST");
	
	private String isValidUserStatus;
	
	private IsValidUserEnum(String isValidUserStatus) {
		this.isValidUserStatus = isValidUserStatus;
	}
	
	@Override
	public String toString() {
		return this.isValidUserStatus;
	}
	
	
}
