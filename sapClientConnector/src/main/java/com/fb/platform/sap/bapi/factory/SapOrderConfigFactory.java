package com.fb.platform.sap.bapi.factory;

import com.fb.platform.sap.bapi.order.TinlaOrderType;
import com.fb.platform.sap.client.commons.SapOrderConstants;
import com.fb.platform.sap.client.commons.TinlaClient;

public class SapOrderConfigFactory {
	
	public static String getConfigValue(String configKey, TinlaClient client, TinlaOrderType orderType) {
		if (configKey.equals(SapOrderConstants.SALES_ORGANIZATION)) {
			if (client.equals(TinlaClient.BIGBAZAAR)) {
				return SapOrderConstants.BBIL_SALES_ORGANIZATION;
			}
			return SapOrderConstants.FBIL_SALES_ORGANIZATION;
		} else if (configKey.equals(SapOrderConstants.CUSTOMER_GROUP)) {
			if (client.equals(TinlaClient.BIGBAZAAR)) {
				return SapOrderConstants.BB_CUSTOMER_GROUP;
			}
			return SapOrderConstants.FB_CUSTOMER_GROUP;
		} else if (configKey.equals(SapOrderConstants.ITEM_CATEGORY)) {
			if (client.equals(TinlaClient.BIGBAZAAR)) {
				return SapOrderConstants.DEFAULT_BB_ITEM_CATEGORY;
			}
			return SapOrderConstants.DEFAULT_FB_ITEM_CATEGORY;
		} else if (configKey.equals(SapOrderConstants.HEADER_LSP)) {
			if (client.equals(TinlaClient.BIGBAZAAR)) {
				return SapOrderConstants.DEFAULT_HEADER_LSP;
			}
			return "";
		}
		return null;
	}

}
