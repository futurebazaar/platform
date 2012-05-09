package com.fb.platform.wallet.model;

import java.util.Date;

import com.fb.commons.to.Money;

public abstract class SubWallet {
	
	Money amount = null;
	boolean isRefundable;
	Date expiryDate = null;
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
	/**
	 * @return the isRefundable
	 */
	public boolean isRefundable() {
		return isRefundable;
	}
	/**
	 * @param isRefundable the isRefundable to set
	 */
	public void setRefundable(boolean isRefundable) {
		this.isRefundable = isRefundable;
	}
	/**
	 * @return the expiryDate
	 */
	public Date getExpiryDate() {
		return expiryDate;
	}
	/**
	 * @param expiryDate the expiryDate to set
	 */
	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}
	
	
		
}
