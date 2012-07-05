/**
 * 
 */
package com.fb.platform.sap.launcher;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.fb.commons.PlatformException;
import com.fb.platform.sap.FBSapServer;

/**
 * @author vinayak
 *
 */
public class SapServerLauncher {

	private static Log logger = LogFactory.getLog(SapServerLauncher.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		logger.warn("Starting the Sap Server Connector.");
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext-service.xml", "platformMom-applicationContext-resources.xml", "platformMom-applicationContext-service.xml");

		FBSapServer fbSapServer = (FBSapServer) applicationContext.getBean("fbSapServer");
		if (args.length > 0 && args[0].equalsIgnoreCase("qa")) {
			fbSapServer.startServer("qalistener");
		} else if(args.length > 0 && args[0].equalsIgnoreCase("prod")) {
			fbSapServer.startServer("prodlistener");
		} else {
			throw new PlatformException("Please specify SAP environment (qa or prod).");
		}
		
	}
}
