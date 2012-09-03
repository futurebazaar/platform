package com.fb.commons.mom.to;

import java.util.HashMap;
import java.util.Map;

/**
 * @author nehaga
 *
 */
public enum OrderStateEnum {
	C("C", 10),
	J("J", 20),
	R("R", 30),
	M("M", 40),
	O("O", 50),
	T("T", 60),
	H("H", 70),
	N("N", 80),
	Q("Q", -30),
	X("X", -20),
	PLUS("+", -10);
	
	private String orderState;
	
	private int priority;

	private static Map<String, OrderStateEnum> orderStateMap = new HashMap<String, OrderStateEnum>();
	
	static {
		for (OrderStateEnum orderStateEnum : OrderStateEnum.values()) {
			orderStateMap.put(orderStateEnum.getOrderState(), orderStateEnum);
		}
	}
	
	private OrderStateEnum(String orderState, int priority) {
		this.orderState = orderState;
		this.priority = priority;
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

	public int getPriority() {
		return priority;
	}
}
