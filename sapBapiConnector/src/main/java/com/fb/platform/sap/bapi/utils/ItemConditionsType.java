package com.fb.platform.sap.bapi.utils;

public enum ItemConditionsType {
	
	ITZ_CONDITION_TYPE,
	SHIPPING_CONDITION_TYPE,
	COUPON_CONDITION_TYPE,
	ITEM_DISCOUNT_CONDITION_TYPE,
	NLC_CONDITION_TYPE,
	MRP_CONDITION_TYPE,
	LIST_CONDITION_TYPE,
	SALES_CONDITION_TYPE,
	WARRANTY_CONDITION_TYPE;

	@Override
	public String toString() {
		return this.name();
	}
}
