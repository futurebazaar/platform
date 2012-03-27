/**
 * 
 */
package com.fb.platform.promotion.dao;

import com.fb.platform.promotion.model.GlobalPromotionUses;
import com.fb.platform.promotion.model.Promotion;
import com.fb.platform.promotion.model.UserPromotionUses;

/**
 * @author vinayak
 *
 */
public interface PromotionDao {

	public Promotion load(int promotionId);

	public GlobalPromotionUses loadGlobalUses(int promotionId);

	public UserPromotionUses loadUserUses(int promotionId, int userId);
}
