package com.fb.sap.client;

public class SAPEventFactory {
	
	public static String DELIVERY_DELETED_IDOC_TYPE = "ZATGDELD";
	public static String ORDER_STATUS_CHANGED_IDOC_TYPE = "ZATGFLOW";

	public static ISAPEvent getEventForIDoc(String idocType) {
		if (idocType.equals(DELIVERY_DELETED_IDOC_TYPE))
			return DeliveryDeletedObservable.getInstance();
		if (idocType.equals(ORDER_STATUS_CHANGED_IDOC_TYPE))
			return OrderStatusChangedObservable.getInstance();
		return null;
	}
}
