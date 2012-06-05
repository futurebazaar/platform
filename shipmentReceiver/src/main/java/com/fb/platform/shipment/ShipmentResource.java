package com.fb.platform.shipment;

import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.fb.commons.PlatformException;
import com.fb.platform.shipment.manager.ShipmentManager;
import com.fb.platform.shipment.to.GatePassItem;
import com.fb.platform.shipment.to.ShipmentLSPEnum;
import com.sap.abapxml.Abap;
import com.sap.abapxml.Values.TAB.Item;

/**
 * @author nehaga
 *
 */
public class ShipmentResource implements ApplicationContextAware {
	
	private static Log infoLog = LogFactory.getLog("LOGINFO");
	
	private static Log errorLog = LogFactory.getLog("LOGERROR");
	
	//JAXBContext class is thread safe and can be shared
	private static final JAXBContext context = initContext();
	
	private ApplicationContext applicationContext = null;
	
	private ShipmentManager shipmentManager = null;
	
	private static JAXBContext initContext() {
		try {
			return JAXBContext.newInstance("com.sap.abapxml");
		} catch (JAXBException e) {
			errorLog.error("Error Initializing the JAXBContext to bind the schema classes", e);
			throw new PlatformException("Error Initializing the JAXBContext to bind the schema classes", e);
		}
	}
	
	/**
	 * This function accepts the gatePass.xml as String and initiates the process of creating the outbound files.
	 * @param gatePassString
	 */
	public boolean processGatepass(String gatePassString) {
		infoLog.info("GatePass xml String received : " + gatePassString);
		List<Item> ordersList = createItemList(gatePassString);
		List<GatePassItem> deliveryList = createLSPLists(ordersList);
		shipmentManager = (ShipmentManager)applicationContext.getBean("shipmentManager");
		boolean outboundCreated = shipmentManager.generateOutboundFile(deliveryList);
		return outboundCreated;
	}
	
	/**
	 * This function converts the JAXB objects to ShipmentCore internal objects and splits the parcels items list into separate lsp lists.
	 * This function calls the service layer and fetches the required information from the database to generate ParcelItem object. 
	 * @param ordersList
	 */
	private List<GatePassItem> createLSPLists(List<Item> ordersList) {
		List<GatePassItem> deliveryItems = new ArrayList<GatePassItem>();
		for(Item gatePassItemReceived : ordersList) {
			GatePassItem gatePassItem = new GatePassItem();
			gatePassItem.setAwbno(gatePassItemReceived.getAWBNO());
			gatePassItem.setDeece(gatePassItemReceived.getDEECE());
			if(gatePassItemReceived.getDELDT() != null && isValidDate(gatePassItemReceived.getDELDT())) {
				gatePassItem.setDeldt(new DateTime(gatePassItemReceived.getDELDT()));
			} else {
				gatePassItem.setDeldt(new DateTime());
			}
			gatePassItem.setDelno(gatePassItemReceived.getDELNO());
			gatePassItem.setGtpas(gatePassItemReceived.getGTPAS());
			if(gatePassItemReceived.getINVDT() != null && isValidDate(gatePassItemReceived.getINVDT())) {
				gatePassItem.setInvdt(new DateTime(gatePassItemReceived.getINVDT()));
			} else {
				gatePassItem.setInvdt(new DateTime());
			}
			gatePassItem.setInvno(gatePassItemReceived.getINVNO());
			String lspCode = gatePassItemReceived.getLSPCODE().substring(4);
			gatePassItem.setLspcode(ShipmentLSPEnum.getLSP(lspCode));
			gatePassItem.setSonum(gatePassItemReceived.getSONUM());
			deliveryItems.add(gatePassItem);
		}
		return deliveryItems;
	}
	
	public boolean isValidDate(String inDate) {

	    if (inDate == null)
	      return false;

	    //set the format to use as a constructor argument
	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	    
	    if (inDate.trim().length() != dateFormat.toPattern().length())
	      return false;

	    dateFormat.setLenient(false);
	    
	    try{
	    	dateFormat.parse(inDate.trim());
	    } catch (ParseException pe) {
	      return false;
	    }
	    return true;
	  }
	
	/**
	 * This generates JAXB objects after unmarshalling the gatePass.xml
	 * @param gatePassString
	 * @return
	 *  
	 */
	private List<Item> createItemList(String gatePassString) {
		try {
			
			Unmarshaller unmarshaller = context.createUnmarshaller();
			
			Abap abap = (Abap) unmarshaller.unmarshal(new StreamSource(new StringReader(gatePassString)));
			
			return abap.getValues().getTAB().getItem();
		} catch (JAXBException e) {
			System.out.println(e.getStackTrace());
		}
		
		return null;
	}
	
	public void setShipmentManager(ShipmentManager shipmentManager) {
		this.shipmentManager = shipmentManager;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}
}
