package com.fb.platform.wallet.manager.model.access;

public enum PayStatusEnum {

	SUCCESS("SUCCESS"),
	NO_SESSION("NO SESSION"),
	WRONG_PASSWORD("WRONG PASSWORD"),
	INVALID_WALLET("INVALID WALLET"),
	BALANCE_UNAVAILABLE("BALANCE UNAVAILABLE"),
	FAILED_TRANSACTION("FAILED TRANSACTION");

	private String status = null;

	private PayStatusEnum(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return this.status;
	}

}
