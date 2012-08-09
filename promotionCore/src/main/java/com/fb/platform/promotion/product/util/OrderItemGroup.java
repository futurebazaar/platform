/**
 * 
 */
package com.fb.platform.promotion.product.util;

import java.util.ArrayList;
import java.util.List;

import com.fb.platform.promotion.to.OrderItem;

/**
 * @author vinayak
 *
 */
public class OrderItemGroup {

	private List<OrderItem> orderItems = new ArrayList<OrderItem>();

	public void add(OrderItem orderItem) {
		this.orderItems.add(orderItem);
	}

	public List<OrderItem> getItems() {
		return this.orderItems;
	}

}
