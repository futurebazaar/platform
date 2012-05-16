package com.fb.platform.wallet.manager.model.access;

public enum RefundStatusEnum {

	SUCCESS("SUCCESS"),
	NO_SESSION("NO SESSION"),
	BALANCE_UNAVAILABLE("BALANCE UNAVAILABLE"),
	FAILED_TRANSACTION("FAILED TRANSACTION");

	private String status = null;

	private RefundStatusEnum(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return this.status;
	}

}
