package com.fb.platform.wallet.manager.model.access;

public enum WalletHistoryStatusEnum {

	SUCCESS("SUCCESS"), NO_SESSION("NO SESSION"), ERROR_RETRIVING_WALLET_HISTORY(
			"ERROR RETRIVING WALLET HISTORY");

	private String walletHistoryStatus = null;

	private WalletHistoryStatusEnum(String walletHistoryStatus) {
		this.walletHistoryStatus = walletHistoryStatus;
	}

	@Override
	public String toString() {
		return this.walletHistoryStatus;
	}

}
