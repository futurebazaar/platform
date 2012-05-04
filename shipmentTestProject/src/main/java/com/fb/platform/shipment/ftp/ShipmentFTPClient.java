package com.fb.platform.shipment.ftp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import com.fb.platform.shipment.util.StringGenerator;

public class ShipmentFTPClient {
	
	private static Log infoLog = LogFactory.getLog("LOGINFO");
	
	private static Log errorLog = LogFactory.getLog("LOGERROR");
	
	private static FTPClient ftpClient = null;
	private static String hostName = "10.0.5.18";
	private static String userName = "nehag";
	private static String password = "P@ssW0rdN3h@g";
	private static boolean isLoggedIn = false;
	private static String home = "~";
	private static String lspPath = home + "/LSP";
	private static String gatePassPath = lspPath + "/Gatepass";
	private static String destinationPath = "src/main/gatepass";
	private static String aramexOutboundUploadPath = lspPath + "/test/Aramex/Outbound";
	private static String blueDartOutboundUploadPath = lspPath + "/test/BlueDart/Outbound";
	private static String quantiumOutboundUploadPath = lspPath + "/test/Quantium/Outbound";
	private static String firstFlightOutboundUploadPath = lspPath + "/test/firstFlight/Outbound";
	
	private static String aramexOutboundSourcePath = destinationPath + "/Aramex/Outbound";
	private static String blueDartOutboundSourcePath = destinationPath + "/BlueDart/Outbound";
	private static String quantiumOutboundSourcePath = destinationPath + "/Quantium/Outbound";
	private static String firstFlightOutboundSourcePath = destinationPath + "/firstFlight/Outbound";
	
	private static String movePath =  "Processed";
	
	public ShipmentFTPClient() {
		try{
			if(ftpClient == null){
				ftpClient = new FTPClient();
			}
			login();
		} catch (Exception e) {
			System.out.println(e.getStackTrace());
		}
	}
	
	private void login() {
		try{
			if(!isLoggedIn) {
				ftpClient.connect(hostName);
				isLoggedIn = ftpClient.login(userName, password);
			}
		} catch (IOException ioexception) {
			System.out.println(ioexception.getStackTrace());
		}
	}
	
	private void logout() {
		try {
			ftpClient.logout();
			isLoggedIn = false;
			ftpClient.disconnect();
		} catch (IOException e) {
			errorLog.error("FTP disconnect error : " + e.getStackTrace());
		}
	}
	
	public List<String> getGatePassList() {
		List<String> gatePassList = new ArrayList<String>();
		downloadFiles();
		File gatePassDir = new File(destinationPath);
		File[] gatePassFiles = gatePassDir.listFiles();
		try {
			for(File file : gatePassFiles) {
				if(file.isFile()) {
					InputStream gatePassXML = ShipmentFTPClient.class.getClassLoader().getResourceAsStream(file.getName());
					String gatePassString = StringGenerator.convertInputStreamToString(gatePassXML);
					infoLog.info(gatePassString);
					gatePassList.add(gatePassString);
					gatePassXML.close();
				}
			}
			logout();
		} catch (IOException e) {
			errorLog.error("FTP disconnect error : " + e.getStackTrace());
		}
		return gatePassList;
	}
	
	private void downloadFiles() {
		//TODO get a count and verify if all are downloaded
		//TODO what to do if any file is corrupted and cannot be downloaded
		if(!isLoggedIn) {
			login();
		}
		FileOutputStream fos = null;
		try {
			ftpClient.cwd(gatePassPath);
			FTPFile[] ftpFiles = ftpClient.listFiles();
		    for (FTPFile ftpFile : ftpFiles) {
		    	if(ftpFile.isFile()) {
		    		String fileName = ftpFile.getName();
		    		fos = new FileOutputStream(destinationPath + File.separator + fileName);
		    		boolean retrieveResponse = ftpClient.retrieveFile(fileName, fos);
		    		infoLog.info("Downloading : " + fileName + " , response : " + retrieveResponse);
		    		//TODO this code is tested it should be uncommented before going into production.
//		    		if(retrieveResponse == true) {
//		    			boolean moveResponse = ftpClient.rename(fileName, movePath + File.separator + fileName);
//		 		    	infoLog.info("Moving : " + fileName + " , to : " + movePath + File.separator + fileName + " ,response : " + moveResponse);
//		 		       	if(!moveResponse) {
//		 		       		errorLog.warn("Moving : " + fileName + " , to : " + movePath + File.separator + fileName + " , failed :" + ftpClient.getReplyString());
//		 		       	}
//		    		}
		    		fos.close();
		    	}
		    }
		} catch (IOException e) {
			errorLog.error("FTP file download error : " + e.getStackTrace());
		}
		
	}
	
