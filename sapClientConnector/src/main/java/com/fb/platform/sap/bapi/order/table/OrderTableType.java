package com.fb.platform.sap.bapi.order.table;

public enum OrderTableType {
	VALUE_TABLE,
	COMMIT_TABLE;
	
	@Override
	public String toString() {
		return this.name();
	}
}
