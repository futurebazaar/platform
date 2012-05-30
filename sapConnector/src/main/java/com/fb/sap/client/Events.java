package com.fb.sap.client;

import java.util.Observable;

public class Events {

	public static Observable DELIVERY_DELETED_EVENT = DeliveryDeletedObservable.getInstance();
	public static Observable ORDER_STATUS_CHANGED = OrderStatusChangedObservable.getInstance();
}
