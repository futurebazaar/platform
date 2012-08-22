package com.fb.platform.wallet.to;

import java.util.List;

public class WalletTransactionResultSet {
	List<WalletTransaction> walletTransactions;
	int totalTransactionSize;
	/**
	 * @return the walletTransactions
	 */
	public List<WalletTransaction> getWalletTransactions() {
		return walletTransactions;
	}
	/**
	 * @param walletTransactions the walletTransactions to set
	 */
	public void setWalletTransactions(List<WalletTransaction> walletTransactions) {
		this.walletTransactions = walletTransactions;
	}
	/**
	 * @return the totalTransactionSize
	 */
	public int getTotalTransactionSize() {
		return totalTransactionSize;
	}
	/**
	 * @param totalTransactionSize the totalTransactionSize to set
	 */
	public void setTotalTransactionSize(int totalTransactionSize) {
		this.totalTransactionSize = totalTransactionSize;
	}
	
	
}
