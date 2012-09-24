package com.fb.platform.sap.test;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fb.platform.sap.SapInventoryResource;
import com.fb.platform.sap.SapLspResource;
import com.fb.platform.sap.bapi.to.SapLspAwbUpdateRequestTO;
import com.fb.platform.sap.client.connector.SapClientConnector;
import com.fb.platform.sap.client.handler.impl.SapClientHandler;

public class TestResource {
	
	private static Log logger = LogFactory.getLog(TestResource.class);
	
	public static void main(String[] args) {
		SapClientHandler sapClientHandler = new SapClientHandler();
		SapClientConnector sapClientConnector = new SapClientConnector();
		sapClientHandler.setBapiConnector(sapClientConnector);
		
		logger.info("Setting xml");
		try {
			SapInventoryResource sapInventoryResource = new SapInventoryResource();
			sapInventoryResource.setSapClientHandler(sapClientHandler);
			String inventoryLevelXml = new TestSapResource().getInventoryLevelXml();
			System.out.println(inventoryLevelXml);
			System.out.println(sapInventoryResource.inventorylevel(inventoryLevelXml));
			
			String inventoryDashboardXml = new TestSapResource().getInventoryDashboardXml();
			System.out.println(inventoryDashboardXml);
			System.out.println(sapInventoryResource.inventoryDashboard(inventoryDashboardXml));
			
			SapLspResource sapLspResource = new SapLspResource();
			sapLspResource.setSapClientHandler(sapClientHandler);
			String lspAwbUpdateXml = new TestSapResource().getLspAwbUpdateXml();
			System.out.println(lspAwbUpdateXml);
			System.out.println(sapLspResource.assignAwbToDeliveryl(lspAwbUpdateXml));
			
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (DatatypeConfigurationException e) {
			e.printStackTrace();
		} 
	}

}
