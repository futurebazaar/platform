package com.fb.api.promotion.impl;

import java.util.List;

import com.fb.api.promotion.PromotionService;
import com.fb.commons.promotion.to.OrderTO;
import com.fb.commons.promotion.to.ProductTO;
import com.fb.commons.promotion.to.PromotionResult;
import com.fb.commons.promotion.to.PromotionTO;
import com.fb.commons.promotion.to.UserTO;

public class PromotionServiceImpl implements PromotionService{

	@Override
	public boolean isCouponValid(String couponCode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void applyPromotion(PromotionTO promo) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public PromotionResult checkPromotion() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PromotionResult applyAllPromotions(OrderTO order, UserTO user,
			List<ProductTO> products) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PromotionTO> getAllApplicablePromotions(OrderTO order,
			UserTO user, List<ProductTO> products) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PromotionResult applyCouponOnOrder(String couponCode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PromotionResult applyCouponOnProduct(String couponCode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PromotionResult applyCouponOnCategory(String couponCode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PromotionResult applyPromotionRsOff(PromotionTO promo,int orderValue) {
		if(promo.getDiscountType().equals("Rs Off")){
			PromotionResult pr = new PromotionResult();
			int discAmount = (int) (orderValue - promo.getDiscountValue());
			pr.setDiscountAmount(discAmount);
			return pr;
		}
		return null;
	}

	@Override
	public PromotionResult applyPercentageOff(PromotionTO promo,int orderValue) {
		if(promo.getDiscountType().equals("Percentage")){
			PromotionResult pr = new PromotionResult();
			int discAmount = (int) (orderValue * ( promo.getDiscountValue()/100));
			pr.setDiscountAmount(discAmount);
			return pr;
		}
		return null;
	}

	
}
