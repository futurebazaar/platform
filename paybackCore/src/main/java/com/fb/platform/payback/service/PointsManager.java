package com.fb.platform.payback.service;

import com.fb.platform.payback.to.PointsRequest;
import com.fb.platform.payback.to.PointsResponse;

public interface PointsManager {

	public PointsResponse getPointsReponse(PointsRequest request);
	
	public String uploadEarnFilesOnSFTP();
	
	public String mailBurnData();

}
