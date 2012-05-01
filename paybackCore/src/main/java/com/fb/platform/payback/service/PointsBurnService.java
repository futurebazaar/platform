package com.fb.platform.payback.service;

import com.fb.platform.payback.to.StoreBurnPointsRequest;

public interface PointsBurnService {

	void saveBurnPoints(StoreBurnPointsRequest request);

	void mailBurnData(String txnActionCode, String merchantId);

}
