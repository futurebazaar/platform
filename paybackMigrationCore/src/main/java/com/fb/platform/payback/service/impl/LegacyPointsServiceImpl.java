package com.fb.platform.payback.service.impl;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Iterator;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;

import com.fb.commons.PlatformException;
import com.fb.commons.util.SFTPConnector;
import com.fb.platform.payback.dao.LegacyPointsDao;
import com.fb.platform.payback.model.LegacyPointsHeader;
import com.fb.platform.payback.model.LegacyPointsItems;
import com.fb.platform.payback.service.BurnActionCodesEnum;
import com.fb.platform.payback.service.EarnActionCodesEnum;
import com.fb.platform.payback.service.LegacyPointsService;
import com.fb.platform.payback.service.PointsTxnClassificationCodeEnum;
import com.fb.platform.payback.util.LegacyPointsUtil;
import com.jcraft.jsch.SftpException;

public class LegacyPointsServiceImpl implements LegacyPointsService{
	
	private static Log logger = LogFactory.getLog(LegacyPointsServiceImpl.class);
	
	private static int ROUND =BigDecimal.ROUND_HALF_DOWN; 
	
	private LegacyPointsDao legacyPointsDao;
	private LegacyPointsUtil legacyPointsUtil;
	
	public void setLegacyPointsDao(LegacyPointsDao legacyPointsDao) {
		this.legacyPointsDao = legacyPointsDao;
	}
	public void setLegacyPointsUtil(LegacyPointsUtil pointsUtil){
		this.legacyPointsUtil = pointsUtil;
	}
	
	@Override
	public void migratePaybackData(){
		Collection<LegacyPointsHeader> pointsHeaders = legacyPointsDao.loadPointsHeaderData();
		Iterator<LegacyPointsHeader> pointsHeaderIterator = pointsHeaders.iterator();
		while (pointsHeaderIterator.hasNext()){
			LegacyPointsHeader pointsHeader = pointsHeaderIterator.next();
			try{
				System.out.println(" LegayHeaderId : "  + pointsHeader.getId());
				//long generatedHeaderId = legacyPointsDao.insertPointsHeaderData(pointsHeader);
				//System.out.println(" LegayHeaderId : "  + pointsHeader.getId() + " ############ Inserted Header: " + generatedHeaderId );
				/*if (pointsHeader.getTxnClassificationCode().equals("BONUS_POINTS")){
					continue;
				}
				Collection<LegacyPointsItems> pointsItems = legacyPointsDao.loadPointsItemData(pointsHeader.getId());
				Iterator<LegacyPointsItems> pointsItemIterator = pointsItems.iterator();
				while (pointsItemIterator.hasNext()){
					LegacyPointsItems pointsItem = pointsItemIterator.next();
					pointsItem.setEarnRatio(pointsHeader.getEarnRatio());
					pointsItem.setBurnRatio(pointsHeader.getBurnRatio());
					pointsItem.setPointsHeaderId(generatedHeaderId);
					BigDecimal txnPoints = pointsItem.getItemAmount().multiply(pointsItem.getEarnRatio());
					pointsItem.setTxnPoints(txnPoints.setScale(0, ROUND));
					System.out.println(" Llegacy Points id  : " + pointsItem.getItemId() + "");
					legacyPointsDao.insertPointsItemsData(pointsItem);
				}*/
			} catch (Exception e){
				System.out.println(e.getMessage());
			}
		}
	}
	
	@Override
	public String mailBurnData(BurnActionCodesEnum txnActionCode, String merchantId) {
		String settlementDate = legacyPointsUtil.getPreviousDayDate();
		String fileName = "Burn Reversal_" + settlementDate +  ".csv";
		String header = "Transaction ID, Transaction Date, Merchant ID, Points, Reason \n";
		String fileBody = "";
		Collection<LegacyPointsHeader> burnList = legacyPointsDao.loadPointsHeaderData(txnActionCode.name(), settlementDate, merchantId);
		Iterator<LegacyPointsHeader> burnIterator = burnList.iterator();
		while(burnIterator.hasNext()){
			LegacyPointsHeader burnValues = burnIterator.next();
			String transactionID = burnValues.getReferenceId();
			String transactionDate = legacyPointsUtil.convertDateToFormat(burnValues.getTxnDate(), "yyyy-MM-dd");
			String points = String.valueOf(burnValues.getTxnPoints());
			String reason = burnValues.getReason();
			
			fileBody += transactionID + ", " + transactionDate + ", " + merchantId + ", " +
					points + ", " + reason + "\n";
		}
		
		if (fileBody != null && !fileBody.equals("")){
			fileBody = header + fileBody;
			logger.info("Updating Status for " + txnActionCode.name());
			//legacyPointsDao.updateStatus(txnActionCode.name(), settlementDate, merchantId);
			legacyPointsUtil.sendMail(txnActionCode.name(), merchantId, fileName, fileBody, "POINTS");
		}	
		return fileBody;
	}

