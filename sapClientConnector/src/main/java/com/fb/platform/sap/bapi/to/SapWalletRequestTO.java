package com.fb.platform.sap.bapi.to;

import java.math.BigDecimal;

import org.joda.time.DateTime;

public class SapWalletRequestTO {
	
	private DateTime transactionTimestamp;
	private String name;
	private String orderID;
	private String loginID;
	private long walletNumber;
	private String paymentMode;
	private BigDecimal amount;
	private String transactionType;
	
	public DateTime getTransactionTimestamp() {
		return transactionTimestamp;
	}
	public void setTransactionTimestamp(DateTime transactionTimestamp) {
		this.transactionTimestamp = transactionTimestamp;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getOrderID() {
		return orderID;
	}
	public void setOrderID(String orderID) {
		this.orderID = orderID;
	}
	public String getLoginID() {
		return loginID;
	}
	public void setLoginID(String loginID) {
		this.loginID = loginID;
	}
	public long getWalletNumber() {
		return walletNumber;
	}
	public void setWalletNumber(long walletNumber) {
		this.walletNumber = walletNumber;
	}
	public String getPaymentMode() {
		return paymentMode;
	}
	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
	
	@Override
	public String toString() {
		return "sapWalletRequestTO [transactionTimestamp="
				+ transactionTimestamp + ", name=" + name + ", orderID="
				+ orderID + ", loginID=" + loginID + ", walletNumber="
				+ walletNumber + ", paymentMode=" + paymentMode + ", amount="
				+ amount + ", transactionType=" + transactionType + "]";
	}

}
