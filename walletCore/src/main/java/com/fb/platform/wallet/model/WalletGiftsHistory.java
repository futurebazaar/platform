package com.fb.platform.wallet.model;

import java.io.Serializable;

import com.fb.commons.to.Money;

public class WalletGiftsHistory implements Serializable {
	
	private long subTransactionId;
	private long giftId;
	private Money amount;
	/**
	 * @return the subTransactionId
	 */
	public long getSubTransactionId() {
		return subTransactionId;
	}
	/**
	 * @param subTransactionId the subTransactionId to set
	 */
	public void setSubTransactionId(long subTransactionId) {
		this.subTransactionId = subTransactionId;
	}
	/**
	 * @return the giftId
	 */
	public long getGiftId() {
		return giftId;
	}
	/**
	 * @param giftId the giftId to set
	 */
	public void setGiftId(long giftId) {
		this.giftId = giftId;
	}
	/**
	 * @return the amount
	 */
	public Money getAmount() {
		return amount;
	}
	/**
	 * @param amount the amount to set
	 */
	public void setAmount(Money amount) {
		this.amount = amount;
	}
	
	
}
