/**
 * 
 */
package com.fb.platform.promotion.product.util;

import com.fb.platform.promotion.to.OrderRequest;

/**
 * @author vinayak
 *
 */
public interface ConditionResultProcessor {

	public boolean process(OrderRequest orderRequest);
}
