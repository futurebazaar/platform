package com.fb.commons.ftp.manager;

import com.fb.commons.ftp.FTPConnector;
import com.fb.commons.ftp.to.FTPConnectionTO;
import com.fb.commons.ftp.to.FTPDownloadTO;
import com.fb.commons.ftp.to.FTPUploadTO;

/**
 * @author nehaga
 *
 */
public class FTPManager {

	private FTPConnector connector= null;
	
	private FTPManager() {
		super();
	}
	
	public FTPManager(FTPConnectionTO ftpConnectionTO) {
		this();
		connector = new FTPConnector(ftpConnectionTO);
	}
	
	public void download(FTPDownloadTO downloadTO) {
		connector.download(downloadTO);
	}
	
	public void upload(FTPUploadTO uploadTO) {
		connector.upload(uploadTO);
	}
	
}
