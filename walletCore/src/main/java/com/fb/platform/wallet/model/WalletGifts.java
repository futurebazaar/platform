package com.fb.platform.wallet.model;

import java.io.Serializable;

import org.joda.time.DateTime;

import com.fb.commons.to.Money;

public class WalletGifts implements Serializable {
	long giftId;
	long walletId;
	String giftCode = null;
	DateTime giftExpiry;
	boolean isExpired = false;
	Money amountRemaining ;
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
	 * @return the giftExpiry
	 */
	public DateTime getGiftExpiry() {
		return giftExpiry;
	}
	/**
	 * @param giftExpiry the giftExpiry to set
	 */
	public void setGiftExpiry(DateTime giftExpiry) {
		this.giftExpiry = giftExpiry;
	}
	/**
	 * @return the isExpired
	 */
	public boolean isExpired() {
		return isExpired;
	}
	/**
	 * @param isExpired the isExpired to set
	 */
	public void setExpired(boolean isExpired) {
		this.isExpired = isExpired;
	}
	/**
	 * @return the amountRemaining
	 */
	public Money getAmountRemaining() {
		return amountRemaining;
	}
	/**
	 * @param amountRemaining the amountRemaining to set
	 */
	public void setAmountRemaining(Money amountRemaining) {
		this.amountRemaining = amountRemaining;
	}
	/**
	 * @return the walletId
	 */
	public long getWalletId() {
		return walletId;
	}
	/**
	 * @param walletId the walletId to set
	 */
	public void setWalletId(long walletId) {
		this.walletId = walletId;
	}
	
	
}
