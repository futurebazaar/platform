package com.fb.platform.payback.service;

import com.fb.platform.payback.to.StorePointsRequest;
import com.fb.platform.payback.to.StorePointsResponse;

public interface PointsManager {

	public StorePointsResponse getPointsReponse(StorePointsRequest request);

}
