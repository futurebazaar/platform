package com.fb.platform.sap.bapi.factory;

import com.fb.commons.mom.to.OrderHeaderTO;
import com.fb.platform.sap.bapi.order.TinlaOrderType;
import com.fb.platform.sap.client.commons.SapOrderConstants;
import com.fb.platform.sap.client.commons.SapUtils;
import com.fb.platform.sap.client.commons.TinlaClient;

public class SapOrderConfigFactory {
	
	public static String getConfigValue(String configKey, TinlaClient client, TinlaOrderType orderType, OrderHeaderTO orderHeaderTO) {
		if (configKey.equals(SapOrderConstants.SALES_ORGANIZATION)) {
			if (SapUtils.isBigBazaar(client)) {
				return SapOrderConstants.BBIL_SALES_ORGANIZATION;
			}
			return SapOrderConstants.FBIL_SALES_ORGANIZATION;
		} else if (configKey.equals(SapOrderConstants.CUSTOMER_GROUP)) {
			if (SapUtils.isBigBazaar(client)) {
				return SapOrderConstants.BB_CUSTOMER_GROUP;
			}
			return SapOrderConstants.FB_CUSTOMER_GROUP;
		} else if (configKey.equals(SapOrderConstants.ITEM_CATEGORY)) {
			if (SapUtils.isBigBazaar(client)) {
				return SapOrderConstants.DEFAULT_BB_ITEM_CATEGORY;
			}
			return SapOrderConstants.DEFAULT_FB_ITEM_CATEGORY;
		} else if (configKey.equals(SapOrderConstants.HEADER_LSP)) {
			if (SapUtils.isBigBazaar(client)) {
				return SapOrderConstants.DEFAULT_HEADER_LSP;
			}
			return orderHeaderTO.getPricingTO().getPayableAmount().toString();
		}
		return null;
	}

}
