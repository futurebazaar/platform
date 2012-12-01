package com.fb.platform.sap.client.connector;

import java.io.IOException;

import com.sap.conn.idoc.IDocParseException;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoException;

public interface PlatformClientConnector {
	
	 public JCoDestination connectSap() throws IOException, JCoException;
	    
	 public void sendIdoc(String xml) throws JCoException, IOException, IDocParseException;

}
