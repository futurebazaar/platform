package com.fb.platform.payback.dao;

import java.util.Collection;

import com.fb.platform.payback.model.PointsHeader;
import com.fb.platform.payback.model.PointsItems;
import com.fb.platform.payback.model.RollbackHeader;
import com.fb.platform.payback.to.OrderItemRequest;

public interface PointsDao {

	public Long insertPointsHeaderData(PointsHeader pointsHeader);

	public void updateStatus(String txnActionCode, String settlementDate,
			String merchantId);

	public PointsHeader getHeaderDetails(long orderId, String txnActionCode,
			String txnClassificationCode);

	public Collection<PointsHeader> loadPointsHeaderData(String txnActionCode,
			String settlementDate, String merchantId);

	public Collection<PointsItems> loadPointsItemData(long pointsHeaderId);

	public void insertPointsItemsData(OrderItemRequest itemRequest,
			long headerId, int txnPoints);

	public RollbackHeader rollbackTransaction(RollbackHeader header);

}
