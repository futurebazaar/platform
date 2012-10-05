/**
 * 
 */
package com.fb.platform.ifs.service;

import com.fb.platform.ifs.model.DCAvailability;
import com.fb.platform.ifs.to.order.Order;

/**
 * @author vinayak
 *
 */
public interface DCManager {

	/**
	 * Finds out which articles in the order are available at this DC and are serviceable from this DC
	 * to given pincode.
	 * @param order
	 * @param pincode
	 * @return
	 */
	public DCAvailability findStockAvailability(Order order, String pincode, boolean isCod);
}
