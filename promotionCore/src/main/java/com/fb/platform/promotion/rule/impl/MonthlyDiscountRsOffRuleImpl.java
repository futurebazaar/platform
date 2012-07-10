package com.fb.platform.promotion.rule.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fb.commons.to.Money;
import com.fb.platform.promotion.model.OrderDiscount;
import com.fb.platform.promotion.rule.PromotionRule;
import com.fb.platform.promotion.rule.RulesEnum;
import com.fb.platform.promotion.rule.config.RuleConfigDescriptorEnum;
import com.fb.platform.promotion.rule.config.RuleConfigItemDescriptor;
import com.fb.platform.promotion.rule.config.RuleConfiguration;
import com.fb.platform.promotion.rule.config.data.MonthlyDiscountRsOffRuleData;
import com.fb.platform.promotion.rule.metadata.MonthlyDiscountRsOffRuleMetadata;
import com.fb.platform.promotion.rule.metadata.RuleConfigMetadata;
import com.fb.platform.promotion.to.OrderRequest;
import com.fb.platform.promotion.to.PromotionStatusEnum;
import com.fb.platform.promotion.util.ListUtil;
import com.fb.platform.promotion.dao.PromotionDao;

public class MonthlyDiscountRsOffRuleImpl implements PromotionRule, Serializable {

	private static transient Log log = LogFactory.getLog(MonthlyDiscountRsOffRuleImpl.class);

	MonthlyDiscountRsOffRuleData data = new MonthlyDiscountRsOffRuleData();
	private PromotionDao promotionDao = null;
	
	private int promotionId = 0;

	public void setPromotionDao(PromotionDao promotionDao) {
		this.promotionDao = promotionDao;
	}

	
	public int getPromotionId() {
		return promotionId;
	}


	public void setPromotionId(int promotionId) {
		this.promotionId = promotionId;
	}


	@Override
	public void init(RuleConfiguration ruleConfig) {

		data = (MonthlyDiscountRsOffRuleData) RulesEnum.MONTHLY_DISCOUNT_RS_OFF.getRuleData(ruleConfig);
				
	}


	@Override
	public PromotionStatusEnum isApplicable(OrderRequest request, int userId,  
			boolean isCouponCommitted) {
		if(log.isDebugEnabled()) {
			log.debug("Checking if MonthlyDiscountRsOffRuleImpl applies on order : " + request.getOrderId());
		}
		if (ListUtil.isValidList(data.getClientList()) && !request.isValidClient(data.getClientList())) {
			return PromotionStatusEnum.INVALID_CLIENT;
		}
		if (ListUtil.isValidList(data.getIncludeCategoryList()) && !request.isAnyProductInCategory(data.getIncludeCategoryList())) {
			return PromotionStatusEnum.CATEGORY_MISMATCH;
		}
		if (ListUtil.isValidList(data.getExcludeCategoryList()) && request.isAnyProductInCategory(data.getExcludeCategoryList())){
			return PromotionStatusEnum.CATEGORY_MISMATCH;
		}
		if (ListUtil.isValidList(data.getBrands()) && !request.isAnyProductInBrand(data.getBrands())){
			return PromotionStatusEnum.BRAND_MISMATCH;
		}
		
		Money orderValue = request.getOrderValueForRelevantProducts(data.getBrands(), data.getIncludeCategoryList(), data.getExcludeCategoryList());
		if(data.getMinOrderValue() !=null && orderValue.lt(data.getMinOrderValue())){
			return PromotionStatusEnum.LESS_ORDER_AMOUNT;
		}

		log.info("No of times allowed " +  data.getNoOfTimesinMonth()) ;
		
		if (   !promotionDao.isValidNoOfTimesInMonth(userId, data.getNoOfTimesinMonth() , this.promotionId )  ) {
			return PromotionStatusEnum.NUMBER_OF_USES_EXCEEDED ;
		}
		
		return PromotionStatusEnum.SUCCESS;
	}


	@Override
	public OrderDiscount execute(OrderDiscount orderDiscount) {
		OrderRequest request = orderDiscount.getOrderRequest();	
		if(log.isDebugEnabled()) {
			log.debug("Executing BuyWorthXGetYRsOffRuleImpl on order : " + request.getOrderId());
		}
		
		orderDiscount.setOrderDiscountValue(data.getFixedRsOff().getAmount());

		return orderDiscount.distributeDiscountOnOrder(orderDiscount,data.getBrands(),data.getIncludeCategoryList(),data.getExcludeCategoryList());
	}


	@Override
	public List<RuleConfigItemDescriptor> getRuleConfigs() {
		
		List<RuleConfigItemDescriptor> ruleConfigs = new ArrayList<RuleConfigItemDescriptor>();
		
		ruleConfigs.add(new RuleConfigItemDescriptor(RuleConfigDescriptorEnum.CLIENT_LIST, false));
		ruleConfigs.add(new RuleConfigItemDescriptor(RuleConfigDescriptorEnum.CATEGORY_INCLUDE_LIST, false));
		ruleConfigs.add(new RuleConfigItemDescriptor(RuleConfigDescriptorEnum.CATEGORY_EXCLUDE_LIST, false));
		ruleConfigs.add(new RuleConfigItemDescriptor(RuleConfigDescriptorEnum.BRAND_LIST, false));
		ruleConfigs.add(new RuleConfigItemDescriptor(RuleConfigDescriptorEnum.MIN_ORDER_VALUE, false));
		ruleConfigs.add(new RuleConfigItemDescriptor(RuleConfigDescriptorEnum.FIXED_DISCOUNT_RS_OFF, true));
		ruleConfigs.add(new RuleConfigItemDescriptor(RuleConfigDescriptorEnum.NUMBER_OF_TIMES_IN_MONTH,true));
		
		// TODO Auto-generated method stub
		return ruleConfigs;
	}


	@Override
	public RuleConfigMetadata getRuleConfigMetadata() {
		// TODO Auto-generated method stub
		return new MonthlyDiscountRsOffRuleMetadata();
	}

	
	
}
