package com.fb.platform.payback.service;

import org.springframework.transaction.annotation.Transactional;

import com.fb.platform.payback.model.PointsItems;
import com.fb.platform.payback.util.EarnActionCodesEnum;


public interface PointsEarnService {

	@Transactional
	public String postEarnData(EarnActionCodesEnum txnActionCode, String merchantId);
	
	@Transactional
	public void saveEarnData(EarnActionCodesEnum txnActionCode, String merchantId, PointsItems pointsItems);
	
	@Transactional
	public void saveEarnReversalData(EarnActionCodesEnum txnActionCode, String merchantId, PointsItems pointsItems);

}
