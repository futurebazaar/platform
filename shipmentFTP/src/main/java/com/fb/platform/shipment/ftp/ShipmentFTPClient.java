package com.fb.platform.shipment.ftp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import com.fb.platform.shipment.util.StringGenerator;

public class ShipmentFTPClient {
	
	private static Log infoLog = LogFactory.getLog("LOGINFO");
	
	private static Log errorLog = LogFactory.getLog("LOGERROR");
	
	private static FTPClient ftpClient = null;
	private static Properties prop = null;
	private static String ftpPropertiesFile = "ftp_details.properties";
	private static boolean isLoggedIn = false;
	
//	private static String hostName = "10.0.5.18";
//	private static String userName = "nehag";
//	private static String password = "P@ssW0rdN3h@g";
//	private static boolean isLoggedIn = false;
//	private static String home = "~";
//	private static String lspPath = home + "/LSP";
//	private static String gatePassPath = lspPath + "/Gatepass";
//	private static String destinationPath = "src/main/gatepass";
//	private static String aramexOutboundUploadPath = lspPath + "/test/Aramex/Outbound";
//	private static String blueDartOutboundUploadPath = lspPath + "/test/BlueDart/Outbound";
//	private static String quantiumOutboundUploadPath = lspPath + "/test/Quantium/Outbound";
//	private static String firstFlightOutboundUploadPath = lspPath + "/test/firstFlight/Outbound";
//	
//	private static String aramexOutboundSourcePath = destinationPath + "/Aramex/Outbound";
//	private static String blueDartOutboundSourcePath = destinationPath + "/BlueDart/Outbound";
//	private static String quantiumOutboundSourcePath = destinationPath + "/Quantium/Outbound";
//	private static String firstFlightOutboundSourcePath = destinationPath + "/firstFlight/Outbound";
	
	private static String hostName;
	private static String userName;
	private static String password;
	private static String home;
	private static String lspPath;
	private static String gatePassPath;
	private static String destinationPath;
	private static String aramexOutboundUploadPath;
	private static String blueDartOutboundUploadPath;
	private static String quantiumOutboundUploadPath;
	private static String firstFlightOutboundUploadPath;
	
	private static String aramexOutboundSourcePath;
	private static String blueDartOutboundSourcePath;
	private static String quantiumOutboundSourcePath;
	private static String firstFlightOutboundSourcePath;
	
	private static String aramexOutboundSourcePathUploaded;
	private static String blueDartOutboundSourcePathUploaded;
	private static String quantiumOutboundSourcePathUploaded;
	private static String firstFlightOutboundSourcePathUploaded;
	
	private static String movePath;
	
	public ShipmentFTPClient() {
		try{
			if(ftpClient == null){
				ftpClient = new FTPClient();
			}
			loadFTPProperties();
			login();
		} catch (Exception e) {
			System.out.println(e.getStackTrace());
		}
	}
	
