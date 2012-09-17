package com.fb.platform.payback.service.impl;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Iterator;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.springframework.dao.DataAccessException;

import com.fb.commons.PlatformException;
import com.fb.commons.util.SFTPConnector;
import com.fb.platform.payback.cache.RuleCacheAccess;
import com.fb.platform.payback.dao.PointsDao;
import com.fb.platform.payback.dao.PointsRuleDao;
import com.fb.platform.payback.exception.PointsHeaderDoesNotExist;
import com.fb.platform.payback.model.PointsHeader;
import com.fb.platform.payback.model.PointsItems;
import com.fb.platform.payback.model.RollbackHeader;
import com.fb.platform.payback.rule.BurnPointsRuleEnum;
import com.fb.platform.payback.rule.EarnPointsRuleEnum;
import com.fb.platform.payback.rule.PointsRule;
import com.fb.platform.payback.rule.PointsRuleConfigConstants;
import com.fb.platform.payback.rule.impl.PurchaseOrderBurnXPoints;
import com.fb.platform.payback.service.PointsService;
import com.fb.platform.payback.to.BurnActionCodesEnum;
import com.fb.platform.payback.to.ClassificationCodesEnum;
import com.fb.platform.payback.to.EarnActionCodesEnum;
import com.fb.platform.payback.to.OrderItemRequest;
import com.fb.platform.payback.to.OrderRequest;
import com.fb.platform.payback.to.PointsRequest;
import com.fb.platform.payback.to.PointsResponseCodeEnum;
import com.fb.platform.payback.to.PointsTxnClassificationCodeEnum;
import com.fb.platform.payback.util.PointsUtil;
import com.jcraft.jsch.SftpException;

public class PointsServiceImpl implements PointsService {

	private static Log logger = LogFactory.getLog(PointsServiceImpl.class);

	private static int ROUND = BigDecimal.ROUND_HALF_DOWN;

	private PointsDao pointsDao;
	private PointsRuleDao pointsRuleDao;
	private RuleCacheAccess ruleCacheAccess;
	private PointsUtil pointsUtil;

	public void setPointsDao(PointsDao pointsDao) {
		this.pointsDao = pointsDao;
	}

	public void setPointsRuleDao(PointsRuleDao pointsRuleDao) {
		this.pointsRuleDao = pointsRuleDao;
	}

	public void setPointsUtil(PointsUtil pointsUtil) {
		this.pointsUtil = pointsUtil;
	}

	public void setRuleCacheAccess(RuleCacheAccess ruleCacheAccess) {
		this.ruleCacheAccess = ruleCacheAccess;
	}

	@Override
	public PointsResponseCodeEnum storePoints(PointsRequest request) {

		PointsTxnClassificationCodeEnum actionCode = PointsTxnClassificationCodeEnum
				.valueOf(request.getTxnActionCode());

		if (actionCode.equals(PointsTxnClassificationCodeEnum.BURN_REVERSAL)) {
			return saveBurnReversalPoints(request);
		}

		if (request.getOrderRequest().getLoyaltyCard() == null
				|| !pointsUtil.isValidLoyaltyCard(request.getOrderRequest()
						.getLoyaltyCard())) {
			return PointsResponseCodeEnum.INVALID_CARD_NO;
		}

		if (actionCode.equals(PointsTxnClassificationCodeEnum.PREALLOC_EARN)) {
			return savePreallocEarnPoints(request);
		} else if (actionCode
				.equals(PointsTxnClassificationCodeEnum.EARN_REVERSAL)) {
			return saveEarnReversalPoints(request);
		}

		return PointsResponseCodeEnum.INVALID_REQUEST;
	}

