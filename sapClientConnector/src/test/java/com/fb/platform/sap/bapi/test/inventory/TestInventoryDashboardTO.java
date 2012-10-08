package com.fb.platform.sap.bapi.test.inventory;

import org.joda.time.DateTime;
import org.junit.Test;

import com.fb.commons.test.BaseTestCase;
import com.fb.platform.sap.bapi.to.SapInventoryDashboardRequestTO;

public class TestInventoryDashboardTO {
	
	public SapInventoryDashboardRequestTO getRequestTO() {
		SapInventoryDashboardRequestTO inventoryDashboardRequestTO = new SapInventoryDashboardRequestTO();
		inventoryDashboardRequestTO.setArticle("000000000300000560");
		inventoryDashboardRequestTO.setFromDateTime(new DateTime(2012, 9, 1, 0, 0, 0));
		inventoryDashboardRequestTO.setToDateTime(DateTime.now());
		inventoryDashboardRequestTO.setPlant("");
		return inventoryDashboardRequestTO;
	}
	
	@Test
	public void dummy() {
		
	}
}
