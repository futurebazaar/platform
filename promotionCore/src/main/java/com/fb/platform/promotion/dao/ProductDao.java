/**
 * 
 */
package com.fb.platform.promotion.dao;

import java.util.Set;

/**
 * This is a temporay interaface most probably. Should be moved to product/catalogue module
 * once that module is ported to java. 
 * 
 * @author vinayak
 *
 */
public interface ProductDao {

	/**
	 * Find the product Ids on which clearance sale tag is applied.
	 * @param orderProductIds The list of productIds from the order.
	 * @return The productIds on which clearance tag is applied.
	 */
	public Set<Integer> findClearanceProductIds(Set<Integer> orderProductIds);
}
