package com.fb.platform.payback.service.impl;

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
import com.fb.platform.payback.model.OrderDetail;
import com.fb.platform.payback.model.PointsHeader;
import com.fb.platform.payback.model.PointsItems;
import com.fb.platform.payback.service.PointsEarnService;
import com.fb.platform.payback.service.PointsService;
import com.fb.platform.payback.to.EarnActionCodesEnum;
import com.fb.platform.payback.to.PointsTxnClassificationCodeEnum;
import com.fb.platform.payback.to.StorePointsHeaderRequest;
import com.fb.platform.payback.to.StorePointsItemRequest;
import com.fb.platform.payback.util.PointsUtil;

public class PointsEarnServiceImpl implements PointsEarnService{
	
	private PointsEarnDao pointsEarnDao;
	private PointsUtil pointsUtil;
	private PointsDao pointsDao;
	private PointsService pointsService;
	
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
	
	@Override	
	public String postEarnData(EarnActionCodesEnum txnActionCode, String merchantId){
		String dataToUpload = "";
		try{
			Properties props = pointsUtil.getProperties("payback.properties");
			String sequenceNumber = pointsService.getSequenceNumber();
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
			
			DateTime newSettlementDate = DateTime.now();
			dataToUpload = getEarnDataToUpload(txnActionCode.name(), settlementDate, newSettlementDate, merchantId, sequenceNumber);
			if (dataToUpload != null && !dataToUpload.equals("")){
				String fileName = txnActionCode.toString() + "_" + String.valueOf(merchantId) + 
						"_"+ pointsUtil.convertDateToFormat(settlementDate, "ddMMyyyy") + "_" + sequenceNumber + ".txt";
				String remoteDirectory = "/" + sftpUsername + "/" + props.getProperty("sftpRemoteDirectory");
				sftpConnector.upload(dataToUpload, fileName, remoteDirectory);
				fileName = fileName.replace("txt", "chk");
				sftpConnector.upload("", fileName, remoteDirectory);
				pointsDao.updateStatus(txnActionCode.name(), settlementDate, merchantId);
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
						earnHeader.getTxnPoints() + "|" + String.valueOf(itemRows) + "|" + 
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
							departmentName + "|||" + pointsItem.getArticleId() + "|" + String.valueOf(itemRows) + 
							"|||||||||||||||||||||||||||||||";
					rowData += "\n";
				}
			}
			totalTxnPoints += earnHeader.getTxnPoints();
			totalTxnValue += earnHeader.getTxnValue();
		}
		if (rowData.length() > 0){
			rowData += "PB_ACT_1.1|" + currentTime + "|" + sequenceNumber + "|" + partnerMerchantId + "||" + headerRows + "|" + 
							totalTxnValue + "|" + totalTxnPoints + "|||||||||||||||9|||||||||||||||||||||||||||||||";
		}
		return rowData;
	}

	@Override
	public void saveEarnData(EarnActionCodesEnum txnActionCode, StorePointsHeaderRequest request) throws IOException {
		Properties props = pointsUtil.getProperties("points.properties");
		OrderDetail orderDetail = pointsDao.getOrderDetail(request.getOrderId());
		
		PointsHeader pointsHeader = new PointsHeader();
		pointsHeader.setReason(request.getReason());
		pointsHeader.setTxnActionCode(txnActionCode.name());
		pointsHeader.setOrderId(request.getOrderId());
		pointsHeader.setLoyaltyCard(request.getLoyaltyCard());
		pointsHeader.setSettlementDate(DateTime.now());
		
		pointsHeader.setOrderDetails(orderDetail, props);
		pointsHeader.setTxnDate(orderDetail.getTimestamp());
		pointsHeader.setTxnTimestamp(orderDetail.getTimestamp());
		pointsHeader.setReferenceId(orderDetail.getReferenceOrderId());
		
		PointsTxnClassificationCodeEnum classificationCodeEnum = PointsTxnClassificationCodeEnum.valueOf(txnActionCode.name());
		String classificationCode = classificationCodeEnum.toString().split(",")[0];
		String paymentType = classificationCodeEnum.toString().split(",")[1];
		pointsHeader.setTxnClassificationCode(classificationCode);
		pointsHeader.setTxnPaymentType(paymentType);
		
		//Day will check for Earn Ratio and Bonus Points
		String day = pointsHeader.getTxnDate().dayOfWeek().getAsText();
		String clientName = orderDetail.getClientName().toUpperCase().replaceAll(" ","");
		pointsHeader.setEarnRatio(pointsService.getEarnRatio(day, props, clientName,
				request.getOrderId(), txnActionCode));
		pointsHeader.setBurnRatio(pointsService.getBurnRatio(day, props, clientName));
		pointsHeader.setTxnPoints(pointsService.getTxnPoints(request.getAmount(), day, props, 
				clientName, txnActionCode.name(), request.getOrderId()));
		pointsHeader.setTxnValue(request.getAmount().intValue());
		long pointsHeaderId = pointsDao.insertPointsHeaderData(pointsHeader);
		
		//store Points Item
		storePointsItem(request, pointsHeaderId, txnActionCode.name());
		
		//check and save Bonus Points
		storeBonusPoints(pointsHeader, request.getAmount(), day, props, clientName);
	}

	private void storePointsItem(StorePointsHeaderRequest request, long pointsHeaderId, String txnActionCode) {
		for (StorePointsItemRequest storePointsItem : request.getStorePointsItemRequest()){
			PointsItems pointsItem = new PointsItems();
			pointsItem.setPointsHeaderId(pointsHeaderId);
			pointsItem.setArticleId(storePointsItem.getArticleId());
			pointsItem.setTxnActionCode(txnActionCode);
			pointsItem.setItemAmount(storePointsItem.getAmount());
			pointsItem.setItemId(storePointsItem.getId());
			pointsItem.setQuantity(storePointsItem.getQuantity());
			pointsItem.setDepartmentCode(storePointsItem.getDepartmentCode());
			pointsItem.setDepartmentName(storePointsItem.getDepartmentName());
			pointsEarnDao.insertPointsItemsData(pointsItem);
		}
		
	}
	
	private void storeBonusPoints(PointsHeader pointsHeader, BigDecimal amount, String day, Properties props, 
			String clientName) {
		
		int burnPoints = pointsService.getBonusPoints(amount, day, props, clientName, pointsHeader.getOrderId(), 
				pointsHeader.getTxnActionCode());
		if (burnPoints > 0){
			pointsHeader.setTxnPoints(burnPoints);
			pointsHeader.setTxnPaymentType("NO_PAYMENT");
			pointsHeader.setTxnValue(0);
			pointsHeader.setTxnClassificationCode("BONUS_POINTS");
			long pointsHeaderId = pointsDao.insertPointsHeaderData(pointsHeader);
			
			PointsItems pointsItem = new PointsItems();
			pointsItem.setPointsHeaderId(pointsHeaderId);
			
			String articleId = props.getProperty("PAYBACK_BONUS_ARTICLE_ID");
			String itemId = props.getProperty("PAYBACK_BONUS_ITEM_ID");
			String departmentCode = props.getProperty("PAYBACK_BONUS_DEPARTMENT_CODE");
			String departmentName = props.getProperty("PAYBACK_BONUS_DEPARTMENT_NAME");
			pointsItem.setArticleId(Long.parseLong(articleId == null ? "1111" : articleId));
			pointsItem.setItemId(Long.parseLong(itemId == null ? "80000" : itemId));
			pointsItem.setDepartmentCode(Long.parseLong(departmentCode == null ? "100" : departmentCode));
			pointsItem.setDepartmentName(departmentName == null ? "BONUS" : departmentName);
			pointsItem.setTxnActionCode(pointsHeader.getTxnActionCode());
			pointsItem.setItemAmount(BigDecimal.ZERO);
			pointsItem.setQuantity(0);
			
			pointsEarnDao.insertPointsItemsData(pointsItem);
			
			
			
		}
		
		
		
		
	}

}
