package com.fb.platform.payback.service;

import com.fb.platform.payback.to.PointsRequest;
import com.fb.platform.payback.to.PointsResponse;
import com.fb.platform.payback.to.PointsResponseCodeEnum;

public interface PointsManager {

	public PointsResponse getPointsReponse(PointsRequest request);
	
	public void uploadEarnFilesOnSFTP();
	
	public void mailBurnData();

}
