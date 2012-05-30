package com.fb.sap.client;

import com.sap.conn.jco.server.JCoServer;
import com.sap.conn.jco.server.JCoServerContextInfo;
import com.sap.conn.jco.server.JCoServerErrorListener;
import com.sap.conn.jco.server.JCoServerExceptionListener;

public class ErrorListener implements JCoServerErrorListener,
		JCoServerExceptionListener {

	public void serverErrorOccurred(JCoServer server, String connectionId,
			JCoServerContextInfo ctx, Error error) {
		System.out.println(">>> Error occured on " + server.getProgramID()
				+ " connection " + connectionId);
		error.printStackTrace();
	}

	public void serverExceptionOccurred(JCoServer server, String connectionId,
			JCoServerContextInfo ctx, Exception error) {
		System.out.println(">>> Error occured on " + server.getProgramID()
				+ " connection " + connectionId);
		error.printStackTrace();
	}
}
