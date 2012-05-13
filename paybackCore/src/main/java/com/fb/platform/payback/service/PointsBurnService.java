package com.fb.platform.payback.service;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fb.platform.payback.to.PointsRequest;
import com.fb.platform.payback.to.PointsResponseCodeEnum;
import com.fb.platform.payback.to.PointsTxnClassificationCodeEnum;

public interface PointsBurnService {

	@Transactional
	void mailBurnData(String txnActionCode, String merchantId);

	@Transactional(propagation=Propagation.REQUIRED)
	PointsResponseCodeEnum storeBurnPoints(PointsRequest request,
			PointsTxnClassificationCodeEnum actionCode);
	

}
