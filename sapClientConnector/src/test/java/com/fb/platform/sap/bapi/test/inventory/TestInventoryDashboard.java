package com.fb.platform.sap.bapi.test.inventory;

import static org.junit.Assert.*;

import java.util.List;

import org.joda.time.DateTime;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.fb.commons.test.BaseTestCase;
import com.fb.platform.sap.bapi.to.SapInventoryDashboardRequestTO;
import com.fb.platform.sap.bapi.to.SapInventoryDashboardResponseTO;
import com.fb.platform.sap.client.handler.PlatformClientHandler;

public class TestInventoryDashboard extends BaseTestCase{
	
	public SapInventoryDashboardRequestTO getRequestTO() {
		SapInventoryDashboardRequestTO inventoryDashboardRequestTO = new SapInventoryDashboardRequestTO();
		inventoryDashboardRequestTO.setArticle("000000000300000560");
		inventoryDashboardRequestTO.setFromDateTime(new DateTime(2012, 9, 1, 0, 0, 0));
		inventoryDashboardRequestTO.setToDateTime(DateTime.now());
		inventoryDashboardRequestTO.setPlant("");
		return inventoryDashboardRequestTO;
	}
	
	@Test
	public void testInventoryDashboard() {
		//Test Inventory Dashboard
				SapInventoryDashboardRequestTO inventoryDashboardRequestTO = getRequestTO();
				ApplicationContext context = new ClassPathXmlApplicationContext("test-applicationContext-service.xml");
				PlatformClientHandler bh = (PlatformClientHandler) context.getBean("sapClientHandler");
				List<SapInventoryDashboardResponseTO> sapInventoryDashboardResponseTOList = bh.processInventoryDashboard(inventoryDashboardRequestTO);
				for (SapInventoryDashboardResponseTO sapInventoryDashboardResponseTO : sapInventoryDashboardResponseTOList) {
					assertNull(sapInventoryDashboardResponseTO.getIdocNumber());
					assertTrue(sapInventoryDashboardResponseTO.getActualQuantity() == 0);
				}
		
	}
}
