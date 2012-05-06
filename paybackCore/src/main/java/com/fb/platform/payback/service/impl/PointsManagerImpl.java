package com.fb.platform.payback.service.impl;

import com.fb.platform.payback.service.PointsBurnManager;
import com.fb.platform.payback.service.PointsEarnManager;
import com.fb.platform.payback.service.PointsManager;
import com.fb.platform.payback.to.StorePointsHeaderRequest;
import com.fb.platform.payback.to.StorePointsResponse;
import com.fb.platform.payback.to.StorePointsResponseCodeEnum;

public class PointsManagerImpl implements PointsManager{
	
	private PointsEarnManager pointsEarnManager = null;
	private PointsBurnManager pointsBurnManager = null;
	
	public void setPointsEarnManager(PointsEarnManager pointsEarnManager) {
		this.pointsEarnManager = pointsEarnManager;
	}
	
	public void setPointsBurnManager(PointsBurnManager pointsBurnManager) {
		this.pointsBurnManager = pointsBurnManager;
	}

	
	@Override
	public StorePointsResponse getPointsReponse(StorePointsHeaderRequest request){
		int flag = 1;
		try{
			if (request.getTxnActionCode().equals("PREALLOC_EARN") || request.getTxnActionCode().equals("EARN_REVERSAL")){
				flag =  pointsEarnManager.storeEarnPoints(request, request.getTxnActionCode());
			}
			if (request.getTxnActionCode().equals("BURN_REVERSAL")){
				flag = pointsBurnManager.storeBurnPoints(request, request.getTxnActionCode());
			}
		} catch (Exception e){
			flag = -1;
		}
		
		return response(flag, request.getTxnActionCode());
		
	}

	private StorePointsResponse response(int flag, String actionCode) {
		StorePointsResponse storePointsResponse = new StorePointsResponse();
		StorePointsResponseCodeEnum storePointsResponseCodeEnum = StorePointsResponseCodeEnum.INTERNAL_ERROR;
		switch(flag){
		case 0:
			storePointsResponseCodeEnum = StorePointsResponseCodeEnum.SUCCESS;
			break;
		case 1:
			storePointsResponseCodeEnum = StorePointsResponseCodeEnum.FAILURE;
			break;
		default:
			storePointsResponseCodeEnum = StorePointsResponseCodeEnum.INTERNAL_ERROR;
		}
		storePointsResponse.setActionCode(actionCode);
		storePointsResponse.setStorePointsResponseCodeEnum(storePointsResponseCodeEnum);
		return storePointsResponse;
	}

}
