package com.fb.platform.payback.rule.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

import com.fb.platform.payback.rule.PointsRule;
import com.fb.platform.payback.rule.PointsRuleConfigConstants;
import com.fb.platform.payback.rule.RuleConfiguration;
import com.fb.platform.payback.to.OrderItemRequest;
import com.fb.platform.payback.to.OrderRequest;
import com.fb.platform.payback.to.PaymentRequest;
import com.fb.platform.payback.util.PointsUtil;

public class EarnXPointsOnYPaymentMode implements PointsRule {
	private BigDecimal earnRatio;
	private BigDecimal earnFactor;
	private PointsUtil pointsUtil;
	private List<Long> includedCategoryList = new ArrayList<Long>();
	private DateTime validFrom;
	private DateTime validTill;
	private String paymentMode;
	private String clientName;
	
	@Override
	public void setPointsUtil(PointsUtil pointsUtil) {
		this.pointsUtil = pointsUtil;
	}
	
	@Override
	public void init(RuleConfiguration ruleConfig) {
		this.earnRatio = new BigDecimal(ruleConfig.getConfigItemValue(PointsRuleConfigConstants.EARN_RATIO));
		this.earnFactor = new BigDecimal(ruleConfig.getConfigItemValue(PointsRuleConfigConstants.POINTS_FACTOR));
		this.paymentMode = ruleConfig.getConfigItemValue(PointsRuleConfigConstants.PAYMENT_MODE);
		String commaSeparatedIncludedCategoryList = ruleConfig.getConfigItemValue(PointsRuleConfigConstants.INCLUDED_CATEGORY_LIST);
		if (commaSeparatedIncludedCategoryList != null && !commaSeparatedIncludedCategoryList.equals("")){
			StringTokenizer categoryTokenizer = new StringTokenizer(commaSeparatedIncludedCategoryList, ",");
			while (categoryTokenizer.hasMoreTokens()){
				this.includedCategoryList.add(Long.parseLong(categoryTokenizer.nextToken()));
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
		
		if (this.includedCategoryList == null || this.includedCategoryList.isEmpty() || !includedCategoryList.contains(itemRequest.getCategoryId())){
			return false;
		}
		
		//Last Condition to be Checked
		if(!StringUtils.isBlank(this.paymentMode)){
			boolean isSinglePaymentOrder = false;
			for (PaymentRequest paymentRequest : request.getPaymentRequest()){
				if (paymentRequest.getPaymentMode().equalsIgnoreCase(this.paymentMode) && 
						paymentRequest.getAmount().compareTo(request.getAmount()) >= 0){
					isSinglePaymentOrder = true;
					break;
				}
			}
			return isSinglePaymentOrder;
		}
			
		return true;
	}

	@Override
	public BigDecimal execute(OrderRequest request, OrderItemRequest itemRequest) {
		return earnRatio.multiply(earnFactor.multiply(itemRequest.getAmount()));
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
