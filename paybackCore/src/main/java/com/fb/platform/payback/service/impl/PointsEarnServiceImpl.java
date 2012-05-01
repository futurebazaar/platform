package com.fb.platform.payback.service.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Iterator;
import java.util.Properties;

import org.joda.time.DateTime;

import com.fb.commons.PlatformException;
import com.fb.commons.util.SFTPConnector;
import com.fb.platform.payback.dao.PointsDao;
import com.fb.platform.payback.dao.PointsEarnDao;
import com.fb.platform.payback.model.PointsItems;
import com.fb.platform.payback.service.PointsEarnService;
import com.fb.platform.payback.util.PointsUtil;

public class PointsEarnServiceImpl implements PointsEarnService{
	
	private PointsEarnDao pointsEarnDao;
	private PointsDao pointsDao;
	private PointsUtil pointsUtil;
	
	public void setPointsUtil(PointsUtil pointsUtil){
		this.pointsUtil = pointsUtil;
	}

	public void setPointsEarnDao(PointsEarnDao pointsEarnDao) {
		this.pointsEarnDao = pointsEarnDao;
	}
	
	public void setPointsDao(PointsDao pointsDao) {
		this.pointsDao = pointsDao;
	}
	
	@Override
	public int postEarnData(String txnActionCode, String merchantId){
		try{
			Properties props = pointsUtil.getProperties("payback.properties");
			String sequenceNumber = getSequenceNumber();
			String settlementDate = pointsUtil.getPreviousDayDate();
		
			// SFTP Connection
			SFTPConnector sftpConnector = new SFTPConnector();
			sftpConnector.setHost(props.getProperty("sftpHost"));
			sftpConnector.setUsername(props.getProperty("sftpUsername"));
			sftpConnector.setPassword(props.getProperty("sftpPassword"));
			if (!sftpConnector.isConnected()){
				// false determines that host key checking will be disabled
				sftpConnector.connect(false);
			}
			
			Collection<PointsItems> earnList = pointsEarnDao.loadEarnData(txnActionCode, settlementDate, merchantId);
			String newSettlementDate = pointsUtil.convertDateToFormat(settlementDate, "ddMMyyyy");
			String dataToUpload = getEarnDataToUpload(earnList, newSettlementDate, merchantId, sequenceNumber);
			if (dataToUpload != null && !dataToUpload.equals("")){
				String fileName = txnActionCode.toString() + "_" + String.valueOf(merchantId) + 
						"_"+ newSettlementDate + "_" + sequenceNumber + ".txt";
				String remoteDirectory = props.getProperty("sftpRemoteDirectory");
				sftpConnector.upload(dataToUpload, fileName, remoteDirectory);
				fileName = fileName.replace(".txt", ".chk");
				sftpConnector.upload(dataToUpload, "", remoteDirectory);
				pointsDao.updateStatus(txnActionCode, settlementDate, merchantId);
			}
			sftpConnector.closeConnection();			
			
		}catch(FileNotFoundException fileException){
			fileException.printStackTrace();
		}catch(IOException ioException){
			ioException.printStackTrace();
		} catch (PlatformException pe) {
			pe.printStackTrace();
		}
		return 0;
		
	}

	private String getEarnDataToUpload(Collection<PointsItems> earnList, String settlementDate, String partnerMerchantId, String sequenceNumber) {
		Iterator<PointsItems> EarnIterator = earnList.iterator();
		int totalRows = 0;
		int totalTxnPoints = 0;
		int totalTxnValue = 0;
		String pointsHeaderId = "";
		String rowData = "";
		int itemRows = 0;
		String timestamp = "";
		while(EarnIterator.hasNext()){
			PointsItems earnValues = EarnIterator.next();
			if (!pointsHeaderId.equals(earnValues.getPointsHeaderId())){
				pointsHeaderId = earnValues.getPointsHeaderId();
				timestamp = earnValues.getTxnTimestamp();
				totalRows++;
				itemRows = 1;
				rowData += earnValues.getLoyaltyCard() + "||||" + earnValues.getPartnerMerchantId() + "||" +
						earnValues.getPartnerTerminalId() + "||" + earnValues.getOrderId() + "|||" +
						earnValues.getTxnActionCode() + "|" + earnValues.getTxnClassificationCode() + "|||" +
						earnValues.getTxnPaymentType() + "|" + 
						pointsUtil.convertDateToFormat(earnValues.getTxnDate(), "ddMMyyyy") + "|" + 
						earnValues.getPointsHeaderId() + "|" + String.valueOf(earnValues.getTxnValue()) + "||" +  
						earnValues.getMarketingCode() + "|" + String.valueOf(earnValues.getTxnPoints()) + "|" + 
						String.valueOf(itemRows) + "|" + 
						pointsUtil.convertDateToFormat(earnValues.getSettlementDate(), "ddMMyyyy") + "|||||||" + 
						String.valueOf(earnValues.getTxnValue()) + "|" + earnValues.getBranchId() + "||||||||||||||||||||||";
				rowData += "\n";
				itemRows++;
				rowData += "|" + String.valueOf(earnValues.getItemAmount()) + "|" + String.valueOf(earnValues.getItemAmount()) + "||" +
						earnValues.getPartnerMerchantId() + "||1.00|AMOUNT|" + earnValues.getOrderId() + "|" + 
						String.valueOf(itemRows) + "||||||||" + earnValues.getDepartmentCode() + "|" + 
						earnValues.getDepartmentName() + "|||" + earnValues.getArticleId() + "|" + String.valueOf(itemRows) + 
						"|||||||||||||||||||||||||||||||";
				rowData += "\n";
				totalTxnPoints += earnValues.getTxnPoints();
				totalTxnValue += earnValues.getTxnValue();
			}
			else{
				itemRows++;
				rowData += "|" + String.valueOf(earnValues.getItemAmount()) + "|" + String.valueOf(earnValues.getItemAmount()) + "||" +
						earnValues.getPartnerMerchantId() + "||1.00|AMOUNT|" + earnValues.getOrderId() + "|" + 
						String.valueOf(itemRows) + "||||||||" + earnValues.getDepartmentCode() + "|" + 
						earnValues.getDepartmentName() + "|||" + earnValues.getArticleId() + "|" + String.valueOf(itemRows) + 
						"|||||||||||||||||||||||||||||||";
				rowData += "\n";
			}
		}
		if (earnList.size() > 0){
			rowData += "PB_ACT_1.1|" + timestamp + "|" + sequenceNumber + "|" + partnerMerchantId + "||" + totalRows + "|" + 
							totalTxnValue + "|" + totalTxnPoints + "|||||||||||||||9|||||||||||||||||||||||||||||||";
		}
		return rowData;
	}
	
	private String getSequenceNumber() {
		DateTime datetime = new DateTime();
		int seconds = (datetime.getSecondOfDay()%999999) + 1;
		String sequenceNumber = String.valueOf(seconds);
		while(sequenceNumber.length() < 6){
			sequenceNumber = "0" + sequenceNumber;
		}
		return sequenceNumber;
	}
	

}
