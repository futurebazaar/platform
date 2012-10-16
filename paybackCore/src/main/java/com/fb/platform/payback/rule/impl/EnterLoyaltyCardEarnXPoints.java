package com.fb.platform.payback.rule.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.joda.time.DateTime;

import com.fb.platform.payback.rule.PointsRule;
import com.fb.platform.payback.rule.PointsRuleConfigConstants;
import com.fb.platform.payback.rule.RuleConfiguration;
import com.fb.platform.payback.to.OrderItemRequest;
import com.fb.platform.payback.to.OrderRequest;
import com.fb.platform.payback.util.PointsUtil;

public class EnterLoyaltyCardEarnXPoints implements PointsRule {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BigDecimal earnRatio;
	private List<Long> excludedCategoryList = new ArrayList<Long>();
	private DateTime validFrom;
	private DateTime validTill;
	
	@Override
	public void init(RuleConfiguration ruleConfig) {
		this.earnRatio = new BigDecimal(ruleConfig.getConfigItemValue(PointsRuleConfigConstants.EARN_RATIO));
		String commaSeparatedExcludedCategoryList = ruleConfig.getConfigItemValue(PointsRuleConfigConstants.EXCLUDED_CATEGORY_LIST);
		if (commaSeparatedExcludedCategoryList != null && !commaSeparatedExcludedCategoryList.equals("")){
			StringTokenizer categoryTokenizer = new StringTokenizer(commaSeparatedExcludedCategoryList, ",");
			while (categoryTokenizer.hasMoreTokens()){
				this.excludedCategoryList.add(Long.parseLong(categoryTokenizer.nextToken()));
			}
		}
		String startsOn = ruleConfig.getConfigItemValue(PointsRuleConfigConstants.VALID_FROM);
		String endsOn = ruleConfig.getConfigItemValue(PointsRuleConfigConstants.VALID_TILL);
		
		this.validFrom = PointsUtil.getDateTimeFromString(startsOn, "yyyy-MM-dd");
		this.validTill = PointsUtil.getDateTimeFromString(endsOn, "yyyy-MM-dd");
	}

	@Override
	public boolean isApplicable(OrderRequest request, OrderItemRequest itemRequest) {
		if(request.getTxnTimestamp().toDate().compareTo(validFrom.toDate()) <0 || request.getTxnTimestamp().toDate().compareTo(validTill.toDate()) > 0){
			return false;
		}
		if (this.excludedCategoryList != null && !this.excludedCategoryList.isEmpty() && excludedCategoryList.contains(itemRequest.getCategoryId())) {
			return false;
		}
		return true;
	}

	@Override
	public BigDecimal execute(OrderRequest request, OrderItemRequest itemRequest) {
		return earnRatio.multiply(itemRequest.getAmount());
	}

	@Override
	public boolean allowNext(OrderRequest request) {
		if(request.getTxnTimestamp().toDate().compareTo(validFrom.toDate()) <0 || request.getTxnTimestamp().toDate().compareTo(validTill.toDate()) > 0){
			return false;
		}
		return true;
	}

}
