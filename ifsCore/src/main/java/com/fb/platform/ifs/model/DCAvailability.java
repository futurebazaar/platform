package com.fb.platform.ifs.model;

import java.util.ArrayList;
import java.util.List;


public class DCAvailability {
	List<OrderItemAvailability> orderItemAvailability = new ArrayList<OrderItemAvailability>();
	
	public void add(OrderItemAvailability orderItemAvailability){
		this.orderItemAvailability.add(orderItemAvailability);
	}
}
