package com.fb.platform.payback.rule.impl;

import java.math.BigDecimal;

import org.joda.time.DateTime;
import org.springframework.dao.DataAccessException;

import com.fb.platform.payback.cache.ListCacheAccess;
import com.fb.platform.payback.cache.PointsCacheConstants;
import com.fb.platform.payback.dao.ListDao;
import com.fb.platform.payback.rule.PointsRule;
import com.fb.platform.payback.rule.PointsRuleConfigConstants;
import com.fb.platform.payback.rule.RuleConfiguration;
import com.fb.platform.payback.to.OrderItemRequest;
import com.fb.platform.payback.to.OrderRequest;
import com.fb.platform.payback.util.PointsUtil;

public class BuyHeroDealEarnYPoints implements PointsRule {

	private BigDecimal earnFactor;
	private BigDecimal earnRatio;
	private DateTime validFrom;
	private DateTime validTill;
	private ListDao listDao;
	private ListCacheAccess listCacheAccess;
	private PointsUtil pointsUtil;
	
	public void setListDao(ListDao listDao){
		this.listDao = listDao;
	}

	public void setListCacheAccess(ListCacheAccess listCacheAccess) {
		this.listCacheAccess = listCacheAccess;
	}
	
	public void setPointsUtil(PointsUtil pointsUtil){
		this.pointsUtil = pointsUtil;
	}
	
	@Override
	public void init(RuleConfiguration ruleConfig) {
		
		this.earnFactor = new BigDecimal(ruleConfig.getConfigItemValue(PointsRuleConfigConstants.POINTS_FACTOR));
		this.earnRatio =  new BigDecimal(ruleConfig.getConfigItemValue(PointsRuleConfigConstants.EARN_RATIO));
		
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
		
		if (itemRequest.getSellerRateChartId() != getHeroDealSellerRateChart(request.getTxnTimestamp())){
			return false;
		}
		
		return true;
	}

	private Long getHeroDealSellerRateChart(DateTime orderDate) {
		String bookingDate = pointsUtil.convertDateToFormat(orderDate, "yyyy-MM-dd");
		String key = PointsCacheConstants.HERO_DEAL +  "#" + bookingDate;
		Long sellerRateChartId = listCacheAccess.get(key);
		if (sellerRateChartId == null){
			try {
				sellerRateChartId = listDao.getHeroDealSellerRateChart(orderDate);
			} catch (DataAccessException e) {
				e.printStackTrace();
				return new Long(-1);
			}

			if (sellerRateChartId != null) {
				cacheHeroDeal(sellerRateChartId, key);
			} else {
				return new Long(-1);
			}
	
		}
		
		return sellerRateChartId;
	}

	//Caches Deal Id and Deal Date
	private void cacheHeroDeal(Long sellerRateChartId, String key) {
		try {
			listCacheAccess.lock(key);
			if (listCacheAccess.get(key) == null) {
				listCacheAccess.put(key, sellerRateChartId);
			}
		} finally {
			listCacheAccess.unlock(key);
		}
		
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
