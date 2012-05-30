package com.fb.tinla.eventhandlers;


import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import com.fb.sap.idocs.OrderIDoc;
import com.fb.sap.tinla.idocadapters.OrderIDocOrderAdapter;
import com.fb.tinla.models.Order;
import com.fb.tinla.models.OrderItem;
import com.fb.tinla.models.Return;
import com.fb.tinla.models.Shipment;

public class OrderStatusChangedEventHandler extends EventHandler implements Observer{
	
	private static OrderStatusChangedEventHandler INSTANCE = new OrderStatusChangedEventHandler();
	
	public static OrderStatusChangedEventHandler getInstance() {
		return INSTANCE;
	}
	
	private OrderStatusChangedEventHandler() {
		super("/item_ack/");
	}

	public void onOrderStatusChanged(Order order) throws Exception {
		
		Iterator<OrderItem> itemIterator = order.getOrderItems().iterator();
		while(itemIterator.hasNext()) {
			OrderItem orderItem = itemIterator.next();
			Map<String, String> params = new HashMap<String, String>();
			params.put("orderId", order.getOrderId());
			params.put("header", "");
			params.put("atgDocumentId", orderItem.getLineItemNumber());
			params.put("quantity", orderItem.getQuantity());
			params.put("itemState", orderItem.getStatus());
			
			if(orderItem.getShipment() != null) {
				Shipment shipment = orderItem.getShipment();
				params.put("plantId", shipment.getDcCode());
				params.put("deliveryNumber", shipment.getDeliveryNumber());
				params.put("deliveryDate", shipment.getDeliveryCreatedDate());
				params.put("invoiceNumber", shipment.getInvoiceNumber());
				params.put("invoiceDate", shipment.getInvoiceCreatedDate());
				params.put("invoiceNetValue", shipment.getInvoiceNetValue());
				params.put("pgiCreationDate", shipment.getPgiCreatedDate());
				params.put("awbNumber", shipment.getTrackingNumber());
				params.put("lspName", shipment.getLspName());
			}
			
			if(orderItem.getReturn() != null) {
				Return ret = new Return();
				params.put("retID", ret.getReturnId());
				params.put("retCreatedDate", ret.getCreatedDate());
				params.put("retInvoiceDate", ret.getInvoiceDate());
				params.put("pgrCreationDate", ret.getPgrCreatedDate());
			}
			
			this.sendEventToTinla(params);
		}
	}

	@Override
	public void update(Observable o, Object obj) {
		// TODO Auto-generated method stub
				try {
					OrderIDocOrderAdapter adapter = new OrderIDocOrderAdapter((OrderIDoc)obj);
					this.onOrderStatusChanged(adapter);
				} catch (Exception e) {
					// TODO Log this exception
					e.printStackTrace();
				}
	}

}
