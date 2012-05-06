package com.fb.platform.payback.dao;

import java.util.Collection;

import com.fb.platform.payback.model.PointsItems;

public interface PointsEarnDao {
	
	public Collection<PointsItems> loadPointsItemData(long pointsHeaderId);
	
	public PointsItems getEarnData(String txnActionCode, long orderId);
	
	public void insertPointsItemsData(PointsItems pointsItems);

}
