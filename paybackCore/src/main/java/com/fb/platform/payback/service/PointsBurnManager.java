package com.fb.platform.payback.service;

import com.fb.platform.payback.to.StorePointsRequest;

public interface PointsBurnManager {
	
	public void mailBurnData();

	public int storeBurnPoints(StorePointsRequest request,
			String txnActionCode);

}
