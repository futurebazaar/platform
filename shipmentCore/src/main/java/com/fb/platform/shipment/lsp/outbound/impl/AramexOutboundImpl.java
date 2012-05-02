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
public class AramexOutboundImpl implements ShipmentOutbound {

	@Override
	public void generateOutboundFile(List<ParcelItem> parcelItemList) {
		generateFile(parcelItemList);
	}
	
	public void generateFile(List<ParcelItem> parcelItemList) {
		StringBuilder outboundFile = null;
		for(ParcelItem parcelItem : parcelItemList) {
			String parcelItemString = 	parcelItem.getDeliveryNumber() + "||" +
										parcelItem.getCustomerName() + "||-||" +
										parcelItem.getAddress() + "||-||" +
										parcelItem.getCity() + "||" +
										parcelItem.getState() + "||" +
										parcelItem.getCountry() + "||" +
										parcelItem.getPincode() + "||" +
										parcelItem.getPhoneNumber() + "||" +
										parcelItem.getAmountPayable() + "||" + 
										parcelItem.getArticleDescription() + "||" +
										parcelItem.getWeight() + "||" +
										parcelItem.getQuantity() + "||" +
										parcelItem.getDeliverySiteId() + "||" +
										parcelItem.getPaymentMode() + "||" +
										parcelItem.getTrackingNumber();
			if(outboundFile == null) {
				outboundFile = new StringBuilder();
			} else {
				outboundFile.append("\n");
			}
			outboundFile.append(parcelItemString);
		}
		System.out.println("======================================== ARAMEX ========================================\n" + outboundFile.toString());
		File file = new File("aramex.ar");
		try {
			Writer writer = new BufferedWriter(new FileWriter(file));
			writer.write(outboundFile.toString());
			writer.close();
		} catch (IOException ioExceptio) {
			System.out.println("Error writing to file");
		}
	}

}
