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

public class BuyProductXEarnYPoints implements PointsRule {

	//Multiple Category List
	private List<Long> gvCategoryList = new ArrayList<Long>();
	
	private String earnMap;
	private BigDecimal earnRatio;
	private DateTime validFrom;
	private DateTime validTill;
	private PointsUtil pointsUtil;
	private String clientName;
	
	//Identifies whether it is a gv or electronics or any other
	private String categoryType;
	
	@Override
	public void setPointsUtil(PointsUtil pointsUtil) {
		this.pointsUtil = pointsUtil;
	}
	
	@Override
	public void init(RuleConfiguration ruleConfig) {
		
		this.earnMap = ruleConfig.getConfigItemValue(PointsRuleConfigConstants.EARN_MAP);
		this.earnRatio =  new BigDecimal(ruleConfig.getConfigItemValue(PointsRuleConfigConstants.EARN_RATIO));
		
		//Different Categories
		String commaSeparatedgvCategoryList = ruleConfig.getConfigItemValue(PointsRuleConfigConstants.GV_CATEGORY_LIST);
		if (commaSeparatedgvCategoryList != null && !commaSeparatedgvCategoryList.equals("")){
			StringTokenizer categoryTokenizer = new StringTokenizer(commaSeparatedgvCategoryList, ",");
			while (categoryTokenizer.hasMoreTokens()){
				this.gvCategoryList.add(Long.parseLong(categoryTokenizer.nextToken().replaceAll(" ", "")));
			}
		}
		
		String startsOn = ruleConfig.getConfigItemValue(PointsRuleConfigConstants.VALID_FROM);
		String endsOn = ruleConfig.getConfigItemValue(PointsRuleConfigConstants.VALID_TILL);

		this.validFrom = pointsUtil.getDateTimeFromString(startsOn, "yyyy-MM-dd");
		this.validTill = pointsUtil.getDateTimeFromString(endsOn, "yyyy-MM-dd");
		
	}

	@Override
	public boolean isApplicable(OrderRequest request, OrderItemRequest itemRequest) {
		if(request.getTxnTimestamp().toDate().compareTo(validFrom.toDate()) <0 || request.getTxnTimestamp().toDate().compareTo(validTill.toDate()) > 0){
			return false;
		}
		
		//At the end
		if (gvCategoryList != null && !gvCategoryList.isEmpty() && gvCategoryList.contains(itemRequest.getCategoryId())){
			this.categoryType = "gv";
			return true;
		}
		
		return false;
	}

	@Override
	public BigDecimal execute(OrderRequest request, OrderItemRequest itemRequest) {
		BigDecimal earnFactor = new BigDecimal(pointsUtil.getMapValue(earnMap, categoryType));
		return earnFactor.multiply(earnRatio.multiply(itemRequest.getAmount()));
	}

	@Override
	public boolean allowNext() {
		return true;
	}

	@Override
	public void setClientName(String clientName) {
		this.clientName = clientName;
		
	}

}
