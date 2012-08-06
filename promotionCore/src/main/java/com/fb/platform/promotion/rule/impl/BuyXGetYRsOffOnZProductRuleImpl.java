/**
 * 
 */
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
import com.fb.platform.promotion.rule.config.data.BuyXGetYRsOffOnZProductRuleData;
import com.fb.platform.promotion.rule.metadata.BuyXGetYRsOffOnZProductRuleMetadata;
import com.fb.platform.promotion.rule.metadata.RuleConfigMetadata;
import com.fb.platform.promotion.to.OrderRequest;
import com.fb.platform.promotion.to.PromotionStatusEnum;
import com.fb.platform.promotion.util.ListUtil;

/**
 * @author keith
 * 
 */
public class BuyXGetYRsOffOnZProductRuleImpl implements PromotionRule, Serializable {

	private static transient Log log = LogFactory.getLog(BuyXGetYRsOffOnZProductRuleImpl.class);

	private BuyXGetYRsOffOnZProductRuleData data = null;

	@Override
	public void init(RuleConfiguration ruleConfig) {

		data = (BuyXGetYRsOffOnZProductRuleData) RulesEnum.BUY_X_GET_Y_RS_OFF_ON_Z_PRODUCT.getRuleData(ruleConfig);

	}

	@Override
	public PromotionStatusEnum isApplicable(OrderRequest request, int userId, boolean isCouponCommitted) {
		if (log.isDebugEnabled()) {
			log.debug("Checking if BuyXBrandGetYRsOffOnZProductRuleImpl applies on order : " + request.getOrderId());
		}
		// Money orderValue = new Money(request.getOrderValue());
		if (ListUtil.isValidList(data.getClientList()) && !request.isValidClient(data.getClientList())) {
			return PromotionStatusEnum.INVALID_CLIENT;
		}
		if (ListUtil.isValidList(data.getIncludeCategoryList())
			&& !request.isAnyProductInCategory(data.getIncludeCategoryList())) {
			return PromotionStatusEnum.CATEGORY_MISMATCH;
		}
		if (ListUtil.isValidList(data.getExcludeCategoryList())
			&& request.isAnyProductInCategory(data.getExcludeCategoryList())) {
			return PromotionStatusEnum.CATEGORY_MISMATCH;
		}
		if (ListUtil.isValidList(data.getBrandList()) && !request.isAnyProductInBrand(data.getBrandList())) {
			return PromotionStatusEnum.BRAND_MISMATCH;
		}
		if (ListUtil.isValidList(data.getSellerList()) && !request.isAnyProductInBrand(data.getSellerList())) {
			return PromotionStatusEnum.SELLER_MISMATCH;
		}

		Money orderValue = request.getOrderValueForRelevantProducts(data.getBrandList(), data.getIncludeCategoryList(),
			data.getExcludeCategoryList(), data.getSellerList());
		if (data.getMinOrderValue() != null && orderValue.lt(data.getMinOrderValue())) {
			return PromotionStatusEnum.LESS_ORDER_AMOUNT;
		}
		if (!request.isProductPresent(data.getProductId())) {
			return PromotionStatusEnum.PRODUCT_NOT_PRESENT;
		}
		return PromotionStatusEnum.SUCCESS;
	}

	@Override
	public OrderDiscount execute(OrderDiscount orderDiscount) {

		// throw new NotImplementedException();
		OrderRequest request = orderDiscount.getOrderRequest();
		if (log.isDebugEnabled()) {
			log.debug("Executing BuyXGetYRsOffOnZProductRuleImpl on order : " + request.getOrderId());
		}
		Money productPrice = orderDiscount.getOrderRequest().getProductPrice(data.getProductId());
		Money discountValue = productPrice.minus(data.getProductDiscountValue());
		orderDiscount.setOrderDiscountValue(discountValue.getAmount());

		return orderDiscount.distributeDiscountOnOrder(orderDiscount, data.getBrandList(),
			data.getIncludeCategoryList(), data.getExcludeCategoryList(), data.getSellerList());

	}

	/*
	 * private OrderDiscount distributeDiscountOnOrder(OrderDiscount
	 * orderDiscount) { OrderRequest orderRequest =
	 * orderDiscount.getOrderRequest(); Money totalOrderValueForApplicableItems
	 * = orderRequest.getOrderValueForBrand(this.brands); for (OrderItem
	 * eachOrderItemInRequest : orderRequest.getOrderItems()) {
	 * if(!eachOrderItemInRequest.isLocked() &&
	 * isApplicableToOrderItem(eachOrderItemInRequest)){ BigDecimal
	 * orderItemDiscount = new BigDecimal(0); BigDecimal orderItemPrice =
	 * eachOrderItemInRequest.getPrice(); orderItemDiscount =
	 * (orderItemPrice.multiply(orderDiscount.getTotalOrderDiscount())).divide(
	 * totalOrderValueForApplicableItems.getAmount());
	 * eachOrderItemInRequest.setTotalDiscount(orderItemDiscount);
	 * totalOrderValueForApplicableItems =
	 * totalOrderValueForApplicableItems.minus(new Money(orderItemPrice)); } }
	 * return orderDiscount; }
	 * 
	 * private boolean isApplicableToOrderItem(OrderItem orderItem){
	 * if(orderItem.isProductPresent(productId)){ return true; } return false; }
	 */

	@Override
	public List<RuleConfigItemDescriptor> getRuleConfigs() {
		List<RuleConfigItemDescriptor> ruleConfigs = new ArrayList<RuleConfigItemDescriptor>();

		ruleConfigs.add(new RuleConfigItemDescriptor(RuleConfigDescriptorEnum.CLIENT_LIST, false));
		ruleConfigs.add(new RuleConfigItemDescriptor(RuleConfigDescriptorEnum.BRAND_LIST, true));
		ruleConfigs.add(new RuleConfigItemDescriptor(RuleConfigDescriptorEnum.PRODUCT_ID, true));
		ruleConfigs.add(new RuleConfigItemDescriptor(RuleConfigDescriptorEnum.MIN_ORDER_VALUE, true));
		ruleConfigs.add(new RuleConfigItemDescriptor(RuleConfigDescriptorEnum.PRODUCT_DISCOUNTED_VALUE, true));
		return ruleConfigs;
	}

	@Override
	public RuleConfigMetadata getRuleConfigMetadata() {
		return new BuyXGetYRsOffOnZProductRuleMetadata();
	}
}
