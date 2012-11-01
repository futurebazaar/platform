/**
 * 
 */
package com.fb.platform.promotion.rule.impl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fb.commons.to.Money;
import com.fb.platform.promotion.dao.ProductDao;
import com.fb.platform.promotion.model.OrderDiscount;
import com.fb.platform.promotion.rule.PromotionRule;
import com.fb.platform.promotion.rule.RulesEnum;
import com.fb.platform.promotion.rule.config.RuleConfigDescriptorEnum;
import com.fb.platform.promotion.rule.config.RuleConfigItemDescriptor;
import com.fb.platform.promotion.rule.config.RuleConfiguration;
import com.fb.platform.promotion.rule.config.data.DiscountOnClearanceProductPercentOffRuleData;
import com.fb.platform.promotion.rule.metadata.DiscountOnClearanceProductPercentOffRuleMetadata;
import com.fb.platform.promotion.rule.metadata.RuleConfigMetadata;
import com.fb.platform.promotion.to.OrderItem;
import com.fb.platform.promotion.to.OrderRequest;
import com.fb.platform.promotion.to.PromotionStatusEnum;

/**
 * @author vinayak
 *
 */
public class DiscountOnClearanceProductPercentOffRuleImpl implements PromotionRule, Serializable {

	private static transient Log logger = LogFactory.getLog(DiscountOnClearanceProductPercentOffRuleImpl.class);

	private DiscountOnClearanceProductPercentOffRuleData data = new DiscountOnClearanceProductPercentOffRuleData();

	private ProductDao productDao = null;

	/* (non-Javadoc)
	 * @see com.fb.platform.promotion.rule.PromotionRule#init(com.fb.platform.promotion.rule.config.RuleConfiguration)
	 */
	@Override
	public void init(RuleConfiguration ruleConfig) {
		data = (DiscountOnClearanceProductPercentOffRuleData) RulesEnum.DISCOUNT_ON_CLEARANCE_PRODUCT_PERCENT_OFF.getRuleData(ruleConfig);
	}

	/* (non-Javadoc)
	 * @see com.fb.platform.promotion.rule.PromotionRule#isApplicable(com.fb.platform.promotion.to.OrderRequest, int, boolean)
	 */
	@Override
	public PromotionStatusEnum isApplicable(OrderRequest request, int userId, boolean isCouponCommitted) {
		if (logger.isDebugEnabled()) {
			logger.debug("Checking if DiscountOnClearanceProductPercentOffRuleImpl applies on order : " + request.getOrderId());
		}

		Set<Integer> allProductIdsInOrder = getAllProductIds(request);

		Set<Integer> clearanceProductIds = productDao.findClearanceProductIds(allProductIdsInOrder);

		Money orderValue = calculateOrderValueOfClearanceProducts(request, clearanceProductIds);

		if(data.getMinOrderValue() != null && orderValue.lt(data.getMinOrderValue())) {
			return PromotionStatusEnum.LESS_ORDER_AMOUNT;
		}
		
		return PromotionStatusEnum.SUCCESS;
	}

	private Set<Integer> getAllProductIds(OrderRequest request) {
		Set<Integer> productIds = new HashSet<Integer>();
		for (OrderItem orderItem : request.getOrderItems()) {
			productIds.add(orderItem.getProduct().getProductId());
		}
		return productIds;
	}

	private Money calculateOrderValueOfClearanceProducts(OrderRequest request, Set<Integer> clearanceProductIds) {
		Money clearanceOrderValue = new Money(BigDecimal.ZERO);

		for (OrderItem orderItem : request.getOrderItems()) {
			if (clearanceProductIds.contains(orderItem.getProduct().getProductId())) {
				clearanceOrderValue = clearanceOrderValue.plus(orderItem.orderItemOfferPrice());
			}
		}

		return clearanceOrderValue;
	}

	/* (non-Javadoc)
	 * @see com.fb.platform.promotion.rule.PromotionRule#execute(com.fb.platform.promotion.model.OrderDiscount)
	 */
	@Override
	public OrderDiscount execute(OrderDiscount orderDiscount) {
		Set<Integer> allProductIdsInOrder = getAllProductIds(orderDiscount.getOrderRequest());

		Set<Integer> clearanceProductIds = productDao.findClearanceProductIds(allProductIdsInOrder);

		Money clearanceOrderValue = calculateOrderValueOfClearanceProducts(orderDiscount.getOrderRequest(), clearanceProductIds);

		Money discount = (clearanceOrderValue.times(data.getDiscountPercentage().doubleValue())).div(100);
		
		orderDiscount.setOrderDiscountValue(discount.getAmount());

		return orderDiscount.distributeDiscountOnOrder(orderDiscount, null, null, null, clearanceProductIds);
	}

	/* (non-Javadoc)
	 * @see com.fb.platform.promotion.rule.PromotionRule#getRuleConfigs()
	 */
	@Override
	public List<RuleConfigItemDescriptor> getRuleConfigs() {
		List<RuleConfigItemDescriptor> ruleConfigs = new ArrayList<RuleConfigItemDescriptor>();
		
		ruleConfigs.add(new RuleConfigItemDescriptor(RuleConfigDescriptorEnum.MIN_ORDER_VALUE, false));
		ruleConfigs.add(new RuleConfigItemDescriptor(RuleConfigDescriptorEnum.DISCOUNT_PERCENTAGE, true));
		
		return ruleConfigs;
	}

	/* (non-Javadoc)
	 * @see com.fb.platform.promotion.rule.PromotionRule#getRuleConfigMetadata()
	 */
	@Override
	public RuleConfigMetadata getRuleConfigMetadata() {
		return new DiscountOnClearanceProductPercentOffRuleMetadata();
	}

	public void setProductDao(ProductDao productDao) {
		this.productDao = productDao;
	}

}
