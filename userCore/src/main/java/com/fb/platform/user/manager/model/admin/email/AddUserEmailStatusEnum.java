package com.fb.platform.user.manager.model.admin.email;

public enum AddUserEmailStatusEnum {
	
	SUCCESS("SUCCESS"),
	ALREADY_PRESENT("ALREADY ADDED EMAIL ID"),
	INVALID_EMAIL ("INVALID EMAIL"),
	ERROR_ADDING_EMAIL ("ERROR ADDING EMAIL"),
	NO_SESSION("NO_SESSION");
	
	private String addUserEmailStatus = null;
	
	private AddUserEmailStatusEnum(String addUserEmailStatus) {
		this.addUserEmailStatus = addUserEmailStatus;
	}

	@Override
	public String toString() {
		return this.addUserEmailStatus;
	}

}
