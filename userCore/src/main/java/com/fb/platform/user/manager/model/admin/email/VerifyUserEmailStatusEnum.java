package com.fb.platform.user.manager.model.admin.email;

public enum VerifyUserEmailStatusEnum {
	
	SUCCESS("SUCCESS"),
    INVALID_USER ("INVALID USER"),
    NO_EMAIL("NO EMAIL WITH THIS EMAILID"),
    ALREADY_VERIFIED("ALREADY VERIFIED"),
    ERROR_VERIFYING_EMAIL("ERROR VERIFYING EMAIL"),
    NO_SESSION("NO SESSION");
	
	private String verifyUserEmailStatus;

	private VerifyUserEmailStatusEnum(String verifyUserEmailStatus) {
		this.verifyUserEmailStatus = verifyUserEmailStatus;
	}
	
	@Override
	public String toString(){
		return this.verifyUserEmailStatus;
	}
}
