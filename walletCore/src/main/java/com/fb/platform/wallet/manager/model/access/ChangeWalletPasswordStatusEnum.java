package com.fb.platform.wallet.manager.model.access;

public enum ChangeWalletPasswordStatusEnum {
	
	SUCCESS("SUCCESS"),
	NO_SESSION("NO SESSION"),
	WRONG_PASSWORD("WRONG PASSWORD"),
	INVALID_WALLET("INVALID WALLET"),
	FAILED_TRANSACTION("FAILED TRANSACTION");

	private String status = null;

	private ChangeWalletPasswordStatusEnum(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return this.status;
	}


}
