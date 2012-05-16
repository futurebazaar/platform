package com.fb.platform.wallet.manager.model.access;

import java.math.BigDecimal;
import javax.xml.datatype.XMLGregorianCalendar;

public class Transaction {

	private String transactionId;
	private String type;
	private SubWalletEnum subWallet;
	private BigDecimal amount;
	private XMLGregorianCalendar timestamp;
	private long orderId;
	private long paymentId;

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public SubWalletEnum getSubWallet() {
		return subWallet;
	}

	public void setSubWallet(SubWalletEnum subWallet) {
		this.subWallet = subWallet;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public XMLGregorianCalendar getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(XMLGregorianCalendar timestamp) {
		this.timestamp = timestamp;
	}

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public long getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(long paymentId) {
		this.paymentId = paymentId;
	}

}
