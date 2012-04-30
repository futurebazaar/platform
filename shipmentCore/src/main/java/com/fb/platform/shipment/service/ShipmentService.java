package com.fb.platform.shipment.service;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fb.platform.shipment.to.GatePassItem;
import com.fb.platform.shipment.to.ParcelItem;

/**
 * @author nehaga
 * Any transactions for Shipment are routed via the functions of this service interface.
 */
@Transactional
public interface ShipmentService {

	/**
	 * Returns the parcel details of an item that is mentioned in the gatePass.xml. 
	 * @param gatePassItem
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public ParcelItem getParcelDetails(GatePassItem gatePassItem);
}
