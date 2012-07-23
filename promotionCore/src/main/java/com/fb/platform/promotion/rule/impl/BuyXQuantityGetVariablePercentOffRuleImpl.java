/**
 * 
 */
package com.fb.platform.promotion.rule.impl;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fb.commons.to.Money;
import com.fb.platform.promotion.model.OrderDiscount;
import com.fb.platform.promotion.rule.PromotionRule;
import com.fb.platform.promotion.rule.RulesEnum;
import com.fb.platform.promotion.rule.config.RuleConfigDescriptor;
import com.fb.platform.promotion.rule.config.RuleConfigDescriptorEnum;
import com.fb.platform.promotion.rule.config.RuleConfigItemDescriptor;
import com.fb.platform.promotion.rule.config.RuleConfiguration;
import com.fb.platform.promotion.rule.config.data.BuyXQuantityGetVariablePercentOffRuleData;
import com.fb.platform.promotion.rule.metadata.BuyXQuantityGetVariablePercentOffRuleMetadata;
import com.fb.platform.promotion.rule.metadata.RuleConfigMetadata;
import com.fb.platform.promotion.to.OrderRequest;
import com.fb.platform.promotion.to.PromotionStatusEnum;
import com.fb.platform.promotion.util.ListUtil;

/**
 * @author keith
 * 
 */
