/**
 * 
 */
package com.fb.platform.promotion.to;

import java.math.BigDecimal;
import java.util.Comparator;

/**
 * @author nehaga
 *
 */
public class OrderItemOfferPriceComparator implements Comparator<OrderItem>{

	@Override
	public int compare(OrderItem orderItem1, OrderItem orderItem2) {
		BigDecimal offerPriceDiff = orderItem2.getProduct().getPrice().subtract(orderItem1.getProduct().getPrice());
		return offerPriceDiff.intValue();
	}

}