	@Override
	public PointsRequest getPointsToBeDisplayed(PointsRequest request) {
		PointsTxnClassificationCodeEnum actionCode = PointsTxnClassificationCodeEnum
				.valueOf(request.getTxnActionCode());
		switch (actionCode) {
		case PREALLOC_EARN:
		case EARN_REVERSAL:
			setEarnPoints(request.getOrderRequest());
			for (OrderItemRequest itemRequest : request.getOrderRequest()
					.getOrderItemRequest()) {
				itemRequest.setTxnPoints(itemRequest.getTxnPoints().setScale(0,
						ROUND));
			}
			request.getOrderRequest().setTxnPoints(
					getTxnPoints(request).setScale(0, ROUND));
			break;
		case BURN_REVERSAL:
			setBurnPoints(request.getOrderRequest());
			request.getOrderRequest().setTxnPoints(
					getTxnPoints(request).setScale(0, ROUND));
			break;
		default:

		}
		request.getOrderRequest().setPointsValue(purchasablePointsValue(request).setScale(0, ROUND));
		return request;
	}
	
	private BigDecimal purchasablePointsValue(PointsRequest request){
		PointsRule rule = null;
		rule = loadBurnRule(BurnPointsRuleEnum.PURCHASE_ORDER_BURN_X_POINTS);
		BigDecimal burnRatio = ((PurchaseOrderBurnXPoints)rule).getBurnRatio();
		return request.getOrderRequest().getTotalTxnPoints().divide(burnRatio);
	}

	@Override
	public PointsResponseCodeEnum clearPointsCache(String ruleName) {
		try {
			boolean cleared = ruleCacheAccess.clear(ruleName);
			if (cleared) {
				logger.info("Cache for rule :" + ruleName + " cleared");
				return PointsResponseCodeEnum.SUCCESS;
			}
			return PointsResponseCodeEnum.FAILURE;
		} catch (Exception e) {
			e.printStackTrace();
			return PointsResponseCodeEnum.INTERNAL_ERROR;
		}
	}

	private PointsResponseCodeEnum savePreallocEarnPoints(PointsRequest request) {
		boolean alreadySaved = false;
		try {
			PointsTxnClassificationCodeEnum actionCode = PointsTxnClassificationCodeEnum.PREALLOC_EARN;
			String classificationCode = actionCode.toString().split(",")[0];
			PointsHeader pointsHeader = pointsDao.getHeaderDetails(request .getOrderRequest().getOrderId(), actionCode.name(), classificationCode);
			logger.info("Header Entry Already exists :" + pointsHeader.getId() + " for order id : " + request.getOrderRequest().getOrderId());
			alreadySaved = true;
		} catch (DataAccessException e) {
			logger.info("Save Earn for order id : " + request.getOrderRequest().getOrderId());
		}
		if (!alreadySaved){
			setEarnPoints(request.getOrderRequest());
			request.getOrderRequest().setTxnPoints(
					getTxnPoints(request).setScale(0, ROUND));
		}
		return doOperation(request);
	}

	private PointsResponseCodeEnum saveBurnReversalPoints(PointsRequest request) {
		setBurnPoints(request.getOrderRequest());
		return doOperation(request);
	}

	private void setEarnPoints(OrderRequest orderRequest) {
		PointsRule rule = null;

		for (EarnPointsRuleEnum ruleName : EarnPointsRuleEnum.values()) {
			boolean applied = false;
			rule = loadEarnRule(ruleName);
			if (rule != null) {
				for (OrderItemRequest itemRequest : orderRequest
						.getOrderItemRequest()) {
					logger.info("Checking rule : " + ruleName
							+ " for order id : " + orderRequest.getOrderId());
					if (rule.isApplicable(orderRequest, itemRequest)) {
						BigDecimal points = rule.execute(orderRequest,
								itemRequest);
						logger.info("Rule : " + ruleName
								+ " applicable for  item amount "
								+ itemRequest.getAmount() + " . Txn Points = "
								+ points + ". Bonus Points = " + orderRequest.getBonusPoints());
						if (points.compareTo(itemRequest.getTxnPoints()) > 0) {
							itemRequest.setTxnPoints(points);
							itemRequest.setEarnRatio(points.divide(itemRequest
									.getAmount()));
						}
						applied = true;
					}
				}
				if (!rule.allowNext(orderRequest, null) && applied) {
					break;
				}
			}
		}
	}

	private void setBurnPoints(OrderRequest orderRequest) {
		PointsRule rule = null;
		for (BurnPointsRuleEnum ruleName : BurnPointsRuleEnum.values()) {
			rule = loadBurnRule(ruleName);
			if (rule != null) {
				if (rule.isApplicable(orderRequest, null)) {
					orderRequest.setTxnPoints(rule.execute(orderRequest, null));
				}
			}
			if (!rule.allowNext(orderRequest, null)) {
				break;
			}
		}
	}

