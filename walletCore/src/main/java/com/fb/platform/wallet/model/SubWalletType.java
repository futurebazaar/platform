package com.fb.platform.wallet.model;

public enum SubWalletType {
	CASH_SUB_WALLET("CASH"), GIFT_SUB_WALLET("GIFT"), REFUND_SUB_WALLET(
			"REFUND");

	private String subWalletType = null;

	private SubWalletType(String subWalletType) {
		this.subWalletType = subWalletType;
	}

	@Override
	public String toString() {
		return this.subWalletType;
	}

}
