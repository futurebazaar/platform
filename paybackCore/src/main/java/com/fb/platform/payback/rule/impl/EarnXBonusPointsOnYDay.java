package com.fb.platform.payback.rule.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.StringTokenizer;

import com.fb.platform.payback.rule.PointsRule;
import com.fb.platform.payback.rule.PointsRuleConfigConstants;
import com.fb.platform.payback.rule.RuleConfiguration;
import com.fb.platform.payback.to.OrderRequest;

public class EarnXBonusPointsOnYDay implements PointsRule {
	
	private String offerDay;
	private BigDecimal earnFactor;
	private List<Long> excludedCategoryList;
	private BigDecimal earnRatio;

	@Override
	public void init(RuleConfiguration ruleConfig) {
		this.offerDay = ruleConfig.getConfigItemValue(PointsRuleConfigConstants.OFFER_DAY);
		this.earnFactor = new BigDecimal(ruleConfig.getConfigItemValue(PointsRuleConfigConstants.POINTS_FACTOR));
		this.earnRatio =  new BigDecimal(ruleConfig.getConfigItemValue(PointsRuleConfigConstants.EARN_RATIO));
		StringTokenizer categoryTokenizer = new StringTokenizer(ruleConfig.getConfigItemValue(PointsRuleConfigConstants.EXCLUDED_CATEGORY_LIST), ",");
		while (categoryTokenizer.hasMoreTokens()){
			this.excludedCategoryList.add(Long.parseLong(categoryTokenizer.nextToken()));
		}
	}
	
	@Override
	public boolean isApplicable(OrderRequest request){
		if (!request.getTxnTimestamp().dayOfWeek().getAsText().equals(this.offerDay)){
			return false;
		}
		if (this.excludedCategoryList != null && !this.excludedCategoryList.isEmpty() && request.isInExcludedCategory(excludedCategoryList)){
			return false;
		}
		return true;
	}

	@Override
	public BigDecimal execute(OrderRequest request) {
		return this.earnRatio.multiply(this.earnFactor).multiply(request.getAmount());
	}

	@Override
	public boolean isBonus() {
		return false;
	}

}
