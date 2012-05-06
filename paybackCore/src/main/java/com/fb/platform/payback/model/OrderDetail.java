package com.fb.platform.payback.model;

import java.math.BigDecimal;

import org.joda.time.DateTime;

public class OrderDetail {
	
	private long id;
	private String clientName;
	private String paymentMode;
	private String orderDate;
	private String referenceOrderId;
	private String supportState;
	private long clientId;
	private String clientDomainType;
	private DateTime timestamp;
	private String loyaltyCard;
	private BigDecimal amount;
	
	public long getId() {
		return id;
	}
	public DateTime getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(DateTime timestamp) {
		this.timestamp = timestamp;
	}
	public String getLoyaltyCard() {
		return loyaltyCard;
	}
	public void setLoyaltyCard(String loyaltyCard) {
		this.loyaltyCard = loyaltyCard;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getClientName() {
		return clientName;
	}
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	public String getPaymentMode() {
		return paymentMode;
	}
	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}
	public String getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}
	public String getReferenceOrderId() {
		return referenceOrderId;
	}
	public void setReferenceOrderId(String referenceOrderId) {
		this.referenceOrderId = referenceOrderId;
	}
	public String getSupportState() {
		return supportState;
	}
	public void setSupportState(String supportState) {
		this.supportState = supportState;
	}
	public long getClientId() {
		return clientId;
	}
	public void setClientId(long clientId) {
		this.clientId = clientId;
	}
	public String getClientDomainType() {
		return clientDomainType;
	}
	public void setClientDomainType(String clientDomainType) {
		this.clientDomainType = clientDomainType;
	}
	
	

}