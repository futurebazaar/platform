package com.fb.platform.sap.bapi.order;

public enum TinlaOrderType {
	
	NEW_ORDER,
	RET_ORDER,
	CAN_ORDER,
	MOD_ORDER;
	
	@Override
	public String toString() {
		return this.name();
	}
}