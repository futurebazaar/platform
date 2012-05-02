package com.fb.platform.shipment.manager.impl;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;

import com.fb.commons.PlatformException;
import com.fb.platform.shipment.lsp.impl.AramexLSP;
import com.fb.platform.shipment.lsp.impl.BlueDartLSP;
import com.fb.platform.shipment.lsp.impl.FirstFlightLSP;
import com.fb.platform.shipment.lsp.impl.QuantiumLSP;
import com.fb.platform.shipment.manager.ShipmentManager;
import com.fb.platform.shipment.service.ShipmentService;
import com.fb.platform.shipment.to.GatePassItem;
import com.fb.platform.shipment.to.ParcelItem;
import com.fb.platform.shipment.to.ShipmentLSPEnum;
import com.fb.platform.shipment.util.ShipmentProcessor;
import com.sap.abapxml.Abap;
import com.sap.abapxml.Values.TAB.Item;

/**
 * @author nehaga
 * This is the manager Interface implementor, it is the entry point in this project.
 * Any module that wants to convert the gatePass.xml to outbound files has to get an instance of this class.
 *
 */
public class ShipmentManagerImpl implements ShipmentManager {
	
	private static Log logger = LogFactory.getLog(ShipmentManagerImpl.class);
	
	//JAXBContext class is thread safe and can be shared
	private static final JAXBContext context = initContext();
	private List<ParcelItem> quantiumParcelsList = null;
	private List<ParcelItem> firstFlightParcelsList = null;
	private List<ParcelItem> aramexParcelsList = null;
	private List<ParcelItem> blueDartParcelsList = null;
	
	@Autowired
	private ShipmentService shipmentService = null;
	
	private static JAXBContext initContext() {
		try {
			return JAXBContext.newInstance("com.sap.abapxml");
		} catch (JAXBException e) {
			logger.error("Error Initializing the JAXBContext to bind the schema classes", e);
			throw new PlatformException("Error Initializing the JAXBContext to bind the schema classes", e);
		}
	}
	
	/**
	 * This function accepts the gatePass.xml as String and initiates the process of creating the outbound files.
	 * @param gatePassString
	 */
	@Override
	public void generateOutboundFile(String gatePassString) {
		logger.info("GatePass xml String received : " + gatePassString);
		List<Item> ordersList = createItemList(gatePassString);
		createLSPLists(ordersList);
		processLSPLists();
		
	}
	
	/**
	 * This function converts the JAXB objects to ShipmentCore internal objects and splits the parcels items list into separate lsp lists.
	 * This function calls the service layer and fetches the required information from the database to generate ParcelItem object. 
	 * @param ordersList
	 */
	private void createLSPLists(List<Item> ordersList) {
		quantiumParcelsList = new ArrayList<ParcelItem>();
		firstFlightParcelsList = new ArrayList<ParcelItem>();
		aramexParcelsList = new ArrayList<ParcelItem>();
		blueDartParcelsList = new ArrayList<ParcelItem>();
		
		for(Item gatePassItemReceived : ordersList) {
			GatePassItem gatePassItem = new GatePassItem();
			gatePassItem.setAwbno(gatePassItemReceived.getAWBNO());
			gatePassItem.setDeece(gatePassItemReceived.getDEECE());
			gatePassItem.setDeldt(new DateTime(gatePassItemReceived.getDELDT()));
			gatePassItem.setDelno(gatePassItemReceived.getDELNO());
			gatePassItem.setGtpas(gatePassItemReceived.getGTPAS());
			gatePassItem.setInvdt(new DateTime(gatePassItemReceived.getINVDT()));
			gatePassItem.setInvno(gatePassItemReceived.getINVNO());
			gatePassItem.setLspcode(ShipmentLSPEnum.getLSP(gatePassItemReceived.getLSPCODE()));
			gatePassItem.setSonum(gatePassItemReceived.getSONUM());
			ParcelItem parcelItem = shipmentService.getParcelDetails(gatePassItem);
			switch (gatePassItem.getLspcode()) {
			case Quantium:
				quantiumParcelsList.add(parcelItem);
				break;
			case FirstFlight:
				firstFlightParcelsList.add(parcelItem);
				break;
			case Aramex:
				aramexParcelsList.add(parcelItem);
				break;
			case BlueDart:
				blueDartParcelsList.add(parcelItem);
				break;
			default:
				logger.error("Invalid LSP code : " + gatePassItemReceived.getLSPCODE() + " , for gate pass delivery : " + gatePassItem.toString());
				break;
			}
		}
	}
	
	/**
	 * This function creates instances for each lsp processor and passes them the list of parcels.
	 * It is the responsibility of the processor to create the outbound file in the desired format, save it in the expected extension and
	 * deliver it to the lsp.
	 */
	
	private void processLSPLists() {
		if(aramexParcelsList != null) {
			ShipmentProcessor aramexShipmentProcessor = new ShipmentProcessor(new AramexLSP());
			aramexShipmentProcessor.generateOutboundFile(aramexParcelsList);
		}
		if(firstFlightParcelsList != null) {
			ShipmentProcessor firstFlightShipmentProcessor = new ShipmentProcessor(new FirstFlightLSP());
			firstFlightShipmentProcessor.generateOutboundFile(firstFlightParcelsList);
		}
		if(blueDartParcelsList != null) {
			ShipmentProcessor blueDartShipmentProcessor = new ShipmentProcessor(new BlueDartLSP());
			blueDartShipmentProcessor.generateOutboundFile(blueDartParcelsList);
		}
		if(quantiumParcelsList != null) {
			ShipmentProcessor quantiumShipmentProcessor = new ShipmentProcessor(new QuantiumLSP());
			quantiumShipmentProcessor.generateOutboundFile(quantiumParcelsList);
		}
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
	
	public void setShipmentService(ShipmentService shipmentService) {
		this.shipmentService = shipmentService;
	}

}
