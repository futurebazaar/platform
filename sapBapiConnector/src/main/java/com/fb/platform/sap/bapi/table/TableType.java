package com.fb.platform.sap.bapi.table;

public enum TableType {
	VALUE_TABLE,
	COMMIT_TABLE;
	
	@Override
	public String toString() {
		return this.name();
	}
}
