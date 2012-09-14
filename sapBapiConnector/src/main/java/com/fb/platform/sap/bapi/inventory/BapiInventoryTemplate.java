package com.fb.platform.sap.bapi.inventory;

public enum BapiInventoryTemplate {
	
	ZFB_INVCHECK,
	ZBAPI_FM_TINLASTKQTY;

	@Override
	public String toString() {
		return this.name();
	}
	
}
