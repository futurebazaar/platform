package com.fb.platform.promotion.admin.service;

import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fb.platform.promotion.rule.RulesEnum;
import com.fb.platform.promotion.to.PromotionTO;

/**
 * @author nehaga
 *
 */

@Transactional
public interface PromotionAdminService {

	/**
	 * This function returns a list of Promotion rules.
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public List<RulesEnum> getAllPromotionRules();
	
	/**
	 * This function creates a new promotion.
	 * @param promotion
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public int createPromotion(PromotionTO promotionTO);

}
