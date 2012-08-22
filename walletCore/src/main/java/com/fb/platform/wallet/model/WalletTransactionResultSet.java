package com.fb.platform.wallet.model;

import java.util.List;


public class WalletTransactionResultSet {

	List<WalletTransaction> walletTransactions;
	int totalNumberTransations;
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
	 * @return the totalNumberTransations
	 */
	public int getTotalNumberTransations() {
		return totalNumberTransations;
	}
	/**
	 * @param totalNumberTransations the totalNumberTransations to set
	 */
	public void setTotalNumberTransations(int totalNumberTransations) {
		this.totalNumberTransations = totalNumberTransations;
	}
	
	
}
