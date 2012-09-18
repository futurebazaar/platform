package com.fb.platform.ifs.model;

import java.util.ArrayList;
import java.util.List;

import com.fb.platform.ifs.to.lsp.DeliveryCenter;
import com.fb.platform.ifs.to.order.OrderItem;

public class OrderItemAvailability {

	private List<DeliveryCenter> dcs = new ArrayList<DeliveryCenter>();
	private OrderItem orderItem = new OrderItem();
	
	public OrderItemAvailability(OrderItem orderItem, List<DeliveryCenter> dcs) {
		this.orderItem = orderItem;
		this.dcs = dcs;
	}
}
