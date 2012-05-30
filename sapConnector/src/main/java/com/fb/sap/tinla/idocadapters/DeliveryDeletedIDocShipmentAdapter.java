package com.fb.sap.tinla.idocadapters;

import java.util.*;
import com.fb.sap.idocs.DeliveryDeletedIDoc;
import com.fb.sap.idocs.DeliveryDeletedIDoc.IDOC.ZATGSOH;
import com.fb.sap.idocs.DeliveryDeletedIDoc.IDOC.ZATGSOH.ZATGSOD;
import com.fb.tinla.models.Shipment;
import com.fb.tinla.models.ShipmentItem;

public class DeliveryDeletedIDocShipmentAdapter extends Shipment {
	
	public DeliveryDeletedIDocShipmentAdapter(DeliveryDeletedIDoc deliveryDeletedIDoc) {
		super();
		List<ShipmentItem> shipmentItems = new ArrayList<ShipmentItem>();
		List<ZATGSOH> zatgsohList = deliveryDeletedIDoc.getIDOC().getZATGSOH();
		Iterator<ZATGSOH> zatgsohIterator = zatgsohList.iterator();
		
		while(zatgsohIterator.hasNext()) {
			ZATGSOH zatgsoh = zatgsohIterator.next();
			this.setOrderId(zatgsoh.getVBELN());
			
			List<ZATGSOD> zatgsodList = zatgsoh.getZATGSOD();
			Iterator<ZATGSOD> zatgsodIterator = zatgsodList.iterator();
			
			while(zatgsodIterator.hasNext()) {
				ZATGSOD zatgsod = zatgsodIterator.next();
				
				if (zatgsod.getPOSNR().trim().equals("000000")) {
					// Header section
					this.setDeliveryNumber(zatgsod.getVBELN());
					this.setDeletedDate(zatgsod.getDELDATE());
					this.setDeletedTime(zatgsod.getDELTIME());
					this.setDeletedBy(zatgsod.getDELUSER());
					continue;
				}
				
				ShipmentItem shipmentItem = new ShipmentItem();
				shipmentItem.setLineItemNumber(zatgsod.getPOSNR());
				shipmentItems.add(shipmentItem);
			}
		}
		this.setShipmentItems(shipmentItems);
	}
}
