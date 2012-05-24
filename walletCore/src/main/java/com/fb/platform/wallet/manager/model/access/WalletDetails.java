package com.fb.platform.wallet.manager.model.access;

import java.math.BigDecimal;

public class WalletDetails {

	long walletId;
	private BigDecimal cashAmount;
	private BigDecimal refundAmount;
	private BigDecimal giftAmount;
	private BigDecimal totalAmount;

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

}
