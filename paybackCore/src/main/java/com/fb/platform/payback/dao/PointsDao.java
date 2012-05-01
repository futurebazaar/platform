package com.fb.platform.payback.dao;

import com.fb.platform.payback.model.PointsHeader;

public interface PointsDao {
	
	public void insertPointsHeaderData(PointsHeader pointsHeader);
	
	public void insertPointsItemsData();

	public void updateStatus(String txnActionCode, String settlementDate, String merchantId);

}
