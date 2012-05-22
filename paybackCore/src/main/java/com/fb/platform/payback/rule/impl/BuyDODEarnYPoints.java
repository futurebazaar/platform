package com.fb.platform.payback.rule.impl;

import java.math.BigDecimal;

import org.joda.time.DateTime;

import com.fb.platform.payback.dao.ListDao;
import com.fb.platform.payback.rule.PointsRule;
import com.fb.platform.payback.rule.PointsRuleConfigConstants;
import com.fb.platform.payback.rule.RuleConfiguration;
import com.fb.platform.payback.to.OrderItemRequest;
import com.fb.platform.payback.to.OrderRequest;
import com.fb.platform.payback.util.PointsUtil;

public class BuyDODEarnYPoints implements PointsRule {

	private BigDecimal earnFactor;
	private BigDecimal earnRatio;
	private DateTime validFrom;
	private DateTime validTill;
	private ListDao listDao;
	
	public void setListDao(ListDao listDao){
		this.listDao = listDao;
	}
	
	@Override
	public void init(RuleConfiguration ruleConfig) {
		
		this.earnFactor = new BigDecimal(ruleConfig.getConfigItemValue(PointsRuleConfigConstants.POINTS_FACTOR));
		this.earnRatio =  new BigDecimal(ruleConfig.getConfigItemValue(PointsRuleConfigConstants.EARN_RATIO));
		
		String startsOn = ruleConfig.getConfigItemValue(PointsRuleConfigConstants.VALID_FROM);
		String endsOn = ruleConfig.getConfigItemValue(PointsRuleConfigConstants.VALID_TILL);

		PointsUtil pointsUtil = new PointsUtil();
		this.validFrom = pointsUtil.getDateTimeFromString(startsOn, "yyyy-MM-dd");
		this.validTill = pointsUtil.getDateTimeFromString(endsOn, "yyyy-MM-dd");
		
	}

	@Override
	public boolean isApplicable(OrderRequest request, OrderItemRequest itemRequest) {
		if(request.getTxnTimestamp().toDate().compareTo(validFrom.toDate()) <0 || request.getTxnTimestamp().toDate().compareTo(validTill.toDate()) > 0){
			return false;
		}
		
		if (itemRequest.getSellerRateChartId() != getHeroDealSellerRateChart()){
			return false;
		}
		
		return true;
	}

	//need to implement the cache
	private Long getHeroDealSellerRateChart() {
		return listDao.getHeroDealSellerRateChart();
	}

	@Override
	public BigDecimal execute(OrderRequest request, OrderItemRequest itemRequest) {
		return earnFactor.multiply(earnRatio.multiply(itemRequest.getAmount()));
	}

	@Override
	public boolean allowNext() {
		return true;
	}
	
}
