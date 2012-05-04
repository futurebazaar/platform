package com.fb.platform.payback.to;

import java.math.BigDecimal;

public class StoreBurnPointsRequest {
	
	private long orderId;
	private BigDecimal amount;
	private String reason;
	
	public long getOrderId() {
		return orderId;
	}
	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}	
	
}