	private PointsResponseCodeEnum doOperation(PointsRequest request) {
		String clientName = request.getClientName();
		OrderRequest orderRequest = request.getOrderRequest();
		BigDecimal txnPoints = getTxnPoints(request);
		PointsTxnClassificationCodeEnum actionCode = PointsTxnClassificationCodeEnum
				.valueOf(request.getTxnActionCode());
		String classificationCode = actionCode.toString().split(",")[0];
		String paymentType = actionCode.toString().split(",")[1];
		BigDecimal bonusPoints = orderRequest.getBonusPoints();
		if (txnPoints.compareTo(BigDecimal.ZERO) == 1) {
			long headerId = savePoints(orderRequest, txnPoints, actionCode, classificationCode,
					paymentType, clientName);
			request.getOrderRequest().setPointsHeaderId(headerId);
			if (bonusPoints.compareTo(BigDecimal.ZERO) == 1) {
				logger.info("Bonus Points Exist for order id: "
						+ orderRequest.getOrderId());
				savePoints(orderRequest, bonusPoints, actionCode,
						PointsRuleConfigConstants.BONUS_POINTS, paymentType,
						clientName);
			}
			return PointsResponseCodeEnum.SUCCESS;
		}
		return PointsResponseCodeEnum.INVALID_POINTS;
	}

	private BigDecimal getTxnPoints(PointsRequest request) {
		PointsTxnClassificationCodeEnum actionCode = PointsTxnClassificationCodeEnum
				.valueOf(request.getTxnActionCode());
		BigDecimal totalTxnPoint = BigDecimal.ZERO;
		switch (actionCode) {
		case PREALLOC_EARN:
		case EARN_REVERSAL:
			for (OrderItemRequest itemRequest : request.getOrderRequest()
					.getOrderItemRequest()) {
				totalTxnPoint = totalTxnPoint.add(itemRequest.getTxnPoints());
			}
			break;
		case BURN_REVERSAL:
			totalTxnPoint = request.getOrderRequest().getTxnPoints();
			break;
		}
		return totalTxnPoint;
	}

	private long savePoints(OrderRequest orderRequest, BigDecimal txnPoints,
			PointsTxnClassificationCodeEnum actionCode,
			String classificationCode, String paymentType, String client) {
		Properties props = pointsUtil.getProperties("payback.properties");
		String partnerMerchantId = props.getProperty(client + "_MERCHANT_ID");
		String partnerTerminalId = props.getProperty(client + "_TERMINAL_ID");

		PointsHeader pointsHeader = new PointsHeader();
		pointsHeader.setPartnerMerchantId(partnerMerchantId);
		pointsHeader.setPartnerTerminalId(partnerTerminalId);
		pointsHeader.setTxnClassificationCode(classificationCode);
		pointsHeader.setTxnPaymentType(paymentType);
		pointsHeader.setTxnActionCode(actionCode.name());
		pointsHeader.setTxnPoints(txnPoints.setScale(0, ROUND).intValue());
		pointsHeader.setTxnValue(orderRequest.getAmount().setScale(0, ROUND)
				.intValue());
		if (classificationCode.equals(ClassificationCodesEnum.BONUS_POINTS)) {
			pointsHeader.setTxnValue(0);
		}
		pointsHeader.setTxnValue(orderRequest.getAmount().setScale(0, ROUND)
				.intValue());
		pointsHeader.setReferenceId(orderRequest.getReferenceId());
		pointsHeader.setLoyaltyCard(orderRequest.getLoyaltyCard());
		pointsHeader.setReason(orderRequest.getReason());
		pointsHeader.setSettlementDate(DateTime.now());
		pointsHeader.setTxnDate(orderRequest.getTxnTimestamp());
		pointsHeader.setTxnTimestamp(orderRequest.getTxnTimestamp());
		pointsHeader.setOrderId(orderRequest.getOrderId());

		long headerId = pointsDao.insertPointsHeaderData(pointsHeader);
		if (orderRequest.getOrderItemRequest() != null
				&& !orderRequest.getOrderItemRequest().isEmpty()
				&& pointsHeader.hasSKUItems() > 0) {
			savePointsItems(orderRequest, headerId);
		}
		
		return headerId;
	}

