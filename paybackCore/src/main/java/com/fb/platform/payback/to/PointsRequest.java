package com.fb.platform.payback.to;


public class PointsRequest {
	
	private String txnActionCode;
	private OrderRequest orderRequest;
	private String clientName;
	private String sessionToken;
	
	public OrderRequest getOrderRequest() {
		return orderRequest;
	}
	public void setOrderRequest(OrderRequest orderRequest) {
		this.orderRequest = orderRequest;
	}
	public String getTxnActionCode() {
		return txnActionCode;
	}
	public void setTxnActionCode(String txnActionCode) {
		this.txnActionCode = txnActionCode;
	}
	public String getClientName() {
		return clientName.replaceAll(" ", "").toUpperCase();
	}
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	public String getSessionToken() {
		return sessionToken;
	}
	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}
	
}
