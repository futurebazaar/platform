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
public class CategoryCondition implements Condition {

	private List<Integer> categoryIds = new ArrayList<Integer>();
	private boolean include = true;
	private int quantity = 0;

	public List<Integer> getCategoryIds() {
		return categoryIds;
	}
	public void setCategoryIds(List<Integer> categoryIds) {
		this.categoryIds = categoryIds;
	}
	public boolean isInclude() {
		return include;
	}
	public void setInclude(boolean include) {
		this.include = include;
	}
	
	@Override
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
