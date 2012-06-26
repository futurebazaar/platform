package com.fb.platform.payback.to;

import java.math.BigDecimal;

public class PaymentRequest {
	private BigDecimal amount;
	private String paymentMode;
	
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getPaymentMode() {
		return paymentMode;
	}
	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}
	
}