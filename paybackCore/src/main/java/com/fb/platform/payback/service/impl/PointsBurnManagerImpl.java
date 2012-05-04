package com.fb.platform.payback.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Properties;

import com.fb.platform.payback.service.PointsBurnManager;
import com.fb.platform.payback.service.PointsBurnService;
import com.fb.platform.payback.to.StoreBurnPointsRequest;
import com.fb.platform.payback.util.BurnActionCodesEnum;
import com.fb.platform.payback.util.PointsUtil;

public class PointsBurnManagerImpl implements PointsBurnManager{
	
	private PointsBurnService pointsBurnService;
	private PointsUtil pointsUtil;
	
	public void setPointsBurnService(PointsBurnService pointsBurnService){
		this.pointsBurnService = pointsBurnService;
	}
	
	public void setPointsUtil(PointsUtil pointsUtil){
		this.pointsUtil = pointsUtil;
	}

	@Override
	public void mailBurnData() {
		try {
			Properties props = pointsUtil.getProperties("points.properties");
			String[] clients =  props.getProperty("CLIENTS").split(",");
			for(String client : clients){
				String[] partnerIds = props.getProperty(client + "_IDS").split(",");
				String merchantId = partnerIds[0];
				for (BurnActionCodesEnum txnActionCode : BurnActionCodesEnum.values()){
					pointsBurnService.mailBurnData(txnActionCode.name(), merchantId);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public int storeBurnReversalPoints(StoreBurnPointsRequest request){
		BurnActionCodesEnum txnActionCode = BurnActionCodesEnum.BURN_REVERSAL;
		BigDecimal amount = request.getAmount();
		long orderId = request.getOrderId();
		String reason = request.getReason();
		pointsBurnService.saveBurnData(txnActionCode, amount, orderId, reason);		
		return 0;
	}

}
