package com.fb.platform.promotion.product.dao;

import com.fb.platform.promotion.product.model.PromotionConfig;

/**
 * 
 * @author vinayak
 *
 */
public interface PromotionConfigDao {
	
	public PromotionConfig load(int promotionId);
	
}
