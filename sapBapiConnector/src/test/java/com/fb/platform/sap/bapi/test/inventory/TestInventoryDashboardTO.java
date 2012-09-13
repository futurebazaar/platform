package com.fb.platform.sap.bapi.test.inventory;

import org.joda.time.DateTime;

import com.fb.platform.sap.bapi.to.SapInventoryDashboardRequestTO;

public class TestInventoryDashboardTO {
	
	public SapInventoryDashboardRequestTO getRequestTO() {
		SapInventoryDashboardRequestTO inventoryDashboardRequestTO = new SapInventoryDashboardRequestTO();
		inventoryDashboardRequestTO.setArticle("");
		inventoryDashboardRequestTO.setFromDateTime(DateTime.now().minusDays(25));
		inventoryDashboardRequestTO.setToDateTime(DateTime.now());
		inventoryDashboardRequestTO.setPlant("");
		return inventoryDashboardRequestTO;
	}

}
