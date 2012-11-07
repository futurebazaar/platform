/**
 * 
 */
package com.fb.platform.promotion.product.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
import com.fb.platform.promotion.to.ConfigResultApplyStatusEnum;
import com.fb.platform.promotion.to.OrderItem;
import com.fb.platform.promotion.to.OrderItemMRPComparator;
import com.fb.platform.promotion.to.OrderItemPromotionApplicationEnum;
import com.fb.platform.promotion.to.OrderRequest;

/**
 * @author vinayak
 *
 */
public class ProdConditionProdResult implements ConditionResultProcessor {

	private ConfigModule configModule = null;
	private ProductCondition productCondition = null;
	private ProductResult productResult = null;

	public ProdConditionProdResult(ConfigModule configModule) {
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
	public ConfigResultApplyStatusEnum process(OrderRequest orderRequest) {
		List<OrderItem> matchingConditionItems = findMatchingConditionOrderItems(orderRequest);

		if (matchingConditionItems.size() == 0) {
			return ConfigResultApplyStatusEnum.ERROR;
		}

		int matchingConditionQuantity = findMatchingQuantity(matchingConditionItems);
		if (matchingConditionQuantity < productCondition.getQuantity()) {
			return ConfigResultApplyStatusEnum.ERROR;
		}

		List<OrderItem> matchingResultItems = findMatchingResultOrderItems(orderRequest);

		if (matchingResultItems.size() == 0) {
			//the user did not buy the discounted product even when user was entitled to it. what can i do? his choice :-)
			//should we automatically add it to the order if it is free? something for business to answer LATER ;-)
			return ConfigResultApplyStatusEnum.SUCESS;
		}

		//int matchingResultQuantity = findMatchingQuantity(matchingResultItems); 

		//this will be the number of discounted result items we can have
		int conditionGroups = matchingConditionQuantity / productCondition.getQuantity();
		int applicableResultQuantity = productResult.getQuantity() * conditionGroups;

		//sort the result order items by mrp
		List<OrderItem> sortedMatchingResultItems = new ArrayList<OrderItem>(matchingResultItems);
		sortDescByMrp(sortedMatchingResultItems);

		List<ProductItem> productItems = createProductItems(sortedMatchingResultItems);

		List<ProductItem> applicableResultItems = findApplicableResultProductItems(productItems, applicableResultQuantity);
		List<ProductItem> offerPriceResultItems = productItems.subList(applicableResultQuantity, productItems.size());

		List<ProductItem> conditionProdItems = createProductItems(matchingConditionItems);
		Money totalPriceForMatchingItems = calculateTotalPrice(conditionProdItems, applicableResultItems, offerPriceResultItems);

		List<OrderItem> matchingItems = new ArrayList<OrderItem>();
		matchingItems.addAll(matchingConditionItems);
		matchingItems.addAll(matchingResultItems);

		updatePromotionStatus(matchingItems);
		orderRequest.setTotalPrice(totalPriceForMatchingItems);

		distributePriceOnMatchingItems(matchingItems, totalPriceForMatchingItems);

		return isProcessSuccessful(orderRequest.getOrderItems());
	}

	private void updatePromotionStatus(List<OrderItem> orderItems) {
		for (OrderItem orderItem : orderItems) {
			orderItem.getOrderItemPromotionStatus().setOrderItemPromotionApplication(OrderItemPromotionApplicationEnum.SUCCESS);
			orderItem.getOrderItemPromotionStatus().setRemainingQuantity(0);
			orderItem.getOrderItemPromotionStatus().setAppliedQuantity(orderItem.getQuantity());
		}
	}

	private ConfigResultApplyStatusEnum isProcessSuccessful(List<OrderItem> orderItems) {
		ConfigResultApplyStatusEnum isSuccesfull = ConfigResultApplyStatusEnum.SUCESS;
		for (OrderItem orderItem : orderItems) {
			if(orderItem.getOrderItemPromotionStatus().getOrderItemPromotionApplication() != OrderItemPromotionApplicationEnum.SUCCESS) {
				isSuccesfull = ConfigResultApplyStatusEnum.PARTIAL;
				break;
			}
		}
		return isSuccesfull;
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
		finalPrice = finalPrice.plus(resultPrice);

		for (ProductItem offerPriceItem : offerPriceProdItems) {
			finalPrice = finalPrice.plus(new Money(offerPriceItem.getProduct().getPrice()));
		}
		
		BigDecimal amount = finalPrice.getAmount().setScale(0,  RoundingMode.FLOOR);	//Rounding down to maximize discount
		finalPrice = new Money(amount);
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
			resultPrice = resultPrice.plus(totalFixedPrice);
		}

		return resultPrice;
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

	private List<ProductItem> findApplicableResultProductItems(List<ProductItem> matchingResultItems, int applicableResultQuantity) {
		if (matchingResultItems.size() <= applicableResultQuantity) {
			return matchingResultItems;
		}

		return matchingResultItems.subList(0, applicableResultQuantity);
	}


	private List<OrderItem> findMatchingConditionOrderItems(OrderRequest orderRequest) {
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

	private List<OrderItem> findMatchingResultOrderItems(OrderRequest orderRequest) {
		List<OrderItem> items = new ArrayList<OrderItem>();
		for (OrderItem orderItem : orderRequest.getOrderItems()) {
			if (orderItem.getOrderItemPromotionStatus().getOrderItemPromotionApplication() == OrderItemPromotionApplicationEnum.SUCCESS) {
				continue;
			}
			if (productResult.isApplicableOn(orderItem.getProduct().getProductId())) {
				items.add(orderItem);
			}
		}
		return items;
	}

	private int findMatchingQuantity(List<OrderItem> matchingItems) {
		int quantity = 0;
		for (OrderItem orderItem : matchingItems) {
			quantity += orderItem.getQuantity();
		}
		return quantity;
	}

	private void sortDescByMrp(List<OrderItem> matchingItems) {
        Comparator<OrderItem> orderItemComparator = new OrderItemMRPComparator();

        Collections.sort(matchingItems, orderItemComparator);
	}
}
