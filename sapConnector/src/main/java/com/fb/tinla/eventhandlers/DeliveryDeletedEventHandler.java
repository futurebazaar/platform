package com.fb.tinla.eventhandlers;

import java.util.*;

import org.apache.log4j.Logger;

import com.fb.sap.client.Events;
import com.fb.sap.idocs.DeliveryDeletedIDoc;
import com.fb.sap.tinla.idocadapters.DeliveryDeletedIDocShipmentAdapter;
import com.fb.tinla.models.Shipment;
import com.fb.tinla.models.ShipmentItem;

public class DeliveryDeletedEventHandler extends EventHandler implements Observer {

	private static DeliveryDeletedEventHandler INSTANCE = new DeliveryDeletedEventHandler();
	private static Logger logger = Logger.getLogger("auris");
	
	public static DeliveryDeletedEventHandler getInstance() {
		return INSTANCE;
	}
	
	private DeliveryDeletedEventHandler() {
		super("/del_ack/");
		Events.DELIVERY_DELETED_EVENT.addObserver(this);
	}

	public void onShipmentDeleted(Shipment shipment) throws Exception {	
		Iterator<ShipmentItem> itemIterator = shipment.getShipmentItems().iterator();
		
		// Tinla does not support partial delivery deletion currently.
		// We have information at item level, tinla should support partial deletions.
		boolean sendItemDeletionNotifications = false;
		if (sendItemDeletionNotifications) {
			while( itemIterator.hasNext()) {
				ShipmentItem shipmentItem = itemIterator.next();
				Map<String, String> params = new HashMap<String, String>();
				params.put("header", "DEL_ACK");
				params.put("deliveryNumber", shipment.getDeliveryNumber());
				params.put("orderId", shipment.getOrderId());
				params.put("atgDocumentId", shipmentItem.getLineItemNumber());
				params.put("deliveryDateTime", shipment.getDeletedDate());
				this.sendEventToTinla(params);
			}
		} else {
			// Send notification to delete entire delivery
			Map<String, String> params = new HashMap<String, String>();
			params.put("header", "DEL_ACK");
			params.put("deliveryNumber", shipment.getDeliveryNumber());
			params.put("orderId", shipment.getOrderId());
			params.put("deliveryDateTime", shipment.getDeletedDate() + " " + shipment.getDeletedTime());
			this.sendEventToTinla(params);
		}
	}

	@Override
	public void update(Observable observable, Object obj) {
		// TODO Auto-generated method stub
		try {
			DeliveryDeletedIDocShipmentAdapter adapter = new DeliveryDeletedIDocShipmentAdapter((DeliveryDeletedIDoc)obj);
			this.onShipmentDeleted(adapter);
			logger.debug("Sent event to tinla");
		} catch (Exception e) {
			logger.error(
					"Error consuming delivery deleted event", e);
		}
	}
}
