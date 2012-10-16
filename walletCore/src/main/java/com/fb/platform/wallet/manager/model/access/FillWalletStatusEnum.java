package com.fb.platform.wallet.manager.model.access;

public enum FillWalletStatusEnum {

	SUCCESS("SUCCESS"),
	NO_SESSION("NO SESSION"),
	INVALID_WALLET("INVALID WALLET"),
	INACTIVE_WALLET("INACTIVE WALLET"),
	FAILED_TRANSACTION("FAILED TRANSACTION");

	private String status = null;

	private FillWalletStatusEnum(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return this.status;
	}

}
