/**
 * 
 */
package com.fb.platform.sap.client.error;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fb.platform.sap.FBSapServer;
import com.sap.conn.jco.server.JCoServer;
import com.sap.conn.jco.server.JCoServerContextInfo;
import com.sap.conn.jco.server.JCoServerErrorListener;
import com.sap.conn.jco.server.JCoServerExceptionListener;

/**
 * @author vinayak
 *
 */
public class PlatformSapErrorListener implements JCoServerErrorListener, JCoServerExceptionListener {

	private static Log logger = LogFactory.getLog(FBSapServer.class);

	@Override
	public void serverErrorOccurred(JCoServer server, String connectionID, JCoServerContextInfo serverCtx, Error error) {
		logger.error("Error occured while reading idocs from Sap Server", error);
	}

	@Override
	public void serverExceptionOccurred(JCoServer server, String connectionID, JCoServerContextInfo serverCtx, Exception exception) {
		logger.error("Exception occured while reading idocs from Sap Server", exception);
	}

}
