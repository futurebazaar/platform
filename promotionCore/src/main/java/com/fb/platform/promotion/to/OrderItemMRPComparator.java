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
public class OrderItemMRPComparator implements Comparator<OrderItem>{

	@Override
	public int compare(OrderItem orderItem1, OrderItem orderItem2) {
		BigDecimal mrpDiff = orderItem2.getProduct().getMrpPrice().subtract(orderItem1.getProduct().getMrpPrice());
		return mrpDiff.intValue();
	}

}
