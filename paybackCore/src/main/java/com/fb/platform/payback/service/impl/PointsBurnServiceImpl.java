package com.fb.platform.payback.service.impl;

import java.io.IOException;
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
import com.fb.platform.payback.util.BurnActionCodesEnum;
import com.fb.platform.payback.util.PointsTxnClassificationCodeEnum;
import com.fb.platform.payback.util.PointsUtil;

public class PointsBurnServiceImpl implements PointsBurnService{
	
	private PointsBurnDao pointsBurnDao;
	private PointsDao pointsDao;
	private PointsUtil pointsUtil;
	
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
		Collection<PointsHeader> burnList = pointsBurnDao.loadBurnData(txnActionCode, settlementDate, merchantId);
		Iterator<PointsHeader> burnIterator = burnList.iterator();
		while(burnIterator.hasNext()){
			PointsHeader burnValues = burnIterator.next();
			String transactionID = burnValues.getTransactionId();
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
		Properties props;
		try {
			props = pointsUtil.getProperties("points.properties");
			pointsHeader.setReason(reason);
			pointsHeader.setTxnActionCode(txnActionCode.name());
			pointsHeader.setOrderId(orderId);
			pointsHeader.setSettlementDate(DateTime.now());
			OrderDetail orderDetail = pointsDao.getOrderDetail(orderId);
			pointsHeader.setOrderDetails(orderDetail, props);
			PaymentDetail paymentDetail = pointsBurnDao.getPaymentDetails(orderId);
			pointsHeader.setTxnDetails(paymentDetail, props, txnActionCode.name());
			pointsHeader.setTxnValue(amount.intValue());
			pointsHeader.setTxnPoints(getBurnPoints(orderDetail.getClientName(), amount, orderId,  props));
			pointsDao.insertPointsHeaderData(pointsHeader);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private int getBurnPoints(String clientName,  BigDecimal amount, long orderId,  Properties burnProps) {
		PointsTxnClassificationCodeEnum txnCode = PointsTxnClassificationCodeEnum.PREALLOC_EARN;
		String txnClassificationCode = txnCode.toString().split(",")[0];
		PointsHeader previousPointsHeader = pointsDao.getHeaderDetails(orderId, txnCode.name(), txnClassificationCode);
		BigDecimal burnRatio = previousPointsHeader.getBurnRatio();
		if (burnRatio == null || burnRatio.equals(BigDecimal.ZERO)){
			burnRatio = new BigDecimal(burnProps.getProperty(clientName + "_BURN_POINTS"));
			String burnFactorProp = burnProps.getProperty(clientName + "_BURN_FACTOR");
			BigDecimal burnFactor = new BigDecimal(burnFactorProp == null ? "1" : burnFactorProp);
			burnRatio = burnRatio.multiply(burnFactor);
		}
		BigDecimal txnPoints = amount.multiply(burnRatio).setScale(2, BigDecimal.ROUND_HALF_DOWN);
		return txnPoints.intValue();
	}

}
