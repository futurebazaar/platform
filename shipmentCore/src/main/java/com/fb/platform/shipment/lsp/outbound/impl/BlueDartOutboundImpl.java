package com.fb.platform.shipment.lsp.outbound.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

import com.fb.platform.shipment.lsp.outbound.ShipmentOutbound;
import com.fb.platform.shipment.to.ParcelItem;

/**
 * @author nehaga
 *
 */
public class BlueDartOutboundImpl implements ShipmentOutbound {

	@Override
	public void generateOutboundFile(List<ParcelItem> parcelItemList) {
		generateFile(parcelItemList);
	}
	
	public void generateFile(List<ParcelItem> parcelItemList) {
		StringBuilder outboundFile = null;
		for(ParcelItem parcelItem : parcelItemList) {
			String parcelItemString = 	parcelItem.getDeliveryNumber() + "\t" +
										parcelItem.getQuantity() + "\t" +
										parcelItem.getCustomerName() + "\t" +
										parcelItem.getAddress() + "\t" +
										parcelItem.getCity() + "\t" +
										parcelItem.getState() + "\t" +
										parcelItem.getPincode() + "\t" +
										parcelItem.getPhoneNumber() + "\t" +
										parcelItem.getArticleDescription();
			if(outboundFile == null) {
				outboundFile = new StringBuilder();
			} else {
				outboundFile.append("\n");
			}
			outboundFile.append(parcelItemString);
		}
		System.out.println("======================================== BLUE DART ========================================\n" + outboundFile.toString());
		File file = new File("blueDart.bd");
		try {
			Writer writer = new BufferedWriter(new FileWriter(file));
			writer.write(outboundFile.toString());
			writer.close();
		} catch (IOException ioExceptio) {
			System.out.println("Error writing to file");
		}
	}

}
