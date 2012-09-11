/**
 * 
 */
package com.fb.platform.ifs.service;

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
	public Object findAvailability(Object order, String pincode);
}
