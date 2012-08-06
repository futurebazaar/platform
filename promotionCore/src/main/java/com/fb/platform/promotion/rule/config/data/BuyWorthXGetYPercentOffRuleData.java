/**
 * 
 */
package com.fb.platform.promotion.rule.config.data;

import java.math.BigDecimal;
import java.util.List;

import com.fb.commons.to.Money;

/**
 * @author keith
 * 
 */
public class BuyWorthXGetYPercentOffRuleData implements RuleData {

	private BigDecimal discountPercentage;
	private Money maxDiscountPerUse;
	private Money minOrderValue = null;
	private List<Integer> clientList = null;
	private List<Integer> includeCategoryList = null;
	private List<Integer> excludeCategoryList = null;
	private List<Integer> brandList = null;
	private List<Integer> sellerList = null;

	public BigDecimal getDiscountPercentage() {
		return discountPercentage;
	}

	public void setDiscountPercentage(BigDecimal discountPercentage) {
		this.discountPercentage = discountPercentage;
	}

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

	public List<Integer> getBrandList() {
		return brandList;
	}

	public void setBrandList(List<Integer> brands) {
		this.brandList = brands;
	}

	public List<Integer> getSellerList() {
		return sellerList;
	}

	public void setSellerList(List<Integer> sellerList) {
		this.sellerList = sellerList;
	}

}
