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
import org.apache.commons.lang.StringEscapeUtils;
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
public class ExpressItOutboundImpl implements ShipmentOutbound {

	private FTPManager lspFTPConnection = null;
	private FTPManager futureFTPConnection = null;
	
	private static Properties futureProp = new Properties();
	private static Properties lspProp = new Properties();
	
	private static Log infoLog = LogFactory.getLog("LOGINFO");
	
	private static Log errorLog = LogFactory.getLog("LOGERROR");
	
	public ExpressItOutboundImpl() {
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
			
			if(lspProp.getProperty("expressIt.ftp.hostName") != null) {
				connectionTO = new FTPConnectionTO();
				connectionTO.setHostName(lspProp.getProperty("expressIt.ftp.hostName"));
				connectionTO.setUserName(lspProp.getProperty("expressIt.ftp.userName"));
				connectionTO.setPassword(lspProp.getProperty("expressIt.ftp.password"));
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
	
	public void generateFile(List<ParcelItem> parcelItemList) {
		StringBuilder outboundFile = null;
		String serverFilePathFtp = futureProp.getProperty("future.server.outbound.expressIt.ftp");
		String serverFilePathMail = futureProp.getProperty("future.server.outbound.expressIt.mail");
		
		String extension = lspProp.getProperty("expressIt.outbound.extension");
		String fileNamePrefix = lspProp.getProperty("expressIt.outbound.fileNamePrefix");
		String fileNameFormat = lspProp.getProperty("expressIt.outbound.fileNameFormat");
		String separator = lspProp.getProperty("expressIt.outbound.separator");
		for(ParcelItem parcelItem : parcelItemList) {
			String parcelItemString = 	StringEscapeUtils.escapeCsv(parcelItem.getDeliveryNumber()) + separator +
					StringEscapeUtils.escapeCsv(parcelItem.getTrackingNumber()) + separator +
					StringEscapeUtils.escapeCsv(parcelItem.getCustomerName()) + separator +
					StringEscapeUtils.escapeCsv(parcelItem.getAddress()) + separator +
					StringEscapeUtils.escapeCsv(parcelItem.getState()) + separator +
					StringEscapeUtils.escapeCsv(parcelItem.getCountry()) + separator +
					StringEscapeUtils.escapeCsv(parcelItem.getCity()) + separator +
					StringEscapeUtils.escapeCsv(parcelItem.getPincode()) + separator +
					parcelItem.getPhoneNumber() + separator +
					parcelItem.getAmountPayable() + separator + 
					StringEscapeUtils.escapeCsv(parcelItem.getArticleDescription());
			if(outboundFile == null) {
				outboundFile = new StringBuilder();
			} else {
				outboundFile.append("\n");
			}
			outboundFile.append(parcelItemString);
		}
		infoLog.info("======================================== expressIt ========================================");
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
			errorLog.error("Error writing to file : " + fileName, ioException);
			new OutboundFileCreationException("Error writing to file : " + fileName);
		}
	}
	

	@Override
	public void uploadOutboundFile() {
		String uploadPath = lspProp.getProperty("expressIt.ftp.outbound");
		String serverFilePathFtp = futureProp.getProperty("future.server.outbound.expressIt.ftp");
		String uploadedPath = futureProp.getProperty("future.server.outbound.expressIt.ftp.uploaded");
		String futureFtpUploadPath = futureProp.getProperty("future.ftp.outbound.expressIt");
		
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
		String serverFilePathMail = futureProp.getProperty("future.server.outbound.expressIt.mail");
		String mailSentPath = futureProp.getProperty("future.server.outbound.expressIt.mail.sent");
		
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
		String[] to = lspProp.getProperty("expressIt.ftp.to").split(",");
		String[] cc = null;
		if(StringUtils.isNotBlank(lspProp.getProperty("expressIt.ftp.cc"))) {
			cc = lspProp.getProperty("expressIt.ftp.cc").split(",");
		}
		String[] bcc = null;
		if(StringUtils.isNotBlank(lspProp.getProperty("expressIt.ftp.bcc"))) {
			bcc = lspProp.getProperty("expressIt.ftp.bcc").split(",");
		}
		ftpMail.setFrom(lspProp.getProperty("expressIt.ftp.from"));
		Date today = new Date();
		ftpMail.setMessage(lspProp.getProperty("expressIt.ftp.message").replace("%date%", today.toString()));
		ftpMail.setSubject(lspProp.getProperty("expressIt.ftp.subject").replace("%date%", today.toString()));
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
