package com.fb.platform.sap.bapi;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoFunctionTemplate;
import com.sap.conn.jco.ext.DestinationDataEventListener;
import com.sap.conn.jco.ext.DestinationDataProvider;

public class BapiConnector
{
	private JCoFunction bapiFunction = null;
	private JCoDestination bapiDestination = null;
	
	private Properties bapiProperties = null;
    private class BapiDestinationDataProvider implements DestinationDataProvider
    {
    	 
        private DestinationDataEventListener eL;
        
        public Properties getDestinationProperties(String environment)
        {
        	return bapiProperties;
         }
        
        public void setDestinationDataEventListener(DestinationDataEventListener eventListener)
        {
            this.eL = eventListener;
        }

        public boolean supportsEvents()
        {
            return true;
        }
    } 
    
    // This function returns the Properties From Enviroment
    private Properties getDestinationPropertiesFromEnvironment(String environment) throws IOException
    {
    	InputStream inStream = this.getClass().getClassLoader().getResourceAsStream(environment + "bapi.jcoDestination");
    	Properties connectProperties = new Properties();
    	connectProperties.load(inStream);
        return connectProperties;
    }
    
    public void connect(String environment, String template) throws JCoException, IOException{
        BapiDestinationDataProvider bapiProvider = new BapiDestinationDataProvider();
        bapiProperties = getDestinationPropertiesFromEnvironment(environment);
        try
        {
            com.sap.conn.jco.ext.Environment.registerDestinationDataProvider(bapiProvider);
        }
        catch(IllegalStateException providerAlreadyRegisteredException)
        {
            //somebody else registered its implementation, 
            //stop the execution
            throw new Error(providerAlreadyRegisteredException);
        }
         
        JCoDestination destination = JCoDestinationManager.getDestination(bapiProperties.getProperty(DestinationDataProvider.JCO_DEST));
        setBapiDestination(destination);
        System.out.println(destination.getAttributes());
        JCoFunctionTemplate jCoFunctionTemplate = destination.getRepository().getFunctionTemplate(template);
        setBapiFunction(jCoFunctionTemplate.getFunction());
        
    }

	public JCoFunction getBapiFunction() {
		return bapiFunction;
	}

	public void setBapiFunction(JCoFunction bapiFunction) {
		this.bapiFunction = bapiFunction;
	}
	
	public JCoDestination getBapiDestination() {
		return bapiDestination;
	}

	public void setBapiDestination(JCoDestination bapiDestination) {
		this.bapiDestination = bapiDestination;
	}
}
