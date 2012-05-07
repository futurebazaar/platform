package com.fb.platform.payback.to;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class StorePointsRequest {
	
	private String txnActionCode;
	private long orderId;
	private BigDecimal amount;
	private String reason;
	private List<StorePointsItemRequest> storePointsItemRequest = new ArrayList<StorePointsItemRequest>();
	private String loyaltyCard;
	
	public String getTxnActionCode() {
		return txnActionCode;
	}
	public void setTxnActionCode(String txnActionCode) {
		this.txnActionCode = txnActionCode;
	}
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
	public List<StorePointsItemRequest> getStorePointsItemRequest() {
		return storePointsItemRequest;
	}
	public void setStorePointsItemRequest(List<StorePointsItemRequest> storePointsItemRequest) {
		this.storePointsItemRequest = storePointsItemRequest;
	}
	public String getLoyaltyCard() {
		return loyaltyCard;
	}
	public void setLoyaltyCard(String loyaltyCard) {
		this.loyaltyCard = loyaltyCard;
	}	
	
}
