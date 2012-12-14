/**
 * 
 */
package com.fb.platform.promotion.rule.config.data;

import java.math.BigDecimal;

import com.fb.commons.to.Money;

/**
 * @author vinayak
 *
 */
public class DiscountOnClearanceProductPercentOffRuleData implements RuleData {

	private BigDecimal discountPercentage = BigDecimal.ZERO;
	private Money minOrderValue = null;

	public BigDecimal getDiscountPercentage() {
		return discountPercentage;
	}

	public void setDiscountPercentage(BigDecimal discountPercentage) {
		this.discountPercentage = discountPercentage;
	}

	public Money getMinOrderValue() {
		return minOrderValue;
	}
	public void setMinOrderValue(Money minOrderValue) {
		this.minOrderValue = minOrderValue;
	}
}
