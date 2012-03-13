package com.fb.platform.promotion.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class PromotionOrder{

	private static final Gson gson = new Gson();
	private List<Integer> productIds; 
	private double orderTotal;
	private double shippingTotal;
	private double paymentCharges;
	private String couponCode;
	private int userId;
	
	private double discountValue;
	private String discountType;
	
	/**
	 * The promotions which are currently being applied on this order.
	 */
	private List<String> appliedPromotions = new ArrayList<String>();
	
	/**
	 * What promotions have already been used by the user.
	 */
	
	
	public void addAppliedPromotion(String promotion){
		appliedPromotions.add(promotion);
	}
	
	public double getOrderTotal() {
		return orderTotal;
	}

	public void setOrderTotal(double orderTotal) {
		this.orderTotal = orderTotal;
	}

	public double getShippingTotal() {
		return shippingTotal;
	}

	public void setShippingTotal(double shippingTotal) {
		this.shippingTotal = shippingTotal;
	}

	public double getPaymentCharges() {
		return paymentCharges;
	}

	public void setPaymentCharges(double paymentCharges) {
		this.paymentCharges = paymentCharges;
	}

	public static PromotionOrder fromJson(JsonObject postData) {
		PromotionOrder promotionOrder = gson.fromJson(postData, PromotionOrder.class);
		return promotionOrder;

		
	}

	public List<Integer> getProductIds() {
		return productIds;
	}

	public void setProductIds(List<Integer> productIds) {
		this.productIds = productIds;
	}

//	@Override
//	public JsonObject toJson() throws Exception {
//		String jsonString = gson.toJson(this);
//		JsonParser parser = new JsonParser();
//		JsonObject jsonObject = (JsonObject) parser.parse(jsonString);
//		return jsonObject;
//	}

	
	public String getCouponCode() {
		return couponCode;
	}

	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public double getDiscountValue() {
		return discountValue;
	}

	public void setDiscountValue(double discountValue) {
		this.discountValue = discountValue;
	}

	public String getDiscountType() {
		return discountType;
	}

	public void setDiscountType(String discountOn) {
		this.discountType = discountOn;
	} 
	
}
