package com.fb.platform.payback.service.impl;

import java.util.Collection;
import java.util.Iterator;

import com.fb.platform.payback.dao.PointsDao;
import com.fb.platform.payback.model.PointsHeader;
import com.fb.platform.payback.service.PointsBurnService;
import com.fb.platform.payback.service.PointsService;
import com.fb.platform.payback.to.PointsRequest;
import com.fb.platform.payback.to.PointsResponseCodeEnum;
import com.fb.platform.payback.to.PointsTxnClassificationCodeEnum;
import com.fb.platform.payback.util.PointsUtil;

public class PointsBurnServiceImpl implements PointsBurnService{
	
	private PointsDao pointsDao;
	private PointsUtil pointsUtil;
	private PointsService pointsService;
	
	public void setPointsService(PointsService pointsService) {
		this.pointsService = pointsService;
	}

	public void setPointsUtil(PointsUtil pointsUtil){
		this.pointsUtil = pointsUtil;
	}
	
	public void setPointsDao(PointsDao pointsDao){
		this.pointsDao = pointsDao;
	}

	@Override
	public void mailBurnData(String txnActionCode, String merchantId) {
		String settlementDate = pointsUtil.getPreviousDayDate();
		String fileName = "Burn Reversal_" + settlementDate +  ".csv";
		String header = "Transaction ID, Transaction Date, Merchant ID, Points, Reason \n";
		String fileBody = "";
		Collection<PointsHeader> burnList = pointsDao.loadPointsHeaderData(txnActionCode, settlementDate, merchantId);
		Iterator<PointsHeader> burnIterator = burnList.iterator();
		while(burnIterator.hasNext()){
			PointsHeader burnValues = burnIterator.next();
			String transactionID = burnValues.getReferenceId();
			String transactionDate = pointsUtil.convertDateToFormat(burnValues.getTxnDate(), "yyyy-MM-dd");
			String points = String.valueOf(burnValues.getTxnPoints());
			String reason = burnValues.getReason();
			
			fileBody += transactionID + ", " + transactionDate + ", " + merchantId + ", " +
					points + ", " + reason + "\n";
		}
		
		if (fileBody != null && !fileBody.equals("")){
			fileBody = header + fileBody;
			pointsUtil.sendMail(txnActionCode, merchantId, fileName, fileBody);
			//pointsDao.updateStatus(txnActionCode, merchantId, settlementDate);
		}	
	}
	
	@Override
	public PointsResponseCodeEnum storeBurnPoints(PointsRequest request,
			PointsTxnClassificationCodeEnum actionCode) {
		return null;
	}
}
