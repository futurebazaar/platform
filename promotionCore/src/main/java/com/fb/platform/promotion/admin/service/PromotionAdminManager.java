package com.fb.platform.promotion.admin.service;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fb.platform.promotion.admin.to.CreatePromotionRequest;
import com.fb.platform.promotion.admin.to.CreatePromotionResponse;
import com.fb.platform.promotion.admin.to.FetchRuleRequest;
import com.fb.platform.promotion.admin.to.FetchRuleResponse;
import com.fb.platform.promotion.admin.to.SearchPromotionRequest;
import com.fb.platform.promotion.admin.to.SearchPromotionResponse;
import com.fb.platform.promotion.admin.to.UpdatePromotionRequest;
import com.fb.platform.promotion.admin.to.UpdatePromotionResponse;
import com.fb.platform.promotion.admin.to.ViewPromotionRequest;
import com.fb.platform.promotion.admin.to.ViewPromotionResponse;

/**
 * @author nehaga
 *
 */
public interface PromotionAdminManager {
	
	/**
	 * This function returns a list of rules and their corresponding rule config items.
	 * @param fetchRuleRequest
	 * @return
	 */
	public FetchRuleResponse fetchRules(FetchRuleRequest fetchRuleRequest); 
	
	/**
	 * This function creates a new promotion and returns the promotion Id.
	 * @param createPromotionRequest
	 * @return
	 */
	public CreatePromotionResponse createPromotion(CreatePromotionRequest createPromotionRequest);
	
	/**
	 * This function fetches a list of promotions that meet the specified search criteria.
	 * @param searchPromotionRequest
	 * @return
	 */
	public SearchPromotionResponse searchPromotion(SearchPromotionRequest searchPromotionRequest);
	
	/**
	 * Fetching a complete view of promotion. Including limits and rule config.
	 * @param viewPromotionRequest
	 * @return
	 */
	public ViewPromotionResponse viewPromotion(ViewPromotionRequest viewPromotionRequest);
	
	/**
	 * This function updates an existing promotion.
	 * @param promotion
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public UpdatePromotionResponse updatePromotion(UpdatePromotionRequest updatePromotionRequest);

}
