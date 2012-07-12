/**
 * 
 */
package com.fb.platform.sap.launcher;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.fb.platform.sap.client.idoc.platform.PlatformIDocHandler;
import com.fb.platform.sap.client.idoc.platform.PlatformIDocHandlerFactory;
import com.fb.platform.sap.client.idoc.platform.impl.DeliveryInventoryIDocHandler;
import com.fb.platform.sap.client.idoc.platform.impl.InventoryIDocHandler;

/**
 * @author vinayak
 *
 */
public class MockInventorySapServer {

	private static Log logger = LogFactory.getLog(MockInventorySapServer.class);

	private static String INVENTORY_IDOC_0 = null;
	private static String INVENTORY_IDOC_1 = null;
	private static String INVENTORY_IDOC_2 = null;
	private static String DELIVERY_INVENTORY_IDOC_0 = null;

	static {
		InputStream inputStream = MockInventorySapServer.class.getClassLoader().getResourceAsStream("ztinla_idoctype1.xml");
		StringWriter sw = new StringWriter();
		try {
			IOUtils.copy(inputStream, sw);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		INVENTORY_IDOC_0 = sw.toString();

		inputStream = MockInventorySapServer.class.getClassLoader().getResourceAsStream("ztinla_idoctype2.xml");
		sw = new StringWriter();
		try {
			IOUtils.copy(inputStream, sw);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		INVENTORY_IDOC_1 = sw.toString();

		inputStream = MockInventorySapServer.class.getClassLoader().getResourceAsStream("ztinla_idoctype3.xml");
		sw = new StringWriter();
		try {
			IOUtils.copy(inputStream, sw);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		INVENTORY_IDOC_2 = sw.toString();

		inputStream = MockInventorySapServer.class.getClassLoader().getResourceAsStream("ztinla_dlvry.xml");
		sw = new StringWriter();
		try {
			IOUtils.copy(inputStream, sw);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DELIVERY_INVENTORY_IDOC_0 = sw.toString();
	}
	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		logger.warn("Starting the Sap Server Connector.");
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext-service.xml", "platformMom-applicationContext-resources.xml", "platformMom-applicationContext-service.xml");

		PlatformIDocHandlerFactory idocFactory = (PlatformIDocHandlerFactory) applicationContext.getBean("platformIDocHandlerFactory");
		PlatformIDocHandler inventoryIDocHandler = idocFactory.getHandler(InventoryIDocHandler.INVENTORY_IDOC_TYPE);

		PlatformIDocHandler deliveryInventoryIDocHandler = idocFactory.getHandler(DeliveryInventoryIDocHandler.DELIVERY_INVENTORY_IDOC_TYPE);

		int count = 0;
		while (true) {
			if (count == 0) {
				inventoryIDocHandler.handle(INVENTORY_IDOC_0);
			} else if (count == 1) {
				inventoryIDocHandler.handle(INVENTORY_IDOC_2);
			} else  if (count == 2) {
				inventoryIDocHandler.handle(INVENTORY_IDOC_1);
			} else {
				deliveryInventoryIDocHandler.handle(DELIVERY_INVENTORY_IDOC_0);
			}
			logger.info("sent the inventory message. sleeping for 15 seconds");
			count ++;
			if (count > 3) {
				break;
			}
			Thread.sleep(20000);
		}
	}
}
