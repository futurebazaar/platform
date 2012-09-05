package com.fb.platform.sap.bapi.table;

public enum TableMapperType {
	
	HEADER,
	ITEM,
	CONDITIONS,
	SCHEDULES;
	
	@Override
	public String toString() {
		return this.name();
	}

}
