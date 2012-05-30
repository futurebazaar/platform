package com.fb.platform.payback.service;

import javax.naming.NoPermissionException;

import com.fb.platform.payback.to.ClearCacheRequest;
import com.fb.platform.payback.to.PointsRequest;
import com.fb.platform.payback.to.PointsResponse;
import com.fb.platform.payback.to.PointsResponseCodeEnum;

public interface PointsManager {

	public PointsResponse getPointsReponse(PointsRequest request);
	
	public String uploadEarnFilesOnSFTP();
	
	public String mailBurnData();

	PointsResponseCodeEnum clearPointsCache(ClearCacheRequest request);

	PointsRequest getPointsToBeDisplayed(PointsRequest request) throws NoPermissionException;

}
