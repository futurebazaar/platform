/**
 * 
 */
package com.fb.platform.promotion.rule.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fb.commons.to.Money;
import com.fb.platform.promotion.dao.OrderDao;
import com.fb.platform.promotion.model.OrderDiscount;
import com.fb.platform.promotion.rule.PromotionRule;
import com.fb.platform.promotion.rule.RulesEnum;
import com.fb.platform.promotion.rule.config.RuleConfigDescriptorEnum;
import com.fb.platform.promotion.rule.config.RuleConfigItemDescriptor;
import com.fb.platform.promotion.rule.config.RuleConfiguration;
import com.fb.platform.promotion.rule.config.data.FirstPurchaseBuyWorthXGetYRsOffRuleData;
import com.fb.platform.promotion.rule.metadata.FirstPurchaseBuyWorthXGetYRsOffRuleMetadata;
import com.fb.platform.promotion.rule.metadata.RuleConfigMetadata;
import com.fb.platform.promotion.to.OrderRequest;
import com.fb.platform.promotion.to.PromotionStatusEnum;
import com.fb.platform.promotion.util.ListUtil;

/**
 * @author keith
 *
 */
public class FirstPurchaseBuyWorthXGetYRsOffRuleImpl  implements PromotionRule{

	private static transient Log log = LogFactory.getLog(FirstPurchaseBuyWorthXGetYRsOffRuleImpl.class);
	
	FirstPurchaseBuyWorthXGetYRsOffRuleData data = new FirstPurchaseBuyWorthXGetYRsOffRuleData();
	
	private OrderDao orderDao = null;
	
	public void setOrderDao(OrderDao orderDao) {
		this.orderDao = orderDao;
	}

	@Override
	public void init(RuleConfiguration ruleConfig) {

		data = (FirstPurchaseBuyWorthXGetYRsOffRuleData) RulesEnum.FIRST_PURCHASE_BUY_WORTH_X_GET_Y_RS_OFF.getRuleData(ruleConfig);

	}

	@Override
	public PromotionStatusEnum isApplicable(OrderRequest request,int userId,boolean isCouponCommitted) {
		if(log.isDebugEnabled()) {
			log.debug("Checking if FirstPurchaseBuyWorthXGetYRsOffRuleImpl applies on order : " + request.getOrderId());
		}
		if (ListUtil.isValidList(data.getClientList()) && !request.isValidClient(data.getClientList())) {
			return PromotionStatusEnum.INVALID_CLIENT;
		}
		if (ListUtil.isValidList(data.getIncludeCategoryList()) && !request.isAnyProductInCategory(data.getIncludeCategoryList())) {
			return PromotionStatusEnum.CATEGORY_MISMATCH;
		}
		if (ListUtil.isValidList(data.getExcludeCategoryList()) && request.isAnyProductInCategory(data.getExcludeCategoryList())) {
			return PromotionStatusEnum.CATEGORY_MISMATCH;
		}
		if (ListUtil.isValidList(data.getBrands()) && !request.isAnyProductInBrand(data.getBrands())) {
			return PromotionStatusEnum.BRAND_MISMATCH;
		}
		
		Money orderValue = request.getOrderValueForRelevantProducts(data.getBrands(), data.getIncludeCategoryList(), data.getExcludeCategoryList());
		if(data.getMinOrderValue() !=null && orderValue.lt(data.getMinOrderValue())) {
			return PromotionStatusEnum.LESS_ORDER_AMOUNT;
		}
		if(!isCouponCommitted) {
			if(!orderDao.isUserFirstOrder(userId)) {
				return PromotionStatusEnum.NOT_FIRST_PURCHASE;
			}
		}
		return PromotionStatusEnum.SUCCESS;
	}
	
	@Override
	public OrderDiscount execute(OrderDiscount orderDiscount) {
		OrderRequest request = orderDiscount.getOrderRequest();
		if(log.isDebugEnabled()) {
			log.debug("Executing FirstPurchaseBuyWorthXGetYRsOffRuleImpl on order : " + request.getOrderId());
		}
		orderDiscount.setOrderDiscountValue(data.getFixedRsOff().getAmount());
		
		return orderDiscount.distributeDiscountOnOrder(orderDiscount,data.getBrands(),data.getIncludeCategoryList(),data.getExcludeCategoryList());
		
	}
		
	@Override
	public RuleConfigMetadata getRuleConfigMetadata() {
		return new FirstPurchaseBuyWorthXGetYRsOffRuleMetadata();
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
		return ruleConfigs;
	}
	
}
