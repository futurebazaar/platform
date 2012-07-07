package com.fb.platform.wallet.manager.model.access;

public enum SubWalletEnum {

	CASH("CASH"), REFUND("REFUND"), GIFT("GIFT");

	private String subWallet;

	private SubWalletEnum(String subWallet) {
		this.subWallet = subWallet;
	}

	@Override
	public String toString() {
		return this.subWallet;
	}

}
