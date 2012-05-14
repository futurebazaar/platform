/**
 * 
 */
package com.fb.platform.promotion.admin.dao;

import java.util.List;

import org.joda.time.DateTime;

import com.fb.commons.to.Money;
import com.fb.platform.promotion.admin.to.PromotionTO;
import com.fb.platform.promotion.admin.to.SearchPromotionOrderBy;
import com.fb.platform.promotion.admin.to.SearchPromotionOrderByOrder;

/**
 * @author vinayak
 *
 */
public interface PromotionAdminDao {


	public int createPromotion(String name, String description, int ruleId, DateTime validFrom, 
			DateTime validTill, int active);
	
	public int createPromotionLimitConfig(int promotionId, int maxUses, Money maxAmount,
			int maxUsesPerUser, Money maxAmountPerUser);
	
	public int createPromotionRuleConfig(String name, String value, int promotionId, int ruleId);
	
	public List<PromotionTO> searchPromotion(String promotionName, DateTime validFrom, DateTime validTill, int isActive, SearchPromotionOrderBy orderBy,
			SearchPromotionOrderByOrder order, int startRecord, int batchSize);
	
	public int getPromotionCount(String promotionName, DateTime validFrom, DateTime validTill, int isActive);
	
	public PromotionTO viewPromotion(int promotionId);
	
	public int updatePromotion(int promotionId, String name, String description, DateTime validFrom, 
			DateTime validTill, int active, int ruleId);
	
	public int updatePromotionLimitConfig(int promotionId, int maxUses, Money maxAmount,
			int maxUsesPerUser, Money maxAmountPerUser);
	
	public int updatePromotionRuleConfig(String name, String value, int promotionId);
	
	public int deletePromotionRuleConfig(int promotionId);
	
	public int getCouponCount(int promotionId);
	
	boolean createCoupon(String couponCode,int promotionId, int maxUsesPerCoupon,String appliedOn,String DiscountType);
	
	boolean createCouponUser(String couponCode, int userId);
	
	/**
	 * To be done from orders table
	 * @return
	 */
	boolean createCouponUses();
	
	/**
	 * To be done from orders table
	 * @return
	 */
	boolean createPromotionUses();
}
