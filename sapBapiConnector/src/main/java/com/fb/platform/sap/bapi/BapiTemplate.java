package com.fb.platform.sap.bapi;

public enum BapiTemplate {
	
	ZATG_BAPI_FBILFECIL_SO_CR8_NEW, // For New Order
	ZCUST_RETURN_ORDER_CREATE, // for return Order
	ZATG_BAPI_SO_FB_FECIL_CHNG_NEW; // For Modified or Cancelled Order
	
	@Override
	public String toString() {
		return this.name();
	}
}

