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
	
	private static Log infoLog = LogFactory.getLog("LOGINFO");
	
	private static Log errorLog = LogFactory.getLog("LOGERROR");

	@Autowired
	private ShipmentDao shipmentDao;
	
	/**
	 * Returns the parcel details of an item that is mentioned in the gatePass.xml. 
	 * @param gatePassItem
	 * @return
	 */
	@Override
	public ParcelItem getParcelDetails(GatePassItem gatePassItem) {
		ParcelItem parcelItem = null;
		try {
			infoLog.debug("GatePassItem received : " + gatePassItem.toString());
			parcelItem = shipmentDao.getParcelDetails(gatePassItem);
			infoLog.debug("ParcelItem retrieved : " + parcelItem.toString());
		} catch (Exception e) {
			errorLog.error("Outbound entry not created for : " + gatePassItem.getSonum() + " , LSP code : " + gatePassItem.getLspcode().toString());
			errorLog.error(e.getStackTrace());
		}
		
		return parcelItem;
	}
	
	public void setShipmentDao(ShipmentDao shipmentDao) {
		this.shipmentDao = shipmentDao;
	}
	
}
