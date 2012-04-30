package com.fb.platform.shipment.util;

import java.util.List;

import com.fb.platform.shipment.lsp.ShipmentLSP;
import com.fb.platform.shipment.lsp.outbound.ShipmentOutbound;
import com.fb.platform.shipment.to.ParcelItem;

/**
 * @author nehaga
 * This class is the processor for processing the parcels list for each lsp.
 */
public class ShipmentProcessor {
	
	private ShipmentOutbound shipmentOutboundProcessor;
	
	private ShipmentProcessor() {
		super();
	}
	
	/**
	 * This constructor accepts an instance of a lsp and creates the objects associated with that lsp.
	 * @param shipmentLSP
	 */
	public ShipmentProcessor(ShipmentLSP shipmentLSP) {
		this();
		shipmentOutboundProcessor = shipmentLSP.outboundProcessor();
	}
	
	/**
	 * This function calls the outbound processor of lsp to create the outbound files for a list of parcels.
	 * @param parcelItem
	 */
	public void generateOutboundFile(List<ParcelItem> parcelItem) {
		shipmentOutboundProcessor.generateOutboundFile(parcelItem);
	}
}
