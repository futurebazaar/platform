package com.fb.platform.payback.service.impl;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Iterator;
import java.util.Properties;

import org.joda.time.DateTime;

import com.fb.commons.PlatformException;
import com.fb.commons.util.SFTPConnector;
import com.fb.platform.payback.dao.PointsDao;
import com.fb.platform.payback.dao.PointsRuleDao;
import com.fb.platform.payback.model.PointsHeader;
import com.fb.platform.payback.model.PointsItems;
import com.fb.platform.payback.rule.EarnPointsRuleEnum;
import com.fb.platform.payback.rule.PointsRule;
import com.fb.platform.payback.rule.PointsRuleConfigConstants;
import com.fb.platform.payback.service.PointsEarnService;
import com.fb.platform.payback.service.PointsService;
import com.fb.platform.payback.to.EarnActionCodesEnum;
import com.fb.platform.payback.to.OrderItemRequest;
import com.fb.platform.payback.to.OrderRequest;
import com.fb.platform.payback.to.PointsRequest;
import com.fb.platform.payback.to.PointsResponseCodeEnum;
import com.fb.platform.payback.to.PointsTxnClassificationCodeEnum;
import com.fb.platform.payback.util.PointsUtil;

public class PointsEarnServiceImpl implements PointsEarnService{
	
	private PointsUtil pointsUtil;
	private PointsDao pointsDao;
	private PointsRuleDao pointsRuleDao;
	private PointsService pointsService;
	
	public void setPointsService(PointsService pointsService) {
		this.pointsService = pointsService;
	}

