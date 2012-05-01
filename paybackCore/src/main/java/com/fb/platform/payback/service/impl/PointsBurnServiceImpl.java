package com.fb.platform.payback.service.impl;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Properties;

import javax.mail.MessagingException;

import com.fb.commons.util.MailSender;
import com.fb.platform.payback.dao.PointsBurnDao;
import com.fb.platform.payback.dao.PointsDao;
import com.fb.platform.payback.model.PointsHeader;
import com.fb.platform.payback.service.PointsBurnService;
import com.fb.platform.payback.service.PointsService;
import com.fb.platform.payback.to.StoreBurnPointsRequest;
import com.fb.platform.payback.util.EarnActionCodesEnum;
import com.fb.platform.payback.util.PointsUtil;

public class PointsBurnServiceImpl implements PointsBurnService{
	
	private PointsBurnDao pointsBurnDao;
	private PointsService pointsService;
	private PointsDao pointsDao;
	private PointsUtil pointsUtil;
	
	public void setPointsBurnDao(PointsBurnDao pointsBurnDao) {
		this.pointsBurnDao = pointsBurnDao;
	}
	
	public void setPointsDao(PointsDao pointsDao) {
		this.pointsDao = pointsDao;
	}
	
	public void setPointsUtil(PointsUtil pointsUtil){
		this.pointsUtil = pointsUtil;
	}
	
	public void setPointsService(PointsService pointsService){
		this.pointsService = pointsService;
	}

	@Override
	public void saveBurnPoints(StoreBurnPointsRequest request) {
		PointsHeader pointsHeader = new PointsHeader();
		
	}

	@Override
	public void mailBurnData(String txnActionCode, String merchantId) {
		String settlementDate = pointsUtil.getPreviousDayDate();
		String fileName = "Burn Reversal" + settlementDate +  ".csv";
		String header = "Transaction ID, Transaction Date, Merchant ID, Points, Reason";
		String fileBody = header + "\n";
		Collection<PointsHeader> burnList = pointsBurnDao.loadBurnData(txnActionCode, settlementDate, merchantId);
		Iterator<PointsHeader> burnIterator = burnList.iterator();
		while(burnIterator.hasNext()){
			PointsHeader burnValues = burnIterator.next();
			String transactionID = burnValues.getTransactionId();
			String transactionDate = burnValues.getTxnDate();
			String points = String.valueOf(burnValues.getTxnPoints());
			String reason = burnValues.getReason();
			
			fileBody += transactionID + ", " + transactionDate + ", " + merchantId + ", " +
					points + ", " + reason + "\n";
			System.out.println(fileBody);
			
		}
		if (fileBody != null && !fileBody.equals("")){
			pointsService.sendMail(txnActionCode, merchantId, fileBody, fileName);
		}
		
	}
}
