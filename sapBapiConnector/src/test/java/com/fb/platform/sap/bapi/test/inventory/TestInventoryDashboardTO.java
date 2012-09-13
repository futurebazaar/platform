package com.fb.platform.sap.bapi.test.inventory;

import org.joda.time.DateTime;

import com.fb.platform.sap.bapi.to.SapInventoryDashboardRequestTO;

public class TestInventoryDashboardTO {
	
	public SapInventoryDashboardRequestTO getRequestTO() {
		SapInventoryDashboardRequestTO inventoryDashboardRequestTO = new SapInventoryDashboardRequestTO();
		inventoryDashboardRequestTO.setArticle("000000000300000560");
		inventoryDashboardRequestTO.setFromDateTime(new DateTime(2012, 9, 4, 0, 0, 0));
		inventoryDashboardRequestTO.setToDateTime(new DateTime(2012, 9, 13, 23, 59, 59));
		inventoryDashboardRequestTO.setPlant("");
		return inventoryDashboardRequestTO;
	}

}
