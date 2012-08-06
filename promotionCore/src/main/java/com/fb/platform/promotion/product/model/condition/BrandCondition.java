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
public class BrandCondition implements Condition {

	private List<Integer> brandIds = new ArrayList<Integer>();
	private boolean include = true;
	private int quantity = 0;

	public List<Integer> getBrandIds() {
		return brandIds;
	}
	public void setBrandIds(List<Integer> brandIds) {
		this.brandIds = brandIds;
	}
	public boolean isInclude() {
		return include;
	}
	public void setInclude(boolean include) {
		this.include = include;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	@Override
	public boolean isApplicableOn(int productId) {
		// TODO Auto-generated method stub
		return false;
	}
}
