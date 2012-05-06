package com.fb.platform.payback.dao;

import java.util.Collection;

import com.fb.platform.payback.model.OrderDetail;
import com.fb.platform.payback.model.PointsHeader;

public interface PointsDao {
	
	public Long insertPointsHeaderData(PointsHeader pointsHeader);
	
	public void updateStatus(String txnActionCode, String settlementDate, String merchantId);

	public OrderDetail getOrderDetail(long orderId);

	public PointsHeader getHeaderDetails(long orderId, String txnActionCode, String txnClassificationCode);

	public Collection<PointsHeader> loadPointsHeaderData(String txnActionCode, String settlementDate, String merchantId);

}
