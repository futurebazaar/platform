package com.fb.platform.wallet.manager.model.access;

public enum RefundStatusEnum {

	SUCCESS("SUCCESS"),
	NO_SESSION("NO SESSION"),
	INVALID_WALLET("INVALID WALLET"),
	ALREADY_REFUNDED("ALREADY REFUNDED"),
	DUPLICATE_REFUND_REQUEST("DUPLICATE REFUND REQUEST"),
	BALANCE_UNAVAILABLE("BALANCE UNAVAILABLE"),
	INACTIVE_WALLET("INACTIVE WALLET"),
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
