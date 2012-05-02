package com.fb.platform.shipment.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fb.platform.shipment.dao.ShipmentDao;
import com.fb.platform.shipment.service.ShipmentService;
import com.fb.platform.shipment.to.GatePassItem;
import com.fb.platform.shipment.to.ParcelItem;

/**
 * @author nehaga
 * Any transactions for Shipment are routed via the service interface implementor.
 */
public class ShipmentServiceImpl implements ShipmentService {
	
	private static Log logger = LogFactory.getLog(ShipmentServiceImpl.class);

	@Autowired
	private ShipmentDao shipmentDao;
	
	/**
	 * Returns the parcel details of an item that is mentioned in the gatePass.xml. 
	 * @param gatePassItem
	 * @return
	 */
	@Override
	public ParcelItem getParcelDetails(GatePassItem gatePassItem) {
		if(logger.isDebugEnabled()) {
			logger.debug("GatePassItem received : " + gatePassItem.toString());
		}
		ParcelItem parcelItem = shipmentDao.getParcelDetails(gatePassItem);
		if(logger.isDebugEnabled()) {
			logger.debug("ParcelItem received : " + parcelItem.toString());
		}
		return parcelItem;
	}
	
	public void setShipmentDao(ShipmentDao shipmentDao) {
		this.shipmentDao = shipmentDao;
	}
	
}
