/**
 * 
 */
package com.fb.platform.promotion.rule;

import java.io.Serializable;
import java.util.List;

import com.fb.platform.promotion.model.OrderDiscount;
import com.fb.platform.promotion.rule.config.RuleConfigItemDescriptor;
import com.fb.platform.promotion.rule.config.RuleConfiguration;
import com.fb.platform.promotion.rule.metadata.RuleConfigMetadata;
import com.fb.platform.promotion.to.OrderRequest;
import com.fb.platform.promotion.to.PromotionStatusEnum;


/**
 * @author vinayak
 *
 */
public interface PromotionRule extends Serializable {

	/**
	 * Called at the time of rule objects creation.
	 * @param ruleConfig The configuration items from DB.
	 */
	public void init(RuleConfiguration ruleConfig);

	/**
	 * @return true if the rule is applicable on the request
	 */
	public PromotionStatusEnum isApplicable(OrderRequest request,int userId,boolean isCouponCommitted);
	
	/**
	 * Applies the rule on the request.
	 * @param request
	 */
	public OrderDiscount execute(OrderDiscount orderDiscount);
	
	/**
	 * This function returns a list of RuleConfigDescriptors for each rule.
	 * @return
	 */
	public List<RuleConfigItemDescriptor> getRuleConfigs();
	
	/**
	 * This function returns Metadata information related to each rule
	 * @return
	 */
	public RuleConfigMetadata getRuleConfigMetadata();
	
	
}
