package com.fb.platform.promotion.rule;

import com.fb.commons.to.Money;

public class OrderRuleResponse {

	private Money discountValue;

	public Money getDiscountValue() {
		return discountValue;
	}

	public void setDiscountValue(Money discountValue) {
		this.discountValue = discountValue;
	}
	
}
