/**
 * 
 */
package com.fb.platform.promotion.product.model.result;

import java.util.ArrayList;
import java.util.List;

/**
 * @author vinayak
 *
 */
public class ProductResult extends AbstractResult {

	private List<Integer> productIds = new ArrayList<Integer>();

	public List<Integer> getProductIds() {
		return productIds;
	}
	public void setProductIds(List<Integer> productIds) {
		this.productIds = productIds;
	}
}
