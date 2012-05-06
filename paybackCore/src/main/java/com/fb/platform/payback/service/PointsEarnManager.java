package com.fb.platform.payback.service;

import com.fb.platform.payback.to.StorePointsHeaderRequest;

public interface PointsEarnManager{

	public int putEarnDataOnSftp();

	public int storeEarnPoints(StorePointsHeaderRequest request,
			String txnActionCode);
	

}