	@Override	
	public String postEarnData(EarnActionCodesEnum txnActionCode, String merchantId, String client) throws PlatformException{
		String dataToUpload = "";
		try{
			Properties props = legacyPointsUtil.getProperties("payback.properties");
			String sequenceNumber = legacyPointsUtil.getSequenceNumber();
			String settlementDate = legacyPointsUtil.getPreviousDayDate();
			DateTime newSettlementDate = DateTime.now();
			dataToUpload = getEarnDataToUpload(txnActionCode.name(), settlementDate, newSettlementDate, merchantId, sequenceNumber);
			if (dataToUpload != null && !dataToUpload.equals("")){
				
				// SFTP Connection
				SFTPConnector sftpConnector = new SFTPConnector();
				sftpConnector.setHost(props.getProperty("sftpHost"));

				String sftpUsername = props.getProperty(client + "_sftpUsername");
				sftpConnector.setUsername(sftpUsername);
				sftpConnector.setPassword(props.getProperty(client + "_sftpPassword"));
				if (!sftpConnector.isConnected()){
					// false determines that host key checking will be disabled
					sftpConnector.connect(false, 50000);
					logger.info("SFTP Connected Username: " + sftpUsername);
				}
				
				String fileName = txnActionCode.toString() + "_" + String.valueOf(merchantId) + 
						"_"+ legacyPointsUtil.convertDateToFormat(settlementDate, "ddMMyyyy") + "_" + sequenceNumber + ".txt";
				String remoteDirectory = props.getProperty("sftpRemoteDirectory");
				sftpConnector.upload(dataToUpload, fileName, remoteDirectory);
				logger.info("File Upload Task Done fileName: " + fileName);
				fileName = fileName.replace("txt", "chk");
				sftpConnector.upload("", fileName, remoteDirectory);
				sftpConnector.upload(dataToUpload, fileName, remoteDirectory);
				//legacyPointsDao.updateStatus(txnActionCode.name(), settlementDate, merchantId);
			
				sftpConnector.closeConnection();
				legacyPointsUtil.sendMail(txnActionCode.name(), merchantId, fileName, dataToUpload, "POINTS");
			}			
			
		}catch (SftpException se) {
			logger.error("SFTP Connection Failed :" + se.toString());
			throw new PlatformException(" SFTP connection Failed");
		} catch (UnsupportedEncodingException ue) {
			logger.error("UnsupportedEncodingException :" + ue.toString());
			throw new PlatformException("Encoding not suppported");
		}
		return dataToUpload;
		
	}

	private String getEarnDataToUpload(String txnActionCode, String settlementDate, DateTime currentTime, String partnerMerchantId, String sequenceNumber) {
		int headerRows = 0;
		int totalTxnPoints = 0;
		int totalTxnValue = 0;
		String rowData = "";
		int itemRows = 0;
		
		Collection<LegacyPointsHeader> earnHeaderList = legacyPointsDao.loadPointsHeaderData(txnActionCode, settlementDate, partnerMerchantId);
		Iterator<LegacyPointsHeader> EarnIterator = earnHeaderList.iterator();
		while(EarnIterator.hasNext()){
			LegacyPointsHeader earnHeader = EarnIterator.next();
			Collection<LegacyPointsItems> pointsItems = legacyPointsDao.loadPointsItemData(earnHeader.getId());
			if (!pointsItems.isEmpty() || earnHeader.hasSKUItems() == 0){
				headerRows++;
				totalTxnPoints += earnHeader.getTxnPoints();
				totalTxnValue += earnHeader.getTxnValue();
				rowData += earnHeader.getLoyaltyCard() + "||||" + earnHeader.getPartnerMerchantId() + "||" +
						earnHeader.getPartnerTerminalId() + "||" + earnHeader.getOrderId() + "|||" +
						earnHeader.getTxnActionCode() + "|" + earnHeader.getTxnClassificationCode() + "|||" +
						earnHeader.getTxnPaymentType().trim() + "|" + 
						legacyPointsUtil.convertDateToFormat(earnHeader.getTxnDate(), "ddMMyyyy") + "|" + 
						legacyPointsUtil.convertDateToFormat(earnHeader.getTxnTimestamp(), "hhmmss") + "|" + 
						new BigDecimal(earnHeader.getTxnValue()).setScale(2) + "||" + earnHeader.getMarketingCode() + "|" +
						earnHeader.getTxnPoints() + "|" + earnHeader.hasSKUItems() + "|" + 
						legacyPointsUtil.convertDateToFormat(earnHeader.getSettlementDate(), "ddMMyyyy")	+ "|||||||" + 
						new BigDecimal(earnHeader.getTxnValue()).setScale(2) + "|" + earnHeader.getBranchId() + 
						"||||||||||||||||||||||";
				rowData += "\n";
				
				Iterator<LegacyPointsItems> itemIterator = pointsItems.iterator();
				while(itemIterator.hasNext() && earnHeader.hasSKUItems() > 0){
					itemRows++;
					LegacyPointsItems pointsItem = itemIterator.next();
					String departmentName = pointsItem.getDepartmentName();
					//Check for length as it is required to send only 30 characters
					if (departmentName.length() > 29){
						departmentName = departmentName.substring(0, 29);
					}
					rowData += "|" + pointsItem.getItemAmount().setScale(2) + "|" + 
							pointsItem.getItemAmount().setScale(2) + "||" +	earnHeader.getPartnerMerchantId() + "||" + 
							new BigDecimal(pointsItem.getQuantity()).setScale(2) + "|AMOUNT|" + 
							earnHeader.getOrderId() + "|" +	String.valueOf(itemRows) + "||||||||" + pointsItem.getDepartmentCode() + "|" + 
							departmentName + "|||" + pointsItem.getArticleId() + "|2|" + //This no shows that is an item level quantity 
							"||||||||||||||||||||||||||||||";
					rowData += "\n";
				}
			}
		}
		if (rowData.length() > 0){
			rowData += "PB_ACT_1.1|" + currentTime + "|" + sequenceNumber + "|" + partnerMerchantId + "||" + headerRows + "|" + 
							totalTxnValue + "|" + totalTxnPoints + "|||||||||||||||9|||||||||||||||||||||||||||||||"; //The no 9 shows that it is the end of file
		}
		return rowData;
	}
	
}
