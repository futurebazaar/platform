package com.fb.platform.wallet.manager.model.access;

public enum RevertStatusEnum {

	SUCCESS("SUCCESS"),
	NO_SESSION("NO SESSION"),
	INVALID_WALLET("INVALID WALLET"),
	BALANCE_UNAVAILABLE("BALANCE UNAVAILABLE"),
	INVALID_TRANSACTION_ID("INVALID TRANSACTION ID"),
	INACTIVE_WALLET("INACTIVE WALLET"),
	FAILED_TRANSACTION("FAILED TRANSACTION");

	private String status = null;

	private RevertStatusEnum(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return this.status;
	}


}
