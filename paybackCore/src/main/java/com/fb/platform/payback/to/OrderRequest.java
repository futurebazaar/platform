package com.fb.platform.payback.to;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

public class OrderRequest {

	private long orderId;
	private String referenceId;
	private BigDecimal amount;
	private String reason;
	private List<OrderItemRequest> orderItemRequest = new ArrayList<OrderItemRequest>();
	private String loyaltyCard;
	private DateTime txnTimestamp;
	private BigDecimal bonusPoints = BigDecimal.ZERO;
	
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
		return new DateTime(txnTimestamp.getYear(), txnTimestamp.getMonthOfYear(), txnTimestamp.getDayOfMonth(), 0, 0);
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
	public void setBonusPoints(BigDecimal bonusPoints){
		this.bonusPoints = bonusPoints;
	}
	public BigDecimal getBonusPoints(){
		return bonusPoints;
	}
	public String getReferenceId() {
		return referenceId;
	}
	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
	}
}
