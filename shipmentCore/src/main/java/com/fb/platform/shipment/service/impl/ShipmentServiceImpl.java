package com.fb.platform.shipment.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;

import com.fb.commons.PlatformException;
import com.fb.platform.shipment.dao.ShipmentDao;
import com.fb.platform.shipment.exception.DataNotFoundException;
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
		ParcelItem parcelItem = new ParcelItem();
		try {
			infoLog.debug("GatePassItem received : " + gatePassItem.toString());
			parcelItem = shipmentDao.getParcelDetails(gatePassItem.getOrderReferenceId());
			parcelItem.setDeliveryNumber(gatePassItem.getDelNo());
			parcelItem.setDeliverySiteId(gatePassItem.getDeece());
			parcelItem.setTrackingNumber(gatePassItem.getAwbNo());
			parcelItem.setQuantity(gatePassItem.getQuantity());
			parcelItem.setArticleDescription(gatePassItem.getItemDescription());
			parcelItem.setWeight(gatePassItem.getDelWt());
			infoLog.debug("ParcelItem retrieved : " + parcelItem.toString());
		} catch (EmptyResultDataAccessException e) {
			errorLog.error("Outbound entry not created for : " + gatePassItem.getOrderReferenceId(), e);
			throw new DataNotFoundException("Data not found for : " + gatePassItem.getOrderReferenceId());
		}catch (DataAccessException e) {
			errorLog.error("Outbound entry not created for : " + gatePassItem.getOrderReferenceId(), e);
			throw new PlatformException("Outbound entry not created for : " + gatePassItem.getOrderReferenceId());
		}
		
		return parcelItem;
	}
	
	public void setShipmentDao(ShipmentDao shipmentDao) {
		this.shipmentDao = shipmentDao;
	}
	
}
