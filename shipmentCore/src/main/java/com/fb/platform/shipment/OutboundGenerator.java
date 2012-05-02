package com.fb.platform.shipment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.fb.platform.shipment.manager.ShipmentManager;

/**
 * @author nehaga
 *
 */
public class OutboundGenerator {

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
		ShipmentManager shipmentManager = (ShipmentManager)appContext.getBean("shipmentManager");
		InputStream gatePassXML = OutboundGenerator.class.getClassLoader().getResourceAsStream("gatePass.xml");
		String gatePassString = convertInputStreamToString(gatePassXML);
		shipmentManager.generateOutboundFile(gatePassString);
	}
	
	private static String convertInputStreamToString(InputStream inputStream) {
		BufferedReader bufReader = new BufferedReader(new InputStreamReader(inputStream));
		StringBuilder sb = new StringBuilder();
		try {
			String line = bufReader.readLine();
			while( line != null ) {
				sb.append( line + "\n" );
				line = bufReader.readLine();
			}
			inputStream.close();
		} catch(IOException exception) {
			//logger.error("promotion.xsd loading error : " + exception.getMessage() );
		}
		return sb.toString();
	}

}
