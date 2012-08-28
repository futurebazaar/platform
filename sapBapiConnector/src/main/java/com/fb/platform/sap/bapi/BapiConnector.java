package com.fb.platform.sap.bapi;

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
        
        public void updateEventListener(String destName){
        	eL.updated(destName);
        }
    } 
    
    // This function returns the Properties From Enviroment
    private Properties getDestinationPropertiesFromEnvironment(String environment)
    {
    	Properties connectProperties = new Properties();
    	
        //adapt parameters in order to configure a valid destination
    	
    	//Properties for client TUSHARA
        connectProperties.setProperty(DestinationDataProvider.JCO_ASHOST, "10.0.7.63");
        connectProperties.setProperty(DestinationDataProvider.JCO_USER,   "TUSHARA");
        connectProperties.setProperty(DestinationDataProvider.JCO_PASSWD, "123456");
        connectProperties.setProperty(DestinationDataProvider.JCO_SYSNR,  "01");
        connectProperties.setProperty(DestinationDataProvider.JCO_CLIENT, "500");
        connectProperties.setProperty(DestinationDataProvider.JCO_DEST , "ZATGJCAPS_DV1");

        // Properties for client RFCATG NOT WORKING: Missing Something
        /*connectProperties.setProperty(DestinationDataProvider.JCO_SYSNR,  "00");
        connectProperties.setProperty(DestinationDataProvider.JCO_CLIENT, "239");
        connectProperties.setProperty(DestinationDataProvider.JCO_USER,   "RFCATG");
        connectProperties.setProperty(DestinationDataProvider.JCO_PASSWD, "welcome1");
        connectProperties.setProperty(DestinationDataProvider.JCO_LANG,   "en");*/
        
        connectProperties.setProperty(DestinationDataProvider.JCO_POOL_CAPACITY, "1");
        return connectProperties;
    }
    
    public void connect(String environment, BapiTemplate template) throws JCoException{
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
        JCoFunctionTemplate jCoFunctionTemplate = destination.getRepository().getFunctionTemplate(template.toString());
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
