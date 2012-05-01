package com.fb.platform.payback.service;

import java.io.File;


public interface PointsService {

	void sendMail(String txnActionCode, String merchantId);

	void sendMail(String txnActionCode, String merchantId, String fileName, String fileContent);
	
	void sendMail(String txnActionCode, String merchantId, String fileName, File file);

}
