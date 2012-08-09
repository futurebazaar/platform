/**
 * 
 */
package com.fb.platform.promotion.product.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.fb.commons.to.Money;
import com.fb.platform.promotion.product.model.Condition;
import com.fb.platform.promotion.product.model.ConfigModule;
import com.fb.platform.promotion.product.model.OfferType;
import com.fb.platform.promotion.product.model.Result;
import com.fb.platform.promotion.product.model.condition.ProductCondition;
import com.fb.platform.promotion.product.model.result.ProductResult;
import com.fb.platform.promotion.to.OrderItem;
import com.fb.platform.promotion.to.OrderItemMRPComparator;
import com.fb.platform.promotion.to.OrderRequest;

/**
 * @author vinayak
 *
 */
public class SameProdConditionProdResult implements ConditionResultProcessor {

	private ConfigModule configModule = null;
	private ProductCondition productCondition = null;
	private ProductResult productResult = null;

	public SameProdConditionProdResult(ConfigModule configModule) {
		this.configModule = configModule;
		for (Condition condition : this.configModule.getConditions().getConditions()) {
			if (condition instanceof ProductCondition) {
				productCondition = (ProductCondition) condition;
			}
		}
		for (Result result : configModule.getResults().getResults()) {
			if (result instanceof ProductResult) {
				productResult = (ProductResult) result;
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.fb.platform.promotion.product.util.ConditionResultProcessor#process(com.fb.platform.promotion.to.OrderRequest)
	 */
	@Override
	public boolean process(OrderRequest orderRequest) {
		List<OrderItem> matchingItems = findMatchingOrderItems(orderRequest);

		if (matchingItems.size() == 0) {
			return false;
		}

		int matchingQuantity = findMatchingQuantity(matchingItems);

		int conditionQuantity = productCondition.getQuantity();
		int resultQuantity = productResult.getQuantity();

		if (matchingQuantity < conditionQuantity + resultQuantity) {
			//not enough quantity
			return false;
		}
		//sort the order items descending by mrp
		List<OrderItem> sortedMatchingItems = new ArrayList<OrderItem>(matchingItems);
		sortDescByMrp(sortedMatchingItems);
		
		List<ProductItem> productItems = createProductItems(sortedMatchingItems);

		int numberOfCondResultGroups = matchingQuantity / (conditionQuantity + resultQuantity);
		int numberOfCondtionItems = numberOfCondResultGroups * conditionQuantity;
		int numberOfResultItems = numberOfCondResultGroups * resultQuantity;
		int numberOfOfferPriceItems = matchingQuantity % (conditionQuantity + resultQuantity);

		List<ProductItem> conditionProdItems = findConditionProductItems(productItems, numberOfCondtionItems);
		List<ProductItem> resultProdItems = findResultProductItems(productItems, numberOfResultItems);
		List<ProductItem> offerPriceProdItems = findOfferPriceItems(productItems, numberOfCondtionItems, numberOfOfferPriceItems);

		Money totalPriceForMatchingItems = calculateTotalPrice(conditionProdItems, resultProdItems, offerPriceProdItems);

		distributePriceOnMatchingItems(matchingItems, totalPriceForMatchingItems);

		return true;
		////////
		/*
		List<OrderItem> matchingResultItems = findResultsOrderItems(orderRequest);
		if (matchingResultItems.size() == 0) {
			//nothing to do, thank you
			return false;
		}

		//sort the order items descending by mrp
		//sortDescByMrp(matchingConditionItems);
		sortDescByMrp(matchingResultItems);

		int conditionsToProcess = matchingQuantity / productCondition.getQuantity();
		//List<OrderItem> orderItemForConditions = matchingConditionItems.subList(0, conditionsToProcess);
		return false;*/
	}

	private void distributePriceOnMatchingItems(List<OrderItem> matchingItems, Money totalPriceForMatchingItems) {
		OrderItemPriceDistributor.distributeOnMrp(matchingItems, totalPriceForMatchingItems);
	}

	private Money calculateTotalPrice(List<ProductItem> conditionProdItems, List<ProductItem> resultProdItems, List<ProductItem> offerPriceProdItems) {
		//condition items are at mrp
		//result items are the result price
		//offer price items at are offer price :-)
		Money finalPrice = new Money(BigDecimal.ZERO);

		for (ProductItem conditionItem : conditionProdItems) {
			finalPrice = finalPrice.plus(new Money(conditionItem.getProduct().getMrpPrice()));
		}
		Money resultPrice = calculateResultItemPrice(resultProdItems);
		finalPrice.plus(resultPrice);

		for (ProductItem offerPriceItem : offerPriceProdItems) {
			finalPrice = finalPrice.plus(new Money(offerPriceItem.getProduct().getPrice()));
		}

		return finalPrice;
	}

	private Money calculateResultItemPrice(List<ProductItem> resultProdItems) {
		Money resultPrice = new Money(BigDecimal.ZERO);

		OfferType offerType = productResult.getOfferType();
		BigDecimal offerValue = productResult.getOfferValue().getAmount();

		if (offerType == OfferType.PERCENT_OFF) {
			for (ProductItem resultItem : resultProdItems) {
				Money itemPrice = new Money(resultItem.getProduct().getMrpPrice());
				Money priceOff = itemPrice.times(offerValue.doubleValue()).div(100);
				resultPrice = resultPrice.plus(itemPrice).minus(priceOff);
			}
		} else if (offerType == OfferType.FIXED_OFF) {
			for (ProductItem resultItem : resultProdItems) {
				Money itemPrice = new Money(resultItem.getProduct().getMrpPrice());
				Money priceOff = itemPrice.minus(new Money(offerValue));
				resultPrice = resultPrice.plus(itemPrice).minus(priceOff);
			}
		} else if (offerType == OfferType.FIXED_PRICE) {
			Money totalFixedPrice = new Money(offerValue).times(resultProdItems.size());
			resultPrice.plus(totalFixedPrice);
		}

		return resultPrice;
	}

	private List<ProductItem> findOfferPriceItems(List<ProductItem> productItems, int numberOfCondtionItems, int numberOfOfferPriceItems) {
		return productItems.subList(numberOfCondtionItems, numberOfCondtionItems + numberOfOfferPriceItems);
	}

	private List<ProductItem> findResultProductItems(List<ProductItem> productItems, int numberOfResultItems) {
		List<ProductItem> resultItems = new ArrayList<ProductItem>();
		for (int i = 0; i < numberOfResultItems; i++) {
			//add results from the end of the list
			ProductItem resultItem = productItems.get(productItems.size() - 1 - i);
			resultItems.add(resultItem);
		}
		return resultItems;
	}

	private List<ProductItem> findConditionProductItems(List<ProductItem> productItems, int numberOfCondtionItems) {
		return productItems.subList(0, numberOfCondtionItems);
	}

	private List<ProductItem> createProductItems(List<OrderItem> matchingItems) {
		List<ProductItem> productItems = new ArrayList<ProductItem>();
		for (OrderItem orderItem : matchingItems) {
			for (int i = 0; i < orderItem.getQuantity(); i++) {
				ProductItem productItem = new ProductItem();
				productItem.setProduct(orderItem.getProduct());
				productItems.add(productItem);
			}
		}
		return productItems;
	}

	/*private List<OrderItem> findResultsOrderItems(OrderRequest orderRequest) {
		List<OrderItem> resultItems = new ArrayList<OrderItem>();
		for (OrderItem orderItem : orderRequest.getOrderItems()) {
			if (orderItem.isPromotionProcessed()) {
				continue;
			}
			if (productResult.isApplicableOn(orderItem.getProduct().getProductId())) {
				resultItems.add(orderItem);
			}
		}
		return resultItems;
	}*/

	private void sortDescByMrp(List<OrderItem> matchingItems) {
        Comparator<OrderItem> orderItemComparator = new OrderItemMRPComparator();

        Collections.sort(matchingItems, orderItemComparator);
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