	public void setPointsUtil(PointsUtil pointsUtil){
		this.pointsUtil = pointsUtil;
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
			Collection<PointsItems> pointsItems = pointsDao.loadPointsItemData(earnHeader.getId());
			if (!pointsItems.isEmpty()){
				headerRows++;
				totalTxnPoints += earnHeader.getTxnPoints();
				totalTxnValue += earnHeader.getTxnValue();
				rowData += earnHeader.getLoyaltyCard() + "||||" + earnHeader.getPartnerMerchantId() + "||" +
						earnHeader.getPartnerTerminalId() + "||" + earnHeader.getOrderId() + "|||" +
						earnHeader.getTxnActionCode() + "|" + earnHeader.getTxnClassificationCode() + "|||" +
						earnHeader.getTxnPaymentType() + "|" + 
						pointsUtil.convertDateToFormat(earnHeader.getTxnDate(), "ddMMyyyy") + "|" + 
						pointsUtil.convertDateToFormat(earnHeader.getTxnTimestamp(), "hhmmss") + "|" + 
						new BigDecimal(earnHeader.getTxnValue()).setScale(2) + "||" + earnHeader.getMarketingCode() + "|" +
						earnHeader.getTxnPoints() + "|1|" + //The no. 1 shows that header has items. 
						pointsUtil.convertDateToFormat(earnHeader.getSettlementDate(), "ddMMyyyy")	+ "|||||||" + 
						new BigDecimal(earnHeader.getTxnValue()).setScale(2) + "|" + earnHeader.getBranchId() + 
						"||||||||||||||||||||||";
				rowData += "\n";
				
				Iterator<PointsItems> itemIterator = pointsItems.iterator();
				while(itemIterator.hasNext()){
					itemRows++;
					PointsItems pointsItem = itemIterator.next();
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
		if (rowData.length() > 0 && itemRows > 0){
			rowData += "PB_ACT_1.1|" + currentTime + "|" + sequenceNumber + "|" + partnerMerchantId + "||" + headerRows + "|" + 
							totalTxnValue + "|" + totalTxnPoints + "|||||||||||||||9|||||||||||||||||||||||||||||||"; //The no 9 shows that it is the end of file
			return rowData;
		}
		return null;
	}

	@Override
	public PointsResponseCodeEnum storeEarnPoints(PointsRequest request,
			PointsTxnClassificationCodeEnum actionCode) {
		
		if (!pointsUtil.isValidLoyaltyCard(request.getOrderRequest().getLoyaltyCard())){
			return PointsResponseCodeEnum.INVALID_CARD_NO;
		}
		
		if (actionCode.equals(PointsTxnClassificationCodeEnum.PREALLOC_EARN)){
			return savePreallocEarnPoints(request);
		}
		else if (actionCode.equals(PointsTxnClassificationCodeEnum.EARN_REVERSAL)){
			return saveEarnReversalPoints(request);
		}
		return PointsResponseCodeEnum.INVALID_REQUEST;
	}

	private PointsResponseCodeEnum saveEarnReversalPoints(PointsRequest request) {
		PointsTxnClassificationCodeEnum actionCode = PointsTxnClassificationCodeEnum.PREALLOC_EARN;
		String classificationCode = actionCode.toString().split(",")[0];
		PointsHeader pointsHeader = pointsDao.getHeaderDetails(request.getOrderRequest().getOrderId(), actionCode.name(), classificationCode);
		Collection<PointsItems> pointsItems = pointsDao.loadPointsItemData(pointsHeader.getId());
		for (OrderItemRequest itemRequest : request.getOrderRequest().getOrderItemRequest()){
			Iterator<PointsItems> itemIterator = pointsItems.iterator();
			while (itemIterator.hasNext()){
				PointsItems pointsItem = itemIterator.next();
				if (pointsItem.getItemId() == itemRequest.getId()){
					if (pointsItem.getEarnRatio().compareTo(BigDecimal.ZERO) == 1){
						itemRequest.setTxnPoints(pointsItem.getEarnRatio().multiply(itemRequest.getAmount()));
						itemRequest.setEarnRatio(pointsItem.getEarnRatio());
						continue;
					}
					return PointsResponseCodeEnum.FAILURE;
				}
			}
		}
		// Check for Bonus Points
		PointsHeader bonusPointsHeader = pointsDao.getHeaderDetails(request.getOrderRequest().getOrderId(), 
				actionCode.name(), PointsRuleConfigConstants.BONUS_POINTS);
		if (bonusPointsHeader != null){
			EarnPointsRuleEnum ruleName = EarnPointsRuleEnum.BUY_WORTH_X_EARN_Y_BONUS_POINTS;
			PointsRule rule = loadEarnRule(ruleName);
			// Reverse Bonus Points only when rule is not applicable.
			if (!rule.isApplicable(request.getOrderRequest(), null)){
				request.getOrderRequest().setBonusPoints(new BigDecimal(bonusPointsHeader.getTxnPoints()));
			}
		}
		return pointsService.doOperation(request);
	}

	private PointsResponseCodeEnum savePreallocEarnPoints(PointsRequest request) {
		
		PointsRule rule = null;
		boolean allowNext = true;
		
		//need to check for the DOD. as to it has to be implemented.
		/*
		 * 1) change the value every day in the DB 
		 * 2) Hit the DB get the DOD and set in cache
		 */
		
		for (EarnPointsRuleEnum ruleName : EarnPointsRuleEnum.values()){
			rule = loadEarnRule(ruleName);
			OrderRequest orderRequest = request.getOrderRequest();
			if (rule != null){
				for (OrderItemRequest itemRequest : orderRequest.getOrderItemRequest()){
					if (rule.isApplicable(orderRequest, itemRequest)){
						BigDecimal points = rule.execute(orderRequest, itemRequest);
						if (points.compareTo(itemRequest.getTxnPoints()) == 0){
							itemRequest.setTxnPoints(points);
							itemRequest.setEarnRatio(itemRequest.getAmount().divide(points));
						}
					}
				}
				if (!rule.allowNext()){
					break;
				}
			}
		}
		return pointsService.doOperation(request);
		
	}
	
	private PointsRule loadEarnRule(EarnPointsRuleEnum ruleName) {
		return pointsRuleDao.loadEarnRule(ruleName);
	}

}