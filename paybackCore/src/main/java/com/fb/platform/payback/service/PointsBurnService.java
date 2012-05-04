package com.fb.platform.payback.service;

import java.math.BigDecimal;

import org.springframework.transaction.annotation.Transactional;

import com.fb.platform.payback.util.BurnActionCodesEnum;

public interface PointsBurnService {

	@Transactional
	void mailBurnData(String txnActionCode, String merchantId);

	@Transactional
	void saveBurnData(BurnActionCodesEnum txnActionCode, BigDecimal amount, long orderId, String reason);
	

}
