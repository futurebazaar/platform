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

import com.fb.commons.mom.to.SapMomTO;
import com.fb.platform.sap.client.idoc.platform.PlatformIDocHandler;
import com.fb.platform.sap.client.idoc.platform.PlatformIDocHandlerFactory;
import com.fb.platform.sap.client.idoc.platform.impl.DeliveryDeleteIDocHandler;
import com.fb.platform.sap.client.idoc.platform.impl.DeliveryInventoryIDocHandler;
import com.fb.platform.sap.client.idoc.platform.impl.InventoryIDocHandler;

/**
 * @author vinayak
 *
 */
public class MockInventorySapServer {

	private static Log logger = LogFactory.getLog(MockInventorySapServer.class);

	private static SapMomTO INVENTORY_IDOC_0 = new SapMomTO();
	private static SapMomTO INVENTORY_IDOC_1 = new SapMomTO();
	private static SapMomTO INVENTORY_IDOC_2 = new SapMomTO();
	private static SapMomTO INVENTORY_IDOC_3 = new SapMomTO();
	private static SapMomTO INVENTORY_IDOC_4 = new SapMomTO();
	private static SapMomTO INVENTORY_IDOC_5 = new SapMomTO();
	private static SapMomTO INVENTORY_IDOC_6 = new SapMomTO();
	private static SapMomTO DELIVERY_INVENTORY_IDOC_0 = new SapMomTO();

	static {
		InputStream inputStream = MockInventorySapServer.class.getClassLoader().getResourceAsStream("ztinla_idoctype1.xml");
		StringWriter sw = new StringWriter();
		try {
			IOUtils.copy(inputStream, sw);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		INVENTORY_IDOC_0.setIdoc(sw.toString());
		INVENTORY_IDOC_0.setIdocNumber("INVENTORY_IDOC_0");
		//INVENTORY_IDOC_0.setIdocType(InventoryIDocHandler.INVENTORY_IDOC_TYPE);

		inputStream = MockInventorySapServer.class.getClassLoader().getResourceAsStream("ztinla_idoctype2.xml");
		sw = new StringWriter();
		try {
			IOUtils.copy(inputStream, sw);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		INVENTORY_IDOC_1.setIdoc(sw.toString());
		INVENTORY_IDOC_1.setIdocNumber("INVENTORY_IDOC_1");
		//INVENTORY_IDOC_1.setIdocType(InventoryIDocHandler.INVENTORY_IDOC_TYPE);

		inputStream = MockInventorySapServer.class.getClassLoader().getResourceAsStream("ztinla_idoctype3.xml");
		sw = new StringWriter();
		try {
			IOUtils.copy(inputStream, sw);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		INVENTORY_IDOC_2.setIdoc(sw.toString());
		INVENTORY_IDOC_2.setIdocNumber("INVENTORY_IDOC_2");
		//INVENTORY_IDOC_2.setIdocType(InventoryIDocHandler.INVENTORY_IDOC_TYPE);
		
		inputStream = MockInventorySapServer.class.getClassLoader().getResourceAsStream("ZATG_SO_CREATE-idoc.xml");
		sw = new StringWriter();
		try {
			IOUtils.copy(inputStream, sw);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		INVENTORY_IDOC_3.setIdoc(sw.toString());
		INVENTORY_IDOC_3.setIdocNumber("INVENTORY_IDOC_3");
		//INVENTORY_IDOC_3.setIdocType("ZATG_SO_CREATEFROMDAT202");
		
		inputStream = MockInventorySapServer.class.getClassLoader().getResourceAsStream("ZATGDELD-idoc.xml");
		sw = new StringWriter();
		try {
			IOUtils.copy(inputStream, sw);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		INVENTORY_IDOC_4.setIdoc(sw.toString());
		INVENTORY_IDOC_4.setIdocNumber("INVENTORY_IDOC_4");
		//INVENTORY_IDOC_4.setIdocType("ZATGDELD");
		
		inputStream = MockInventorySapServer.class.getClassLoader().getResourceAsStream("ZATGFLOW-idoc.xml");
		sw = new StringWriter();
		try {
			IOUtils.copy(inputStream, sw);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		INVENTORY_IDOC_5.setIdoc(sw.toString());
		INVENTORY_IDOC_5.setIdocNumber("INVENTORY_IDOC_5");
		//INVENTORY_IDOC_5.setIdocType("ZATGFLOW");
		
		inputStream = MockInventorySapServer.class.getClassLoader().getResourceAsStream("ZATGINVOICE-idoc.xml");
		sw = new StringWriter();
		try {
			IOUtils.copy(inputStream, sw);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		INVENTORY_IDOC_6.setIdoc(sw.toString());
		INVENTORY_IDOC_6.setIdocNumber("INVENTORY_IDOC_6");
		//INVENTORY_IDOC_6.setIdocType("ZATGINVOICE");

		inputStream = MockInventorySapServer.class.getClassLoader().getResourceAsStream("ztinla_dlvry.xml");
		sw = new StringWriter();
		try {
			IOUtils.copy(inputStream, sw);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DELIVERY_INVENTORY_IDOC_0.setIdoc(sw.toString());
		DELIVERY_INVENTORY_IDOC_0.setIdocNumber("DELIVERY_INVENTORY_IDOC_0");
		//DELIVERY_INVENTORY_IDOC_0.setIdocType("ZTINLA_DLVRY");
	}
	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		logger.warn("Starting the Sap Server Connector.");
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("sapReceiver-applicationContext-service.xml", "platformMom-applicationContext-resources.xml", "platformMom-applicationContext-service.xml");

		PlatformIDocHandlerFactory idocFactory = (PlatformIDocHandlerFactory) applicationContext.getBean("platformIDocHandlerFactory");
		PlatformIDocHandler inventoryIDocHandler = idocFactory.getHandler(InventoryIDocHandler.INVENTORY_IDOC_TYPE);

		PlatformIDocHandler deliveryInventoryIDocHandler = idocFactory.getHandler(DeliveryInventoryIDocHandler.DELIVERY_INVENTORY_IDOC_TYPE);
		
		PlatformIDocHandler deleteDeliveryIdocHandler = idocFactory.getHandler(DeliveryDeleteIDocHandler.DELIVERY_DELETE);

		int count = 0;
		while (true) {
			if (count == 0) {
				inventoryIDocHandler.handle(INVENTORY_IDOC_0);
			} else if (count == 1) {
				inventoryIDocHandler.handle(INVENTORY_IDOC_2);
			} else  if (count == 2) {
				inventoryIDocHandler.handle(INVENTORY_IDOC_1);
			} else if (count == 3) {
				inventoryIDocHandler.handle(INVENTORY_IDOC_3);
			} else  if (count == 4) {
				deleteDeliveryIdocHandler.handle(INVENTORY_IDOC_4);
			} else if (count == 5) {
				inventoryIDocHandler.handle(INVENTORY_IDOC_5);
			} else  if (count == 6) {
				inventoryIDocHandler.handle(INVENTORY_IDOC_6);
			} else {
				deliveryInventoryIDocHandler.handle(DELIVERY_INVENTORY_IDOC_0);
			}
			logger.info("sent the inventory message. sleeping for 15 seconds");
			count ++;
			if (count > 7) {
				break;
			}
			Thread.sleep(20000);
		}
	}
}
