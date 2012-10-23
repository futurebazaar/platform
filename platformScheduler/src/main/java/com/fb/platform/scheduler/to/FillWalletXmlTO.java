package com.fb.platform.scheduler.to;

import org.joda.time.DateTime;

import com.fb.commons.to.Money;

public class FillWalletXmlTO {
	
	long id;
	long walletId;
	long userId;
	String paymentMode;
	Money amount;
	String walletFillXml;
	DateTime createdDate;
	boolean status;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getWalletId() {
		return walletId;
	}
	public void setWalletId(long walletId) {
		this.walletId = walletId;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public String getPaymentMode() {
		return paymentMode;
	}
	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}
	public Money getAmount() {
		return amount;
	}
	public void setAmount(Money amount) {
		this.amount = amount;
	}
	public String getWalletFillXml() {
		return walletFillXml;
	}
	public void setWalletFillXml(String walletFillXml) {
		this.walletFillXml = walletFillXml;
	}
	public DateTime getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(DateTime createdDate) {
		this.createdDate = createdDate;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	
	
}
