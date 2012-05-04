package com.fb.platform.payback.model;

import java.math.BigDecimal;
import org.joda.time.DateTime;

public class PaymentDetail {

	private DateTime txnDate;
	private String transactionId;
	private BigDecimal amount;
	private String status;
	private long id;
	private long orderId;
	private String paymentMode;
	
	public DateTime getTxnDate() {
		return txnDate;
	}
	public void setTxnDate(DateTime txnDate) {
		this.txnDate = txnDate;
	}
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getOrderId() {
		return orderId;
	}
	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}
	public String getPaymentMode() {
		return paymentMode;
	}
	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}
}
