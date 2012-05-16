package com.fb.platform.wallet.model;

import org.joda.time.DateTime;

import com.fb.commons.to.Money;

public class WalletTransaction {

	private Wallet wallet;
	private String transactionId = null;
	private SubWalletType subWalletType;
	private TransactionType transactionType = null;
	private Money amount;
	private long orderId;
	private long refundId;
	private long paymentId;
	private long transactionReversalId;
	private String giftCode;
	private String notes;
	private DateTime timeStamp;

	WalletTransaction(Wallet wallet, SubWalletType subWalletType,
			TransactionType transactionType, Money amount, long orderId,
			long refundId, long paymentId, long transactionReversalId,String giftCode) {
		super();
		this.wallet = wallet;
		this.subWalletType = subWalletType;
		this.transactionType = transactionType;
		this.amount = amount;
		this.orderId = orderId;
		this.refundId = refundId;
		this.paymentId = paymentId;
		this.transactionReversalId = transactionReversalId;
		this.giftCode = giftCode;
	}
	/**
	 * @return the wallet
	 */
	public Wallet getWallet() {
		return wallet;
	}

	/**
	 * @param wallet
	 *            the wallet to set
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
	 * @param transactionId
	 *            the transactionId to set
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
	 * @param type
	 *            the type to set
	 */
	public void setTransactionType(TransactionType transactionType) {
		this.transactionType = transactionType;
	}

	/**
	 * @return the amount
	 */
	public Money getAmount() {
		return amount;
	}

	/**
	 * @param amount
	 *            the amount to set
	 */
	public void setAmount(Money amount) {
		this.amount = amount;
	}

	/**
	 * @return the orderId
	 */
	public long getOrderId() {
		return orderId;
	}

	/**
	 * @param orderId
	 *            the orderId to set
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
	 * @param paymentId
	 *            the paymentId to set
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
	 * @param notes
	 *            the notes to set
	 */
	public void setNotes(String notes) {
		this.notes = notes;
	}

	/**
	 * @return the timeStamp
	 */
	public DateTime getTimeStamp() {
		return timeStamp;
	}

	/**
	 * @param timeStamp
	 *            the timeStamp to set
	 */
	public void setTimeStamp(DateTime timeStamp) {
		this.timeStamp = timeStamp;
	}

	/**
	 * @return the subWalletType
	 */
	public SubWalletType getSubWalletType() {
		return subWalletType;
	}

	/**
	 * @param subWalletType
	 *            the subWalletType to set
	 */
	public void setSubWalletType(SubWalletType subWalletType) {
		this.subWalletType = subWalletType;
	}
	/**
	 * @return the transactionReversalId
	 */
	public long getTransactionReversalId() {
		return transactionReversalId;
	}
	/**
	 * @param transactionReversalId the transactionReversalId to set
	 */
	public void setTransactionReversalId(long transactionReversalId) {
		this.transactionReversalId = transactionReversalId;
	}
	/**
	 * @return the giftCode
	 */
	public String getGiftCode() {
		return giftCode;
	}
	/**
	 * @param giftCode the giftCode to set
	 */
	public void setGiftCode(String giftCode) {
		this.giftCode = giftCode;
	}
	/**
	 * @return the refundId
	 */
	public long getRefundId() {
		return refundId;
	}
	/**
	 * @param refundId the refundId to set
	 */
	public void setRefundId(long refundId) {
		this.refundId = refundId;
	}
}
