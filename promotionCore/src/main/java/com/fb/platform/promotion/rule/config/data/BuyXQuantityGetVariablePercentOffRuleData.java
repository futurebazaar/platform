/**
 * 
 */
package com.fb.platform.promotion.rule.config.data;

import java.util.List;

import com.fb.commons.to.Money;
import com.fb.platform.promotion.rule.config.type.QuantityDiscountMap;

/**
 * @author keith
 *
 */
public class BuyXQuantityGetVariablePercentOffRuleData implements RuleData {

	private Money minOrderValue = null;
	private Money maxDiscountPerUse = null;
	private List<Integer> clientList = null;
	private List<Integer> includeCategoryList = null;
	private List<Integer> excludeCategoryList = null;
	private List<Integer> brands;
	private QuantityDiscountMap quantityDiscountMap = null;	// "1=5,2=15,3=20"
	
	public Money getMinOrderValue() {
		return minOrderValue;
	}
	public void setMinOrderValue(Money minOrderValue) {
		this.minOrderValue = minOrderValue;
	}
	public Money getMaxDiscountPerUse() {
		return maxDiscountPerUse;
	}
	public void setMaxDiscountPerUse(Money maxDiscountPerUse) {
		this.maxDiscountPerUse = maxDiscountPerUse;
	}
	public List<Integer> getClientList() {
		return clientList;
	}
	public void setClientList(List<Integer> clientList) {
		this.clientList = clientList;
	}
	public List<Integer> getIncludeCategoryList() {
		return includeCategoryList;
	}
	public void setIncludeCategoryList(List<Integer> includeCategoryList) {
		this.includeCategoryList = includeCategoryList;
	}
	public List<Integer> getExcludeCategoryList() {
		return excludeCategoryList;
	}
	public void setExcludeCategoryList(List<Integer> excludeCategoryList) {
		this.excludeCategoryList = excludeCategoryList;
	}
	public List<Integer> getBrands() {
		return brands;
	}
	public void setBrands(List<Integer> brands) {
		this.brands = brands;
	}
	public QuantityDiscountMap getQuantityDiscountMap() {
		return quantityDiscountMap;
	}
	public void setQuantityDiscountMap(QuantityDiscountMap quantityDiscountMap) {
		this.quantityDiscountMap = quantityDiscountMap;
	}
	
}
