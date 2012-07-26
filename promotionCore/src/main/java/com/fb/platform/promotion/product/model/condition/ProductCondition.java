/**
 * 
 */
package com.fb.platform.promotion.product.model.condition;

import java.util.ArrayList;
import java.util.List;

import com.fb.platform.promotion.product.model.Condition;

/**
 * @author vinayak
 *
 */
public class ProductCondition implements Condition {

	private List<Integer> productIds = new ArrayList<Integer>();
	private int quantity = 0;

	public List<Integer> getProductIds() {
		return productIds;
	}
	public void setProductIds(List<Integer> productIds) {
		this.productIds = productIds;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

}
