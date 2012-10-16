package com.fb.platform.wallet.manager.model.access;

import java.math.BigDecimal;

import org.joda.time.DateTime;

import com.fb.commons.to.Money;

public class WalletDetails {

	private long walletId;
	private BigDecimal cashAmount;
	private BigDecimal refundAmount;
	private BigDecimal giftAmount;
	private BigDecimal totalAmount;
	private Money refundableAmount;
	private Money giftExpiryAmt1 = new Money(new BigDecimal("0.00"));
	private DateTime giftExpiryDt1 = DateTime.now();
	private Money giftExpiryAmt2 = new Money(new BigDecimal("0.00"));
	private DateTime giftExpiryDt2 = DateTime.now();
	

	public long getWalletId(){
		return walletId;
	}
	
	public void setWalletId(long walletId){
		this.walletId = walletId;
	}
	
	public BigDecimal getCashAmount() {
		return cashAmount;
	}

	public void setCashAmount(BigDecimal cashAmount) {
		this.cashAmount = cashAmount;
	}

	public BigDecimal getRefundAmount() {
		return refundAmount;
	}

	public void setRefundAmount(BigDecimal refundAmount) {
		this.refundAmount = refundAmount;
	}

	public BigDecimal getGiftAmount() {
		return giftAmount;
	}

	public void setGiftAmount(BigDecimal giftAmount) {
		this.giftAmount = giftAmount;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Money getRefundableAmount() {
		return refundableAmount;
	}

	public void setRefundableAmount(Money refundableAmount) {
		this.refundableAmount = refundableAmount;
	}

	public Money getGiftExpiryAmt1() {
		return giftExpiryAmt1;
	}

	public void setGiftExpiryAmt1(Money giftExpiryAmt1) {
		this.giftExpiryAmt1 = giftExpiryAmt1;
	}

	public DateTime getGiftExpiryDt1() {
		return giftExpiryDt1;
	}

	public void setGiftExpiryDt1(DateTime giftExpiryDt1) {
		this.giftExpiryDt1 = giftExpiryDt1;
	}

	public Money getGiftExpiryAmt2() {
		return giftExpiryAmt2;
	}

	public void setGiftExpiryAmt2(Money giftExpiryAmt2) {
		this.giftExpiryAmt2 = giftExpiryAmt2;
	}

	public DateTime getGiftExpiryDt2() {
		return giftExpiryDt2;
	}

	public void setGiftExpiryDt2(DateTime giftExpiryDt2) {
		this.giftExpiryDt2 = giftExpiryDt2;
	}
	

}
