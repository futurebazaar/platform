package com.fb.platform.payback.service.impl;

import java.math.BigDecimal;

import org.joda.time.DateTime;

import com.fb.commons.PlatformException;
import com.fb.platform.payback.dao.PointsDao;
import com.fb.platform.payback.dao.PointsRuleDao;
import com.fb.platform.payback.exception.DefinitionNotFound;
import com.fb.platform.payback.exception.InvalidActionCode;
import com.fb.platform.payback.exception.PointsHeaderDoesNotExist;
import com.fb.platform.payback.model.PointsHeader;
import com.fb.platform.payback.rule.EarnPointsRuleEnum;
import com.fb.platform.payback.rule.PointsRule;
import com.fb.platform.payback.rule.PointsRuleConfigConstants;
import com.fb.platform.payback.service.PointsService;
import com.fb.platform.payback.to.OrderItemRequest;
import com.fb.platform.payback.to.OrderRequest;
import com.fb.platform.payback.to.PointsRequest;
import com.fb.platform.payback.to.PointsResponseCodeEnum;
import com.fb.platform.payback.to.PointsTxnClassificationCodeEnum;

public class PointsServiceImpl implements PointsService{
	
	private static int ROUND =BigDecimal.ROUND_HALF_DOWN; 
	
	private PointsDao pointsDao;
	private PointsRuleDao pointsRuleDao;
	
	public void setPointsDao(PointsDao pointsDao) {
		this.pointsDao = pointsDao;
	}

	public void setPointsRuleDao(PointsRuleDao pointsRuleDao) {
		this.pointsRuleDao = pointsRuleDao;
	}
	
	@Override
	public PointsResponseCodeEnum doOperation(PointsRequest request){
		try{
			OrderRequest orderRequest = request.getOrderRequest();
			BigDecimal txnPoints = getTxnPoints(request);
			PointsTxnClassificationCodeEnum actionCode = PointsTxnClassificationCodeEnum.valueOf(request.getTxnActionCode());
			String classificationCode = actionCode.toString().split(",")[0];
			BigDecimal bonusPoints = orderRequest.getBonusPoints();
			if (txnPoints.compareTo(BigDecimal.ZERO) ==1){
				savePoints(orderRequest, txnPoints, actionCode, classificationCode);
				if (bonusPoints.compareTo(BigDecimal.ZERO) ==1){
					savePoints(orderRequest, txnPoints, actionCode, PointsRuleConfigConstants.BONUS_POINTS);
				}
				return PointsResponseCodeEnum.SUCCESS;
			}
			return PointsResponseCodeEnum.INVALID_POINTS;
		} catch (InvalidActionCode e){
			return PointsResponseCodeEnum.INVALID_ACTION_CODE;
		} catch (DefinitionNotFound e){
			return PointsResponseCodeEnum.INVALID_POINTS;
		}catch (PointsHeaderDoesNotExist e){
			return PointsResponseCodeEnum.FAILURE;
		}catch (PlatformException e){
			return PointsResponseCodeEnum.INTERNAL_ERROR;
		} 
	}
	
	private BigDecimal getTxnPoints(PointsRequest request) {
		BigDecimal totalTxnPoint = BigDecimal.ZERO;
		for (OrderItemRequest itemRequest : request.getOrderRequest().getOrderItemRequest()){
			totalTxnPoint.add(itemRequest.getTxnPoints());
		}
			return totalTxnPoint;
	}

	private void savePoints(OrderRequest orderRequest, BigDecimal txnPoints, PointsTxnClassificationCodeEnum actionCode, String classificationCode){
		PointsHeader pointsHeader = new PointsHeader();
		pointsHeader.setTxnClassificationCode(classificationCode);
		pointsHeader.setTxnActionCode(actionCode.name());
		pointsHeader.setTxnPoints(txnPoints.setScale(0, ROUND).intValue());
		pointsHeader.setTxnValue(orderRequest.getAmount().setScale(0, ROUND).intValue());
		pointsHeader.setDetails(orderRequest);
		long headerId = pointsDao.insertPointsHeaderData(pointsHeader);
		if (orderRequest.getOrderItemRequest() != null && !orderRequest.getOrderItemRequest().isEmpty()){
			savePointsItems(orderRequest, headerId);
		}
		
	}
	
	private void savePointsItems(OrderRequest orderRequest, long headerId) {
		for (OrderItemRequest itemRequest : orderRequest.getOrderItemRequest()){
			int txnPoints = itemRequest.getTxnPoints().setScale(0, ROUND).intValue();
			pointsDao.insertPointsItemsData(itemRequest, headerId, txnPoints);
		}
		
	}

	@Override
	public String getSequenceNumber() {
		DateTime datetime = new DateTime();
		int seconds = (datetime.getSecondOfDay()%999999) + 1;
		String sequenceNumber = String.valueOf(seconds);
		while(sequenceNumber.length() < 6){
			sequenceNumber = "0" + sequenceNumber;
		}
		return sequenceNumber;
	}

}
