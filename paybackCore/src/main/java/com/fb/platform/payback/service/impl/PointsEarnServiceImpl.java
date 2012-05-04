package com.fb.platform.payback.service.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Iterator;
import java.util.Properties;
import java.util.StringTokenizer;

import org.joda.time.DateTime;

import com.fb.commons.PlatformException;
import com.fb.commons.util.SFTPConnector;
import com.fb.platform.payback.dao.PointsDao;
import com.fb.platform.payback.dao.PointsEarnDao;
import com.fb.platform.payback.model.PointsItems;
import com.fb.platform.payback.service.PointsEarnService;
import com.fb.platform.payback.util.EarnActionCodesEnum;
import com.fb.platform.payback.util.PointsUtil;

public class PointsEarnServiceImpl implements PointsEarnService{
	
	private PointsEarnDao pointsEarnDao;
	private PointsUtil pointsUtil;
	private PointsDao pointsDao;
	
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
	public String postEarnData(EarnActionCodesEnum txnActionCode, String merchantId){
		String dataToUpload = "";
		try{
			Properties props = pointsUtil.getProperties("payback.properties");
			String sequenceNumber = getSequenceNumber();
			String settlementDate = pointsUtil.getPreviousDayDate();
		
			// SFTP Connection
			SFTPConnector sftpConnector = new SFTPConnector();
			// SFTP username is required since it has been used multiple times
			String sftpUsername = props.getProperty("sftpUsername");
			sftpConnector.setHost(props.getProperty("sftpHost"));
			sftpConnector.setUsername(sftpUsername);
			sftpConnector.setPassword(props.getProperty("sftpPassword"));
			if (!sftpConnector.isConnected()){
				// false determines that host key checking will be disabled
				sftpConnector.connect(false);
			}
			
			Collection<PointsItems> earnList = pointsEarnDao.loadEarnData(txnActionCode.name(), settlementDate, merchantId);
			DateTime newSettlementDate = DateTime.now();
			dataToUpload = getEarnDataToUpload(earnList, newSettlementDate, merchantId, sequenceNumber);
			if (dataToUpload != null && !dataToUpload.equals("")){
				String fileName = txnActionCode.toString() + "_" + String.valueOf(merchantId) + 
						"_"+ pointsUtil.convertDateToFormat(settlementDate, "ddMMyyyy") + "_" + sequenceNumber + ".txt";
				String remoteDirectory = "/" + sftpUsername + "/" + props.getProperty("sftpRemoteDirectory");
				sftpConnector.upload(dataToUpload, fileName, remoteDirectory);
				fileName = fileName.replace("txt", "chk");
				sftpConnector.upload("", fileName, remoteDirectory);
				//pointsDao.updateStatus(txnActionCode, settlementDate, merchantId);
			}
			//sftpConnector.closeConnection();			
			
		}catch(FileNotFoundException fileException){
			fileException.printStackTrace();
		}catch(IOException ioException){
			ioException.printStackTrace();
		} catch (PlatformException pe) {
			pe.printStackTrace();
		}
		return dataToUpload;
		
	}

