package com.fb.platform.wallet.manager.model.access;

public enum VerifyWalletStatusEnum {
	
	SUCCESS("SUCCESS"),
	NO_SESSION("NO SESSION"),
	WRONG_PASSWORD("WRONG PASSWORD"),
	INVALID_WALLET("INVALID WALLET"),
	BALANCE_UNAVAILABLE("BALANCE UNAVAILABLE"),
	INACTIVE_WALLET("INACTIVE WALLET"),
	FAILED_TRANSACTION("FAILED TRANSACTION");

	private String status = null;

	private VerifyWalletStatusEnum(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return this.status;
	}


}