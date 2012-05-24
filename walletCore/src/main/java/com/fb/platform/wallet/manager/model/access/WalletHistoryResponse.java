package com.fb.platform.wallet.manager.model.access;

import java.util.List;
import com.fb.platform.wallet.to.WalletTransaction;

public class WalletHistoryResponse {

	private String sessionToken;
	private WalletHistoryStatusEnum walletHistoryStatus;
	private List<WalletTransaction> transactionList;

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

	public List<WalletTransaction> getTransactionList() {
		return transactionList;
	}

	public void setTransactionList(List<WalletTransaction> transactionList) {
		this.transactionList = transactionList;
	}
}
