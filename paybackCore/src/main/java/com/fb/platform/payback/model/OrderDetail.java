package com.fb.platform.payback.model;

public class OrderDetail {
	
	private long id;
	private String clientName;
	private String paymentMode;
	private String orderDate;
	private String reference_order_id;
	private String supportState;
	private long clientId;
	private String clientDomainType;
	
	public long getId() {
		return id;
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
	public String getReference_order_id() {
		return reference_order_id;
	}
	public void setReference_order_id(String reference_order_id) {
		this.reference_order_id = reference_order_id;
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