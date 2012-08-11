/**
 * 
 */
package com.fb.platform.promotion.rule.product;

/**
 * @author vinayak
 *
 */
public interface Rule {

	public boolean isApplicableOn(int productId, int userId);
}