	private String getEarnDataToUpload(Collection<PointsItems> earnList, DateTime currentTime, String partnerMerchantId, String sequenceNumber) {
		Iterator<PointsItems> EarnIterator = earnList.iterator();
		int headerRows = 0;
		int totalTxnPoints = 0;
		int totalTxnValue = 0;
		long pointsHeaderId = 0;
		String rowData = "";
		int itemRows = 0;
		while(EarnIterator.hasNext()){
			PointsItems earnValues = EarnIterator.next();
			String departmentName = earnValues.getDepartmentName();
			if (departmentName.length() > 30){
				departmentName = departmentName.substring(0, 29);
			}
			if (pointsHeaderId != earnValues.getPointsHeaderId()){
				pointsHeaderId = earnValues.getPointsHeaderId();
				headerRows++;
				itemRows = 1;
				
				rowData += earnValues.getLoyaltyCard() + "||||" + earnValues.getPartnerMerchantId() + "||" +
						earnValues.getPartnerTerminalId() + "||" + earnValues.getOrderId() + "|||" +
						earnValues.getTxnActionCode() + "|" + earnValues.getTxnClassificationCode() + "|||" +
						earnValues.getTxnPaymentType() + "|" + 
						pointsUtil.convertDateToFormat(earnValues.getTxnDate(), "ddMMyyyy") + "|" + 
						pointsUtil.convertDateToFormat(earnValues.getTxnTimestamp(), "hhmmss") + "|" + 
						new BigDecimal(earnValues.getTxnValue()).setScale(2) + "||" + earnValues.getMarketingCode() + "|" +
						earnValues.getTxnPoints() + "|" + String.valueOf(itemRows) + "|" + 
						pointsUtil.convertDateToFormat(earnValues.getSettlementDate(), "ddMMyyyy")	+ "|||||||" + 
						new BigDecimal(earnValues.getTxnValue()).setScale(2) + "|" + earnValues.getBranchId() + 
						"||||||||||||||||||||||";
				rowData += "\n";
				rowData += "|" + new BigDecimal(earnValues.getItemAmount()).setScale(2) + "|" + 
						new BigDecimal(earnValues.getItemAmount()).setScale(2) + "||" +	earnValues.getPartnerMerchantId() + "||" + 
						new BigDecimal(earnValues.getQuantity()).setScale(2) + "|AMOUNT|" + 
						earnValues.getOrderId() + "|" +	String.valueOf(itemRows) + "||||||||" + earnValues.getDepartmentCode() + "|" + 
						departmentName + "|||" + earnValues.getArticleId() + "|" + String.valueOf(itemRows) + 
						"|||||||||||||||||||||||||||||||";
				rowData += "\n";
				totalTxnPoints += earnValues.getTxnPoints();
				totalTxnValue += earnValues.getTxnValue();
			}
			else{
				itemRows++;
				rowData += "|" + new BigDecimal(earnValues.getItemAmount()).setScale(2) + "|" + 
						new BigDecimal(earnValues.getItemAmount()).setScale(2) + "||" +	earnValues.getPartnerMerchantId() + "||" + 
						new BigDecimal(earnValues.getQuantity()).setScale(2) + "|AMOUNT|" + 
						earnValues.getOrderId() + "|" +	String.valueOf(itemRows) + "||||||||" + earnValues.getDepartmentCode() + "|" + 
						departmentName + "|||" + earnValues.getArticleId() + "|" + String.valueOf(itemRows) + 
						"|||||||||||||||||||||||||||||||";
				rowData += "\n";
			}
		}
		if (earnList.size() > 0){
			rowData += "PB_ACT_1.1|" + currentTime + "|" + sequenceNumber + "|" + partnerMerchantId + "||" + headerRows + "|" + 
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
	
	public int getEarnPoints(BigDecimal amount, String clientName){
		try {
			Properties props = pointsUtil.getProperties("points.properties");
			BigDecimal earnRatio = new BigDecimal(props.getProperty(clientName.toUpperCase() + "_EARN_POINTS"));
			BigDecimal earnFactor = getEarnFactor(clientName, props);
			earnRatio = earnRatio.multiply(earnFactor);
			//BigDecimal bonusPoints = getBonusPoints(amount, clientName, props);
			BigDecimal txnPoints = (amount.multiply(earnRatio)).setScale(0, BigDecimal.ROUND_HALF_DOWN);
			return txnPoints.intValue();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	private BigDecimal getEarnFactor(String clientName, Properties props) {
		BigDecimal earnFactor = BigDecimal.ONE;
		String validTill = props.getProperty(clientName.toUpperCase() + "_EARN_FACTOR_VALIDITY");
		DateTime datetime = DateTime.now();
		if (pointsUtil.isValidDate(validTill)){
			String day = datetime.dayOfWeek().getAsText();
			String factorMap = props.getProperty(clientName.toUpperCase() + "_EARN_FACTOR");
			if (factorMap != null){
				StringTokenizer factorIterator = new StringTokenizer(factorMap, ",");
				earnFactor = new BigDecimal(factorIterator.nextToken());
				while(factorIterator.hasMoreTokens()){
					String dayFactorMap = factorIterator.nextToken();
					String[] dayFactorList = dayFactorMap.split(",");
					if (day.equals(dayFactorList[0]) && dayFactorList[1] != null && !dayFactorList[1].equals("")){
						earnFactor = earnFactor.multiply(new BigDecimal(dayFactorList[1]));
						break;
					}			
				}
			}
		}
		return earnFactor;
	}
	
	private BigDecimal getBonusPoints(BigDecimal amount, String clientName, Properties props) {
		BigDecimal bonusPoints = BigDecimal.ZERO;
		String validTill = props.getProperty(clientName.toUpperCase() + "_EARN_BONUS_VALIDITY");
		if (pointsUtil.isValidDate(validTill)){
			BigDecimal minAmount = new BigDecimal(props.getProperty(clientName.toUpperCase() + "_EARN_BONUS_MIN_AMOUNT"));
			if (amount.compareTo(minAmount) > 1){
				String extraPoints = props.getProperty(clientName.toUpperCase() + "_EARN_BONUS_POINTS");
				return new BigDecimal(extraPoints != null ? extraPoints : "0");
			}
		}
		return bonusPoints;
	}

	@Override
	public void saveEarnData(EarnActionCodesEnum txnActionCode, String merchantId, PointsItems pointsItems) {
		
	}

	@Override
	public void saveEarnReversalData(EarnActionCodesEnum txnActionCode, String merchantId, PointsItems pointsItems) {
		
		
	}

}
