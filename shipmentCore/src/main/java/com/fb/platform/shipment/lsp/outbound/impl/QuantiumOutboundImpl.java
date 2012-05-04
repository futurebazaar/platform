package com.fb.platform.shipment.lsp.outbound.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;

import com.fb.platform.shipment.lsp.outbound.ShipmentOutbound;
import com.fb.platform.shipment.to.ParcelItem;

/**
 * @author nehaga
 *
 */
public class QuantiumOutboundImpl implements ShipmentOutbound {

	private static String destinationPath = "src/main/gatepass/Quantium/Outbound";
	
	private static String extension=".QS";
	private static String fileNamePrefix="P";
	private static String fileNameFormat="%f%y%y%y%y%M%M%d%d%H%H%m%m";
	
	private static Log infoLog = LogFactory.getLog("LOGINFO");
	
	private static Log errorLog = LogFactory.getLog("LOGERROR");
	
	@Override
	public boolean generateOutboundFile(List<ParcelItem> parcelItemList) {
		boolean outboundCreated = generateFile(parcelItemList);
		return outboundCreated;
	}
	
	public boolean generateFile(List<ParcelItem> parcelItemList) {
		boolean outboundCreated = false;
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
		infoLog.info("======================================== QUANTIUM ========================================");
		String fileName = fileNameFormat;
		fileName = fileName.replace("%f", fileNamePrefix);
		DateTime timeStamp = new DateTime();
		fileName = fileName.replace("%y%y%y%y", timeStamp.getYear()+"");
		String month = timeStamp.getMonthOfYear()+"";
		if(month.length() < 2) {
			month = '0' + month;
		}
		fileName = fileName.replace("%M%M", month);
		String day = timeStamp.getDayOfMonth()+"";
		if(day.length() < 2) {
			day = '0' + day;
		}
		fileName = fileName.replace("%d%d", day);
		String hour = timeStamp.getHourOfDay()+"";
		if(hour.length() < 2) {
			hour = '0' + hour;
		}
		fileName = fileName.replace("%H%H", hour);
		String min = timeStamp.getMinuteOfHour()+"";
		if(min.length() < 2) {
			min = '0' + min;
		}
		fileName = fileName.replace("%m%m", min);
		fileName += extension;
		infoLog.info("Filename : " + fileName);
		File file = new File(destinationPath + File.separator + fileName);
		try {
			Writer writer = new BufferedWriter(new FileWriter(file));
			writer.write(outboundFile.toString());
			writer.close();
			outboundCreated = true;
			infoLog.info(outboundFile.toString());
		} catch (IOException ioException) {
			errorLog.error("Error writing to file : " + fileName +" , exception : " + ioException.getStackTrace());
		}
		return outboundCreated;
	}

}
