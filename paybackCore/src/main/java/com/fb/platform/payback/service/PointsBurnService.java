package com.fb.platform.payback.service;

import java.util.Collection;

import com.fb.platform.payback.model.PointsHeader;
import com.fb.platform.payback.to.StoreBurnPointsRequest;

public interface PointsBurnService {

	Collection<PointsHeader> loadBurnData(String txnActionCode, String settlementDate, String merchantId);

	void saveBurnPoints(StoreBurnPointsRequest request);

}
