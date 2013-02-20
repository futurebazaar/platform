package com.fb.platform.sap.client.connector;

import java.io.IOException;

import com.fb.platform.sap.client.commons.TinlaClient;
import com.sap.conn.idoc.IDocParseException;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoException;

public interface PlatformClientConnector {
	
	 public JCoDestination connectSap(TinlaClient client) throws IOException, JCoException;
	    
	 public void sendIdoc(String xml) throws JCoException, IOException, IDocParseException;

}
