package com.fb.platform.promotion.interfaces;


import java.util.List;

import com.fb.platform.promotion.to.*;



/**
 * @author Keith Fernandez
 *
 */

public interface PromotionService {

	public boolean isCouponValid(String couponCode);
	
	public void applyPromotion(PromotionTO promo);
	
	public PromotionResult checkPromotion();
	
	public PromotionResult applyAllPromotions(OrderTO order, UserTO user, List<ProductTO> products);
	
	public List<PromotionTO> getAllApplicablePromotions(OrderTO order, UserTO user, List<ProductTO> products);
	
	public PromotionResult applyCouponOnOrder(String couponCode);
	
	public PromotionResult applyCouponOnProduct(String couponCode);
	
	public PromotionResult applyCouponOnCategory(String couponCode);
	
	public PromotionResult applyPromotionRsOff(PromotionTO promo,int orderValue);
	
	public PromotionResult applyPercentageOff(PromotionTO promo,int orderValue);
	
}
