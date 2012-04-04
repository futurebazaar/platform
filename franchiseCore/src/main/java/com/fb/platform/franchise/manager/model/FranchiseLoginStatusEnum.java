package com.fb.platform.franchise.manager.model;

public enum FranchiseLoginStatusEnum {

	LOGIN_SUCCESS("LOGIN_SUCCESS"),
	INVALID_USERNAME_PASSWORD("INVALID_USERNAME_PASSWORD"),
	LOGIN_FAILURE("LOGIN_FAILURE"),
	LOGIN_ACCOUNT_LOCKED("LOGIN_ACCOUNT_LOCKED"),
	LOGIN_ACCOUNT_CLOSED("LOGIN_ACCOUNT_CLOSED");

	private String status = null;

	private FranchiseLoginStatusEnum(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return this.status;
	}
}
