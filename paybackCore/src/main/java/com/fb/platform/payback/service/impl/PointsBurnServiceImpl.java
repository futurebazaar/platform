package com.fb.platform.payback.service.impl;

import java.util.Collection;
import java.util.Iterator;

import com.fb.platform.payback.dao.PointsDao;
import com.fb.platform.payback.dao.PointsRuleDao;
import com.fb.platform.payback.model.PointsHeader;
import com.fb.platform.payback.rule.BurnPointsRuleEnum;
import com.fb.platform.payback.rule.PointsRule;
import com.fb.platform.payback.service.PointsBurnService;
import com.fb.platform.payback.service.PointsService;
import com.fb.platform.payback.to.OrderItemRequest;
import com.fb.platform.payback.to.OrderRequest;
import com.fb.platform.payback.to.PointsRequest;
import com.fb.platform.payback.to.PointsResponseCodeEnum;
import com.fb.platform.payback.to.PointsTxnClassificationCodeEnum;
import com.fb.platform.payback.util.PointsUtil;

public class PointsBurnServiceImpl implements PointsBurnService{
	
	private PointsDao pointsDao;
	private PointsRuleDao pointsRuleDao;
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
	
	public void setPointsRuleDao(PointsRuleDao pointsRuleDao){
		this.pointsRuleDao = pointsRuleDao;
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
			PointsTxnClassificationCodeEnum actionCode, String merchantId, String terminalId) {
		
		PointsRule  rule = null;
		for (BurnPointsRuleEnum ruleName : BurnPointsRuleEnum.values()){
			rule = loadEarnRule(ruleName);
			OrderRequest orderRequest = request.getOrderRequest();
			if (rule != null){
				for (OrderItemRequest itemRequest : orderRequest.getOrderItemRequest()){
					if (rule.isApplicable(orderRequest, itemRequest)){
						itemRequest.setTxnPoints(rule.execute(orderRequest, itemRequest));
					}
				}
			}
			if (!rule.allowNext()){
				break;
			}
		}
			
		return pointsService.doOperation(request);
	}

	private PointsRule loadEarnRule(BurnPointsRuleEnum ruleName) {
		return pointsRuleDao.loadEarnRule(ruleName);
	}
	
}
