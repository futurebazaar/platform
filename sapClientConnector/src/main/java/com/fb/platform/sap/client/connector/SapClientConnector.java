package com.fb.platform.sap.client.connector;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sap.conn.idoc.IDocDocumentList;
import com.sap.conn.idoc.IDocFactory;
import com.sap.conn.idoc.IDocParseException;
import com.sap.conn.idoc.IDocRepository;
import com.sap.conn.idoc.IDocXMLProcessor;
import com.sap.conn.idoc.jco.JCoIDoc;
import com.sap.conn.idoc.jco.rt.JCoIDocDocument;
import com.sap.conn.idoc.jco.rt.JCoIDocFunction;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoFunctionTemplate;
import com.sap.conn.jco.JCoTable;
import com.sap.conn.jco.ext.DestinationDataEventListener;
import com.sap.conn.jco.ext.DestinationDataProvider;

public class SapClientConnector {
	
	private JCoFunction bapiFunction = null;
	private JCoDestination bapiDestination = null;
	private static Log logger = LogFactory.getLog(SapClientConnector.class);
	
	private Properties bapiProperties = null;
    private class BapiDestinationDataProvider implements DestinationDataProvider {
    	 
        private DestinationDataEventListener eL;
        
        public Properties getDestinationProperties(String environment) {
        	return bapiProperties;
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
    
    private Properties getDestinationPropertiesFromEnvironment() throws IOException {
    	InputStream inStream = this.getClass().getClassLoader().getResourceAsStream("bapi.jcoDestination");
    	Properties connectProperties = new Properties();
    	connectProperties.load(inStream);
        return connectProperties;
    }
    
    public void connectBapi(String template) throws JCoException, IOException {
    	logger.info("Making connection for template : " + template);
        BapiDestinationDataProvider bapiProvider = new BapiDestinationDataProvider();
        bapiProperties = getDestinationPropertiesFromEnvironment();
        try {
            com.sap.conn.jco.ext.Environment.registerDestinationDataProvider(bapiProvider);
        } catch(IllegalStateException providerAlreadyRegisteredException) {
            logger.info("Environment already registered. Skipping the registration...");
        }
        JCoDestination destination = JCoDestinationManager.getDestination(bapiProperties.getProperty(DestinationDataProvider.JCO_DEST));
        setBapiDestination(destination);
        logger.info("Template: " +  template + "Connection properties : " + destination.getAttributes());
        JCoFunctionTemplate jCoFunctionTemplate = destination.getRepository().getFunctionTemplate(template);
        setBapiFunction(jCoFunctionTemplate.getFunction());
    }
    
    public void sendIdoc(String xml) throws JCoException, IOException, IDocParseException{  
        // get the JCo destination  
    	bapiProperties = getDestinationPropertiesFromEnvironment();
    	JCoDestination destination = JCoDestinationManager.getDestination("bapi");  
        // Create repository  
    	
        IDocRepository iDocRepository = JCoIDoc.getIDocRepository(destination);
        String transactionID = destination.createTID();
        IDocFactory iDocFactory = JCoIDoc.getIDocFactory();  
        IDocXMLProcessor iDocXMLProcessor = iDocFactory.getIDocXMLProcessor();
        IDocDocumentList iDocDocumentList = iDocXMLProcessor.parse(iDocRepository, xml);  
        JCoIDoc.send(iDocDocumentList, IDocFactory.IDOC_VERSION_DEFAULT, destination, transactionID);
        System.out.println(destination.getRepository().getFunction("ZABHI_INBOUND"));
//        function.execute(destination);
//        JCoTable table = function.getTableParameterList().getTable("RETURN");
//        table.setRow(0);
//        System.out.println(table.getValue("IDOC_STATUS"));
        destination.confirmTID(transactionID);
    }
    
	public JCoFunction getBapiFunction() {
		return bapiFunction;
	}

	private void setBapiFunction(JCoFunction bapiFunction) {
		this.bapiFunction = bapiFunction;
	}
	
	public JCoDestination getBapiDestination() {
		return bapiDestination;
	}

	private void setBapiDestination(JCoDestination bapiDestination) {
		this.bapiDestination = bapiDestination;
	}
}
