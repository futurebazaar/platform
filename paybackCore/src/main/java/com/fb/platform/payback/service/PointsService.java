package com.fb.platform.payback.service;


import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fb.platform.payback.to.BurnActionCodesEnum;
import com.fb.platform.payback.to.EarnActionCodesEnum;
import com.fb.platform.payback.to.PointsRequest;
import com.fb.platform.payback.to.PointsResponseCodeEnum;

public interface PointsService {

	String postEarnData(EarnActionCodesEnum txnActionCode, String merchantId, String client);
	
	String mailBurnData(BurnActionCodesEnum txnActionCode, String merchantId);
	
	@Transactional(propagation=Propagation.REQUIRED)
	PointsResponseCodeEnum storePoints(PointsRequest request);

	
}