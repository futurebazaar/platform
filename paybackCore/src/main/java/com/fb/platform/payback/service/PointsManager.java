package com.fb.platform.payback.service;

import javax.naming.NoPermissionException;

import com.fb.platform.payback.to.ClearCacheRequest;
import com.fb.platform.payback.to.PointsRequest;
import com.fb.platform.payback.to.PointsResponse;
import com.fb.platform.payback.to.PointsResponseCodeEnum;
import com.fb.platform.payback.to.RollbackRequest;
import com.fb.platform.payback.to.RollbackResponse;

public interface PointsManager {

	public PointsResponse getPointsReponse(PointsRequest request);
	
	public String uploadEarnFilesOnSFTP();
	
	public String mailBurnData();

	public PointsResponseCodeEnum clearPointsCache(ClearCacheRequest request);

	public PointsRequest getPointsToBeDisplayed(PointsRequest request) throws NoPermissionException;

	public RollbackResponse rollbackTransaction(RollbackRequest request);

}
