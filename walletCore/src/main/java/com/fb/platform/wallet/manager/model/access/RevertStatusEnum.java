package com.fb.platform.wallet.manager.model.access;

public enum RevertStatusEnum {

	SUCCESS("SUCCESS"),
	NO_SESSION("NO SESSION"),
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
