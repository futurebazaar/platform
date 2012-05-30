package com.fb.platform.payback.service.impl;

import java.util.Properties;

import javax.naming.NoPermissionException;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fb.commons.PlatformException;
import com.fb.platform.auth.AuthenticationService;
import com.fb.platform.auth.AuthenticationTO;
import com.fb.platform.payback.exception.InvalidActionCode;
import com.fb.platform.payback.exception.InvalidSession;
import com.fb.platform.payback.exception.PointsHeaderDoesNotExist;
import com.fb.platform.payback.service.PointsManager;
import com.fb.platform.payback.service.PointsService;
import com.fb.platform.payback.to.BurnActionCodesEnum;
import com.fb.platform.payback.to.ClearCacheRequest;
import com.fb.platform.payback.to.EarnActionCodesEnum;
import com.fb.platform.payback.to.PointsRequest;
import com.fb.platform.payback.to.PointsResponse;
import com.fb.platform.payback.to.PointsResponseCodeEnum;
import com.fb.platform.payback.to.PointsTxnClassificationCodeEnum;
import com.fb.platform.payback.util.PointsUtil;

public class PointsManagerImpl implements PointsManager{
	
	private static Log logger = LogFactory.getLog(PointsManagerImpl.class);
	
	private PointsUtil pointsUtil;
	private PointsService pointsService;
	
	public void setPointsUtil(PointsUtil pointsUtil){
		this.pointsUtil = pointsUtil;
	}
	public void setPointsService(PointsService pointsService){
		this.pointsService = pointsService;
	}
	
	@Autowired
	private AuthenticationService authenticationService;

	
	@Override
	public PointsResponse getPointsReponse(PointsRequest request){
	
		PointsTxnClassificationCodeEnum actionCode = PointsTxnClassificationCodeEnum.valueOf(request.getTxnActionCode());
		PointsResponseCodeEnum responseEnum = PointsResponseCodeEnum.FAILURE;
		try {
			if (request == null || StringUtils.isBlank(request.getSessionToken())) {
				throw new InvalidSession("Invalid Session Toekn");
			}

			//authenticate the session token and find out the userId
			AuthenticationTO authentication = authenticationService.authenticate(request.getSessionToken());
			if (authentication == null) {
				throw new InvalidSession("Invalid Session Toekn");
			}

			responseEnum = pointsService.storePoints(request);
		} catch (InvalidActionCode e){
			responseEnum =  PointsResponseCodeEnum.INVALID_ACTION_CODE;
		}catch (PointsHeaderDoesNotExist e){
			responseEnum =  PointsResponseCodeEnum.FAILURE;
		}catch(InvalidSession e){
			responseEnum =  PointsResponseCodeEnum.NO_SESSION;
		}catch (PlatformException e){
			logger.error(e.toString());
			responseEnum =  PointsResponseCodeEnum.INTERNAL_ERROR;
		} catch(Exception e){
			logger.error(e.toString());
			responseEnum = PointsResponseCodeEnum.INTERNAL_ERROR;
		}
		logger.info("Store Points Status Code : " + responseEnum.name());
		PointsResponse pointsResponse = new PointsResponse();
		pointsResponse.setActionCode(actionCode);
		pointsResponse.setPointsResponseCodeEnum(responseEnum);
		pointsResponse.setStatusMessage(responseEnum.toString());
		pointsResponse.setTxnPoints(request.getOrderRequest().getTxnPoints().intValue());
		return pointsResponse;
	}

	@Override
	public String uploadEarnFilesOnSFTP() {
		String failedCodes = "";
		Properties props = pointsUtil.getProperties("payback.properties");
		String[] clients =  props.getProperty("CLIENTS").split(",");
		for(String client : clients){
			String merchantId = props.getProperty(client + "_MERCHANT_ID");
			for (EarnActionCodesEnum txnActionCode : EarnActionCodesEnum.values()){
				try{
					logger.info("Uploading " + txnActionCode.name() +" data for client : " + client + " and merchantId : " + merchantId);
					String dataToUpload = pointsService.postEarnData(txnActionCode, merchantId, client);
					if (dataToUpload != null && !dataToUpload.equals("")){
						pointsUtil.sendMail(txnActionCode.name(), merchantId, txnActionCode.toString()+ ".txt", dataToUpload, "POINTS");
					}
				} catch (Exception e){
					logger.error(e.toString());
					// send  mail to check the exception
					failedCodes += txnActionCode.name() + "; " ;
					pointsUtil.sendMail(txnActionCode.name(), merchantId, "ERROR_OCCURRED" + ".txt", e.toString(), "ERROR");
					
				}
				
			}
		}
		return failedCodes;
	}

	@Override
	public String mailBurnData() {
		String failedCodes = "";
		Properties props = pointsUtil.getProperties("payback.properties");
		String[] clients =  props.getProperty("CLIENTS").split(",");
		for(String client : clients){
			String merchantId = props.getProperty(client + "_MERCHANT_ID");
			for (BurnActionCodesEnum txnActionCode : BurnActionCodesEnum.values()){
				try {
					logger.info("Sending " +  txnActionCode.name() + " Data for client: " + client + " and merchantID : " + merchantId);
					pointsService.mailBurnData(txnActionCode, merchantId);
				} catch (Exception e) {
					logger.error(e.toString());
					failedCodes += txnActionCode.name() + "; " ;
					pointsUtil.sendMail(txnActionCode.name(), merchantId, "ERROR_OCCURRED" + ".txt", e.toString(), "ERROR");
				}
			}
		}
		return failedCodes;
	}
	
	@Override
	public PointsResponseCodeEnum clearPointsCache(ClearCacheRequest request){
		if (request == null || StringUtils.isBlank(request.getSessionToken())) {
			return PointsResponseCodeEnum.NO_SESSION;
		}

		//authenticate the session token and find out the userId
		AuthenticationTO authentication = authenticationService.authenticate(request.getSessionToken());
		if (authentication == null) {
			return PointsResponseCodeEnum.NO_SESSION;
		}
		return pointsService.clearPointsCache(request.getRuleName());

	}
	
	@Override
	public PointsRequest getPointsToBeDisplayed(PointsRequest request) {
		return pointsService.getPointsToBeDisplayed(request);
	}

}
