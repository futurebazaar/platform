package com.fb.platform.sap.bapi.inventory.table;

public enum BapiInventoryTable {
	
	TAB_ARTICLE,
	TAB_PLANT,
	TAB_RETURN,
	FROM_DATE,
	FROM_TIME,
	TO_DATE,
	TO_TIME;
	
	@Override
	public String toString() {
		return this.name();
	}
	
}
