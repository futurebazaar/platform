package com.fb.platform.wallet.manager.model.access;

public enum ResetWalletPasswordStatusEnum {
	
	SUCCESS("SUCCESS"),
	NO_SESSION("NO SESSION"),
	INVALID_WALLET("INVALID WALLET"),
	FAILED_TRANSACTION("FAILED TRANSACTION");

	private String status = null;

	private ResetWalletPasswordStatusEnum(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return this.status;
	}


}