	private void savePointsItems(OrderRequest orderRequest, long headerId) {
		for (OrderItemRequest itemRequest : orderRequest.getOrderItemRequest()) {
			int txnPoints = itemRequest.getTxnPoints().setScale(0, ROUND)
					.intValue();
			pointsDao.insertPointsItemsData(itemRequest, headerId, txnPoints);
		}

	}

	private PointsRule loadEarnRule(EarnPointsRuleEnum ruleName) {
		PointsRule rule = ruleCacheAccess.get(ruleName.name());
		if (rule == null) {
			try {
				rule = pointsRuleDao.loadEarnRule(ruleName);
			} catch (DataAccessException e) {
				e.printStackTrace();
				return null;
			}

			if (rule != null) {
				cacheRule(ruleName.name(), rule);
			}
		}

		return rule;
	}

	private PointsRule loadBurnRule(BurnPointsRuleEnum ruleName) {
		PointsRule rule = ruleCacheAccess.get(ruleName.name());
		if (rule == null) {
			try {
				rule = pointsRuleDao.loadBurnRule(ruleName);
			} catch (DataAccessException e) {
				e.printStackTrace();
				return null;
			}

			if (rule != null) {
				cacheRule(ruleName.name(), rule);
			}
		}

		return rule;
	}

	// Caches Deal Id and Deal Date
	private void cacheRule(String ruleName, PointsRule rule) {

		try {
			ruleCacheAccess.lock(ruleName);
			if (ruleCacheAccess.get(ruleName) == null) {
				ruleCacheAccess.put(ruleName, rule);
			}
		} finally {
			ruleCacheAccess.unlock(ruleName);
		}

	}

	@Override
	public String mailBurnData(BurnActionCodesEnum txnActionCode,
			String merchantId) {
		String settlementDate = pointsUtil.getPreviousDayDate();
		String fileName = "Burn Reversal_" + settlementDate + ".csv";
		String header = "Transaction ID, Transaction Date, Merchant ID, Points, Reason \n";
		String fileBody = "";
		Collection<PointsHeader> burnList = pointsDao.loadPointsHeaderData(
				txnActionCode.name(), settlementDate, merchantId);
		Iterator<PointsHeader> burnIterator = burnList.iterator();
		while (burnIterator.hasNext()) {
			PointsHeader burnValues = burnIterator.next();
			String transactionID = burnValues.getReferenceId();
			String transactionDate = pointsUtil.convertDateToFormat(
					burnValues.getTxnDate(), "yyyy-MM-dd");
			String points = String.valueOf(burnValues.getTxnPoints());
			String reason = burnValues.getReason();

			fileBody += transactionID + ", " + transactionDate + ", "
					+ merchantId + ", " + points + ", " + reason + "\n";
		}

		if (fileBody != null && !fileBody.equals("")) {
			fileBody = header + fileBody;
			logger.info("Updating Status for " + txnActionCode.name());
			pointsDao.updateStatus(txnActionCode.name(), settlementDate,
					merchantId);
			pointsUtil.sendMail(txnActionCode.name(), merchantId, fileName,
					fileBody, "POINTS");
		}
		return fileBody;
	}

