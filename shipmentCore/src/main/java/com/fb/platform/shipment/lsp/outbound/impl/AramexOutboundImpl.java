package com.fb.platform.shipment.lsp.outbound.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;

import com.fb.platform.shipment.lsp.outbound.ShipmentOutbound;
import com.fb.platform.shipment.to.ParcelItem;

/**
 * @author nehaga
 *
 */
public class AramexOutboundImpl implements ShipmentOutbound {

	private static String serverFilePath;
	private static Properties prop = new Properties();
	
	private static String extension;
	private static String fileNamePrefix;
	private static String fileNameFormat;
	
	private static Log infoLog = LogFactory.getLog("LOGINFO");
	
	private static Log errorLog = LogFactory.getLog("LOGERROR");
	
	public AramexOutboundImpl() {
		super();
		loadProperties();
	}
	
	private void loadProperties() {
		try {
			InputStream configPropertiesStream = this.getClass().getClassLoader().getResourceAsStream("ftp_details.properties");
			prop.load(configPropertiesStream);
			serverFilePath = prop.getProperty("future.server.outbound.aramex");
			prop.clear();
			
			InputStream lspPropertiesStream = this.getClass().getClassLoader().getResourceAsStream("lsp_configurations.properties");
			prop.load(lspPropertiesStream);
			extension = prop.getProperty("aramex.outbound.extension");
			fileNamePrefix = prop.getProperty("aramex.outbound.fileNamePrefix");
			fileNameFormat = prop.getProperty("aramex.outbound.fileNameFormat");
		} catch (FileNotFoundException e) {
			System.out.println(e.getStackTrace());
		} catch (Exception e) {
			System.out.println(e.getStackTrace());
		}
	}
	
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
		infoLog.info("======================================== ARAMEX ========================================");
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
		File file = new File(serverFilePath + File.separator + fileName);
		try {
			Writer writer = new BufferedWriter(new FileWriter(file));
			writer.write(outboundFile.toString());
			writer.close();
			outboundCreated = true;
			infoLog.info(outboundFile.toString());
		} catch (IOException ioException) {
			errorLog.error("Error writing to file : " + fileName + ", exception : " + ioException.getStackTrace());
		}
		return outboundCreated;
	}

}
