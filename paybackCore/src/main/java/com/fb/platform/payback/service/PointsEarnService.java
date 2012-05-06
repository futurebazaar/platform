package com.fb.platform.payback.service;

import java.io.IOException;

import org.springframework.transaction.annotation.Transactional;

import com.fb.platform.payback.to.EarnActionCodesEnum;
import com.fb.platform.payback.to.StorePointsHeaderRequest;


public interface PointsEarnService {

	@Transactional
	public String postEarnData(EarnActionCodesEnum txnActionCode, String merchantId);
	
	@Transactional
	public void saveEarnData(EarnActionCodesEnum txnActionCode, StorePointsHeaderRequest request) throws IOException;

}
