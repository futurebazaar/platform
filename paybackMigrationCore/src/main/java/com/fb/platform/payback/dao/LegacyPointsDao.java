package com.fb.platform.payback.dao;

import java.util.Collection;

import com.fb.platform.payback.model.LegacyPointsHeader;
import com.fb.platform.payback.model.LegacyPointsItems;

public interface LegacyPointsDao {
	
	public Long insertPointsHeaderData(LegacyPointsHeader pointsHeader);

	public Collection<LegacyPointsHeader> loadPointsHeaderData();

	public Collection<LegacyPointsItems> loadPointsItemData(long pointsHeaderId);

	public void insertPointsItemsData(LegacyPointsItems pointItems);

	public void updateStatus(String name, String settlementDate,
			String merchantId);

	public Collection<LegacyPointsHeader> loadPointsHeaderData(String name,
			String settlementDate, String merchantId);

}
