package com.fb.platform.shipment.manager.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fb.platform.shipment.lsp.impl.AramexLSP;
import com.fb.platform.shipment.lsp.impl.BlueDartLSP;
import com.fb.platform.shipment.lsp.impl.FirstFlightLSP;
import com.fb.platform.shipment.lsp.impl.QuantiumLSP;
import com.fb.platform.shipment.manager.ShipmentManager;
import com.fb.platform.shipment.service.ShipmentService;
import com.fb.platform.shipment.to.GatePassItem;
import com.fb.platform.shipment.to.ParcelItem;
import com.fb.platform.shipment.util.ShipmentProcessor;

/**
 * @author nehaga
 * This is the manager Interface implementor, it is the entry point in this project.
 * Any module that wants to convert the gatePass.xml to outbound files has to get an instance of this class.
 *
 */
public class ShipmentManagerImpl implements ShipmentManager {
	
	private static Log infoLog = LogFactory.getLog("LOGINFO");
	
	private static Log errorLog = LogFactory.getLog("LOGERROR");	
	
	private List<ParcelItem> quantiumParcelsList = null;
	private List<ParcelItem> firstFlightParcelsList = null;
	private List<ParcelItem> aramexParcelsList = null;
	private List<ParcelItem> blueDartParcelsList = null;
	
	@Autowired
	private ShipmentService shipmentService = null;
	
	/**
	 * This function accepts a list of gate pass delivery items and processes it to create outbound files.
	 * @param gatePassString
	 */
	@Override
	public boolean generateOutboundFile(List<GatePassItem> deliveryList) {
		createLSPLists(deliveryList);
		boolean outboundCreated = processLSPLists();
		return outboundCreated;
	}
	
	/**
	 * This function converts the JAXB objects to ShipmentCore internal objects and splits the parcels items list into separate lsp lists.
	 * This function calls the service layer and fetches the required information from the database to generate ParcelItem object. 
	 * @param ordersList
	 */
	private void createLSPLists(List<GatePassItem> deliveryList) {
		for(GatePassItem gatePassItem : deliveryList) {
			infoLog.debug("Gate Pass item : " + gatePassItem.toString());
			ParcelItem parcelItem = shipmentService.getParcelDetails(gatePassItem);
			if(parcelItem != null) {
				infoLog.debug("Parcel Item Data : " + parcelItem.toString());
				if(gatePassItem.getLspcode() == null) {
					errorLog.error("Incorrect LSP code for order : " + gatePassItem.toString());
				} else {
					switch (gatePassItem.getLspcode()) {
					case Quantium:
						if(quantiumParcelsList == null) {
							quantiumParcelsList = new ArrayList<ParcelItem>();
						}
						quantiumParcelsList.add(parcelItem);
						break;
					case FirstFlight:
						if(firstFlightParcelsList == null) {
							firstFlightParcelsList = new ArrayList<ParcelItem>();
						}
						firstFlightParcelsList.add(parcelItem);
						break;
					case Aramex:
						if(aramexParcelsList == null) {
							aramexParcelsList = new ArrayList<ParcelItem>();
						}
						aramexParcelsList.add(parcelItem);
						break;
					case BlueDart:
						if(blueDartParcelsList == null) {
							blueDartParcelsList = new ArrayList<ParcelItem>();
						}
						blueDartParcelsList.add(parcelItem);
						break;
					default:
						errorLog.error("Invalid LSP code : " + gatePassItem.getLspcode() + " , for gate pass delivery : " + gatePassItem.toString());
						break;
					}
				}
			}
		}
	}
	
	/**
	 * This function creates instances for each lsp processor and passes them the list of parcels.
	 * It is the responsibility of the processor to create the outbound file in the desired format, save it in the expected extension and
	 * deliver it to the lsp.
	 */
	
	private boolean processLSPLists() {
		boolean aramexOutboundCreated = false;
		boolean firstFlightOutboundCreated = false;
		boolean blueDartOutboundCreated = false;
		boolean quantiumOutboundCreated = false;
		if(aramexParcelsList != null) {
			ShipmentProcessor aramexShipmentProcessor = new ShipmentProcessor(new AramexLSP());
			aramexOutboundCreated = aramexShipmentProcessor.generateOutboundFile(aramexParcelsList);
		}
		if(firstFlightParcelsList != null) {
			ShipmentProcessor firstFlightShipmentProcessor = new ShipmentProcessor(new FirstFlightLSP());
			firstFlightOutboundCreated = firstFlightShipmentProcessor.generateOutboundFile(firstFlightParcelsList);
		}
		if(blueDartParcelsList != null) {
			ShipmentProcessor blueDartShipmentProcessor = new ShipmentProcessor(new BlueDartLSP());
			blueDartOutboundCreated = blueDartShipmentProcessor.generateOutboundFile(blueDartParcelsList);
		}
		if(quantiumParcelsList != null) {
			ShipmentProcessor quantiumShipmentProcessor = new ShipmentProcessor(new QuantiumLSP());
			quantiumOutboundCreated = quantiumShipmentProcessor.generateOutboundFile(quantiumParcelsList);
		}
		return (aramexOutboundCreated || firstFlightOutboundCreated || blueDartOutboundCreated || quantiumOutboundCreated);
	}
	
	public void setShipmentService(ShipmentService shipmentService) {
		this.shipmentService = shipmentService;
	}

}
