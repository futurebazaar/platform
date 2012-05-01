package com.fb.platform.payback.dao;

import java.util.Collection;

import com.fb.platform.payback.model.PointsItems;

public interface PointsEarnDao {
	
	public Collection<PointsItems> loadEarnData(String txnActionCode, String settlementDate, String merchantId);
	
	public PointsItems getEarnData(String txnActionCode, long orderId);

}
