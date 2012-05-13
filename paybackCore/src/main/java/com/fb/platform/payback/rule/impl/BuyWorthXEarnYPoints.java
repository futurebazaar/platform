package com.fb.platform.payback.rule.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.StringTokenizer;

import com.fb.platform.payback.rule.PointsRule;
import com.fb.platform.payback.rule.PointsRuleConfigConstants;
import com.fb.platform.payback.rule.RuleConfiguration;
import com.fb.platform.payback.to.OrderRequest;

public class BuyWorthXEarnYPoints implements PointsRule {
	
	private String offerDay;
	private List<Long> excludedCategoryList;
	private BigDecimal bonusPoints;
	private BigDecimal minimumOrderValue;

	@Override
	public void init(RuleConfiguration ruleConfig) {
		this.offerDay = ruleConfig.getConfigItemValue(PointsRuleConfigConstants.OFFER_DAY);
		this.bonusPoints = new BigDecimal(ruleConfig.getConfigItemValue(PointsRuleConfigConstants.BONUS_POINTS));
		this.minimumOrderValue = new BigDecimal(ruleConfig.getConfigItemValue(PointsRuleConfigConstants.MIN_ORDER_VALUE));
		StringTokenizer categoryTokenizer = new StringTokenizer(ruleConfig.getConfigItemValue(PointsRuleConfigConstants.EXCLUDED_CATEGORY_LIST), ",");
		while (categoryTokenizer.hasMoreTokens()){
			this.excludedCategoryList.add(Long.parseLong(categoryTokenizer.nextToken()));
		}
		
	}
	
	@Override
	public boolean isApplicable(OrderRequest request){
		if (request.getTxnTimestamp().dayOfWeek().getAsText().equalsIgnoreCase(this.offerDay)){
			return false;
		}
		if (minimumOrderValue.compareTo(request.getAmount()) == 1){
			return false;
		}
		if (excludedCategoryList != null && !excludedCategoryList.isEmpty() && request.isInExcludedCategory(excludedCategoryList)){
			return false;
		}
		
		return true;
	}
	
	@Override
	public BigDecimal execute(OrderRequest request){
		return this.bonusPoints;
	}

	@Override
	public boolean isBonus() {
		return true;
	}

}
