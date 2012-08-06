/**
 * 
 */
package com.fb.platform.promotion.product.model;

/**
 * @author vinayak
 *
 */
public interface Condition {
	public boolean isApplicableOn(int productId);
}
