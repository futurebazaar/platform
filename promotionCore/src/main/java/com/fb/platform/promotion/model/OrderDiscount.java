package com.fb.platform.promotion.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import com.fb.platform.promotion.to.OrderItem;
import com.fb.platform.promotion.to.OrderRequest;

public class OrderDiscount implements Serializable {

	private OrderRequest orderRequest = null;
	private BigDecimal orderDiscountValue = BigDecimal.ZERO;
	private List<Integer> promotions = new ArrayList<Integer>();
	
	public OrderRequest getOrderRequest() {
		return orderRequest;
	}
	public void setOrderRequest(OrderRequest orderRequest) {
		this.orderRequest = orderRequest;
	}
	public BigDecimal getOrderDiscountValue() {
		return orderDiscountValue;
	}
	public void setOrderDiscountValue(BigDecimal orderDiscountValue) {
		this.orderDiscountValue = orderDiscountValue;
	}
	
	public OrderDiscount distributeDiscountOnOrder(OrderDiscount orderDiscount,List<Integer> brands, List<Integer> includeCategoryList, List<Integer> excludeCategoryList){ 

		OrderRequest orderRequest = orderDiscount.getOrderRequest();
		BigDecimal totalRemainingDiscountOnOrder = orderDiscount.getOrderDiscountValue();
		List<OrderItem> notLockedAplicableOrderItems = new ArrayList<OrderItem>();
		BigDecimal totalOrderValueForRemainingApplicableItems = BigDecimal.ZERO;
		
		for (OrderItem eachOrderItemInRequest : orderRequest.getOrderItems()) {
			if(eachOrderItemInRequest.isApplicableToOrderItem(eachOrderItemInRequest,brands,includeCategoryList,excludeCategoryList)){
				if(eachOrderItemInRequest.isLocked()){
					totalRemainingDiscountOnOrder = totalRemainingDiscountOnOrder.subtract(eachOrderItemInRequest.getTotalDiscount());
				}else{
					notLockedAplicableOrderItems.add(eachOrderItemInRequest);
					totalOrderValueForRemainingApplicableItems = totalOrderValueForRemainingApplicableItems.add(eachOrderItemInRequest.getPrice());
				}
			}
		}
		
		return distributeRemainingDiscountOnRemainingOrderItems(orderDiscount, totalRemainingDiscountOnOrder, notLockedAplicableOrderItems, 
				totalOrderValueForRemainingApplicableItems);
	}
	
	private OrderDiscount distributeRemainingDiscountOnRemainingOrderItems(OrderDiscount orderDiscount, BigDecimal totalRemainingDiscountOnOrder,
			List<OrderItem> notLockedAplicableOrderItems,
			BigDecimal totalOrderValueForRemainingApplicableItems) {
		
		for (OrderItem eachOrderItemInRequest : notLockedAplicableOrderItems) {
			BigDecimal orderItemDiscount = new BigDecimal(0);
			BigDecimal orderItemPrice = eachOrderItemInRequest.getPrice();
			orderItemDiscount = (orderItemPrice.multiply(totalRemainingDiscountOnOrder)).divide(totalOrderValueForRemainingApplicableItems, 2, RoundingMode.HALF_EVEN).setScale(0, RoundingMode.HALF_EVEN);
			eachOrderItemInRequest.setTotalDiscount(orderItemDiscount);
			/*
			 * after setting the right discount for the current order item,
			 * Subtract the discount (set in the current order item) from the total discount to be distributed and 
			 * Subtract the order item value from the total applicable unlocked order item values
			 * 
			 * For Example:
			 *  discountOnOrderItemA = (priceOfA / priceOfA + priceOfB + priceOfC + priceOfD) X totalDiscountToBeDistributedOnABCD ;
			 *  totalDiscountToBeDistributedOnBCD = totalDiscountToBeDistributedOnABCD - discountOnOrderItemA ;
			 *  
			 *  discountOnOrderItemB = (priceOfB / priceOfB + priceOfC + priceOfD) X totalDiscountToBeDistributedOnBCD ;
			 */
			totalOrderValueForRemainingApplicableItems = totalOrderValueForRemainingApplicableItems.subtract(orderItemPrice);
			totalRemainingDiscountOnOrder = totalRemainingDiscountOnOrder.subtract(orderItemDiscount);
		}
		return orderDiscount;
	}

	public void promotionApplied(int promotionId) {
		this.promotions.add(promotionId);
	}

	public List<Integer> getAppliedPromotions() {
		return this.promotions;
	}
}
