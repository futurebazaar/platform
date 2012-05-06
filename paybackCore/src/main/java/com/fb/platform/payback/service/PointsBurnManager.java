package com.fb.platform.payback.service;

import com.fb.platform.payback.to.StorePointsHeaderRequest;

public interface PointsBurnManager {
	
	public void mailBurnData();

	public int storeBurnPoints(StorePointsHeaderRequest request,
			String txnActionCode);

}
