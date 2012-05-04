package com.fb.platform.payback.dao;

import com.fb.platform.payback.model.OrderDetail;
import com.fb.platform.payback.model.PointsHeader;
import com.fb.platform.payback.model.PointsItems;

public interface PointsDao {
	
	public void insertPointsHeaderData(PointsHeader pointsHeader);
	
	public void insertPointsItemsData(PointsItems pointsItems);

	public void updateStatus(String txnActionCode, String settlementDate, String merchantId);

	public OrderDetail getOrderDetail(long orderId);

	public PointsHeader getHeaderDetails(long orderId, String txnActionCode, String txnClassificationCode);

}
