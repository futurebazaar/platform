package com.fb.platform.shipment.ftp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fb.commons.ftp.exception.FTPConnectException;
import com.fb.commons.ftp.exception.FTPDisconnectException;
import com.fb.commons.ftp.exception.FTPDownloadException;
import com.fb.commons.ftp.exception.FTPLoginException;
import com.fb.commons.ftp.exception.FTPLogoutException;
import com.fb.commons.ftp.manager.FTPManager;
import com.fb.commons.ftp.to.FTPConnectionTO;
import com.fb.commons.ftp.to.FTPDownloadTO;
import com.fb.platform.shipment.util.StringGenerator;

/**
 * @author nehaga
 *
 */
public class ShipmentFTPClient {
	
	private static Log infoLog = LogFactory.getLog("LOGINFO");
	
	private static Log errorLog = LogFactory.getLog("LOGERROR");
	
	private static Properties prop = null;
	private static String ftpPropertiesFile = "ftp_details.properties";
	
	private static String hostName;
	private static String userName;
	private static String password;
	private static String home;
	private static String lspPath;
	private static String gatePassPath;
	private static String destinationPath;
	
	private static String movePath;
	
	public ShipmentFTPClient() {
		loadFTPProperties();
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
			
			destinationPath = prop.getProperty("future.server.downloadPath");
			
		} catch (FileNotFoundException e) {
			System.out.println(e.getStackTrace());
		} catch (Exception e) {
			System.out.println(e.getStackTrace());
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
					String gatePassString = StringGenerator.convertInputStreamToString(gatePassXML);
					infoLog.info(gatePassString);
					gatePassList.add(gatePassString);
					gatePassXML.close();
				}
			}
		} catch (IOException e) {
			errorLog.error("FTP disconnect error : " + e.getStackTrace());
		}
		return gatePassList;
	}
	
	private void downloadFiles() {
		try {
			FTPConnectionTO connectionTO = new FTPConnectionTO();
			connectionTO.setHostName(hostName);
			connectionTO.setUserName(userName);
			connectionTO.setPassword(password);
			
			FTPManager ftpConnection = new FTPManager(connectionTO);
			
			FTPDownloadTO downloadTO = new FTPDownloadTO();
			downloadTO.setSourcePath(gatePassPath);
			downloadTO.setMovePath(movePath);
			downloadTO.setDestinationPath(destinationPath);
			
			ftpConnection.download(downloadTO);
		} catch (FTPConnectException e) {
			errorLog.error("FTP connection error", e);
		} catch (FTPLoginException e) {
			errorLog.error("FTP login error", e);
		} catch (FTPLogoutException e) {
			errorLog.error("FTP logout error", e);
		} catch (FTPDisconnectException e) {
			errorLog.error("FTP disconnect error", e);
		} catch (FTPDownloadException e) {
			errorLog.error("FTP download error", e);
		}
		
	}
	
}