	@Override
	public String postEarnData(EarnActionCodesEnum txnActionCode,
			String merchantId, String client) throws PlatformException {
		String dataToUpload = "";
		try {
			Properties props = pointsUtil.getProperties("payback.properties");
			String sequenceNumber = pointsUtil.getSequenceNumber();
			String settlementDate = pointsUtil.getPreviousDayDate();
			DateTime newSettlementDate = DateTime.now();
			dataToUpload = getEarnDataToUpload(txnActionCode.name(),
					settlementDate, newSettlementDate, merchantId,
					sequenceNumber);
			if (dataToUpload != null && !dataToUpload.equals("")) {

				// SFTP Connection
				SFTPConnector sftpConnector = new SFTPConnector();
				sftpConnector.setHost(props.getProperty("sftpHost"));

				String sftpUsername = props.getProperty(client
						+ "_sftpUsername");
				sftpConnector.setUsername(sftpUsername);
				sftpConnector.setPassword(props.getProperty(client
						+ "_sftpPassword"));
				if (!sftpConnector.isConnected()) {
					// false determines that host key checking will be disabled
					sftpConnector.connect(false, 50000);
					logger.info("SFTP Connected Username: " + sftpUsername);
				}

				String fileName = txnActionCode.toString()
						+ "_"
						+ String.valueOf(merchantId)
						+ "_"
						+ pointsUtil.convertDateToFormat(settlementDate,
								"ddMMyyyy") + "_" + sequenceNumber + ".txt";
				String remoteDirectory = props
						.getProperty("sftpRemoteDirectory");
				sftpConnector.upload(dataToUpload, fileName, remoteDirectory);
				logger.info("File Upload Task Done fileName: " + fileName);
				fileName = fileName.replace("txt", "chk");
				sftpConnector.upload("", fileName, remoteDirectory);
				sftpConnector.upload(dataToUpload, fileName, remoteDirectory);
				pointsDao.updateStatus(txnActionCode.name(), settlementDate,
						merchantId);

				sftpConnector.closeConnection();
			}

		} catch (SftpException se) {
			logger.error("SFTP Connection Failed :" + se.toString());
			throw new PlatformException(" SFTP connection Failed");
		} catch (UnsupportedEncodingException ue) {
			logger.error("UnsupportedEncodingException :" + ue.toString());
			throw new PlatformException("Encoding not suppported");
		}
		return dataToUpload;

	}

	private String getEarnDataToUpload(String txnActionCode,
			String settlementDate, DateTime currentTime,
			String partnerMerchantId, String sequenceNumber) {
		int headerRows = 0;
		int totalTxnPoints = 0;
		int totalTxnValue = 0;
		String rowData = "";
		int itemRows = 0;

		Collection<PointsHeader> earnHeaderList = pointsDao
				.loadPointsHeaderData(txnActionCode, settlementDate,
						partnerMerchantId);
		Iterator<PointsHeader> EarnIterator = earnHeaderList.iterator();
		while (EarnIterator.hasNext()) {
			PointsHeader earnHeader = EarnIterator.next();
			Collection<PointsItems> pointsItems = pointsDao
					.loadPointsItemData(earnHeader.getId());
			if (!pointsItems.isEmpty() || earnHeader.hasSKUItems() == 0) {
				headerRows++;
				totalTxnPoints += earnHeader.getTxnPoints();
				totalTxnValue += earnHeader.getTxnValue();
				rowData += earnHeader.getLoyaltyCard()
						+ "||||"
						+ earnHeader.getPartnerMerchantId()
						+ "||"
						+ earnHeader.getPartnerTerminalId()
						+ "||"
						+ earnHeader.getOrderId()
						+ "|||"
						+ earnHeader.getTxnActionCode()
						+ "|"
						+ earnHeader.getTxnClassificationCode()
						+ "|||"
						+ earnHeader.getTxnPaymentType().trim()
						+ "|"
						+ pointsUtil.convertDateToFormat(
								earnHeader.getTxnDate(), "ddMMyyyy")
						+ "|"
						+ pointsUtil.convertDateToFormat(
								earnHeader.getTxnTimestamp(), "hhmmss")
						+ "|"
						+ new BigDecimal(earnHeader.getTxnValue()).setScale(2)
						+ "||"
						+ earnHeader.getMarketingCode()
						+ "|"
						+ earnHeader.getTxnPoints()
						+ "|"
						+ earnHeader.hasSKUItems()
						+ "|"
						+ pointsUtil.convertDateToFormat(
								earnHeader.getSettlementDate(), "ddMMyyyy")
						+ "|||||||"
						+ new BigDecimal(earnHeader.getTxnValue()).setScale(2)
						+ "|" + earnHeader.getBranchId()
						+ "||||||||||||||||||||||";
				rowData += "\n";

				Iterator<PointsItems> itemIterator = pointsItems.iterator();
				while (itemIterator.hasNext() && earnHeader.hasSKUItems() > 0) {
					itemRows++;
					PointsItems pointsItem = itemIterator.next();
					String departmentName = pointsItem.getDepartmentName();
					// Check for length as it is required to send only 30
					// characters
					if (departmentName.length() > 29) {
						departmentName = departmentName.substring(0, 29);
					}
					rowData += "|"
							+ pointsItem.getItemAmount().setScale(2)
							+ "|"
							+ pointsItem.getItemAmount().setScale(2)
							+ "||"
							+ earnHeader.getPartnerMerchantId()
							+ "||"
							+ new BigDecimal(pointsItem.getQuantity())
									.setScale(2) + "|AMOUNT|"
							+ earnHeader.getOrderId() + "|"
							+ String.valueOf(itemRows) + "||||||||"
							+ pointsItem.getDepartmentCode() + "|"
							+ departmentName + "|||"
							+ pointsItem.getArticleId() + "|2|" + 
							"||||||||||||||||||||||||||||||";
					rowData += "\n";
				}
			}
		}
		if (rowData.length() > 0) {
			rowData += "PB_ACT_1.1|" + currentTime + "|" + sequenceNumber + "|"
					+ partnerMerchantId + "||" + headerRows + "|"
					+ totalTxnValue + "|" + totalTxnPoints
					+ "|||||||||||||||9|||||||||||||||||||||||||||||||"; 
		}
		return rowData;
	}