	private void loadFTPProperties() {
		prop = new Properties();
		try {
			InputStream ftpPropertiesStream = this.getClass().getClassLoader().getResourceAsStream(ftpPropertiesFile);
			prop.load(ftpPropertiesStream);
			hostName = prop.getProperty("future.ftp.hostName");
			userName = prop.getProperty("future.ftp.userName");
			password = prop.getProperty("future.ftp.password");
			home = prop.getProperty("future.ftp.homePath");
			lspPath = prop.getProperty("future.ftp.lspPath");
			gatePassPath = prop.getProperty("future.ftp.gatePassPath");
			movePath = prop.getProperty("future.ftp.processed");
			aramexOutboundUploadPath = prop.getProperty("future.ftp.outbound.aramex");
			blueDartOutboundUploadPath = prop.getProperty("future.ftp.outbound.blueDart");
			quantiumOutboundUploadPath = prop.getProperty("future.ftp.outbound.quantium");
			firstFlightOutboundUploadPath = prop.getProperty("future.ftp.outbound.firstFlight");
			
			destinationPath = prop.getProperty("future.server.downloadPath");
			blueDartOutboundSourcePath = prop.getProperty("future.server.outbound.blueDart");
			aramexOutboundSourcePath = prop.getProperty("future.server.outbound.aramex");
			quantiumOutboundSourcePath = prop.getProperty("future.server.outbound.quantium");
			firstFlightOutboundSourcePath = prop.getProperty("future.server.outbound.firstFlight");
			blueDartOutboundSourcePathUploaded = prop.getProperty("future.server.outbound.blueDart.uploaded");
			aramexOutboundSourcePathUploaded = prop.getProperty("future.server.outbound.aramex.uploaded");
			quantiumOutboundSourcePathUploaded = prop.getProperty("future.server.outbound.quantium.uploaded");
			firstFlightOutboundSourcePathUploaded = prop.getProperty("future.server.outbound.firstFlight.uploaded");
			
		} catch (FileNotFoundException e) {
			System.out.println(e.getStackTrace());
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
					InputStream gatePassXML = new FileInputStream(file); 
					//this.getClass().getClassLoader().getResourceAsStream(gatePassDir + File.separator + file.getName());
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
							String fileName = file.getName();
							infoLog.info("uploading file : " + fileName);
							File outboundFile =  new File(aramexOutboundSourcePath + File.separator + fileName);
							InputStream aramexOutboundFile = new FileInputStream(outboundFile);
							boolean uploadResponse = ftpClient.storeFile(fileName, aramexOutboundFile);
							if(uploadResponse) {
								boolean moveResponse = file.renameTo(new File(aramexOutboundSourcePathUploaded, fileName));
				 		    	infoLog.info("Moving : " + fileName + " , to : " + aramexOutboundSourcePathUploaded + File.separator + fileName + " ,response : " + moveResponse);
				 		       	if(!moveResponse) {
				 		       		errorLog.warn("Moving : " + fileName + " , to : " + aramexOutboundSourcePathUploaded + File.separator + fileName + " , failed :" + ftpClient.getReplyString());
				 		       	}
							}
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
							String fileName = file.getName();
							infoLog.info("uploading file : " + fileName);
							File outboundFile =  new File(blueDartOutboundSourcePath + File.separator + fileName);
							InputStream blueDartOutboundFile = new FileInputStream(outboundFile);
							boolean uploadResponse = ftpClient.storeFile(fileName, blueDartOutboundFile);
							if(uploadResponse) {
								boolean moveResponse = file.renameTo(new File(blueDartOutboundSourcePathUploaded, fileName));
				 		    	infoLog.info("Moving : " + fileName + " , to : " + blueDartOutboundSourcePathUploaded + File.separator + fileName + " ,response : " + moveResponse);
				 		       	if(!moveResponse) {
				 		       		errorLog.warn("Moving : " + fileName + " , to : " + blueDartOutboundSourcePathUploaded + File.separator + fileName + " , failed :" + ftpClient.getReplyString());
				 		       	}
							}
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
							String fileName = file.getName();
							infoLog.info("uploading file : " + fileName);
							File outboundFile =  new File(firstFlightOutboundSourcePath + File.separator + fileName);
							InputStream firstFlightOutboundFile = new FileInputStream(outboundFile);
							boolean uploadResponse = ftpClient.storeFile(fileName, firstFlightOutboundFile);
							if(uploadResponse) {
								boolean moveResponse = file.renameTo(new File(firstFlightOutboundSourcePathUploaded, fileName));
				 		    	infoLog.info("Moving : " + fileName + " , to : " + firstFlightOutboundSourcePathUploaded + File.separator + fileName + " ,response : " + moveResponse);
				 		       	if(!moveResponse) {
				 		       		errorLog.warn("Moving : " + fileName + " , to : " + firstFlightOutboundSourcePathUploaded + File.separator + fileName + " , failed :" + ftpClient.getReplyString());
				 		       	}
							}
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
							String fileName = file.getName();
							infoLog.info("uploading file : " + fileName);
							File outboundFile =  new File(quantiumOutboundSourcePath + File.separator + fileName);
							InputStream quantiumOutboundFile = new FileInputStream(outboundFile);
							boolean uploadResponse = ftpClient.storeFile(fileName, quantiumOutboundFile);
							if(uploadResponse) {
								boolean moveResponse = file.renameTo(new File(quantiumOutboundSourcePathUploaded, fileName));
				 		    	infoLog.info("Moving : " + fileName + " , to : " + quantiumOutboundSourcePathUploaded + File.separator + fileName + " ,response : " + moveResponse);
				 		       	if(!moveResponse) {
				 		       		errorLog.warn("Moving : " + fileName + " , to : " + quantiumOutboundSourcePathUploaded + File.separator + fileName + " , failed :" + ftpClient.getReplyString());
				 		       	}
							}
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
