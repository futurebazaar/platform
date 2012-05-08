package com.fb.platform.wallet.model;

import java.util.Date;

public abstract class SubWallet {
	
	double amount = 0.0;
	boolean isRefundable;
	Date expiryDate = null;
	/**
	 * @return the amount
	 */
	public double getAmount() {
		return amount;
	}
	/**
	 * @param amount the amount to set
	 */
	public void setAmount(double amount) {
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
