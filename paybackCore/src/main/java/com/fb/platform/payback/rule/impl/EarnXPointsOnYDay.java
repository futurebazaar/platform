package com.fb.platform.payback.rule.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.StringTokenizer;

import org.joda.time.DateTime;

import com.fb.platform.payback.rule.PointsRule;
import com.fb.platform.payback.rule.PointsRuleConfigConstants;
import com.fb.platform.payback.rule.RuleConfiguration;
import com.fb.platform.payback.to.OrderItemRequest;
import com.fb.platform.payback.to.OrderRequest;
import com.fb.platform.payback.util.PointsUtil;

public class EarnXPointsOnYDay implements PointsRule {
	
	private String offerDay;
	private BigDecimal earnFactor;
	private List<Long> excludedCategoryList;
	private BigDecimal earnRatio;
	private DateTime validTill;
	private DateTime validFrom;
	@Override
	public void init(RuleConfiguration ruleConfig) {
		this.offerDay = ruleConfig.getConfigItemValue(PointsRuleConfigConstants.OFFER_DAY);
		this.earnFactor = new BigDecimal(ruleConfig.getConfigItemValue(PointsRuleConfigConstants.POINTS_FACTOR));
		this.earnRatio =  new BigDecimal(ruleConfig.getConfigItemValue(PointsRuleConfigConstants.EARN_RATIO));
		StringTokenizer categoryTokenizer = new StringTokenizer(ruleConfig.getConfigItemValue(PointsRuleConfigConstants.EXCLUDED_CATEGORY_LIST), ",");
		while (categoryTokenizer.hasMoreTokens()){
			this.excludedCategoryList.add(Long.parseLong(categoryTokenizer.nextToken()));
		}
		String startsOn = ruleConfig.getConfigItemValue(PointsRuleConfigConstants.VALID_FROM);
		String endsOn = ruleConfig.getConfigItemValue(PointsRuleConfigConstants.VALID_TILL);
		
		PointsUtil pointsUtil = new PointsUtil();
		this.validFrom = pointsUtil.getDateTimeFromString(startsOn, "yyyy-MM-dd");
		this.validTill = pointsUtil.getDateTimeFromString(endsOn, "yyyy-MM-dd");
		
	}
	
	@Override
	public boolean isApplicable(OrderRequest request, OrderItemRequest itemRequest){
		if(!request.getTxnTimestamp().isAfter(validFrom.getMillis()) || !request.getTxnTimestamp().isBefore(validTill.getMillis())){
			return false;
		}
		if (!request.getTxnTimestamp().dayOfWeek().getAsText().equals(this.offerDay)){
			return false;
		}
		if (this.excludedCategoryList != null && !this.excludedCategoryList.isEmpty() && request.isInExcludedCategory(excludedCategoryList)){
			return false;
		}
		return true;
	}

	@Override
	public BigDecimal execute(OrderRequest request, OrderItemRequest itemRequest) {
		return earnFactor.multiply(earnRatio.multiply(itemRequest.getAmount()));
	}

	@Override
	public boolean allowNext() {
		return false;
	}

}