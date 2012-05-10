/**
 * 
 */
package com.fb.platform.promotion.admin.dao;

import java.sql.Timestamp;
import java.util.List;

import org.joda.time.DateTime;

import com.fb.commons.to.Money;
import com.fb.platform.promotion.admin.to.PromotionViewTO;
import com.fb.platform.promotion.rule.RulesEnum;

/**
 * @author vinayak
 *
 */
public interface PromotionAdminDao {


	int createPromotion(String name, String description, int ruleId, DateTime validFrom, 
			DateTime validTill, int active);
	
	void createPromotionLimitConfig(int promotionId, int maxUses, Money maxAmount,
			int maxUsesPerUser, Money maxAmountPerUser);
	
	void createPromotionRuleConfig(String name, String value, int promotionId, int ruleId);
	
	public List<PromotionViewTO> searchPromotion(String promotionName, DateTime validFrom, DateTime validTill, int startRecord, int batchSize);
	
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
