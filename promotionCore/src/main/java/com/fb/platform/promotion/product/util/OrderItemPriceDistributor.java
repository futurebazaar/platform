/**
 * 
 */
package com.fb.platform.promotion.product.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fb.commons.to.Money;
import com.fb.platform.promotion.to.OrderItem;

/**
 * @author vinayak
 *
 */
public class OrderItemPriceDistributor {

	public static void distributeOnMrp(List<OrderItem> orderItems, Money totalPrice) {
		Map<BigDecimal, PriceQuantity> priceQuantityMap = new HashMap<BigDecimal, OrderItemPriceDistributor.PriceQuantity>();

		for (OrderItem orderItem : orderItems) {
			if (!priceQuantityMap.containsKey(orderItem.getProduct().getMrpPrice())) {
				
				PriceQuantity pq = new PriceQuantity();
				pq.setPrice(orderItem.getProduct().getMrpPrice());
				pq.setQuantity(orderItem.getQuantity());
				pq.addOrderItem(orderItem);

				priceQuantityMap.put(orderItem.getProduct().getMrpPrice(), pq);
			} else {
				PriceQuantity pq = priceQuantityMap.get(orderItem.getProduct().getMrpPrice());
				pq.addQuantity(orderItem.getQuantity());
				pq.addOrderItem(orderItem);
			}
		}

		Money totalPriceQuantity = new Money(BigDecimal.ZERO);
		for (BigDecimal key : priceQuantityMap.keySet()) {
			PriceQuantity pq = priceQuantityMap.get(key);
			totalPriceQuantity = totalPriceQuantity.plus(pq.getPriceQuantity());
		}

		for (BigDecimal key : priceQuantityMap.keySet()) {
			PriceQuantity pq = priceQuantityMap.get(key);
			pq.setTotalPriceQuantity(totalPriceQuantity, totalPrice);
		}

	}

	private static class PriceQuantity {
		private BigDecimal price;
		private int quantity;
		private BigDecimal distributedPrice;
		private List<OrderItem> orderItems = new ArrayList<OrderItem>();
		private Money totalPriceQuantity = null;

		public BigDecimal getPrice() {
			return price;
		}
		public void setPrice(BigDecimal price) {
			this.price = price;
		}
		public int getQuantity() {
			return quantity;
		}
		public void setQuantity(int quantity) {
			this.quantity = quantity;
		}
		public void addQuantity(int newQuantity) {
			this.quantity = this.quantity + newQuantity;
		}
		public BigDecimal getDistributedPrice() {
			return distributedPrice;
		}
		public void setDistributedPrice(BigDecimal distributedPrice) {
			this.distributedPrice = distributedPrice;
		}
		public Money getPriceQuantity() {
			Money priceMoney = new Money(price);
			return priceMoney.times(quantity);
		}
		public void addOrderItem(OrderItem orderItem) {
			orderItems.add(orderItem);
		}
		public void setTotalPriceQuantity(Money totalPriceQuantity, Money totalPrice) {
			this.totalPriceQuantity = totalPriceQuantity;

			Money priceQuantity = getPriceQuantity();
			double ratio = priceQuantity.getAmount().doubleValue() / totalPriceQuantity.getAmount().doubleValue();
			double orderItemShare = ratio * totalPrice.getAmount().doubleValue();
			//BigDecimal ratio = priceQuantity.getAmount().divide(totalPriceQuantity.getAmount(), RoundingMode.HALF_EVEN);
			//individualProductPrice = priceQuantity.div(totalPriceQuantity.getAmount().doubleValue());
			//BigDecimal orderItemShare = ratio.multiply(totalPrice.getAmount());
			BigDecimal orderItemShareBD = new BigDecimal(orderItemShare);
			orderItemShareBD = orderItemShareBD.setScale(2, RoundingMode.HALF_EVEN);
			Money individualProductPrice = new Money(orderItemShareBD).div(quantity);
			
			//individualProductPrice = getPriceQuantity().div(totalPriceQuantity.getAmount().doubleValue()).times(totalPrice.getAmount().doubleValue()).div(quantity);
			for (OrderItem orderItem : orderItems) {
				orderItem.getProduct().setDiscountedPrice(individualProductPrice.getAmount());
				orderItem.setPromotionProcessed(true);
			}
		}
	}
}
