package com.fb.platform.shipment.dao;

import com.fb.platform.shipment.to.GatePassItem;
import com.fb.platform.shipment.to.ParcelItem;

/**
 * @author nehaga
 */
public interface ShipmentDao {
	/**
	 * Returns parcel details for items specified in gatePass.xml
	 * @param gatePassItem
	 * @return
	 */
	public ParcelItem getParcelDetails(GatePassItem gatePassItem);
}
