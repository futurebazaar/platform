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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fb.commons.PlatformException;
import com.fb.commons.to.Money;
import com.fb.platform.shipment.manager.ShipmentManager;
import com.fb.platform.shipment.to.GatePassItem;
import com.fb.platform.shipment.to.GatePassTO;
import com.fb.platform.shipment.to.ShipmentLSPEnum;
import com.sap.abapxml.Abap;
import com.sap.abapxml.Item;

/**
 * @author nehaga
 *
 */
@Component
public class ShipmentResource {
	
	private static Log infoLog = LogFactory.getLog("LOGINFO");
	
	private static Log errorLog = LogFactory.getLog("LOGERROR");
	
	//JAXBContext class is thread safe and can be shared
	private static final JAXBContext context = initContext();
	
	@Autowired
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
	public void processGatepass(String gatePassString) {
		infoLog.info("GatePass xml String received : " + gatePassString);
		Abap orders = createItemList(gatePassString);
		GatePassTO gatePass = createLSPLists(orders);
		shipmentManager.generateOutboundFile(gatePass);
	}
	
	/**
	 * This function converts the JAXB objects to ShipmentCore internal objects and splits the parcels items list into separate lsp lists.
	 * This function calls the service layer and fetches the required information from the database to generate ParcelItem object. 
	 * @param ordersList
	 */
	private GatePassTO createLSPLists(Abap orders) {
		GatePassTO gatePassTO = new GatePassTO();
		List<Item> ordersList = orders.getValues().getTAB().getItem();
		List<GatePassItem> deliveryItems = new ArrayList<GatePassItem>();
		for(Item gatePassItemReceived : ordersList) {
			GatePassItem gatePassItem = new GatePassItem();
			gatePassItem.setOrderReferenceId(gatePassItemReceived.getORDERREFID());
			gatePassItem.setAwbNo(gatePassItemReceived.getAWBNO());
			gatePassItem.setDeece(gatePassItemReceived.getDEECE());
			gatePassItem.setDelNo(gatePassItemReceived.getDELNO());
			gatePassItem.setDelWt(gatePassItemReceived.getDELWET());
			gatePassItem.setItemDescription(gatePassItemReceived.getDELDIS());
			deliveryItems.add(gatePassItem);
		}
		gatePassTO.setGatePassItems(deliveryItems);
		String lspCode = orders.getLSPCODE().substring(4);
		gatePassTO.setLspcode(ShipmentLSPEnum.getLSP(lspCode));
		gatePassTO.setGatePassId(orders.getGATEPASSID());
		return gatePassTO;
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
	private Abap createItemList(String gatePassString) {
		try {
			
			Unmarshaller unmarshaller = context.createUnmarshaller();
			
			Abap abap = (Abap) unmarshaller.unmarshal(new StreamSource(new StringReader(gatePassString)));
			
			return abap;
		} catch (JAXBException e) {
			System.out.println(e.getStackTrace());
		}
		
		return null;
	}
	
	public void setShipmentManager(ShipmentManager shipmentManager) {
		this.shipmentManager = shipmentManager;
	}
}
