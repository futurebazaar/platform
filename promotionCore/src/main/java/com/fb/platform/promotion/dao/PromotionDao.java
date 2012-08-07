/**
 * 
 */
package com.fb.platform.promotion.dao;

import java.math.BigDecimal;
import java.util.List;

import com.fb.platform.promotion.model.GlobalPromotionUses;
import com.fb.platform.promotion.model.Promotion;
import com.fb.platform.promotion.model.UserPromotionUses;
import com.fb.platform.promotion.model.UserPromotionUsesEntry;

/**
 * @author vinayak
 *
 */
public interface PromotionDao {

	public Promotion load(int promotionId);

	public GlobalPromotionUses loadGlobalUses(int promotionId);

	public UserPromotionUses loadUserUses(int promotionId, int userId);
	
	public boolean updateUserUses(int promotionId, int userId, BigDecimal valueApplied, int orderId);
	
	public boolean releasePromotion(int promotionId, int userId, int orderId);
	
	public boolean isPromotionApplicable(int promotionId, int userId, int orderId);
	
	public UserPromotionUsesEntry load(int promotionId, int userId, int orderId);

	public List<Integer> loadLiveAutoPromotionIds();

}
