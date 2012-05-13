package com.fb.platform.payback.to;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

public class OrderRequest {

	private long orderId;
	private BigDecimal amount;
	private String reason;
	private List<OrderItemRequest> orderItemRequest = new ArrayList<OrderItemRequest>();
	private String loyaltyCard;
	private DateTime txnTimestamp;
	private boolean isBonus;
	
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
	public List<OrderItemRequest> getOrderItemRequest() {
		return orderItemRequest;
	}
	public DateTime getTxnTimestamp() {
		return txnTimestamp;
	}
	public void setTxnTimestamp(DateTime txnTimestamp) {
		this.txnTimestamp = txnTimestamp;
	}
	public void setOrderItemRequest(List<OrderItemRequest> orderItemRequest) {
		this.orderItemRequest = orderItemRequest;
	}
	public String getLoyaltyCard() {
		return loyaltyCard;
	}
	public void setLoyaltyCard(String loyaltyCard) {
		this.loyaltyCard = loyaltyCard;
	}	
	public void setIsBonus(boolean isBonus){
		this.isBonus = isBonus;
	}
	public boolean isBonus(){
		return isBonus;
	}
	
	public boolean isInExcludedCategory(List<Long> categoryList){
		for (OrderItemRequest orderItem : orderItemRequest){
			if (categoryList.contains(orderItem.getCategoryId())){
				return true;
			}
		}
		return false;
	}
}
