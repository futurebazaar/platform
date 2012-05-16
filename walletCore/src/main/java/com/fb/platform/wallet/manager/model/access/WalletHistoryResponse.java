package com.fb.platform.wallet.manager.model.access;

import java.util.List;

public class WalletHistoryResponse {

	private String sessionToken;
	private WalletHistoryStatusEnum walletHistoryStatus;
	private List<Transaction> transaction;

	public String getSessionToken() {
		return sessionToken;
	}

	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}

	public WalletHistoryStatusEnum getWalletHistoryStatus() {
		return walletHistoryStatus;
	}

	public void setWalletHistoryStatus(
			WalletHistoryStatusEnum walletHistoryStatus) {
		this.walletHistoryStatus = walletHistoryStatus;
	}

	public List<Transaction> getTransaction() {
		return transaction;
	}

	public void setTransaction(List<Transaction> transaction) {
		this.transaction = transaction;
	}
}
