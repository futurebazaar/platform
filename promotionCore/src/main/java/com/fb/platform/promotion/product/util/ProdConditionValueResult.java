/**
 * 
 */
package com.fb.platform.promotion.product.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import com.fb.commons.to.Money;
import com.fb.platform.promotion.product.model.Condition;
import com.fb.platform.promotion.product.model.ConfigModule;
import com.fb.platform.promotion.product.model.OfferType;
import com.fb.platform.promotion.product.model.Result;
import com.fb.platform.promotion.product.model.condition.ProductCondition;
import com.fb.platform.promotion.product.model.result.ValueChangeResult;
import com.fb.platform.promotion.to.ConfigResultApplyStatusEnum;
import com.fb.platform.promotion.to.OrderItem;
import com.fb.platform.promotion.to.OrderItemPromotionApplicationEnum;
import com.fb.platform.promotion.to.OrderItemPromotionStatus;
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
	public ConfigResultApplyStatusEnum process(OrderRequest orderRequest) {
		List<OrderItem> matchingItems = findMatchingOrderItems(orderRequest);
		if (matchingItems.size() == 0) {
			return ConfigResultApplyStatusEnum.ERROR;
		}

		int matchingQuantity = findMatchingQuantity(matchingItems);
		if (matchingQuantity < productCondition.getQuantity()) {
			return ConfigResultApplyStatusEnum.ERROR;
		}

		Money totalPrice = getTotalPrice(matchingItems, matchingQuantity, orderRequest.getOrderItems());
		orderRequest.setTotalPrice(totalPrice);
		distributeTotalPriceAccrossOrderItems(matchingItems, totalPrice, matchingQuantity);
		return isProcessSuccessful(orderRequest.getOrderItems());
	}
	
	private ConfigResultApplyStatusEnum isProcessSuccessful(List<OrderItem> orderItems) {
		ConfigResultApplyStatusEnum configResultApplyStatus = ConfigResultApplyStatusEnum.SUCESS;
		for (OrderItem orderItem : orderItems) {
			if(orderItem.getOrderItemPromotionStatus().getOrderItemPromotionApplication() != OrderItemPromotionApplicationEnum.SUCCESS) {
				configResultApplyStatus = ConfigResultApplyStatusEnum.PARTIAL;
				break;
			}
		}
		return configResultApplyStatus;
	}

	private void distributeTotalPriceAccrossOrderItems(List<OrderItem> matchingItems, Money totalPrice, int matchingQuantity) {
		/*Money productPrice = totalPrice.div(matchingQuantity);
		for (OrderItem orderItem : matchingItems) {
			orderItem.getProduct().setDiscountedPrice(productPrice.getAmount());
			orderItem.setPromotionProcessed(true);
		}*/
		OrderItemPriceDistributor.distributeOnMrp(matchingItems, totalPrice);
	}

	private Money getTotalPrice(List<OrderItem> matchingItems, int matchingQuantity, List<OrderItem> orderItems) {
		Money finalOrderItemPrice = new Money(BigDecimal.ZERO);

		int remainder = matchingQuantity % productCondition.getQuantity();
		int multiples = matchingQuantity / productCondition.getQuantity();

		OfferType offerType = valueChangeResult.getOfferType();
		if (offerType == OfferType.FIXED_PRICE) {
			finalOrderItemPrice = valueChangeResult.getOfferValue().times(multiples);
			/*if (remainder > 0) {
				List<Money> highetOfferPrices = findHighestOfferPricesForReminder(matchingItems, remainder);
				for (Money offerPrice : highetOfferPrices) {
					finalOrderItemPrice = finalOrderItemPrice.plus(offerPrice);
				}
			}*/
			for (OrderItem matchItem : matchingItems) {
				if(remainder != 0) {
					matchItem.getOrderItemPromotionStatus().setRemainingQuantity(remainder);
					matchItem.getOrderItemPromotionStatus().setAppliedQuantity(multiples * productCondition.getQuantity());
					matchItem.getOrderItemPromotionStatus().setOrderItemPromotionApplication(OrderItemPromotionApplicationEnum.PARTIAL);
				} else {
					matchItem.getOrderItemPromotionStatus().setRemainingQuantity(0);
					matchItem.getOrderItemPromotionStatus().setAppliedQuantity(multiples * productCondition.getQuantity());
					matchItem.getOrderItemPromotionStatus().setOrderItemPromotionApplication(OrderItemPromotionApplicationEnum.SUCCESS);
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
			setPromotionSuccessStatus(matchingItems);
			
			Money totalOffMoney = valueChangeResult.getOfferValue().times(multiples);
			finalOrderItemPrice = finalOrderItemPrice.minus(totalOffMoney);
		} else if (offerType == OfferType.PERCENT_OFF) {
			//we apply percent off to all the matching items, there is no remainder concept here.
			//used in promotions such as buy 1 for 5% off, buy 2 for 10% off etc, buy 3 or more for 15% off
			for (OrderItem matchingItem : matchingItems) {
				Money productPrice = new Money(matchingItem.getProduct().getMrpPrice());
				//Money itemPrice = new Money((matchingItems.get(i).getProduct().getMrpPrice()));
				BigDecimal percentOff = valueChangeResult.getOfferValue().getAmount();
				Money priceOff = productPrice.times(percentOff.doubleValue()).div(100);

				Money discountedProductPrice = productPrice.minus(priceOff);
				Money orderItemDiscountedPrice = discountedProductPrice.times(matchingItem.getQuantity());
				finalOrderItemPrice = finalOrderItemPrice.plus(orderItemDiscountedPrice);
				
				setPromotionSuccessStatus(matchingItems);
			}
		}
		
		BigDecimal amount = finalOrderItemPrice.getAmount().setScale(0,  RoundingMode.FLOOR);	//Rounding off to the maximize discount
		finalOrderItemPrice = new Money(amount);
		return finalOrderItemPrice;
	}
	
	private void setPromotionSuccessStatus(List<OrderItem> orderItems) {
		for (OrderItem orderItem : orderItems) {
			orderItem.getOrderItemPromotionStatus().setOrderItemPromotionApplication(OrderItemPromotionApplicationEnum.SUCCESS);
			orderItem.getOrderItemPromotionStatus().setRemainingQuantity(0);
			orderItem.getOrderItemPromotionStatus().setAppliedQuantity(orderItem.getQuantity());
		}
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

	public List<OrderItem> findMatchingOrderItems(OrderRequest orderRequest) {
		List<OrderItem> items = new ArrayList<OrderItem>();
		for (OrderItem orderItem : orderRequest.getOrderItems()) {
			if (orderItem.getOrderItemPromotionStatus().getOrderItemPromotionApplication() == OrderItemPromotionApplicationEnum.SUCCESS) {
				continue;
			}
			if (productCondition.isApplicableOn(orderItem.getProduct().getProductId())) {
				items.add(orderItem);
			}
		}
		return items;
	}
}
