package com.fb.platform.payback.service.impl;

import java.util.Collection;

import com.fb.platform.payback.dao.PointsBurnDao;
import com.fb.platform.payback.dao.PointsDao;
import com.fb.platform.payback.model.PointsHeader;
import com.fb.platform.payback.service.PointsBurnService;
import com.fb.platform.payback.to.StoreBurnPointsRequest;

public class PointsBurnServiceImpl implements PointsBurnService{
	
	private PointsBurnDao pointsBurnDao;
	private PointsDao pointsDao;
	
	@Override
	public Collection<PointsHeader> loadBurnData(String txnActionCode, String settlementDate, String merchantId){
		Collection<PointsHeader> burnList = pointsBurnDao.loadBurnData(txnActionCode, settlementDate, merchantId);
		return burnList;
	}
	
	public void setPointsBurnDao(PointsBurnDao pointsBurnDao) {
		this.pointsBurnDao = pointsBurnDao;
	}
	
	public void setPointsDao(PointsDao pointsDao) {
		this.pointsDao = pointsDao;
	}

	@Override
	public void saveBurnPoints(StoreBurnPointsRequest request) {
		PointsHeader pointsHeader = new PointsHeader();
		
	}
}
