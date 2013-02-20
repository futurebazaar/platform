package com.fb.platform.sap.client.connector.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fb.platform.sap.client.commons.SapUtils;
import com.fb.platform.sap.client.commons.TinlaClient;
import com.fb.platform.sap.client.connector.PlatformClientConnector;
import com.sap.conn.idoc.IDocDocumentList;
import com.sap.conn.idoc.IDocFactory;
import com.sap.conn.idoc.IDocParseException;
import com.sap.conn.idoc.IDocRepository;
import com.sap.conn.idoc.IDocXMLProcessor;
import com.sap.conn.idoc.jco.JCoIDoc;
import com.sap.conn.jco.JCoContext;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.ext.DestinationDataEventListener;
import com.sap.conn.jco.ext.DestinationDataProvider;

public class SapClientConnector implements PlatformClientConnector {
	
	private static Log logger = LogFactory.getLog(SapClientConnector.class);
	
    private class SapDestinationDataProvider implements DestinationDataProvider {
    	 
        private DestinationDataEventListener eL;
        private Properties destinationProperty;
        
        public void setDestinationProperty(Properties destinationProperty) {
        	this.destinationProperty = destinationProperty;
        }
        
        public Properties getDestinationProperties(String environment) {
        	return destinationProperty;
         }
        
        public void setDestinationDataEventListener(DestinationDataEventListener eventListener) {
            this.eL = eventListener;
        }
        
        public DestinationDataEventListener getDestinationDataEventListener() {
        	return eL;
        }

        public boolean supportsEvents() {
            return true;
        }
    } 
    
    private static Properties getDestinationPropertiesFromEnvironment(TinlaClient client) throws IOException {
    	InputStream inStream = null;
    	
    	if (SapUtils.isBigBazaar(client)) inStream = SapClientConnector.class.getClassLoader().getResourceAsStream("bbBapi.jcoDestination");
    	else inStream = SapClientConnector.class.getClassLoader().getResourceAsStream("bapi.jcoDestination");
    	
    	Properties connectProperties = new Properties();
    	connectProperties.load(inStream);
        return connectProperties;
    }
    
    @Override
    public JCoDestination connectSap(TinlaClient client) throws IOException, JCoException {
        SapDestinationDataProvider sapProvider = new SapDestinationDataProvider();
        Properties bapiProperties = getDestinationPropertiesFromEnvironment(client);
        sapProvider.setDestinationProperty(bapiProperties);
        try {
            com.sap.conn.jco.ext.Environment.registerDestinationDataProvider(sapProvider);
        } catch(IllegalStateException providerAlreadyRegisteredException) {
            logger.info("Environment already registered. Skipping the registration...");
        }
        JCoDestination destination = JCoDestinationManager.getDestination(bapiProperties.getProperty(DestinationDataProvider.JCO_DEST));
        System.out.println(destination.getAttributes());
        return destination;
    }
    
    @Override
    public void sendIdoc(String xml) throws JCoException, IOException, IDocParseException{  
    	JCoDestination destination = connectSap(TinlaClient.FUTUREBAZAAR);  
    	JCoContext.begin(destination);
        IDocRepository iDocRepository = JCoIDoc.getIDocRepository(destination);
        String transactionID = destination.createTID();
        IDocFactory iDocFactory = JCoIDoc.getIDocFactory();
        IDocXMLProcessor iDocXMLProcessor = iDocFactory.getIDocXMLProcessor();
        IDocDocumentList iDocDocumentList = iDocXMLProcessor.parse(iDocRepository, xml);
        JCoIDoc.send(iDocDocumentList, IDocFactory.IDOC_VERSION_3, destination, transactionID);
        destination.confirmTID(transactionID);
        JCoContext.end(destination);
    }
    
}
