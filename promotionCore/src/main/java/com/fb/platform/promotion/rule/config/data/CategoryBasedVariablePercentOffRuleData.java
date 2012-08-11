/**
 * 
 */
package com.fb.platform.promotion.rule.config.data;

import java.util.List;

import com.fb.commons.to.Money;

/**
 * @author keith
 *
 */
public class CategoryBasedVariablePercentOffRuleData implements RuleData {

	
	private Money maxDiscountPerUse;
	private Money minOrderValue = null;
	private List<Integer> clientList = null;
	private CategoryDiscount categoryDiscountPairs = null;
	
	public Money getMaxDiscountPerUse() {
		return maxDiscountPerUse;
	}
	public void setMaxDiscountPerUse(Money maxDiscountPerUse) {
		this.maxDiscountPerUse = maxDiscountPerUse;
	}
	
	public Money getMinOrderValue() {
		return minOrderValue;
	}
	
	public void setMinOrderValue(Money minOrderValue) {
		this.minOrderValue = minOrderValue;
	}
	
	public List<Integer> getClientList() {
		return clientList;
	}
	
	public void setClientList(List<Integer> clientList) {
		this.clientList = clientList;
	}
	
	public CategoryDiscount getCategoryDiscountPairs() {
		return categoryDiscountPairs;
	}
	
	public void setCategoryDiscountPairs(CategoryDiscount categoryDiscountMap) {
		this.categoryDiscountPairs = categoryDiscountMap;
	}
	
	
	
}
