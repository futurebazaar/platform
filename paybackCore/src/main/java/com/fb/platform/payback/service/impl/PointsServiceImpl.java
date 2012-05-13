package com.fb.platform.payback.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.joda.time.DateTime;

import com.fb.commons.PlatformException;
import com.fb.platform.payback.dao.PointsDao;
import com.fb.platform.payback.exception.DefinitionNotFound;
import com.fb.platform.payback.exception.InvalidActionCode;
import com.fb.platform.payback.exception.PointsHeaderDoesNotExist;
import com.fb.platform.payback.model.PointsHeader;
import com.fb.platform.payback.rule.PointsRule;
import com.fb.platform.payback.service.PointsService;
import com.fb.platform.payback.to.OrderItemRequest;
import com.fb.platform.payback.to.OrderRequest;
import com.fb.platform.payback.to.PointsRequest;
import com.fb.platform.payback.to.PointsResponseCodeEnum;
import com.fb.platform.payback.to.PointsTxnClassificationCodeEnum;
import com.fb.platform.payback.util.PointsUtil;

public class PointsServiceImpl implements PointsService{
	
	private static int ROUND =BigDecimal.ROUND_HALF_DOWN; 
	
	private PointsUtil pointsUtil;
	private PointsDao pointsDao;
	
	public void setPointsDao(PointsDao pointsDao) {
		this.pointsDao = pointsDao;
	}

	public void setPointsUtil(PointsUtil pointsUtil){
		this.pointsUtil = pointsUtil;
	}
	
	@Override
	public PointsResponseCodeEnum doOperation(PointsRequest request, PointsRule rule){
		try{
			
			BigDecimal txnPoints = getTxnPoints(request);
			if (rule != null){
				if (!rule.isBonus()){
					txnPoints = rule.execute(request.getOrderRequest());
				}
				else{
					saveBonusPoints(request, rule.execute(request.getOrderRequest()));
				}
			}
			savePoints(request, txnPoints);
			return PointsResponseCodeEnum.SUCCESS;
			
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
		PointsTxnClassificationCodeEnum txnActionCode = PointsTxnClassificationCodeEnum.valueOf(request.getTxnActionCode());
		BigDecimal earnRatio = BigDecimal.ZERO;
		switch (txnActionCode){
			case PREALLOC_EARN:
				earnRatio = getNormalEarnRatio(request);
				break;
			case EARN_REVERSAL:
				earnRatio = getNormalEarnReversalRatio(request);
			default:
				throw new InvalidActionCode("No action is defined for action code : " + txnActionCode);
		}
		if (earnRatio.compareTo(BigDecimal.ZERO) == 1){
			return earnRatio.multiply(request.getOrderRequest().getAmount());
		}
		throw new DefinitionNotFound("Earn Ratio Not Defined : " + earnRatio);
	}

	private BigDecimal getNormalEarnReversalRatio(PointsRequest request ) {
		PointsTxnClassificationCodeEnum txnActionCode = PointsTxnClassificationCodeEnum.EARN_REVERSAL;
		String txnClassificationCode = txnActionCode.toString().split(",")[0];
		PointsHeader pointsHeader = pointsDao.getHeaderDetails(request.getOrderRequest().getOrderId(), txnActionCode.name(), txnClassificationCode);
		if (pointsHeader.getEarnRatio() != null && !pointsHeader.getEarnRatio().equals(BigDecimal.ZERO)){
			return pointsHeader.getEarnRatio();
		}
		return getNormalEarnRatio(request);
	}

	private BigDecimal getNormalEarnRatio(PointsRequest request) {
		Properties props = pointsUtil.getProperties("points.properties");
		String earnPointsRatio = props.getProperty("EARN_RATIO");
		if (earnPointsRatio != null && !earnPointsRatio.equals("")){
			return new BigDecimal(earnPointsRatio);
		}
		return BigDecimal.ZERO;
	}
	
	private void savePoints(PointsRequest pointsRequest, BigDecimal txnPoints){
		OrderRequest request = pointsRequest.getOrderRequest();
		BigDecimal earnRatio = txnPoints.divide(request.getAmount());
		BigDecimal burnRatio = new BigDecimal(4);
		PointsHeader pointsHeader = new PointsHeader();
		pointsHeader.setDetails(request, txnPoints, earnRatio, burnRatio);
		
	}
	
	private void saveBonusPoints(PointsRequest pointsRequest, BigDecimal bonusPoints){
		OrderRequest orderRequest = pointsRequest.getOrderRequest();
		orderRequest.setAmount(BigDecimal.ZERO);
		orderRequest.setIsBonus(true);
		OrderItemRequest orderItem = new OrderItemRequest();
		orderItem.setId(8000);
		orderItem.setAmount(BigDecimal.ZERO);
		orderItem.setArticleId("1111");
		orderItem.setDepartmentCode(100);
		orderItem.setDepartmentName("BONUS");
		
		List<OrderItemRequest> orderItemRequest = new ArrayList<OrderItemRequest>();
		orderItemRequest.add(orderItem);
		orderRequest.setOrderItemRequest(orderItemRequest);
		pointsRequest.setOrderRequest(orderRequest);
		
		savePoints(pointsRequest, bonusPoints);
		
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
