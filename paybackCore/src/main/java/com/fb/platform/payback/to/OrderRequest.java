package com.fb.platform.payback.to;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

public class OrderRequest {

	private long orderId;
	private long pointsHeaderId;
	private String referenceId;
	private BigDecimal amount =  BigDecimal.ZERO;
	private String reason;
	private List<OrderItemRequest> orderItemRequest = new ArrayList<OrderItemRequest>();
	private String loyaltyCard;
	private DateTime txnTimestamp;
	private BigDecimal bonusPoints = BigDecimal.ZERO;
	private BigDecimal txnPoints = BigDecimal.ZERO;
	private BigDecimal pointsValue = BigDecimal.ZERO;
	private BigDecimal totalTxnPoints = BigDecimal.ZERO;
	
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
	public BigDecimal getTxnPoints() {
		return txnPoints;
	}
	public void setTxnPoints(BigDecimal txnPoints) {
		this.txnPoints = txnPoints;
	}
	public long getPointsHeaderId() {
		return pointsHeaderId;
	}
	public void setPointsHeaderId(long pointsHeaderId) {
		this.pointsHeaderId = pointsHeaderId;
	}
	public BigDecimal getPointsValue() {
		return pointsValue;
	}
	public void setPointsValue(BigDecimal pointsValue) {
		this.pointsValue = pointsValue;
	}
	
	public BigDecimal getTotalTxnPoints() {
		return txnPoints.add(bonusPoints);
	}
}
