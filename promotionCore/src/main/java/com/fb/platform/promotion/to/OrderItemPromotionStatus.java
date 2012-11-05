/**
 * 
 */
package com.fb.platform.promotion.to;

/**
 * @author nehaga
 *
 */
public class OrderItemPromotionStatus {
	private OrderItemPromotionApplicationEnum orderItemPromotionApplication = OrderItemPromotionApplicationEnum.NOT_APPLIED;
	private int appliedQuantity;
	private int remainingQuantity;
	
	public OrderItemPromotionApplicationEnum getOrderItemPromotionApplication() {
		return orderItemPromotionApplication;
	}
	public void setOrderItemPromotionApplication(OrderItemPromotionApplicationEnum orderItemPromotionApplication) {
		this.orderItemPromotionApplication = orderItemPromotionApplication;
	}
	public int getAppliedQuantity() {
		return appliedQuantity;
	}
	public void setAppliedQuantity(int appliedQuantity) {
		this.appliedQuantity = appliedQuantity;
	}
	public int getRemainingQuantity() {
		return remainingQuantity;
	}
	public void setRemainingQuantity(int remainingQuantity) {
		this.remainingQuantity = remainingQuantity;
	}
	
	
}
