package com.fb.platform.sap.test;

import java.io.StringReader;
import java.math.BigDecimal;
import java.util.StringTokenizer;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fb.commons.PlatformException;
import com.fb.platform.sap.InventoryResource;
import com.fb.platform.sap.LspResource;
import com.fb.platform.sap.OrderResource;
import com.fb.platform.sap._1_0.CommonOrderRequest;
import com.fb.platform.sap._1_0.InventoryLevelRequest;
import com.fb.platform.sap._1_0.ModifyOrderRequest;
import com.fb.platform.sap._1_0.ReturnOrderRequest;
import com.fb.platform.sap.bapi.to.SapLspAwbUpdateRequestTO;
import com.fb.platform.sap.client.connector.SapClientConnector;
import com.fb.platform.sap.client.handler.impl.SapClientHandler;

public class TestResource {
	
	private static Log logger = LogFactory.getLog(TestResource.class);
	private static final JAXBContext context = initContext();

	private static JAXBContext initContext() {
		try {
			return JAXBContext.newInstance("com.fb.platform.sap._1_0");
		} catch (JAXBException e) {
			logger.error("Error Initializing the JAXBContext to bind the schema classes", e);
			throw new PlatformException("Error Initializing the JAXBContext to bind the schema classes", e);
		}
	}
	
	public static void main(String[] args) {
		SapClientHandler sapClientHandler = new SapClientHandler();
		SapClientConnector sapClientConnector = new SapClientConnector();
		sapClientHandler.setBapiConnector(sapClientConnector);
		TestSapResource testSapResource = new TestSapResource();
		
		logger.info("Setting xml");
		try {
//			InventoryResource sapInventoryResource = new InventoryResource();
//			sapInventoryResource.setSapClientHandler(sapClientHandler);
//			String inventoryLevelXml = testSapResource.getInventoryLevelXml();
//			System.out.println(inventoryLevelXml);
//			System.out.println(sapInventoryResource.inventorylevel(inventoryLevelXml));
//			
//			String inventoryDashboardXml = testSapResource.getInventoryDashboardXml();
//			System.out.println(inventoryDashboardXml);
//			System.out.println(sapInventoryResource.inventoryDashboard(inventoryDashboardXml));
//			
//			LspResource sapLspResource = new LspResource();
//			sapLspResource.setSapClientHandler(sapClientHandler);
//			String lspAwbUpdateXml = testSapResource.getLspAwbUpdateXml();
//			System.out.println(lspAwbUpdateXml);
//			System.out.println(sapLspResource.assignAwbToDeliveryl(lspAwbUpdateXml));
			
			OrderResource orderResource = new OrderResource();
			orderResource.setSapClientHandler(sapClientHandler);
			String returnXml = testSapResource.getReturnOrderXml();
			System.out.println(orderResource.returnOrder(returnXml));	
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

}
