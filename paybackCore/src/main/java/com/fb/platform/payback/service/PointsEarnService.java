package com.fb.platform.payback.service;

import org.springframework.transaction.annotation.Transactional;

import com.fb.platform.payback.to.EarnActionCodesEnum;
import com.fb.platform.payback.to.PointsRequest;
import com.fb.platform.payback.to.PointsResponseCodeEnum;
import com.fb.platform.payback.to.PointsTxnClassificationCodeEnum;


public interface PointsEarnService {

	@Transactional
	public String postEarnData(EarnActionCodesEnum txnActionCode, String merchantId);

	@Transactional
	public PointsResponseCodeEnum storeEarnPoints(PointsRequest request,
			PointsTxnClassificationCodeEnum actionCode, String merchantId, String terminalId);

}
