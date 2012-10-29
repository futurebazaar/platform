package com.fb.platform.payback.rule.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;

import com.fb.platform.payback.dao.impl.PointsRuleDaoJdbcImpl;
import com.fb.platform.payback.rule.PointsRule;
import com.fb.platform.payback.rule.PointsRuleConfigConstants;
import com.fb.platform.payback.rule.RuleConfiguration;
import com.fb.platform.payback.to.OrderItemRequest;
import com.fb.platform.payback.to.OrderRequest;
import com.fb.platform.payback.to.PaymentRequest;
import com.fb.platform.payback.util.PointsUtil;

public class EarnXPointsOnYCategoryForZPaymentMode implements PointsRule {

	private List<Long> includedCategoryList = new ArrayList<Long>();
	private HashMap<String, Long> categoryPointsFactorMap = new HashMap<String, Long>();
	private HashMap<String, String> categoryPaymentMap = new HashMap<String, String>();
	private HashMap<String, List<Long>> categoryIdListMap = new HashMap<String, List<Long>>();
	private BigDecimal earnFactor;
	private BigDecimal earnRatio;
	private DateTime validFrom;
	private DateTime validTill;
	private PointsUtil pointsUtil;
	private Log log = LogFactory.getLog(EarnXPointsOnYCategoryForZPaymentMode.class);
	
	@Override
	public void setPointsUtil(PointsUtil pointsUtil) {
		this.pointsUtil = pointsUtil;
	}
	
	@Override
	public void init(RuleConfiguration ruleConfig) {		
		this.earnFactor = BigDecimal.ZERO;
		this.earnRatio =  new BigDecimal(ruleConfig.getConfigItemValue(PointsRuleConfigConstants.EARN_RATIO));
		String commaSeparatedIncludedCategoryList = ruleConfig.getConfigItemValue(PointsRuleConfigConstants.INCLUDED_CATEGORY_LIST);
		if (commaSeparatedIncludedCategoryList != null && !commaSeparatedIncludedCategoryList.equals("")){
			StringTokenizer categoryTokenizer = new StringTokenizer(commaSeparatedIncludedCategoryList, ",");
			while (categoryTokenizer.hasMoreTokens()){
				this.includedCategoryList.add(Long.parseLong(categoryTokenizer.nextToken().replaceAll(" ", "")));		
			}
		}
		this.setCategoryPointsFactor(ruleConfig);
		this.setCategoryPaymentMap(ruleConfig);
		this.setCategoryIdListMap(ruleConfig);
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
		if (includedCategoryList == null || includedCategoryList.isEmpty() || !includedCategoryList.contains(itemRequest.getCategoryId())){
			return false;
		}
		
		// get categoryLabel for the categoryId
		String categoryLabel = this.getCategoryLabelForCategoryId(itemRequest.getCategoryId());
		if(categoryLabel == null || categoryLabel == ""){
			return false;
		}
		
		boolean paymentValidtion = this.checkPaymentValidations(categoryLabel, request.getPaymentRequest());
		if(!paymentValidtion){
			return false;
		}
		
		this.earnFactor = this.getEarnFactorForCategoryLabel(categoryLabel);
		return true;
	}

	@Override
	public BigDecimal execute(OrderRequest request, OrderItemRequest itemRequest) {
		return earnFactor.multiply(earnRatio.multiply(itemRequest.getAmount()));
	}

	@Override
	public boolean allowNext(OrderRequest request, OrderItemRequest itemRequest) {
		return true;
	}

	private void setCategoryPointsFactor(RuleConfiguration ruleConfig){
		String commaSeparatedCategoryPointsFactorMap = ruleConfig.getConfigItemValue(PointsRuleConfigConstants.CATEGORY_POINTS_FACTOR_MAP);
		if (commaSeparatedCategoryPointsFactorMap != null && !commaSeparatedCategoryPointsFactorMap.equals("")){
			StringTokenizer categoryPointsTokenizer = new StringTokenizer(commaSeparatedCategoryPointsFactorMap, ",");
			while (categoryPointsTokenizer.hasMoreTokens()){				
				String [] categoryPointsFactor = categoryPointsTokenizer.nextToken().replaceAll(" ", "").split("=");
				this.categoryPointsFactorMap.put(categoryPointsFactor[0], Long.parseLong(categoryPointsFactor[1]));
			}
		}	
	}
	
	private void setCategoryPaymentMap(RuleConfiguration ruleConfig){
		String commaSeparatedCategoryPaymentMap = ruleConfig.getConfigItemValue(PointsRuleConfigConstants.CATEGORY_PAYMENT_MODE_MAP);
		if (commaSeparatedCategoryPaymentMap != null && !commaSeparatedCategoryPaymentMap.equals("")){
			StringTokenizer categoryPaymentTokenizer = new StringTokenizer(commaSeparatedCategoryPaymentMap, ",");
			while (categoryPaymentTokenizer.hasMoreTokens()){				
				String [] categoryPaymentFactor = categoryPaymentTokenizer.nextToken().replaceAll(" ", "").split("=");
				this.categoryPaymentMap.put(categoryPaymentFactor[0], categoryPaymentFactor[1]);
			}
		}
	}
	
	private void setCategoryIdListMap(RuleConfiguration ruleConfig){
		for(String categoryLabel : this.categoryPointsFactorMap.keySet()){			
			String commaSeparatedCategoryIds = ruleConfig.getConfigItemValue(categoryLabel);
			if (commaSeparatedCategoryIds != null && !commaSeparatedCategoryIds.equals("")){
				StringTokenizer categoryIdsTokenizer = new StringTokenizer(commaSeparatedCategoryIds, ",");
				List<Long> categoryIds = new ArrayList<Long	>();
				while (categoryIdsTokenizer.hasMoreTokens()){				
					categoryIds.add(Long.parseLong(categoryIdsTokenizer.nextToken().replaceAll(" ", "")));				
				}
				if(!categoryIds.isEmpty()){
					this.categoryIdListMap.put(categoryLabel, categoryIds);
				}
			}
		}
	}
	
	private String getCategoryLabelForCategoryId(Long categoryId){
	    for(Map.Entry<String, List<Long>> categoryLabelIdsMap : this.categoryIdListMap.entrySet()){
	    	if(categoryLabelIdsMap.getValue().contains(categoryId)){
	    		return categoryLabelIdsMap.getKey();
	    	}
	    }
	    return "";
	}
	
	private boolean checkPaymentValidations(String categoryLabel, List<PaymentRequest> paymentRequestArray){
		if(!this.categoryPaymentMap.keySet().contains(categoryLabel)){
			// no payment mode restriction for the category
			return true;
		}		
		String applicablePaymentForCategory = this.categoryPaymentMap.get(categoryLabel);
		ArrayList<String> paymentModes = new ArrayList<String>();		
        for(PaymentRequest paymentRequest : paymentRequestArray){
        	String paymentMode = paymentRequest.getPaymentMode();
        	if(!paymentModes.contains(paymentMode)){
        		paymentModes.add(paymentMode);
        	}
        }
        if(paymentModes.size() == 1 && paymentModes.contains(applicablePaymentForCategory)){
        	return true;
        }
		return false;
	}
	
	private BigDecimal getEarnFactorForCategoryLabel(String categoryLabel){
		if(this.categoryPointsFactorMap.keySet().contains(categoryLabel)){
			return new BigDecimal(this.categoryPointsFactorMap.get(categoryLabel));
		}		
		return BigDecimal.ZERO;
	}
}
