/**
 * 
 */
package com.fb.platform.promotion.product.to;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fb.platform.promotion.model.OrderDiscount;
import com.fb.platform.promotion.product.model.promotion.AutoPromotion;

/**
 * @author vinayak
 *
 */
public class ApplyAutoPromotionResponse implements Serializable {

	private OrderDiscount orderDiscount;
	private List<AutoPromotion> appliedPromotions = new ArrayList<AutoPromotion>();
	private ApplyAutoPromotionResponseStatusEnum applyAutoPromotionStatus;
	private String sessionToken;
	private Map<Integer, Boolean> appliedPromotionStatuses = null; 

	public OrderDiscount getOrderDiscount() {
		return orderDiscount;
	}
	public void setOrderDiscount(OrderDiscount orderDiscount) {
		this.orderDiscount = orderDiscount;
	}
	public List<AutoPromotion> getAppliedPromotions() {
		return appliedPromotions;
	}
	public void setAppliedPromotions(List<AutoPromotion> appliedPromotions) {
		this.appliedPromotions = appliedPromotions;
	}
	public String getSessionToken() {
		return sessionToken;
	}
	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}
	public ApplyAutoPromotionResponseStatusEnum getApplyAutoPromotionStatus() {
		return applyAutoPromotionStatus;
	}
	public void setApplyAutoPromotionStatus(
			ApplyAutoPromotionResponseStatusEnum applyAutoPromotionStatus) {
		this.applyAutoPromotionStatus = applyAutoPromotionStatus;
	}
	public Map<Integer, Boolean> getAppliedPromotionStatuses() {
		return appliedPromotionStatuses;
	}
	public void setAppliedPromotionStatuses(
			Map<Integer, Boolean> appliedPromotionStatuses) {
		this.appliedPromotionStatuses = appliedPromotionStatuses;
	}

}
