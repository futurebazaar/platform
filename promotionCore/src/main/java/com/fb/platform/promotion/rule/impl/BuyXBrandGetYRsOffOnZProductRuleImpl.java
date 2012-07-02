/**
 * 
 */
package com.fb.platform.promotion.rule.impl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang.text.StrTokenizer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fb.commons.to.Money;
import com.fb.platform.promotion.model.OrderDiscount;
import com.fb.platform.promotion.rule.PromotionRule;
import com.fb.platform.promotion.rule.config.RuleConfigDescriptorEnum;
import com.fb.platform.promotion.rule.config.RuleConfigItemDescriptor;
import com.fb.platform.promotion.rule.config.RuleConfiguration;
import com.fb.platform.promotion.rule.metadata.BuyXBrandGetYRsOffOnZProductRuleMatadata;
import com.fb.platform.promotion.rule.metadata.RuleConfigItemMetadata;
import com.fb.platform.promotion.rule.metadata.RuleConfigMetadata;
import com.fb.platform.promotion.to.OrderRequest;
import com.fb.platform.promotion.to.PromotionStatusEnum;
import com.fb.platform.promotion.util.ListUtil;
import com.fb.platform.promotion.util.StringToIntegerList;

/**
 * @author keith
 *
 */
public class BuyXBrandGetYRsOffOnZProductRuleImpl implements PromotionRule, Serializable {

	private static transient Log log = LogFactory.getLog(BuyXBrandGetYRsOffOnZProductRuleImpl.class);
	private Money minOrderValue;
	private List<Integer> brands;
	private List<Integer> clientList;
	private int productId;
	private Money productDiscountValue;
	private List<RuleConfigItemDescriptor> ruleConfigs = new ArrayList<RuleConfigItemDescriptor>();
	
	@Override
	public void init(RuleConfiguration ruleConfig) {
		minOrderValue = new Money(BigDecimal.valueOf(Double.valueOf(ruleConfig.getConfigItemValue(RuleConfigDescriptorEnum.MIN_ORDER_VALUE.name()))));
		StrTokenizer strTokCategories = new StrTokenizer(ruleConfig.getConfigItemValue(RuleConfigDescriptorEnum.BRAND_LIST.name()),",");
		brands = StringToIntegerList.convert((List<String>)strTokCategories.getTokenList());
		if (ruleConfig.isConfigItemPresent(RuleConfigDescriptorEnum.CLIENT_LIST)) {
			StrTokenizer strTokClients = new StrTokenizer(ruleConfig.getConfigItemValue(RuleConfigDescriptorEnum.CLIENT_LIST.name()),",");
			clientList = StringToIntegerList.convert((List<String>)strTokClients.getTokenList());
			log.info("clientList = "+ clientList);
		}
		productId = Integer.valueOf(ruleConfig.getConfigItemValue(RuleConfigDescriptorEnum.PRODUCT_ID.name())).intValue();
		productDiscountValue = new Money (BigDecimal.valueOf(Double.valueOf(ruleConfig.getConfigItemValue(RuleConfigDescriptorEnum.PRODUCT_DISCOUNTED_VALUE.name()))));
		log.info("minOrderValue : " + minOrderValue.toString() 
				+ ", productId : " + productId 
				+ ", productDiscountValue : " + productDiscountValue 
				+ ", brandList : " + brands);
	}

	@Override
	public PromotionStatusEnum isApplicable(OrderRequest request,int userId,boolean isCouponCommitted) {
		if(log.isDebugEnabled()) {
			log.debug("Checking if BuyXBrandGetYRsOffOnZProductRuleImpl applies on order : " + request.getOrderId());
		}
		//Money orderValue = new Money(request.getOrderValue());
		if(ListUtil.isValidList(clientList) && !request.isValidClient(clientList)){
			return PromotionStatusEnum.INVALID_CLIENT;
		}
		/*if(orderValue.lt(minOrderValue)){
			return PromotionStatusEnum.LESS_ORDER_AMOUNT;
		}*/
		if(!request.isAnyProductInBrand(brands)){
			return PromotionStatusEnum.BRAND_MISMATCH;
		}
		Money brandOrderValue = request.getOrderValueForBrand(brands);
		if(brandOrderValue.lt(minOrderValue)){
			return PromotionStatusEnum.LESS_ORDER_AMOUNT_OF_BRAND_PRODUCTS;
		}
		if(!request.isProductPresent(productId)){
			return PromotionStatusEnum.PRODUCT_NOT_PRESENT;
		}
		return PromotionStatusEnum.SUCCESS;
	}

	@Override
	public OrderDiscount execute(OrderDiscount orderDiscount) {
		
		throw new NotImplementedException();
		/*OrderRequest request = orderDiscount.getOrderRequest();
		if(log.isDebugEnabled()) {
			log.debug("Executing BuyXBrandGetYRsOffOnZProductRuleImpl on order : " + request.getOrderId());
		}
		Money productPrice = request.getProductPrice(productId);
		Money zeroMoney = new Money(new BigDecimal(0));
		Money totalDiscountValue = productPrice.eq(zeroMoney) ? zeroMoney : productPrice.minus(productDiscountValue);
		orderDiscount.setTotalOrderDiscount(totalDiscountValue.getAmount());
		return distributeDiscountOnOrder(orderDiscount);*/
	}
	
	/*private OrderDiscount distributeDiscountOnOrder(OrderDiscount orderDiscount) {
		OrderRequest orderRequest = orderDiscount.getOrderRequest();
		Money totalOrderValueForApplicableItems = orderRequest.getOrderValueForBrand(this.brands);
		for (OrderItem eachOrderItemInRequest : orderRequest.getOrderItems()) {
			if(!eachOrderItemInRequest.isLocked() && isApplicableToOrderItem(eachOrderItemInRequest)){
				BigDecimal orderItemDiscount = new BigDecimal(0);
				BigDecimal orderItemPrice = eachOrderItemInRequest.getPrice();
				orderItemDiscount = (orderItemPrice.multiply(orderDiscount.getTotalOrderDiscount())).divide(totalOrderValueForApplicableItems.getAmount());
				eachOrderItemInRequest.setTotalDiscount(orderItemDiscount);
				totalOrderValueForApplicableItems = totalOrderValueForApplicableItems.minus(new Money(orderItemPrice));
			}
		}
		return orderDiscount;
	}
	
	private boolean isApplicableToOrderItem(OrderItem orderItem){
		if(orderItem.isProductPresent(productId)){
			return true;
		}
		return false;
	}*/
	
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
		return new BuyXBrandGetYRsOffOnZProductRuleMatadata();
	}
}
