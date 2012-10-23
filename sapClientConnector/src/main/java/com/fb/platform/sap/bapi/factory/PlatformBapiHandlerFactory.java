package com.fb.platform.sap.bapi.factory;

import com.fb.commons.PlatformException;
import com.fb.platform.sap.bapi.order.BapiOrderTemplate;
import com.fb.platform.sap.bapi.order.TinlaOrderType;
import com.fb.platform.sap.client.commons.SapUtils;
import com.fb.platform.sap.client.commons.TinlaClient;

public class PlatformBapiHandlerFactory {
	
	public static BapiOrderTemplate getTemplate(TinlaOrderType orderType, String client) {
		switch(orderType) {
			case MOD_ORDER:
				if (SapUtils.isBigBazaar(TinlaClient.valueOf(client))) {
					return BapiOrderTemplate.ZBAPI_SALESORDER_CHANGEFROMDAT;
				}
				return BapiOrderTemplate.ZATG_BAPI_SO_FB_FECIL_CHNG_NEW;
			case RET_ORDER:
				return BapiOrderTemplate.ZCUST_RETURN_ORDER_CREATE;
			case NEW_ORDER:
				if (SapUtils.isBigBazaar(TinlaClient.valueOf(client))) {
					return BapiOrderTemplate.ZBAPI_SALESORDER_CREATEFROMDAT;
				}
				return  BapiOrderTemplate.ZATG_BAPI_FBILFECIL_SO_CR8_NEW;
			default:
				throw new PlatformException("No Bapi Exists for orderType : " + orderType);
		}
	}
	
}
