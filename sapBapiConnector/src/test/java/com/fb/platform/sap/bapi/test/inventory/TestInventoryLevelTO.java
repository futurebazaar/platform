package com.fb.platform.sap.bapi.test.inventory;

import com.fb.platform.sap.bapi.to.SapInventoryLevelRequestTO;

public class TestInventoryLevelTO {
	
	public SapInventoryLevelRequestTO getInventoryLevelRequestTO() {
		SapInventoryLevelRequestTO inventoryLevelRequestTO = new SapInventoryLevelRequestTO();
		inventoryLevelRequestTO.setMaterial("000000000300000560");
		inventoryLevelRequestTO.setStorageLocation("10");
		inventoryLevelRequestTO.setPlant("2786");
		return inventoryLevelRequestTO;
	}

}
