package com.fb.commons.mom.to;

import java.util.HashMap;
import java.util.Map;

/**
 * @author nehaga
 *
 */
public enum OrderStateEnum {
	C("C"),
	R("R"),
	M("M"),
	N("N"),
	O("O"),
	T("T"),
	J("J"),
	Q("Q"),
	H("H"),
	X("X"),
	PLUS("+");
	
	private String orderState;

	private static Map<String, OrderStateEnum> orderStateMap = new HashMap<String, OrderStateEnum>();
	
	static {
		for (OrderStateEnum orderStateEnum : OrderStateEnum.values()) {
			orderStateMap.put(orderStateEnum.getOrderState(), orderStateEnum);
		}
	}
	
	private OrderStateEnum(String orderState) {
		this.orderState = orderState;
	}
	
	public static OrderStateEnum getInstance(String orderState) {
		return orderStateMap.get(orderState.trim());
	}
	
	@Override
	public String toString() {
		return this.name();
	}
	
	private String getOrderState() {
		return orderState;
	}
}
