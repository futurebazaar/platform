package com.fb.platform.order;

/**
 * This class represents the various states of an Order in the system.
 * Copied from Python Order Model. Will refactor later if required.
 * 
 * @author vinayak
 */
public enum OrderStatus {

	CART("cart"),
	UNASSIGNED_CART("unassigned_cart"),
	GUEST_CART("guest_cart"),
	TEMPORARY_CART("temporary_cart"),
	PENDING_ORDER("pending_order"),
	CONFIRMED("confirmed"),
	BUY_LATER("buy_later"),
	CANCELLED("cancelled");

	private final String statusCode;

	OrderStatus(String statusCode) {
		this.statusCode = statusCode;
	}
}
