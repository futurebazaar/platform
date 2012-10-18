package com.fb.platform.sap.bapi.test.inventory;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.fb.commons.test.BaseTestCase;
import com.fb.platform.sap.bapi.to.SapInventoryLevelRequestTO;
import com.fb.platform.sap.bapi.to.SapInventoryLevelResponseTO;
import com.fb.platform.sap.client.handler.PlatformClientHandler;

public class TestInventoryLevel extends BaseTestCase {
	
	private SapInventoryLevelRequestTO getInventoryLevelRequestTO() {
		SapInventoryLevelRequestTO inventoryLevelRequestTO = new SapInventoryLevelRequestTO();
		inventoryLevelRequestTO.setMaterial("300000560");
		inventoryLevelRequestTO.setStorageLocation(10);
		inventoryLevelRequestTO.setPlant("2786");
		return inventoryLevelRequestTO;
	}
	
	@Test
	public void testInventoryStock() {
		SapInventoryLevelRequestTO sapInventoryLevelRequestTO = getInventoryLevelRequestTO();
		ApplicationContext context = new ClassPathXmlApplicationContext("test-applicationContext-service.xml");
		PlatformClientHandler bh = (PlatformClientHandler) context.getBean("sapClientHandler");
		SapInventoryLevelResponseTO sapInventoryLevelResponseTO = bh.processInventoryLevel(sapInventoryLevelRequestTO);
		assertEquals(sapInventoryLevelResponseTO.getStockQuantity(), null);
		assertEquals("TEST", sapInventoryLevelResponseTO.getSite());
	}
}


