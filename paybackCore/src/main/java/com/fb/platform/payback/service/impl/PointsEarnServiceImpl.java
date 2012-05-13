package com.fb.platform.payback.service.impl;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Iterator;
import java.util.Properties;

import org.joda.time.DateTime;

import com.fb.commons.PlatformException;
import com.fb.commons.util.SFTPConnector;
import com.fb.platform.payback.dao.PointsDao;
import com.fb.platform.payback.dao.PointsEarnDao;
import com.fb.platform.payback.dao.PointsRuleDao;
import com.fb.platform.payback.model.PointsHeader;
import com.fb.platform.payback.model.PointsItems;
import com.fb.platform.payback.rule.EarnPointsRuleEnum;
import com.fb.platform.payback.rule.PointsRule;
import com.fb.platform.payback.service.PointsEarnService;
import com.fb.platform.payback.service.PointsService;
import com.fb.platform.payback.to.EarnActionCodesEnum;
import com.fb.platform.payback.to.PointsRequest;
import com.fb.platform.payback.to.PointsResponseCodeEnum;
import com.fb.platform.payback.to.PointsTxnClassificationCodeEnum;
import com.fb.platform.payback.util.PointsUtil;

public class PointsEarnServiceImpl implements PointsEarnService{
	
	private PointsEarnDao pointsEarnDao;
	private PointsUtil pointsUtil;
	private PointsDao pointsDao;
	private PointsService pointsService;
	private PointsRuleDao pointsRuleDao;
	
	public void setPointsService(PointsService pointsService) {
		this.pointsService = pointsService;
	}

	public void setPointsUtil(PointsUtil pointsUtil){
		this.pointsUtil = pointsUtil;
	}

	public void setPointsEarnDao(PointsEarnDao pointsEarnDao) {
		this.pointsEarnDao = pointsEarnDao;
	}
	
	public void setPointsDao(PointsDao pointsDao) {
		this.pointsDao = pointsDao;
	}
	
	public void setPointsRuleDao(PointsRuleDao pointsRuleDao) {
		this.pointsRuleDao = pointsRuleDao;
	}
	
	@Override	
	public String postEarnData(EarnActionCodesEnum txnActionCode, String merchantId){
		String dataToUpload = "";
		try{
			Properties props = pointsUtil.getProperties("payback.properties");
			String sequenceNumber = pointsService.getSequenceNumber();
			String settlementDate = pointsUtil.getPreviousDayDate();
		
			// SFTP Connection
			SFTPConnector sftpConnector = new SFTPConnector();
			String sftpUsername = props.getProperty("sftpUsername");
			sftpConnector.setHost(props.getProperty("sftpHost"));
			sftpConnector.setUsername(sftpUsername);
			sftpConnector.setPassword(props.getProperty("sftpPassword"));
			if (!sftpConnector.isConnected()){
				// false determines that host key checking will be disabled
				sftpConnector.connect(false);
			}
			
			DateTime newSettlementDate = DateTime.now();
			dataToUpload = getEarnDataToUpload(txnActionCode.name(), settlementDate, newSettlementDate, merchantId, sequenceNumber);
			if (dataToUpload != null && !dataToUpload.equals("")){
				String fileName = txnActionCode.toString() + "_" + String.valueOf(merchantId) + 
						"_"+ pointsUtil.convertDateToFormat(settlementDate, "ddMMyyyy") + "_" + sequenceNumber + ".txt";
				String remoteDirectory = props.getProperty("sftpRemoteDirectory");
				sftpConnector.upload(dataToUpload, fileName, remoteDirectory);
				fileName = fileName.replace("txt", "chk");
				sftpConnector.upload("", fileName, remoteDirectory);
				//pointsDao.updateStatus(txnActionCode.name(), settlementDate, merchantId);
			}
			sftpConnector.closeConnection();			
			
		}catch (PlatformException pe) {
			pe.printStackTrace();
		}
		return dataToUpload;
		
	}

