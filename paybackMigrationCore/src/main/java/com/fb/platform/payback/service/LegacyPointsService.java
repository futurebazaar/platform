package com.fb.platform.payback.service;


import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fb.commons.PlatformException;


public interface LegacyPointsService {

	@Transactional(propagation=Propagation.REQUIRED)
	public void migratePaybackData();

	String mailBurnData(BurnActionCodesEnum txnActionCode, String merchantId);

	String postEarnData(EarnActionCodesEnum txnActionCode, String merchantId,
			String client) throws PlatformException;

	
}