	private PointsResponseCodeEnum saveEarnReversalPoints(PointsRequest request) {
		PointsTxnClassificationCodeEnum actionCode = PointsTxnClassificationCodeEnum.PREALLOC_EARN;
		String classificationCode = actionCode.toString().split(",")[0];
		try{
			PointsHeader pointsHeader = pointsDao.getHeaderDetails(request .getOrderRequest().getOrderId(), actionCode.name(), classificationCode);
			Collection<PointsItems> pointsItems = pointsDao.loadPointsItemData(pointsHeader.getId());
			for (OrderItemRequest itemRequest : request.getOrderRequest()
					.getOrderItemRequest()) {
				Iterator<PointsItems> itemIterator = pointsItems.iterator();
				while (itemIterator.hasNext()) {
					PointsItems pointsItem = itemIterator.next();
					if (pointsItem.getItemId() == itemRequest.getId()) {
						if (pointsItem.getEarnRatio().compareTo(BigDecimal.ZERO) == 1) {
							itemRequest.setTxnPoints(pointsItem.getEarnRatio().multiply(itemRequest.getAmount()));
							itemRequest.setEarnRatio(pointsItem.getEarnRatio());
						}
					}
				}
			} 
		
			// Check for Bonus Points
			logger.info("Checking for Bonus Points Entry for Order id : "
					+ request.getOrderRequest().getOrderId());
			try {
				PointsHeader bonusPointsHeader = pointsDao.getHeaderDetails(request
						.getOrderRequest().getOrderId(), actionCode.name(),
						PointsRuleConfigConstants.BONUS_POINTS);
				EarnPointsRuleEnum ruleName = EarnPointsRuleEnum.BUY_WORTH_X_EARN_Y_BONUS_POINTS;
				PointsRule rule = loadEarnRule(ruleName);
				// Reverse Bonus Points only when rule is not applicable.
				if (!rule.isApplicable(request.getOrderRequest(), null)) {
					request.getOrderRequest().setBonusPoints(
							new BigDecimal(bonusPointsHeader.getTxnPoints()));
				}
			} catch (DataAccessException e) {
				logger.info("No Bonus Points Entry found for Order id : "
						+ request.getOrderRequest().getOrderId());
			}

			request.getOrderRequest().setTxnPoints(
					getTxnPoints(request).setScale(0, ROUND));
			return doOperation(request);
		} catch (DataAccessException e) {
			throw new PointsHeaderDoesNotExist("Earn Header not available");
		}
	}
	
	@Override
	public RollbackHeader rollbackTransaction(long headerId){
		RollbackHeader header = new RollbackHeader();
		header.setHeaderId(headerId);
		logger.info("Rolling Back the transaction for header id : " + headerId);
		header = pointsDao.rollbackTransaction(header);
		return header;
	}
	

}
