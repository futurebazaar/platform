package com.fb.platform.wallet.to;

import com.fb.platform.wallet.model.Wallet;

public class WalletTransaction {
	
	private Wallet wallet;
	private String transactionId = null;
	private String notes;
	/**
	 * @return the wallet
	 */
	public Wallet getWallet() {
		return wallet;
	}
	/**
	 * @param wallet the wallet to set
	 */
	public void setWallet(Wallet wallet) {
		this.wallet = wallet;
	}
	/**
	 * @return the transactionId
	 */
	public String getTransactionId() {
		return transactionId;
	}
	/**
	 * @param transactionId the transactionId to set
	 */
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	/**
	 * @return the notes
	 */
	public String getNotes() {
		return notes;
	}
	/**
	 * @param notes the notes to set
	 */
	public void setNotes(String notes) {
		this.notes = notes;
	}
	
	

}
