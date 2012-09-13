package com.fb.platform.sap.bapi.test;

import com.fb.platform.sap.bapi.BapiConnector;
import com.fb.platform.sap.bapi.handler.impl.SapBapiHandler;
import com.fb.platform.sap.bapi.test.inventory.TestInventoryDashboardTO;
import com.fb.platform.sap.bapi.test.order.TestBapiOrderTO;
import com.fb.platform.sap.bapi.to.SapInventoryDashboardRequestTO;
import com.fb.platform.sap.bapi.to.SapOrderRequestTO;

public class TestBapiConnector {
	public static void main(String[] args) {
		BapiConnector bapiConnector = new BapiConnector();
		SapBapiHandler bh = new SapBapiHandler();
		bh.setBapiConnector(bapiConnector);
		
		//Test Order
		SapOrderRequestTO bapiOrderTO = new TestBapiOrderTO().getBapiTO();
		//System.out.println(bh.processOrder("dev", bapiOrderTO));
		
		//Test Inventory Dashboard
		SapInventoryDashboardRequestTO inventoryDashboardRequestTO = new TestInventoryDashboardTO().getRequestTO();
		System.out.println(bh.processInventoryDashboard("dev", inventoryDashboardRequestTO));
	}
	
}
