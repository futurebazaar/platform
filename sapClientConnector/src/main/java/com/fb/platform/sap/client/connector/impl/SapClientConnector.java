package com.fb.platform.sap.client.connector.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fb.platform.sap.client.connector.PlatformClientConnector;
import com.sap.conn.idoc.IDocDocumentList;
import com.sap.conn.idoc.IDocFactory;
import com.sap.conn.idoc.IDocParseException;
import com.sap.conn.idoc.IDocRepository;
import com.sap.conn.idoc.IDocXMLProcessor;
import com.sap.conn.idoc.jco.JCoIDoc;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.ext.DestinationDataEventListener;
import com.sap.conn.jco.ext.DestinationDataProvider;

public class SapClientConnector implements PlatformClientConnector {
	
	private static Log logger = LogFactory.getLog(SapClientConnector.class);
	
    private class SapDestinationDataProvider implements DestinationDataProvider {
    	 
        private DestinationDataEventListener eL;
        
        public Properties getDestinationProperties(String environment) {
        	try {
				return SapClientConnector.getDestinationPropertiesFromEnvironment();
			} catch (IOException e) {
				return null;
			}
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
    
    private static Properties getDestinationPropertiesFromEnvironment() throws IOException {
    	InputStream inStream = SapClientConnector.class.getClassLoader().getResourceAsStream("bapi.jcoDestination");
    	Properties connectProperties = new Properties();
    	connectProperties.load(inStream);
        return connectProperties;
    }
    
    @Override
    public JCoDestination connectSap() throws IOException, JCoException {
        SapDestinationDataProvider sapProvider = new SapDestinationDataProvider();
        Properties bapiProperties = getDestinationPropertiesFromEnvironment();
        try {
            com.sap.conn.jco.ext.Environment.registerDestinationDataProvider(sapProvider);
        } catch(IllegalStateException providerAlreadyRegisteredException) {
            logger.info("Environment already registered. Skipping the registration...");
        }
        JCoDestination destination = JCoDestinationManager.getDestination(bapiProperties.getProperty(DestinationDataProvider.JCO_DEST));
        return destination;
    }
    
    @Override
    public void sendIdoc(String xml) throws JCoException, IOException, IDocParseException{  
    	JCoDestination destination = connectSap();  
        IDocRepository iDocRepository = JCoIDoc.getIDocRepository(destination);
        String transactionID = destination.createTID();
        IDocFactory iDocFactory = JCoIDoc.getIDocFactory();
        IDocXMLProcessor iDocXMLProcessor = iDocFactory.getIDocXMLProcessor();
        IDocDocumentList iDocDocumentList = iDocXMLProcessor.parse(iDocRepository, xml);
        JCoIDoc.send(iDocDocumentList, IDocFactory.IDOC_VERSION_3, destination, transactionID);
        destination.confirmTID(transactionID);
    }
    
}
