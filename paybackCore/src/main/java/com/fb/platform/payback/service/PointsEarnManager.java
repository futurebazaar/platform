package com.fb.platform.payback.service;

import com.fb.platform.payback.to.StorePointsRequest;

public interface PointsEarnManager{

	public int putEarnDataOnSftp();

	public int storeEarnPoints(StorePointsRequest request,
			String txnActionCode);
	

}
