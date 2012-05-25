package com.fb.platform.payback.service.impl;

import java.util.Properties;

import com.fb.commons.PlatformException;
import com.fb.platform.payback.exception.DefinitionNotFound;
import com.fb.platform.payback.exception.InvalidActionCode;
import com.fb.platform.payback.exception.PointsHeaderDoesNotExist;
import com.fb.platform.payback.service.PointsManager;
import com.fb.platform.payback.service.PointsService;
import com.fb.platform.payback.to.BurnActionCodesEnum;
import com.fb.platform.payback.to.EarnActionCodesEnum;
import com.fb.platform.payback.to.PointsRequest;
import com.fb.platform.payback.to.PointsResponse;
import com.fb.platform.payback.to.PointsResponseCodeEnum;
import com.fb.platform.payback.to.PointsTxnClassificationCodeEnum;
import com.fb.platform.payback.util.PointsUtil;

public class PointsManagerImpl implements PointsManager{
	
	private PointsUtil pointsUtil;
	private PointsService pointsService;
	
	public void setPointsUtil(PointsUtil pointsUtil){
		this.pointsUtil = pointsUtil;
	}
	public void setPointsService(PointsService pointsService){
		this.pointsService = pointsService;
	}

	
	@Override
	public PointsResponse getPointsReponse(PointsRequest request){
		PointsTxnClassificationCodeEnum actionCode = PointsTxnClassificationCodeEnum.valueOf(request.getTxnActionCode());
		PointsResponseCodeEnum responseEnum = PointsResponseCodeEnum.FAILURE;
		try {
			responseEnum = pointsService.storePoints(request);
		} catch (InvalidActionCode e){
			responseEnum =  PointsResponseCodeEnum.INVALID_ACTION_CODE;
		} catch (DefinitionNotFound e){
			responseEnum =  PointsResponseCodeEnum.INVALID_POINTS;
		}catch (PointsHeaderDoesNotExist e){
			responseEnum =  PointsResponseCodeEnum.FAILURE;
		}catch (PlatformException e){
			responseEnum =  PointsResponseCodeEnum.INTERNAL_ERROR;
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
		Properties props = pointsUtil.getProperties("payback.properties");
		String[] clients =  props.getProperty("CLIENTS").split(",");
		for(String client : clients){
			String merchantId = props.getProperty(client + "_MERCHANT_ID");
			for (EarnActionCodesEnum txnActionCode : EarnActionCodesEnum.values()){
				try{
					String dataToUpload = pointsService.postEarnData(txnActionCode, merchantId, client);
					if (dataToUpload != null && !dataToUpload.equals("")){
						pointsUtil.sendMail(txnActionCode.name(), merchantId, txnActionCode.toString()+ ".txt", dataToUpload, "POINTS");
					}
				} catch (Exception e){
					// send  mail to check the exception
					pointsUtil.sendMail(txnActionCode.name(), merchantId, "ERROR_OCCURRED" + ".txt", e.toString(), "ERROR");
				}
				
			}
		}
	}

	@Override
	public void mailBurnData() {
		Properties props = pointsUtil.getProperties("payback.properties");
		String[] clients =  props.getProperty("CLIENTS").split(",");
		for(String client : clients){
			String merchantId = props.getProperty(client + "_MERCHANT_ID");
			for (BurnActionCodesEnum txnActionCode : BurnActionCodesEnum.values()){
				try {
					pointsService.mailBurnData(txnActionCode, merchantId);
				} catch (Exception e) {
					pointsUtil.sendMail(txnActionCode.name(), merchantId, "ERROR_OCCURRED" + ".txt", e.toString(), "ERROR");
				}
			}
		}
	}

}
