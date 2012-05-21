package com.fb.platform.wallet.model;

public enum SubWalletType {
	CASH("CASH"), GIFT("GIFT"), REFUND("REFUND");

	private String subWalletType = null;

	private SubWalletType(String subWalletType) {
		this.subWalletType = subWalletType;
	}

	@Override
	public String toString() {
		return this.subWalletType;
	}

}
