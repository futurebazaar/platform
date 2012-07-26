/**
 * 
 */
package com.fb.platform.sap.launcher;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.terracotta.agent.repkg.de.schlichtherle.io.FileInputStream;

import com.fb.commons.PlatformException;
import com.fb.platform.sap.client.idoc.platform.PlatformIDocHandler;
import com.fb.platform.sap.client.idoc.platform.PlatformIDocHandlerFactory;
import com.fb.platform.sap.client.idoc.platform.impl.DeliveryInventoryIDocHandler;
import com.fb.platform.sap.client.idoc.platform.impl.InventoryIDocHandler;

/**
 * @author nehaga
 *
 */
public class MockSapLoadTester {
	private static Log logger = LogFactory.getLog(MockSapLoadTester.class);
	
	private static Properties prop = initProperties();

	private static Properties initProperties() {
		Properties properties = new Properties();
		InputStream propertiesStream = MockSapLoadTester.class.getClassLoader().getResourceAsStream("receivers.properties");
		try {
			properties.load(propertiesStream);
		} catch (IOException e) {
			logger.error("Error loading properties file.", e);
			throw new PlatformException("Error loading properties file.", e);
		}
		return properties;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		logger.warn("Starting the Sap Server Connector.");
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("sapReceiver-applicationContext-service.xml", "platformMom-applicationContext-resources.xml", "platformMom-applicationContext-service.xml");

		PlatformIDocHandlerFactory idocFactory = (PlatformIDocHandlerFactory) applicationContext.getBean("platformIDocHandlerFactory");

		
		sendInventoryIdoc(idocFactory.getHandler(InventoryIDocHandler.INVENTORY_IDOC_TYPE));
		sendDeliveryInventoryIdoc(idocFactory.getHandler(DeliveryInventoryIDocHandler.DELIVERY_INVENTORY_IDOC_TYPE));

	}
	
	private static void sendInventoryIdoc(PlatformIDocHandler inventoryIDocHandler) {
		try {
			StringWriter sw ;
			InputStream inputStream ;
			String inventoryPath = prop.getProperty("sap.load.inventory.path");
			System.out.println("Inventory idoc path : " + inventoryPath);
			File inventoryDir = new File(inventoryPath);
			for (File idoc : inventoryDir.listFiles()) {
				inputStream = new FileInputStream(idoc);
				sw = new StringWriter();
				IOUtils.copy(inputStream, sw);
				//TODO send sapMomTO
				//inventoryIDocHandler.handle(sw.toString());
			}
		} catch (Exception e) {
			logger.error("Error in sendInventoryIdoc : ", e);
		}
	}
	
	private static void sendDeliveryInventoryIdoc(PlatformIDocHandler deliveryInventoryIDocHandler) {
		try {
			StringWriter sw ;
			InputStream inputStream ;
			String deliveryInventoryPath = prop.getProperty("sap.load.deliveryInventory.path");
			System.out.println("Delivery Inventory idoc path : " + deliveryInventoryPath);
			File deliveryInventoryDir = new File(deliveryInventoryPath);
			for (File idoc : deliveryInventoryDir.listFiles()) {
				inputStream = new FileInputStream(idoc);
				sw = new StringWriter();
				IOUtils.copy(inputStream, sw);
				//TODO send sapMomTO
				//deliveryInventoryIDocHandler.handle(sw.toString());
			}
		} catch (Exception e) {
			logger.error("Error in sendDeliveryInventoryIdoc : ", e);
		}
	}

}
