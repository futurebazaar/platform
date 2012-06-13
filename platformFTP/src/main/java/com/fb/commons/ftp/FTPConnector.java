/**
 * 
 */
package com.fb.commons.ftp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import com.fb.commons.ftp.exception.FTPConnectException;
import com.fb.commons.ftp.exception.FTPDisconnectException;
import com.fb.commons.ftp.exception.FTPDownloadException;
import com.fb.commons.ftp.exception.FTPLoginException;
import com.fb.commons.ftp.exception.FTPUploadException;
import com.fb.commons.ftp.to.FTPConnectionTO;
import com.fb.commons.ftp.to.FTPDownloadTO;
import com.fb.commons.ftp.to.FTPUploadTO;

/**
 * @author nehaga
 *
 */
public class FTPConnector {
	
	private Log log = LogFactory.getLog(FTPConnector.class);
	
	private FTPClient ftpClient = null;
	private FTPConnectionTO connectTO;
	
	private FTPConnector() {
		super();
	}
	
	public FTPConnector(FTPConnectionTO connectionTO) {
		this();
		this.connectTO = connectionTO; 
	}
	
	private void connect() {
		try {
			if(ftpClient == null){
				ftpClient = new FTPClient();
			}
			ftpClient.connect(connectTO.getHostName());
		} catch (IOException ioexception) {
			log.error("FTP connect error, hostname : " + connectTO.getHostName(), ioexception);
			throw new FTPConnectException(ioexception);
		}
	}
	
	private void login() {
		try{
			connect();
			ftpClient.login(connectTO.getUserName(), connectTO.getPassword());
		} catch (IOException ioexception) {
			log.error("FTP login error, hostname : " + connectTO.getHostName(), ioexception);
			throw new FTPLoginException(ioexception);
		}
	}
	
	private void disconnect() {
		try {
			ftpClient.disconnect();
		} catch (IOException ioException) {
			log.error("FTP disconnect error, hostname : " + connectTO.getHostName(), ioException);
			throw new FTPDisconnectException(ioException);
		}
	}
	
	private void logout() {
		try {
			ftpClient.logout();
			disconnect();
		} catch (IOException ioException) {
			log.error("FTP logout error, hostname : " + connectTO.getHostName(), ioException);
			throw new FTPLoginException(ioException);
		}
	}
	
	public void upload(FTPUploadTO uploadTO) {
		try {
			login();
			ftpClient.cwd(uploadTO.getDestinationPath());
			if(uploadTO.getFiles() != null && uploadTO.getFiles().length > 0) {
				for(File file : uploadTO.getFiles()) {
					if(file.isFile()) {
						String fileName = file.getName();
						log.info("uploading file : " + fileName);
						File outboundFile =  new File(uploadTO.getSourcePath() + File.separator + fileName);
						InputStream outboundStream = new FileInputStream(outboundFile);
						boolean uploadResponse = ftpClient.storeFile(fileName, outboundStream);
						if(uploadResponse && uploadTO.getMovePath() != null) {
							boolean moveResponse = file.renameTo(new File(uploadTO.getMovePath(), fileName));
			 		    	log.info("Moving : " + fileName + " , to : " + uploadTO.getMovePath() + File.separator + fileName + " ,response : " + moveResponse);
			 		       	if(!moveResponse) {
			 		       		log.warn("Moving : " + fileName + " , to : " + uploadTO.getMovePath() + File.separator + fileName + " , failed :" + ftpClient.getReplyString());
			 		       	}
						}
						outboundStream.close();
					}
				}
			}
			logout();
		} catch (IOException e) {
			log.error("FTP upload error : " + uploadTO.toString(), e);
			throw new FTPUploadException(e);
		}
	}
	
	public void download(FTPDownloadTO downloadTO) {
		try {
			login();
			FileOutputStream fos = null;
			ftpClient.cwd(downloadTO.getSourcePath());
			FTPFile[] ftpFiles = ftpClient.listFiles();
		    for (FTPFile ftpFile : ftpFiles) {
		    	if(ftpFile.isFile()) {
		    		String fileName = ftpFile.getName();
		    		fos = new FileOutputStream(downloadTO.getDestinationPath() + File.separator + fileName);
		    		boolean retrieveResponse = ftpClient.retrieveFile(fileName, fos);
		    		log.info("Downloading : " + fileName + " , response : " + retrieveResponse);
		    		if(retrieveResponse == true && downloadTO.getMovePath() != null) {
		    			boolean moveResponse = ftpClient.rename(fileName, downloadTO.getMovePath() + File.separator + fileName);
		 		    	log.info("Moving : " + fileName + " , to : " + downloadTO.getMovePath() + File.separator + fileName + " ,response : " + moveResponse);
		 		       	if(!moveResponse) {
		 		       		log.warn("Moving : " + fileName + " , to : " + downloadTO.getMovePath() + File.separator + fileName + " , failed :" + ftpClient.getReplyString());
		 		       	}
		    		}
		    		fos.close();
		    	}
		    }
			logout();
		} catch (IOException e) {
			log.error("FTP file download error : " + downloadTO.toString(), e);
			throw new FTPDownloadException(e);
		} 
	}
}
