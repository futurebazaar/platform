package com.fb.platform.sap.client.test;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fb.platform.sap.bapi.test.inventory.TestInventoryDashboardTO;
import com.fb.platform.sap.bapi.test.inventory.TestInventoryLevelTO;
import com.fb.platform.sap.bapi.test.lsp.TestLspAwbUpdateTO;
import com.fb.platform.sap.bapi.test.order.TestBBBapiOrderTO;
import com.fb.platform.sap.bapi.test.order.TestBapiOrderTO;
import com.fb.platform.sap.bapi.to.SapInventoryDashboardRequestTO;
import com.fb.platform.sap.bapi.to.SapInventoryLevelRequestTO;
import com.fb.platform.sap.bapi.to.SapLspAwbUpdateRequestTO;
import com.fb.platform.sap.bapi.to.SapOrderRequestTO;
import com.fb.platform.sap.client.connector.SapClientConnector;
import com.fb.platform.sap.client.handler.impl.SapClientHandler;
import com.fb.platform.sap.idoc.test.wallet.TestWalletIdoc;

public class TestSapClientConnector {
	
	private static Log logger = LogFactory.getLog(TestSapClientConnector.class);
	
	public static void main(String[] args) {
		logger.info("Testing");
		SapClientConnector bapiConnector = new SapClientConnector();
		SapClientHandler bh = new SapClientHandler();
		bh.setBapiConnector(bapiConnector);
		
		//Test Order
		SapOrderRequestTO bbOrderTO = new TestBBBapiOrderTO().getBapiTO();
		System.out.println(bh.processOrder(bbOrderTO));
		SapOrderRequestTO bapiOrderTO = new TestBapiOrderTO().getBapiTO();
		System.out.println(bh.processOrder(bapiOrderTO));
		
		//Test Inventory Dashboard
		SapInventoryDashboardRequestTO inventoryDashboardRequestTO = new TestInventoryDashboardTO().getRequestTO();
		System.out.println(bh.processInventoryDashboard(inventoryDashboardRequestTO));

		//Test Inventory Level
		SapInventoryLevelRequestTO inventoryLevelRequestTO = new TestInventoryLevelTO().getInventoryLevelRequestTO();
		System.out.println(bh.processInventoryLevel(inventoryLevelRequestTO));
		
		//Test Lsp Awb Update
		SapLspAwbUpdateRequestTO lspAwbUpdateRequestTO = new TestLspAwbUpdateTO().getLspAwbUpdateRequestTO();
		System.out.println(bh.processLspAwbUpdate(lspAwbUpdateRequestTO));
		
		//Test Wallet
		String walletIdoc = new TestWalletIdoc().getWalletIdocXml();
		System.out.println(bh.sendIdoc(walletIdoc));
	}
	
}
