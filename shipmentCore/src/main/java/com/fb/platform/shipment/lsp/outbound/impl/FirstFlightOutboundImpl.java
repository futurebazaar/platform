package com.fb.platform.shipment.lsp.outbound.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;

import com.fb.commons.PlatformException;
import com.fb.commons.ftp.manager.FTPManager;
import com.fb.commons.ftp.to.FTPConnectionTO;
import com.fb.commons.ftp.to.FTPUploadTO;
import com.fb.commons.mail.MailSender;
import com.fb.commons.mail.to.MailTO;
import com.fb.platform.shipment.exception.MoveFileException;
import com.fb.platform.shipment.exception.OutboundFileCreationException;
import com.fb.platform.shipment.lsp.outbound.ShipmentOutbound;
import com.fb.platform.shipment.to.ParcelItem;

/**
 * @author nehaga
 *
 */
public class FirstFlightOutboundImpl implements ShipmentOutbound {

	private FTPManager lspFTPConnection = null;
	private FTPManager futureFTPConnection = null;
	
	private static Properties futureProp = new Properties();
	private static Properties lspProp = new Properties();
	
	private static Log infoLog = LogFactory.getLog("LOGINFO");
	
	private static Log errorLog = LogFactory.getLog("LOGERROR");
	
	public FirstFlightOutboundImpl() {
		super();
		loadProperties();
	}	
	
	private void loadProperties() {
		try {
			InputStream configPropertiesStream = this.getClass().getClassLoader().getResourceAsStream("ftp_details.properties");
			futureProp.load(configPropertiesStream);
			
			FTPConnectionTO connectionTO = new FTPConnectionTO();
			connectionTO.setHostName(futureProp.getProperty("future.ftp.hostName"));
			connectionTO.setUserName(futureProp.getProperty("future.ftp.userName"));
			connectionTO.setPassword(futureProp.getProperty("future.ftp.password"));
			
			futureFTPConnection = new FTPManager(connectionTO);
			
			InputStream lspPropertiesStream = this.getClass().getClassLoader().getResourceAsStream("lsp_configurations.properties");
			lspProp.load(lspPropertiesStream);
			
			if(lspProp.getProperty("firstFlight.ftp.hostName") != null) {
				connectionTO = new FTPConnectionTO();
				connectionTO.setHostName(lspProp.getProperty("firstFlight.ftp.hostName"));
				connectionTO.setUserName(lspProp.getProperty("firstFlight.ftp.userName"));
				connectionTO.setPassword(lspProp.getProperty("firstFlight.ftp.password"));
				
				lspFTPConnection = new FTPManager(connectionTO);
			}
			
		} catch (IOException e) {
			errorLog.error("Error loading properties file.", e);
			new PlatformException("Error loading properties file.");
		}
	}
	
	@Override
	public void generateOutboundFile(List<ParcelItem> parcelItemList) {
		generateFile(parcelItemList);
	}
	
	private void generateFile(List<ParcelItem> parcelItemList) {
		StringBuilder outboundFile = null;
		String serverFilePathFtp = futureProp.getProperty("future.server.outbound.firstFlight.ftp");
		String serverFilePathMail = futureProp.getProperty("future.server.outbound.firstFlight.mail");
		
		String extension = lspProp.getProperty("firstFlight.outbound.extension");
		String fileNamePrefix = lspProp.getProperty("firstFlight.outbound.fileNamePrefix");
		String fileNameFormat = lspProp.getProperty("firstFlight.outbound.fileNameFormat");
		String separator = lspProp.getProperty("firstFlight.outbound.separator");
		for(ParcelItem parcelItem : parcelItemList) {
			String parcelItemString = 	parcelItem.getDeliveryNumber() + separator +
										parcelItem.getCustomerName() + separator +
										parcelItem.getAddress() + separator +
										parcelItem.getCity() + separator +
										parcelItem.getState() + separator +
										parcelItem.getCountry() + separator +
										parcelItem.getPincode() + separator +
										parcelItem.getPhoneNumber() + separator +
										parcelItem.getAmountPayable() + separator + 
										parcelItem.getArticleDescription() + separator +
										parcelItem.getWeight() + separator +
										parcelItem.getQuantity() + separator +
										parcelItem.getDeliverySiteId() + separator +
										parcelItem.getPaymentMode() + separator +
										parcelItem.getTrackingNumber();
			if(outboundFile == null) {
				outboundFile = new StringBuilder();
			} else {
				outboundFile.append("\n");
			}
			outboundFile.append(parcelItemString);
		}
		infoLog.info("======================================== FIRST FLIGHT ========================================");
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
		File ftpFile = new File(serverFilePathFtp + File.separator + fileName);
		File mailFile = new File(serverFilePathMail + File.separator + fileName);
		try {
			Writer writer = new BufferedWriter(new FileWriter(ftpFile));
			writer.write(outboundFile.toString());
			writer.close();
			FileUtils.copyFile(ftpFile, mailFile);
			infoLog.info(outboundFile.toString());
		} catch (IOException ioException) {
			errorLog.error("Error writing to file : " + fileName, ioException);
			new OutboundFileCreationException("Error writing to file : " + fileName);
		}
	}

