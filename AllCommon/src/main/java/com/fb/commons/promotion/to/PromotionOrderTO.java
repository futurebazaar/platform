/**
 * 
 */
package com.fb.commons.promotion.to;

import java.util.ArrayList;
import java.util.List;

/**
 * @author vinayak
 *
 */
public class PromotionOrderTO {

	private List<Integer> productIds; 
	private Double orderTotal;
	private Double shippingTotal;
	private Double paymentCharges;
	private String couponCode;
	private Integer userId;
	
	private Double discountValue;
	private String discountType;
	
	/**
	 * The promotions which are currently being applied on this order.
	 */
	private List<String> appliedPromotions = new ArrayList<String>();

	public List<Integer> getProductIds() {
		return productIds;
	}

	public void setProductIds(List<Integer> productIds) {
		this.productIds = productIds;
	}

	public Double getOrderTotal() {
		return orderTotal;
	}

	public void setOrderTotal(Double orderTotal) {
		this.orderTotal = orderTotal;
	}

	public Double getShippingTotal() {
		return shippingTotal;
	}

	public void setShippingTotal(Double shippingTotal) {
		this.shippingTotal = shippingTotal;
	}

	public Double getPaymentCharges() {
		return paymentCharges;
	}

	public void setPaymentCharges(Double paymentCharges) {
		this.paymentCharges = paymentCharges;
	}

	public String getCouponCode() {
		return couponCode;
	}

	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Double getDiscountValue() {
		return discountValue;
	}

	public void setDiscountValue(Double discountValue) {
		this.discountValue = discountValue;
	}

	public String getDiscountType() {
		return discountType;
	}

	public void setDiscountType(String discountType) {
		this.discountType = discountType;
	}

	public List<String> getAppliedPromotions() {
		return appliedPromotions;
	}

	public void setAppliedPromotions(List<String> appliedPromotions) {
		this.appliedPromotions = appliedPromotions;
	}

}
