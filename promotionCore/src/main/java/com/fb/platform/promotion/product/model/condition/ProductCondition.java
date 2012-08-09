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
public class ProductCondition implements Condition,Comparable<ProductCondition> {

	private List<Integer> productIds = new ArrayList<Integer>();
	private int quantity = 0;

	public List<Integer> getProductIds() {
		return productIds;
	}
	public void setProductIds(List<Integer> productIds) {
		this.productIds = productIds;
	}
	
	@Override
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public boolean isApplicableOn(int productId) {
		if (productIds.contains(productId)) {
			return true;
		}
		return false;
	}

	public boolean isApplicableOn(int productId, int orderItemQuantity) {
		if (productIds.contains(productId) && orderItemQuantity >= quantity) {
			return true;
		}
		return false;
	}
	@Override
	public int compareTo(ProductCondition o) {
		return o.quantity - this.quantity;
	}
}
