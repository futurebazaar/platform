package com.fb.platform.shipment.lsp.outbound.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.springframework.mail.MailException;

import com.fb.commons.ftp.manager.FTPManager;
import com.fb.commons.ftp.to.FTPConnectionTO;
import com.fb.commons.ftp.to.FTPUploadTO;
import com.fb.commons.mail.MailSender;
import com.fb.commons.mail.to.MailTO;
import com.fb.platform.shipment.exception.OutboundFileCreationException;
import com.fb.platform.shipment.lsp.outbound.ShipmentOutbound;
import com.fb.platform.shipment.to.ParcelItem;

/**
 * @author nehaga
 *
 */
public class BlueDartOutboundImpl implements ShipmentOutbound {

	private static String hostName;
	private static String userName;
	private static String password;
	private static String serverFilePathFtp;
	private static String serverFilePathMail;
	private static String uploadedPath;
	private static String mailSentPath;
	private FTPManager ftpConnection;
	
	private static Properties prop = new Properties();
	
	private static Log infoLog = LogFactory.getLog("LOGINFO");
	
	private static Log errorLog = LogFactory.getLog("LOGERROR");
	
	public BlueDartOutboundImpl() {
		super();
		loadProperties();
	}
	
	private void loadProperties() {
		try {
			InputStream configPropertiesStream = this.getClass().getClassLoader().getResourceAsStream("ftp_details.properties");
			prop.load(configPropertiesStream);
			hostName = prop.getProperty("future.ftp.hostName");
			userName = prop.getProperty("future.ftp.userName");
			password = prop.getProperty("future.ftp.password");
			serverFilePathFtp = prop.getProperty("future.server.outbound.blueDart.ftp");
			serverFilePathMail = prop.getProperty("future.server.outbound.blueDart.mail");
			uploadedPath = prop.getProperty("future.server.outbound.blueDart.ftp.uploaded");
			mailSentPath = prop.getProperty("future.server.outbound.blueDart.mail.sent");
			prop.clear();
			
			InputStream lspPropertiesStream = this.getClass().getClassLoader().getResourceAsStream("lsp_configurations.properties");
			prop.load(lspPropertiesStream);
			
			FTPConnectionTO connectionTO = new FTPConnectionTO();
			connectionTO.setHostName(hostName);
			connectionTO.setUserName(userName);
			connectionTO.setPassword(password);
			
			ftpConnection = new FTPManager(connectionTO);
		} catch (FileNotFoundException e) {
			System.out.println(e.getStackTrace());
		} catch (Exception e) {
			System.out.println(e.getStackTrace());
		}
	}
	
	@Override
	public void generateOutboundFile(List<ParcelItem> parcelItemList) {
		generateFile(parcelItemList);
	}
	
	public void generateFile(List<ParcelItem> parcelItemList) {
		StringBuilder outboundFile = null;
		String extension = prop.getProperty("blueDart.outbound.extension");
		String fileNamePrefix = prop.getProperty("blueDart.outbound.fileNamePrefix");
		String fileNameFormat = prop.getProperty("blueDart.outbound.fileNameFormat");
		for(ParcelItem parcelItem : parcelItemList) {
			String parcelItemString = 	parcelItem.getDeliveryNumber() + "\t" +
										parcelItem.getWeight() + "\t" +
										parcelItem.getCustomerName() + "\t" +
										parcelItem.getAddress() + "\t" +
										parcelItem.getCity() + "\t" +
										parcelItem.getState() + "\t" +
										parcelItem.getPincode() + "\t" +
										parcelItem.getArticleDescription() + "\t" +
										parcelItem.getAmountPayable().toString() + "\t" +
										parcelItem.getDeliverySiteId() + "\t" +
										parcelItem.getTrackingNumber() + "\t" +
										parcelItem.getPaymentMode();
			if(outboundFile == null) {
				outboundFile = new StringBuilder();
			} else {
				outboundFile.append("\n");
			}
			outboundFile.append(parcelItemString);
		}
		infoLog.info("======================================== BLUE DART ========================================");
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
		File file = new File(serverFilePathFtp + File.separator + fileName);
		File mailFile = new File(serverFilePathMail + File.separator + fileName);
		try {
			Writer writer = new BufferedWriter(new FileWriter(file));
			writer.write(outboundFile.toString());
			writer.close();
			FileUtils.copyFile(file, mailFile);
			infoLog.info(outboundFile.toString());
		} catch (IOException ioException) {
			errorLog.error("Error writing to file : " + fileName + ", exception : " + ioException.getStackTrace());
			new OutboundFileCreationException(ioException);
		}
	}
	

	@Override
	public void uploadOutboundFile() {
		String uploadPath = prop.getProperty("blueDart.ftp.outbound");
		
		File[] outboundFiles = getOutboundFiles(serverFilePathFtp);
		
		FTPUploadTO uploadTO = new FTPUploadTO();
		uploadTO.setSourcePath(serverFilePathFtp);
		uploadTO.setMovePath(uploadedPath);
		uploadTO.setDestinationPath(uploadPath);
		uploadTO.setFiles(outboundFiles);
		
		ftpConnection.upload(uploadTO);
		
	}
	
	private File[] getOutboundFiles(String sourcePath) {
		File outboundDir = new File(sourcePath);
		File[] outboundFiles = outboundDir.listFiles();
		return outboundFiles;
	}

	@Override
	public void mailOutboundFile(MailSender mailSender) {
		File[] outboundFiles = getOutboundFiles(serverFilePathMail);
		List<File> attachments = mailAttachments(outboundFiles);
		if(attachments != null && attachments.size() > 0) {
			try {
				MailTO ftpMail = createMail(attachments);
				mailSender.send(ftpMail);
				moveFiles(outboundFiles, mailSentPath);
			} catch (MailException e) {
				System.out.println(e.getStackTrace());
			}
		}
	}
	
	private MailTO createMail(List<File> attachments) {
		MailTO ftpMail = new MailTO();
		String[] to = prop.getProperty("blueDart.ftp.to").split(",");
		String[] cc = prop.getProperty("blueDart.ftp.cc").split(",");
		String[] bcc = prop.getProperty("blueDart.ftp.bcc").split(",");
		ftpMail.setFrom(prop.getProperty("blueDart.ftp.from"));
		DateTime dateTime = new DateTime();
		ftpMail.setMessage(prop.getProperty("blueDart.ftp.message").replace("%date%", dateTime.toString()));
		ftpMail.setSubject(prop.getProperty("blueDart.ftp.subject").replace("%date%", dateTime.toString()));
		ftpMail.setAttachments(attachments);
		ftpMail.setTo(to);
		ftpMail.setCc(cc);
		ftpMail.setBcc(bcc);
		return ftpMail;
	}
	
	private List<File> mailAttachments(File[] outboundFiles) {
		List<File> attachments = new ArrayList<File>();
		if(outboundFiles != null && outboundFiles.length > 0) {
			for(File file : outboundFiles) {
				if(file.isFile()) {
					attachments.add(file);
				}
			}
		}
		return attachments;
	}
	
	private void moveFiles(File[] outboundFiles, String outboundMailSent) {
		try {
			for(File file : outboundFiles) {
				if(file.isFile()) {
					FileUtils.moveFileToDirectory(file, new File(outboundMailSent), true);
				}
			}
		} catch (IOException e) {
			System.out.println(e.getStackTrace());
		}
	}


}
