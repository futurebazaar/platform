package com.fb.platform.promotion.admin.service;

import java.util.List;

import org.joda.time.DateTime;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fb.platform.promotion.admin.to.PromotionTO;
import com.fb.platform.promotion.rule.RulesEnum;

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
	
	/**
	 * This function fetches a list of promotions that meet the specified search criteria.
	 * @param promotionName
	 * @param validFrom
	 * @param validTill
	 * @param startRecord
	 * @param batchSize
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public List<PromotionTO> searchPromotion(String promotionName, DateTime validFrom, DateTime validTill, int startRecord, int batchSize);

}
