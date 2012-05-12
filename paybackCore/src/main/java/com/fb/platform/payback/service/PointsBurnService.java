package com.fb.platform.payback.service;

import java.math.BigDecimal;

import org.springframework.transaction.annotation.Transactional;

import com.fb.platform.payback.to.BurnActionCodesEnum;
import com.fb.platform.payback.to.PointsRequest;
import com.fb.platform.payback.to.PointsResponseCodeEnum;
import com.fb.platform.payback.to.PointsTxnClassificationCodeEnum;

public interface PointsBurnService {

	@Transactional
	void mailBurnData(String txnActionCode, String merchantId);

	@Transactional
	void saveBurnData(BurnActionCodesEnum txnActionCode, BigDecimal amount, long orderId, String reason);

	PointsResponseCodeEnum storeBurnPoints(PointsRequest request,
			PointsTxnClassificationCodeEnum actionCode);
	

}
