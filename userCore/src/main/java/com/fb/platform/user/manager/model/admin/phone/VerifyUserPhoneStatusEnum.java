package com.fb.platform.user.manager.model.admin.phone;

public enum VerifyUserPhoneStatusEnum {
	
	SUCCESS("SUCCESS"),
    INVALID_USER ("INVALID USER"),
    NO_PHONE("NO PHONE WITH THIS NUMBER"),
    INVALID_VERIFICATION_CODE("INVALID VERIFICATION CODE"),
    ALREADY_VERIFIED("ALREADY VERIFIED"),
    ERROR_VERIFYING_PHONE("ERROR VERIFYING PHONE"),
    NO_SESSION("NO SESSION");
	
	private String verifyUserPhoneStatus;

	private VerifyUserPhoneStatusEnum(String verifyUserPhoneStatus) {
		this.verifyUserPhoneStatus = verifyUserPhoneStatus;
	}
	
	@Override
	public String toString(){
		return this.verifyUserPhoneStatus;
	}
}
