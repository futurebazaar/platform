package com.fb.platform.promotion.rule;

import java.io.Serializable;

import com.fb.commons.to.Money;
import com.fb.platform.promotion.to.OrderRequest;

public class OrderRuleRequest implements Serializable,RuleRequest{
	
	private OrderRequest orderReq;

	public OrderRequest getOrderReq() {
		return orderReq;
	}

	public void setOrderReq(OrderRequest orderReq) {
		this.orderReq = orderReq;
	}
	
	public Money getOrderValue(){
		return new Money(orderReq.getOrderValue());
	}
}
