package com.fb.platform.user.manager.model.admin.email;

public enum GetUserEmailStatusEnum {
	
	SUCCESS("SUCCESS"),
	INVALID_USER ("INVALID USER"),
	NO_EMAIL_ID("NO EMAIL ID FOR THIS USER"),
	ERROR_RETRIVING_EMAIL ("ERROR GETTING EMAIL"),
	NO_SESSION("NO_SESSION");
	
	private String getUserEmailStatus = null;
	
	private GetUserEmailStatusEnum(String getUserEmailStatus) {
		this.getUserEmailStatus = getUserEmailStatus;
	}

	@Override
	public String toString() {
		return this.getUserEmailStatus;
	}


}
