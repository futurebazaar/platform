package com.fb.platform.payback.dao;

import java.util.Collection;

import com.fb.platform.payback.model.PointsHeader;

public interface PointsBurnDao {
	
	public Collection<PointsHeader> loadBurnData(String txnActionCode, String settlementDate, String merchantId);
	
	public PointsHeader getBurnData(String txnActionCode, long orderId);

}
