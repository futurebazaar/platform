package com.fb.platform.sap.client.mock;

import java.io.IOException;

import com.fb.platform.sap.client.commons.TinlaClient;
import com.fb.platform.sap.client.connector.PlatformClientConnector;
import com.sap.conn.idoc.IDocParseException;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoException;

public class MockSapClientConnector implements PlatformClientConnector {
    
	@Override
    public JCoDestination connectSap(TinlaClient client) throws IOException, JCoException {
        JCoDestination destination = new MockJcoDestination();
        return destination;
    }

	@Override
    public void sendIdoc(String xml) throws JCoException, IOException, IDocParseException{  
    }
}
