package com.fb.platform.payback.service;

import com.fb.platform.payback.to.StorePointsHeaderRequest;
import com.fb.platform.payback.to.StorePointsResponse;

public interface PointsManager {

	public StorePointsResponse getPointsReponse(StorePointsHeaderRequest request);

}
