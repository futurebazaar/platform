/**
 * 
 */
package com.fb.platform.sap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fb.platform.sap.client.error.PlatformSapErrorListener;
import com.fb.platform.sap.client.idoc.sap.SapIDocHandlerFactory;
import com.fb.platform.sap.client.tid.PlatformTIDHandler;
import com.sap.conn.idoc.jco.JCoIDoc;
import com.sap.conn.idoc.jco.JCoIDocServer;
import com.sap.conn.jco.JCoException;

/**
 * Encapsulates the Sap's IDoc Server. 
 * @author vinayak
 *
 */
public class FBSapServer {

	private static Log logger = LogFactory.getLog(FBSapServer.class);

	@Autowired
	private SapIDocHandlerFactory sapIDocHandlerFactory = null;

	@Autowired
	private PlatformTIDHandler platformTIDHandler = null;

	@Autowired
	private PlatformSapErrorListener platformSapErrorListener = null;

	public void startServer(String environment) {
		logger.info("Starting the sap server for environment : " + environment);

		try {
			JCoIDocServer idocServer = JCoIDoc.getServer(environment);

			idocServer.setIDocHandlerFactory(sapIDocHandlerFactory);

			idocServer.setTIDHandler(platformTIDHandler);

			idocServer.addServerErrorListener(platformSapErrorListener);
			idocServer.addServerExceptionListener(platformSapErrorListener);

			idocServer.setConnectionCount(1);

			idocServer.start();

		} catch (JCoException e) {
			throw new PlatformSapException("Exception while connecting the sap server to the environment : " + environment, e);
		}
	}

	public void setSapIDocHandlerFactory(SapIDocHandlerFactory sapIDocHandlerFactory) {
		this.sapIDocHandlerFactory = sapIDocHandlerFactory;
	}

	public void setPlatformTIDHandler(PlatformTIDHandler platformTIDHandler) {
		this.platformTIDHandler = platformTIDHandler;
	}

	public void setPlatformSapErrorListener(PlatformSapErrorListener platformSapErrorListener) {
		this.platformSapErrorListener = platformSapErrorListener;
	}

}
