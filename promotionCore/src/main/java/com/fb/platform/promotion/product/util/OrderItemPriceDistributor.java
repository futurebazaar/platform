/**
 * 
 */
package com.fb.platform.promotion.product.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fb.commons.to.Money;
import com.fb.platform.promotion.to.OrderItem;
import com.fb.platform.promotion.to.OrderItemPromotionApplicationEnum;
import com.fb.platform.promotion.to.OrderRequest;

/**
 * @author vinayak
 *
 */
public class OrderItemPriceDistributor {

	public static void distributeOnMrp(List<OrderItem> orderItems, Money totalPrice) {
		
		//updateTotalPrice(orderItems, totalPrice);
		
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

		double totalPriceQuantity = 0;
		for (BigDecimal key : priceQuantityMap.keySet()) {
			PriceQuantity pq = priceQuantityMap.get(key);
			totalPriceQuantity += pq.getPriceQuantity().getAmount().doubleValue();
		}
		
		int keyCount = priceQuantityMap.keySet().size();
		double totalItemShare = totalPrice.getAmount().doubleValue();
		double remItemShare = totalItemShare;
		double itemShare = 0;
		for (BigDecimal key : priceQuantityMap.keySet()) {
			PriceQuantity pq = priceQuantityMap.get(key);
			if (keyCount == 1)
				itemShare = remItemShare;
			else {
				double ratio = pq.getPriceQuantity().getAmount().doubleValue() / totalPriceQuantity;
				itemShare = Math.ceil(ratio * totalPrice.getAmount().doubleValue());
			}
			pq.setTotalPriceQuantity(itemShare);
			remItemShare -= itemShare;
			keyCount--;
		}

	}
	
	public static void updateTotalPrice(OrderRequest orderRequest) {
		List<OrderItem> orderItems = orderRequest.getOrderItems();
		Money totalPrice = orderRequest.getTotalPrice();

		for(OrderItem orderItem : orderItems) {
			if(OrderItemPromotionApplicationEnum.PARTIAL == orderItem.getOrderItemPromotionStatus().getOrderItemPromotionApplication()) {
				totalPrice = totalPrice.plus(new Money(orderItem.getProduct().getPrice()).times(orderItem.getOrderItemPromotionStatus().getRemainingQuantity()));
				orderItem.getOrderItemPromotionStatus().setOrderItemPromotionApplication(OrderItemPromotionApplicationEnum.SUCCESS);
				orderItem.getOrderItemPromotionStatus().setAppliedQuantity(orderItem.getOrderItemPromotionStatus().getAppliedQuantity() + orderItem.getOrderItemPromotionStatus().getRemainingQuantity());
				orderItem.getOrderItemPromotionStatus().setRemainingQuantity(0);
			}
		}
		orderRequest.setTotalPrice(totalPrice);
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
		public void setTotalPriceQuantity(double totalItemShare) {
			int itemCount = orderItems.size();
			double remItemShare = totalItemShare;
			double itemShare = 0;
			for (OrderItem orderItem : orderItems) {
				if (itemCount == 1) {
					itemShare = remItemShare;
				} else {
					itemShare = Math.ceil((orderItem.getQuantity() / this.getQuantity()) * totalItemShare);
				}

				int appliedQuantity = orderItem.getOrderItemPromotionStatus().getAppliedQuantity();
				BigDecimal priceForAppliedItems = orderItem.getProduct().getMrpPrice().multiply(new BigDecimal(appliedQuantity));
				orderItem.setTotalDiscount(priceForAppliedItems.subtract(new BigDecimal(itemShare)));
				//orderItem.getOrderItemPromotionStatus().setOrderItemPromotionApplication(OrderItemPromotionApplicationEnum.SUCCESS);
				if(orderItem.getOrderItemPromotionStatus().getRemainingQuantity() > 0) {
					orderItem.getOrderItemPromotionStatus().setOrderItemPromotionApplication(OrderItemPromotionApplicationEnum.PARTIAL);
				} else if (orderItem.getOrderItemPromotionStatus().getOrderItemPromotionApplication() != OrderItemPromotionApplicationEnum.NOT_APPLIED && orderItem.getOrderItemPromotionStatus().getRemainingQuantity() == 0){
					orderItem.getOrderItemPromotionStatus().setOrderItemPromotionApplication(OrderItemPromotionApplicationEnum.SUCCESS);
				}
				//orderItem.setPromotionProcessed(true);
				remItemShare -= itemShare;
				itemCount--;
			}
		}
	}
}
