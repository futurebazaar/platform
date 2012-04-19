package com.fb.platform.promotion.rule;

import java.io.Serializable;
import java.math.BigDecimal;

import com.fb.commons.to.Money;

public class OrderRuleResponse implements Serializable{

	private Money discountValue;
	
	public OrderRuleResponse(BigDecimal discVal){
		this.discountValue = new Money(discVal);
	}
	
	public OrderRuleResponse(Money discVal){
		this.discountValue = discVal;
	}

	public Money getDiscountValue() {
		return discountValue;
	}

	public void setDiscountValue(Money discountValue) {
		this.discountValue = discountValue;
	}
	
}
