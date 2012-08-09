/**
 * 
 */
package com.fb.platform.promotion.model.coupon;

import com.fb.platform.promotion.model.OrderDiscount;
import com.fb.platform.promotion.model.Promotion;
import com.fb.platform.promotion.rule.PromotionRule;
import com.fb.platform.promotion.to.OrderRequest;
import com.fb.platform.promotion.to.PromotionStatusEnum;

/**
 * @author vinayak
 *
 */
public class CouponPromotion extends Promotion {

	private PromotionRule rule;

	public PromotionStatusEnum isApplicable(OrderRequest request,int userId,boolean isCouponCommitted) {
		if (!isActive) {
			return PromotionStatusEnum.INACTIVE_COUPON;
		}
		boolean withinDates = dates.isWithinDates();
		if (!withinDates) {
			return PromotionStatusEnum.COUPON_CODE_EXPIRED;
		}
		
		if(null!=request)
			return rule.isApplicable(request,userId,isCouponCommitted);
		
		return PromotionStatusEnum.SUCCESS;
	}

	public OrderDiscount apply(OrderRequest request) {
		OrderDiscount orderDiscount = new OrderDiscount();
		orderDiscount.setOrderRequest(request);
		
		return rule.execute(orderDiscount);
	}

	public void setRule(PromotionRule rule) {
		this.rule = rule;
	}

}
