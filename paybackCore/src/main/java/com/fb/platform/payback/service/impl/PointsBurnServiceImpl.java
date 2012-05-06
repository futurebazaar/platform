package com.fb.platform.payback.service.impl;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Iterator;
import java.util.Properties;

import org.joda.time.DateTime;

import com.fb.platform.payback.dao.PointsBurnDao;
import com.fb.platform.payback.dao.PointsDao;
import com.fb.platform.payback.model.OrderDetail;
import com.fb.platform.payback.model.PaymentDetail;
import com.fb.platform.payback.model.PointsHeader;
import com.fb.platform.payback.service.PointsBurnService;
import com.fb.platform.payback.service.PointsService;
import com.fb.platform.payback.to.BurnActionCodesEnum;
import com.fb.platform.payback.to.EarnActionCodesEnum;
import com.fb.platform.payback.to.PointsTxnClassificationCodeEnum;
import com.fb.platform.payback.util.PointsUtil;

public class PointsBurnServiceImpl implements PointsBurnService{
	
	private PointsBurnDao pointsBurnDao;
	private PointsDao pointsDao;
	private PointsUtil pointsUtil;
	private PointsService pointsService;
	
	public void setPointsService(PointsService pointsService) {
		this.pointsService = pointsService;
	}

	public void setPointsBurnDao(PointsBurnDao pointsBurnDao) {
		this.pointsBurnDao = pointsBurnDao;
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
		String header = "Transaction ID, Transaction Date, Merchant ID, Points, Reason";
		String fileBody = header + "\n";
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
			pointsUtil.sendMail(txnActionCode, merchantId, fileName, fileBody);
			pointsDao.updateStatus(txnActionCode, merchantId, settlementDate);
		}	
	}
	
	@Override
	public void saveBurnData(BurnActionCodesEnum txnActionCode, BigDecimal amount, long orderId, String reason) {
		PointsHeader pointsHeader = new PointsHeader();
		try {
			Properties props = pointsUtil.getProperties("points.properties");
			pointsHeader.setReason(reason);
			pointsHeader.setTxnActionCode(txnActionCode.name());
			pointsHeader.setOrderId(orderId);
			pointsHeader.setSettlementDate(DateTime.now());
			OrderDetail orderDetail = pointsDao.getOrderDetail(orderId);
			pointsHeader.setOrderDetails(orderDetail, props);
			PaymentDetail paymentDetail = pointsBurnDao.getPaymentDetails(orderId);
			PointsTxnClassificationCodeEnum classificationCodeEnum = PointsTxnClassificationCodeEnum.valueOf(txnActionCode.name());
			String classificationCode = classificationCodeEnum.toString().split(",")[0];
			String paymentType = classificationCodeEnum.toString().split(",")[1];
			pointsHeader.setBurnTxnDetails(paymentDetail, props, classificationCode, paymentType);
			String day = pointsHeader.getTxnDate().dayOfWeek().getAsText();
			String clientName = orderDetail.getClientName().toUpperCase().replaceAll(" ", "");
			pointsHeader.setTxnValue(amount.intValue());
			pointsHeader.setTxnPoints(pointsService.getTxnPoints(amount, day, props, 
					clientName, txnActionCode.name(), orderId));
			pointsHeader.setEarnRatio(pointsService.getEarnRatio(day, props, clientName, orderId, 
					EarnActionCodesEnum.PREALLOC_EARN));
			pointsHeader.setBurnRatio(pointsService.getBurnRatio(day, props, clientName));
			pointsDao.insertPointsHeaderData(pointsHeader);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