	public void deliverOutboundFiles() {
		if(!isLoggedIn) {
			login();
		}
		uploadOutboundFiles();
	}
	
	private void uploadOutboundFiles() {
		uploadAramexOutboundFiles();
		uploadBlueDartOutboundFiles();
		uploadFirstFlightOutboundFiles();
		uploadQuantiumOutboundFiles();
	}
	
	private void uploadAramexOutboundFiles() {
		try {
			ftpClient.cwd(aramexOutboundUploadPath);
			File aramexOutboundDir = new File(aramexOutboundSourcePath);
			File[] aramexOutboundFiles = aramexOutboundDir.listFiles();
			try {
				if(aramexOutboundFiles != null && aramexOutboundFiles.length > 0) {
					for(File file : aramexOutboundFiles) {
						if(file.isFile()) {
							infoLog.info("uploading file : " + file.getName());
							File outboundFile =  new File(aramexOutboundSourcePath + File.separator + file.getName());
							InputStream aramexOutboundFile = new FileInputStream(outboundFile);
							ftpClient.storeFile(file.getName(), aramexOutboundFile);
							aramexOutboundFile.close();
						}
					}
				}
			} catch (IOException e) {
				errorLog.error("FTP disconnect error : " + e.getStackTrace());
			}
		} catch (IOException e) {
			errorLog.error("FTP file upload error : " + e.getStackTrace());
		}
	}
	
	private void uploadBlueDartOutboundFiles() {
		try {
			ftpClient.cwd(blueDartOutboundUploadPath);
			File bluedartOutboundDir = new File(blueDartOutboundSourcePath);
			File[] blueDartOutboundFiles = bluedartOutboundDir.listFiles();
			try {
				if(blueDartOutboundFiles != null && blueDartOutboundFiles.length > 0) {
					for(File file : blueDartOutboundFiles) {
						if(file.isFile()) {
							infoLog.info("uploading file : " + file.getName());
							File outboundFile =  new File(blueDartOutboundSourcePath + File.separator + file.getName());
							InputStream blueDartOutboundFile = new FileInputStream(outboundFile);
							ftpClient.storeFile(file.getName(), blueDartOutboundFile);
							blueDartOutboundFile.close();
						}
					}
				}
			} catch (IOException e) {
				errorLog.error("FTP disconnect error : " + e.getStackTrace());
			}
		} catch (IOException e) {
			errorLog.error("FTP file upload error : " + e.getStackTrace());
		}
	}
	
	private void uploadFirstFlightOutboundFiles() {
		try {
			ftpClient.cwd(firstFlightOutboundUploadPath);
			File firstFlightOutboundDir = new File(firstFlightOutboundSourcePath);
			File[] firstFlightOutboundFiles = firstFlightOutboundDir.listFiles();
			try {
				if(firstFlightOutboundFiles != null && firstFlightOutboundFiles.length > 0) {
					for(File file : firstFlightOutboundFiles) {
						if(file.isFile()) {
							infoLog.info("uploading file : " + file.getName());
							File outboundFile =  new File(firstFlightOutboundSourcePath + File.separator + file.getName());
							InputStream firstFlightOutboundFile = new FileInputStream(outboundFile);
							ftpClient.storeFile(file.getName(), firstFlightOutboundFile);
							firstFlightOutboundFile.close();
						}
					}
				}
			} catch (IOException e) {
				errorLog.error("FTP disconnect error : " + e.getStackTrace());
			}
		} catch (IOException e) {
			errorLog.error("FTP file upload error : " + e.getStackTrace());
		}
	}
	
	
	private void uploadQuantiumOutboundFiles() {
		try {
			ftpClient.cwd(quantiumOutboundUploadPath);
			File quantiumOutboundDir = new File(quantiumOutboundSourcePath);
			File[] quantiumOutboundFiles = quantiumOutboundDir.listFiles();
			try {
				if(quantiumOutboundFiles != null && quantiumOutboundFiles.length > 0) {
					for(File file : quantiumOutboundFiles) {
						if(file.isFile()) {
							infoLog.info("uploading file : " + file.getName());
							File outboundFile =  new File(quantiumOutboundSourcePath + File.separator + file.getName());
							InputStream quantiumOutboundFile = new FileInputStream(outboundFile);
							ftpClient.storeFile(file.getName(), quantiumOutboundFile);
							quantiumOutboundFile.close();
						}
					}
				}
			} catch (IOException e) {
				errorLog.error("FTP disconnect error : " + e.getStackTrace());
			}
		} catch (IOException e) {
			errorLog.error("FTP file upload error : " + e.getStackTrace());
		}
	}
	
	private void mailOutboundFiles() {
		
	}
}
