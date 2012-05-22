package com.fb.platform.payback.service.impl;

import java.util.Properties;

import com.fb.platform.payback.service.PointsBurnService;
import com.fb.platform.payback.service.PointsEarnService;
import com.fb.platform.payback.service.PointsManager;
import com.fb.platform.payback.to.BurnActionCodesEnum;
import com.fb.platform.payback.to.EarnActionCodesEnum;
import com.fb.platform.payback.to.PointsRequest;
import com.fb.platform.payback.to.PointsResponse;
import com.fb.platform.payback.to.PointsResponseCodeEnum;
import com.fb.platform.payback.to.PointsTxnClassificationCodeEnum;
import com.fb.platform.payback.util.PointsUtil;

public class PointsManagerImpl implements PointsManager{
	
	private PointsEarnService pointsEarnService;
	private PointsBurnService pointsBurnService;
	private PointsUtil pointsUtil;
	
	public void setPointsEarnService(PointsEarnService pointsEarnService) {
		this.pointsEarnService = pointsEarnService;
	}
	
	public void setPointsBurnService(PointsBurnService pointsBurnService) {
		this.pointsBurnService = pointsBurnService;
	}
	
	public void setPointsUtil(PointsUtil pointsUtil){
		this.pointsUtil = pointsUtil;
	}

	
	@Override
	public PointsResponse getPointsReponse(PointsRequest request){
		PointsTxnClassificationCodeEnum actionCode = PointsTxnClassificationCodeEnum.valueOf(request.getTxnActionCode());
		PointsResponseCodeEnum responseEnum = PointsResponseCodeEnum.FAILURE;
		try {
			Properties props = pointsUtil.getProperties("payback.properties");
			String client = request.getClientName();
			String merchantId = props.getProperty(client + "_MERCHANT_ID");
			String terminalId = props.getProperty(client + "_TERMINAL_ID");
			switch(actionCode){
				
				case PREALLOC_EARN : case EARN_REVERSAL:
					responseEnum = pointsEarnService.storeEarnPoints(request, actionCode, merchantId, terminalId);
					break;
				
				case BURN_REVERSAL:
					responseEnum = pointsBurnService.storeBurnPoints(request, actionCode,  merchantId, terminalId);
					break;
			}
		} catch(Exception e){
			responseEnum = PointsResponseCodeEnum.INTERNAL_ERROR;
		}
		PointsResponse pointsResponse = new PointsResponse();
		pointsResponse.setActionCode(actionCode);
		pointsResponse.setPointsResponseCodeEnum(responseEnum);
		pointsResponse.setStatusMessage(responseEnum.toString());
		return pointsResponse;
	}

	@Override
	public void uploadEarnFilesOnSFTP() {
		try {
			Properties props = pointsUtil.getProperties("payback.properties");
			String[] clients =  props.getProperty("CLIENTS").split(",");
			for(String client : clients){
				String merchantId = props.getProperty(client + "_MERCHANT_ID");
				for (EarnActionCodesEnum txnActionCode : EarnActionCodesEnum.values()){
					String dataToUpload = pointsEarnService.postEarnData(txnActionCode, merchantId);
					if (dataToUpload != null && !dataToUpload.equals("")){
						pointsUtil.sendMail(txnActionCode.name(), merchantId, txnActionCode.toString()+ ".txt", dataToUpload);
					}
					
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void mailBurnData() {
		try {
			Properties props = pointsUtil.getProperties("payback.properties");
			String[] clients =  props.getProperty("CLIENTS").split(",");
			for(String client : clients){
				String merchantId = props.getProperty(client + "_MERCHANT_ID");
				for (BurnActionCodesEnum txnActionCode : BurnActionCodesEnum.values()){
					pointsBurnService.mailBurnData(txnActionCode.name(), merchantId);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
