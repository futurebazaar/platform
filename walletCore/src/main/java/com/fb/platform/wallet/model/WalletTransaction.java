package com.fb.platform.wallet.model;

import java.util.Date;

public class WalletTransaction {
	
	private Wallet wallet;
	private String transactionId = null;
	private SubWalletType subWalletType;
	private TransactionType transactionType = null;
	private Double amount;
	private long orderId;
	private long paymentId;
	private String notes;
	private Date timeStamp;
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
	 * @return the type
	 */
	public TransactionType getTransactionType() {
		return transactionType;
	}
	/**
	 * @param type the type to set
	 */
	public void setTransactionType(TransactionType transactionType) {
		this.transactionType = transactionType;
	}
	/**
	 * @return the amount
	 */
	public Double getAmount() {
		return amount;
	}
	/**
	 * @param amount the amount to set
	 */
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	/**
	 * @return the orderId
	 */
	public long getOrderId() {
		return orderId;
	}
	/**
	 * @param orderId the orderId to set
	 */
	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}
	/**
	 * @return the paymentId
	 */
	public long getPaymentId() {
		return paymentId;
	}
	/**
	 * @param paymentId the paymentId to set
	 */
	public void setPaymentId(long paymentId) {
		this.paymentId = paymentId;
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
	/**
	 * @return the timeStamp
	 */
	public Date getTimeStamp() {
		return timeStamp;
	}
	/**
	 * @param timeStamp the timeStamp to set
	 */
	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}
	/**
	 * @return the subWalletType
	 */
	public SubWalletType getSubWalletType() {
		return subWalletType;
	}
	/**
	 * @param subWalletType the subWalletType to set
	 */
	public void setSubWalletType(SubWalletType subWalletType) {
		this.subWalletType = subWalletType;
	}
	
	

}