public class BuyXQuantityGetVariablePercentOffRuleImpl implements
		PromotionRule, Serializable {

	private static transient Log log = LogFactory
			.getLog(BuyXQuantityGetVariablePercentOffRuleImpl.class);

	BuyXQuantityGetVariablePercentOffRuleData data = new BuyXQuantityGetVariablePercentOffRuleData();

	@Override
	public void init(RuleConfiguration ruleConfig) {

		data = (BuyXQuantityGetVariablePercentOffRuleData) RulesEnum.BUY_X_QUANTITY_GET_VARIABLE_PERCENT_OFF
				.getRuleData(ruleConfig);

	}

	@Override
	public PromotionStatusEnum isApplicable(OrderRequest request, int userId,
			boolean isCouponCommitted) {
		if (log.isDebugEnabled()) {
			log.debug("Checking if BuyXQuantityGetVariablePercentOffRuleImpl applies on order : "
					+ request.getOrderId());
		}
		if (ListUtil.isValidList(data.getClientList())
				&& !request.isValidClient(data.getClientList())) {
			return PromotionStatusEnum.INVALID_CLIENT;
		}
		if (ListUtil.isValidList(data.getIncludeCategoryList())
				&& !request.isAnyProductInCategory(data
						.getIncludeCategoryList())) {
			return PromotionStatusEnum.CATEGORY_MISMATCH;
		}
		if (ListUtil.isValidList(data.getExcludeCategoryList())
				&& request
						.isAnyProductInCategory(data.getExcludeCategoryList())) {
			return PromotionStatusEnum.CATEGORY_MISMATCH;
		}
		if (ListUtil.isValidList(data.getBrands())
				&& !request.isAnyProductInBrand(data.getBrands())) {
			return PromotionStatusEnum.BRAND_MISMATCH;
		}
		Money orderValue = request.getOrderValueForRelevantProducts(
				data.getBrands(), data.getIncludeCategoryList(),
				data.getExcludeCategoryList());
		if (data.getMinOrderValue() != null
				&& orderValue.lt(data.getMinOrderValue())) {
			return PromotionStatusEnum.LESS_ORDER_AMOUNT;
		}
		int relevantProductQuantity = request
				.getOrderQuantityForRelevantProducts(data.getBrands(),
						data.getIncludeCategoryList(),
						data.getExcludeCategoryList());
		if (!data.getQuantityDiscountMap().isQuantityApplicable(
				relevantProductQuantity)) {
			return PromotionStatusEnum.NOT_APPLICABLE;
		}
		return PromotionStatusEnum.SUCCESS;
	}

	@Override
	public OrderDiscount execute(OrderDiscount orderDiscount) {
		OrderRequest request = orderDiscount.getOrderRequest();
		if (log.isDebugEnabled()) {
			log.debug("Executing BuyXQuantityGetVariablePercentOffRuleImpl on order : "
					+ request.getOrderId());
		}
		int relevantProductQuantity = request
				.getOrderQuantityForRelevantProducts(data.getBrands(),
						data.getIncludeCategoryList(),
						data.getExcludeCategoryList());
		log.info("Relevant Product Quantity = " + relevantProductQuantity);
		BigDecimal discountPercentage = data.getQuantityDiscountMap()
				.getDiscount(relevantProductQuantity);
		log.info("Relevant Percent Discount = " + discountPercentage);
		Money orderVal = request.getOrderValueForRelevantProducts(
				data.getBrands(), data.getIncludeCategoryList(),
				data.getExcludeCategoryList());
		log.info("Discount Relevant on Order Value = " + orderVal.toString());
		Money discountCalculated = (orderVal.times(discountPercentage
				.doubleValue())).div(100);
		log.info("Calculated Discount on Order Value = "
				+ discountCalculated.toString());
		Money finalDiscountAmount = new Money(BigDecimal.ZERO);
		if (data.getMaxDiscountPerUse() != null
				&& discountCalculated.gt(data.getMaxDiscountPerUse())) {
			log.info("Maximum discount is less than the calculated discount on this order. Max Discount = "
					+ data.getMaxDiscountPerUse()
					+ " and Discount calculated = " + discountCalculated);
			finalDiscountAmount = finalDiscountAmount.plus(data
					.getMaxDiscountPerUse());
		} else {
			log.info("Discount amount calculated is the final discount on order. Discount calculated = "
					+ discountCalculated);
			finalDiscountAmount = finalDiscountAmount.plus(discountCalculated);
		}
		orderDiscount.setOrderDiscountValue(finalDiscountAmount.getAmount());

		return orderDiscount.distributeDiscountOnOrder(orderDiscount,
				data.getBrands(), data.getIncludeCategoryList(),
				data.getExcludeCategoryList());

	}

	@Override
	public RuleConfigMetadata getRuleConfigMetadata() {
		return new BuyXQuantityGetVariablePercentOffRuleMetadata();
	}

	@Override
	public RuleConfigDescriptor getRuleConfigs() {

		RuleConfigDescriptor ruleConfigDescriptor = new RuleConfigDescriptor();

		ruleConfigDescriptor
				.addConfigItemDescriptor(new RuleConfigItemDescriptor(
						RuleConfigDescriptorEnum.CLIENT_LIST, false));
		ruleConfigDescriptor
				.addConfigItemDescriptor(new RuleConfigItemDescriptor(
						RuleConfigDescriptorEnum.CATEGORY_INCLUDE_LIST, false));
		ruleConfigDescriptor
				.addConfigItemDescriptor(new RuleConfigItemDescriptor(
						RuleConfigDescriptorEnum.CATEGORY_EXCLUDE_LIST, false));
		ruleConfigDescriptor
				.addConfigItemDescriptor(new RuleConfigItemDescriptor(
						RuleConfigDescriptorEnum.BRAND_LIST, false));
		ruleConfigDescriptor
				.addConfigItemDescriptor(new RuleConfigItemDescriptor(
						RuleConfigDescriptorEnum.MIN_ORDER_VALUE, false));
		ruleConfigDescriptor
				.addConfigItemDescriptor(new RuleConfigItemDescriptor(
						RuleConfigDescriptorEnum.VARIABLE_DISCOUNT_PERCENTAGE,
						true));
		ruleConfigDescriptor
				.addConfigItemDescriptor(new RuleConfigItemDescriptor(
						RuleConfigDescriptorEnum.MAX_DISCOUNT_CEIL_IN_VALUE,
						false));

		return ruleConfigDescriptor;
	}

}