	private String getEarnDataToUpload(String txnActionCode, String settlementDate, DateTime currentTime, String partnerMerchantId, String sequenceNumber) {
		int headerRows = 0;
		int totalTxnPoints = 0;
		int totalTxnValue = 0;
		String rowData = "";
		int itemRows = 0;
		
		Collection<PointsHeader> earnHeaderList = pointsDao.loadPointsHeaderData(txnActionCode, settlementDate, partnerMerchantId);
		Iterator<PointsHeader> EarnIterator = earnHeaderList.iterator();
		while(EarnIterator.hasNext()){
			PointsHeader earnHeader = EarnIterator.next();
			headerRows++;
			Collection<PointsItems> pointsItems = pointsEarnDao.loadPointsItemData(earnHeader.getId());
			
			if (!pointsItems.isEmpty()){
				rowData += earnHeader.getLoyaltyCard() + "||||" + earnHeader.getPartnerMerchantId() + "||" +
						earnHeader.getPartnerTerminalId() + "||" + earnHeader.getOrderId() + "|||" +
						earnHeader.getTxnActionCode() + "|" + earnHeader.getTxnClassificationCode() + "|||" +
						earnHeader.getTxnPaymentType() + "|" + 
						pointsUtil.convertDateToFormat(earnHeader.getTxnDate(), "ddMMyyyy") + "|" + 
						pointsUtil.convertDateToFormat(earnHeader.getTxnTimestamp(), "hhmmss") + "|" + 
						new BigDecimal(earnHeader.getTxnValue()).setScale(2) + "||" + earnHeader.getMarketingCode() + "|" +
						earnHeader.getTxnPoints() + "|1|" + 
						pointsUtil.convertDateToFormat(earnHeader.getSettlementDate(), "ddMMyyyy")	+ "|||||||" + 
						new BigDecimal(earnHeader.getTxnValue()).setScale(2) + "|" + earnHeader.getBranchId() + 
						"||||||||||||||||||||||";
				rowData += "\n";
				
				Iterator<PointsItems> itemIterator = pointsItems.iterator();
				while(itemIterator.hasNext()){
					itemRows++;
					PointsItems pointsItem = itemIterator.next();
					String departmentName = pointsItem.getDepartmentName();
					if (departmentName.length() > 29){
						departmentName = departmentName.substring(0, 29);
					}
					rowData += "|" + pointsItem.getItemAmount().setScale(2) + "|" + 
							pointsItem.getItemAmount().setScale(2) + "||" +	earnHeader.getPartnerMerchantId() + "||" + 
							new BigDecimal(pointsItem.getQuantity()).setScale(2) + "|AMOUNT|" + 
							earnHeader.getOrderId() + "|" +	String.valueOf(itemRows) + "||||||||" + pointsItem.getDepartmentCode() + "|" + 
							departmentName + "|||" + pointsItem.getArticleId() + "|2" + 
							"|||||||||||||||||||||||||||||||";
					rowData += "\n";
				}
			}
			totalTxnPoints += earnHeader.getTxnPoints();
			totalTxnValue += earnHeader.getTxnValue();
		}
		if (rowData.length() > 0 && itemRows > 0){
			rowData += "PB_ACT_1.1|" + currentTime + "|" + sequenceNumber + "|" + partnerMerchantId + "||" + headerRows + "|" + 
							totalTxnValue + "|" + totalTxnPoints + "|||||||||||||||9|||||||||||||||||||||||||||||||";
			return rowData;
		}
		return null;
	}

	@Override
	public PointsResponseCodeEnum storeEarnPoints(PointsRequest request,
			PointsTxnClassificationCodeEnum actionCode) {
		
		PointsRule rule = null;
		if (actionCode.equals(PointsTxnClassificationCodeEnum.PREALLOC_EARN)){
			for (EarnPointsRuleEnum ruleName : EarnPointsRuleEnum.values()){
				rule = pointsRuleDao.loadEarnRule(ruleName);
				if (rule.isApplicable(request.getOrderRequest())){
					break;
				}
			}
		}
		return pointsService.doOperation(request, rule);
	}

}
