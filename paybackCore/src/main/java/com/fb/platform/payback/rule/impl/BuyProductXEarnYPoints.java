package com.fb.platform.payback.rule.impl;

import java.math.BigDecimal;
import java.util.List;

import com.fb.platform.payback.rule.PointsRule;
import com.fb.platform.payback.rule.RuleConfiguration;
import com.fb.platform.payback.to.OrderRequest;

public class BuyProductXEarnYPoints implements PointsRule {

	private String offerDay;
	private List<Long> excludedCategoryList;
	private BigDecimal earnFactor;
	
	@Override
	public void init(RuleConfiguration ruleConfig) {
		
	}

	@Override
	public boolean isApplicable(OrderRequest request) {
		return false;
	}

	@Override
	public BigDecimal execute(OrderRequest request) {
		return BigDecimal.ZERO;
	}

	@Override
	public boolean isBonus() {
		return false;
	}

}
