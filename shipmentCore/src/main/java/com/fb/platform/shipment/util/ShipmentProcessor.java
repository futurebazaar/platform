package com.fb.platform.shipment.util;

import java.util.List;

import com.fb.commons.mail.MailSender;
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
	
	/**
	 * This function calls the upload processor of lsp to upload the outbound files to LSP.
	 * @param parcelItem
	 */
	public void uploadOutboundFile() {
		shipmentOutboundProcessor.uploadOutboundFile();
	}
	
	/**
	 * This function calls the mail processor of lsp to main the outbound files.
	 * @param parcelItem
	 */
	public void mailOutboundFile(MailSender mailSender) {
		shipmentOutboundProcessor.mailOutboundFile(mailSender);
	}
}
