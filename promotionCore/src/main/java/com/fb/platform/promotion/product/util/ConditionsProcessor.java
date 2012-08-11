/**
 * 
 */
package com.fb.platform.promotion.product.util;

import java.util.List;

import com.fb.platform.promotion.product.model.ConfigModule;
import com.fb.platform.promotion.to.OrderItem;
import com.fb.platform.promotion.to.OrderRequest;

/**
 * @author vinayak
 *
 */
public class ConditionsProcessor {

	public boolean matchOrderCondition(OrderRequest request, ConfigModule config) {
		
		return false;
	}

	public List<OrderItem> matchingOrderItems(OrderRequest request) {
		return null;
	}
}
