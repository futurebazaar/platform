package com.fb.platform.sap.bapi.test.inventory;

import org.junit.Test;

import com.fb.commons.test.BaseTestCase;
import com.fb.platform.sap.bapi.to.SapInventoryLevelRequestTO;

public class TestInventoryLevelTO {
	
	public SapInventoryLevelRequestTO getInventoryLevelRequestTO() {
		SapInventoryLevelRequestTO inventoryLevelRequestTO = new SapInventoryLevelRequestTO();
		inventoryLevelRequestTO.setMaterial("300000560");
		inventoryLevelRequestTO.setStorageLocation(10);
		inventoryLevelRequestTO.setPlant("2786");
		return inventoryLevelRequestTO;
	}
	
	@Test
	public void dummy() {
		
	}
}
