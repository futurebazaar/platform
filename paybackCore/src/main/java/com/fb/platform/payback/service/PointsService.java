package com.fb.platform.payback.service;


import org.springframework.transaction.annotation.Isolation;
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
	
	@Transactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_UNCOMMITTED)
	public  PointsResponseCodeEnum storePoints(PointsRequest request);

	public  PointsResponseCodeEnum clearPointsCache(String ruleName);

	public  PointsRequest getPointsToBeDisplayed(PointsRequest request);

	RollbackHeader rollbackTransaction(long headerId);
	
	//public PointsRequest rollBackTransaction(long headerId);

	
}