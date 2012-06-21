package com.fb.sap.client;


import org.apache.log4j.Logger;

import com.sap.conn.idoc.jco.JCoIDoc;
import com.sap.conn.idoc.jco.JCoIDocServer;

/**
 * Starts a server to listen to messages from SAP
 * @author hemanth
 *
 */
public class Listener {
	
	public static void startServer() {
		Logger logger = Logger.getLogger("auris");
		try {
			// TODO Don't hard code configuration
			JCoIDocServer server = JCoIDoc.getServer("qalistener");
			server.setIDocHandlerFactory(new IDocHandlerFactory());
			server.setTIDHandler(new TIDHandler());

			ErrorListener listener = new ErrorListener();
			server.addServerErrorListener(listener);
			server.addServerExceptionListener(listener);
			server.setConnectionCount(1);
			server.start();
		} catch (Exception e) {
			logger.error("Error starting server", e);
		}
	}
}
