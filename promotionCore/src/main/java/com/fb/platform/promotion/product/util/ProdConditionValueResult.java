/**
 * 
 */
package com.fb.platform.promotion.product.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.fb.commons.to.Money;
import com.fb.platform.promotion.product.model.Condition;
import com.fb.platform.promotion.product.model.ConfigModule;
import com.fb.platform.promotion.product.model.OfferType;
import com.fb.platform.promotion.product.model.Result;
import com.fb.platform.promotion.product.model.condition.ProductCondition;
import com.fb.platform.promotion.product.model.result.ValueChangeResult;
import com.fb.platform.promotion.to.OrderItem;
import com.fb.platform.promotion.to.OrderRequest;

/**
 * @author vinayak
 *
 */
public class ProdConditionValueResult implements ConditionResultProcessor {

	private ConfigModule configModule = null;
	private ProductCondition productCondition = null;
	private ValueChangeResult valueChangeResult = null;

	public ProdConditionValueResult(ConfigModule configModule) {
		this.configModule = configModule;
		for (Condition condition : this.configModule.getConditions().getConditions()) {
			if (condition instanceof ProductCondition) {
				productCondition = (ProductCondition) condition;
			}
		}
		for (Result result : configModule.getResults().getResults()) {
			if (result instanceof ValueChangeResult) {
				valueChangeResult = (ValueChangeResult) result;
			}
		}
	}

	@Override
	public boolean process(OrderRequest orderRequest) {
		List<OrderItem> matchingItems = findMatchingOrderItems(orderRequest);
		if (matchingItems.size() == 0) {
			return false;
		}

		int matchingQuantity = findMatchingQuantity(matchingItems);
		if (matchingQuantity < productCondition.getQuantity()) {
			return false;
		}

		Money totalPrice = getTotalPrice(matchingItems, matchingQuantity);
		distributeTotalPriceAccrossOrderItems(matchingItems, totalPrice, matchingQuantity);
		return true;
	}

	private void distributeTotalPriceAccrossOrderItems(List<OrderItem> matchingItems, Money totalPrice, int matchingQuantity) {
		Money productPrice = totalPrice.div(matchingQuantity);
		for (OrderItem orderItem : matchingItems) {
			orderItem.getProduct().setDiscountedPrice(productPrice.getAmount());
			orderItem.setPromotionProcessed(true);
		}
	}

	private Money getTotalPrice(List<OrderItem> matchingItems, int matchingQuantity) {
		Money finalOrderItemPrice = new Money(BigDecimal.ZERO);

		int remainder = matchingQuantity % productCondition.getQuantity();
		int multiples = matchingQuantity / productCondition.getQuantity();

		OfferType offerType = valueChangeResult.getOfferType();
		if (offerType == OfferType.FIXED_PRICE) {
			finalOrderItemPrice = valueChangeResult.getOfferValue().times(multiples);
			if (remainder > 0) {
				List<Money> highetOfferPrices = findHighestOfferPricesForReminder(matchingItems, remainder);
				for (Money offerPrice : highetOfferPrices) {
					finalOrderItemPrice = finalOrderItemPrice.plus(offerPrice);
				}
			}
		} else if (offerType == OfferType.FIXED_OFF) {
			for (int i = 0; i < matchingQuantity - remainder; i++) {
				finalOrderItemPrice = finalOrderItemPrice.plus(new Money(matchingItems.get(i).getProduct().getMrpPrice()));
			}
			if (remainder > 0) {
				List<Money> highetOfferPrices = findHighestOfferPricesForReminder(matchingItems, remainder);
				for (Money offerPrice : highetOfferPrices) {
					finalOrderItemPrice = finalOrderItemPrice.plus(offerPrice);
				}
			}
			Money totalOffMoney = valueChangeResult.getOfferValue().times(multiples);
			finalOrderItemPrice = finalOrderItemPrice.minus(totalOffMoney);
		} else if (offerType == OfferType.PERCENT_OFF) {
			for (int i = 0; i < matchingQuantity - remainder; i++) {
				Money itemPrice = new Money((matchingItems.get(i).getProduct().getMrpPrice()));
				BigDecimal percentOff = valueChangeResult.getOfferValue().getAmount();
				Money priceOff = itemPrice.times(percentOff.doubleValue()).div(100);

				finalOrderItemPrice = finalOrderItemPrice.plus(itemPrice).minus(priceOff);
			}
			if (remainder > 0) {
				List<Money> highetOfferPrices = findHighestOfferPricesForReminder(matchingItems, remainder);
				for (Money offerPrice : highetOfferPrices) {
					finalOrderItemPrice = finalOrderItemPrice.plus(offerPrice);
				}
			}
		}
		return finalOrderItemPrice;
	}

	private List<Money> findHighestOfferPricesForReminder(List<OrderItem> matchingItems, int reminder) {
		List<Money> offerPrices = new ArrayList<Money>();
		for (int i = 0; i < reminder; i++) {
			offerPrices.add(new Money(matchingItems.get(i).getProductPrice()));
		}
		return offerPrices;
	}

	private int findMatchingQuantity(List<OrderItem> matchingItems) {
		int quantity = 0;
		for (OrderItem orderItem : matchingItems) {
			quantity += orderItem.getQuantity();
		}
		return quantity;
	}

	private List<OrderItem> findMatchingOrderItems(OrderRequest orderRequest) {
		List<OrderItem> items = new ArrayList<OrderItem>();
		for (OrderItem orderItem : orderRequest.getOrderItems()) {
			if (orderItem.isPromotionProcessed()) {
				continue;
			}
			if (productCondition.isApplicableOn(orderItem.getProduct().getProductId())) {
				items.add(orderItem);
			}
		}
		return items;
	}
}
