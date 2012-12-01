package com.fb.platform.sap.bapi.order;

public enum BapiOrderTemplate {
	
	ZATG_BAPI_FBILFECIL_SO_CR8_NEW, // For New Order
	ZCUST_RETURN_ORDER_CREATE, // for return Order
	ZATG_BAPI_SO_FB_FECIL_CHNG_NEW, // For Modified or Cancelled Order
	
	//Big Bazaar bapi
	ZBAPI_SALESORDER_CREATEFROMDAT,
	ZBAPI_SALESORDER_CHANGEFROMDAT,
	ZBB_SALESORDER_RETURN;
	
	@Override
	public String toString() {
		return this.name();
	}
}

