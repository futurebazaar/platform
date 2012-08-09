/**
 * 
 */
package com.fb.platform.promotion.product.to;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fb.platform.promotion.model.OrderDiscount;
import com.fb.platform.promotion.model.Promotion;

/**
 * @author vinayak
 *
 */
public class ApplyAutoPromotionResponse implements Serializable {

	private OrderDiscount orderDiscount;
	private List<Promotion> appliedPromotions = new ArrayList<Promotion>();
	private ApplyAutoPromotionResponseStatusEnum applyAutoPromotionStatus;
	private String sessionToken;

	public OrderDiscount getOrderDiscount() {
		return orderDiscount;
	}
	public void setOrderDiscount(OrderDiscount orderDiscount) {
		this.orderDiscount = orderDiscount;
	}
	public List<Promotion> getAppliedPromotions() {
		return appliedPromotions;
	}
	public void setAppliedPromotions(List<Promotion> appliedPromotions) {
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

}
