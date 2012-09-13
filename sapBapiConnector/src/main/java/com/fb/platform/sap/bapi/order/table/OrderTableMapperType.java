package com.fb.platform.sap.bapi.order.table;

public enum OrderTableMapperType {
	
	HEADER,
	ITEM,
	CONDITIONS,
	SCHEDULES;
	
	@Override
	public String toString() {
		return this.name();
	}

}
