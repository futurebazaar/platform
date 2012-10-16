package com.fb.platform.payback.service;


import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fb.platform.payback.model.RollbackHeader;
import com.fb.platform.payback.to.BurnActionCodesEnum;
import com.fb.platform.payback.to.EarnActionCodesEnum;
import com.fb.platform.payback.to.PointsRequest;
import com.fb.platform.payback.to.PointsResponseCodeEnum;

public interface PointsService {

	public String postEarnData(EarnActionCodesEnum txnActionCode, String merchantId, String client);
	
	public String mailBurnData(BurnActionCodesEnum txnActionCode, String merchantId);
	
	@Transactional(propagation=Propagation.REQUIRED)
	public  PointsResponseCodeEnum storePoints(PointsRequest request);

	public  PointsResponseCodeEnum clearPointsCache(String ruleName);

	public  PointsRequest getPointsToBeDisplayed(PointsRequest request);

	public RollbackHeader rollbackTransaction(long headerId);
	
}