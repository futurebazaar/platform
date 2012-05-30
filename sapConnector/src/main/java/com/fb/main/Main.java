package com.fb.main;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import com.fb.sap.client.Listener;
import com.fb.tinla.eventhandlers.DeliveryDeletedEventHandler;
import com.fb.tinla.eventhandlers.OrderStatusChangedEventHandler;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Setup logging
		PropertyConfigurator.configure("log4j.properties");
		Logger logger = Logger.getLogger("auris");
		logger.debug("Logging setup done");
		// Wire up event handlers
		DeliveryDeletedEventHandler.getInstance();
		logger.debug("Wired up delivery deleted event handler");
		OrderStatusChangedEventHandler.getInstance();
		logger.debug("Wired up order status changed event handler");
		logger.debug("Starting idoc listener server");
		Listener.startServer();
	}

}
