package com.fb.platform.shipment;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.fb.platform.shipment.ftp.ShipmentFTPClient;

public class ShipmentOutboundRunner {
	
	private static Log infoLog = LogFactory.getLog("LOGINFO");
	
	private static Log errorLog = LogFactory.getLog("LOGERROR");
	/**
	 * @param args
	 * This is just a runner class to test this module.
	 * A new project ShipmentReceiver project will be created that will receive the xml from SAP convert it to string
	 * and send it to shipmentManager
	 */
	
	
	public static void main(String[] args) {
		ApplicationContext appContext = new ClassPathXmlApplicationContext("shipment-applicationContext-dao.xml",
				"shipment-applicationContext-resources.xml",
				"shipment-applicationContext-service.xml");
		ShipmentResource shipmentResource = (ShipmentResource)appContext.getBean("shipmentResource");
		ShipmentFTPClient ftpClient = (ShipmentFTPClient)appContext.getBean("shipmentFTPClient");
		List<String> gatePassList = ftpClient.getGatePassList();
		for(String gatePassString : gatePassList) {
		//for(int i=0;i<3;i++) {
			//String gatePassXML = gatePassList.get(i); 
			infoLog.info("Gate Pass XML String : " + gatePassString);
			shipmentResource.processGatepass(gatePassString);
		}
	}
	
}