	@Override
	public void uploadOutboundFile() {
		String uploadPath = lspProp.getProperty("firstFlight.ftp.outbound");
		String serverFilePathFtp = futureProp.getProperty("future.server.outbound.firstFlight.ftp");
		String uploadedPath = futureProp.getProperty("future.server.outbound.firstFlight.ftp.uploaded");
		String futureFtpUploadPath = futureProp.getProperty("future.ftp.outbound.firstFlight");
		
		File[] outboundFiles = getOutboundFiles(serverFilePathFtp);
		
		FTPUploadTO uploadTO = new FTPUploadTO();
		uploadTO.setSourcePath(serverFilePathFtp);
		if(lspFTPConnection == null) {
			uploadTO.setMovePath(uploadedPath);
		} else {
			uploadTO.setMovePath(null);
		}
		uploadTO.setDestinationPath(futureFtpUploadPath);
		uploadTO.setFiles(outboundFiles);
		futureFTPConnection.upload(uploadTO);
		
		if(lspFTPConnection != null) {
			uploadTO = new FTPUploadTO();
			uploadTO.setSourcePath(serverFilePathFtp);
			uploadTO.setMovePath(uploadedPath);
			uploadTO.setDestinationPath(uploadPath);
			uploadTO.setFiles(outboundFiles);
			
			lspFTPConnection.upload(uploadTO);
		}
		
	}
	
	private File[] getOutboundFiles(String sourcePath) {
		File outboundDir = new File(sourcePath);
		File[] outboundFiles = outboundDir.listFiles();
		return outboundFiles;
	}

	@Override
	public void mailOutboundFile(MailSender mailSender) {
		String serverFilePathMail = futureProp.getProperty("future.server.outbound.firstFlight.mail");
		String mailSentPath = futureProp.getProperty("future.server.outbound.firstFlight.mail.sent");
		
		File[] outboundFiles = getOutboundFiles(serverFilePathMail);
		List<File> attachments = mailAttachments(outboundFiles);
		if(attachments != null && attachments.size() > 0) {
			MailTO ftpMail = createMail(attachments);
			mailSender.send(ftpMail);
			moveFiles(outboundFiles, mailSentPath);
		}
	}
	
	private MailTO createMail(List<File> attachments) {
		MailTO ftpMail = new MailTO();
		String[] to = lspProp.getProperty("firstFlight.ftp.to").split(",");
		String[] cc = null;
		if(StringUtils.isNotBlank(lspProp.getProperty("firstFlight.ftp.cc"))) {
			cc = lspProp.getProperty("firstFlight.ftp.cc").split(",");
		}
		String[] bcc = null;
		if(StringUtils.isNotBlank(lspProp.getProperty("firstFlight.ftp.bcc"))) {
			bcc = lspProp.getProperty("firstFlight.ftp.bcc").split(",");
		}
		ftpMail.setFrom(lspProp.getProperty("firstFlight.ftp.from"));
		Date today = new Date();
		ftpMail.setMessage(lspProp.getProperty("firstFlight.ftp.message").replace("%date%", today.toString()));
		ftpMail.setSubject(lspProp.getProperty("firstFlight.ftp.subject").replace("%date%", today.toString()));
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
			errorLog.error("Error moving file to path : " + outboundMailSent + ", exception : " + e.getStackTrace());
			throw new MoveFileException("Error moving file to path : " + outboundMailSent);
		}
	}

}
