package com.fb.platform.ifs.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.fb.platform.ifs.dao.lsp.DCDao;
import com.fb.platform.ifs.model.DCAvailability;
import com.fb.platform.ifs.model.OrderItemAvailability;
import com.fb.platform.ifs.service.DCManager;
import com.fb.platform.ifs.to.lsp.DeliveryCenter;
import com.fb.platform.ifs.to.order.Order;
import com.fb.platform.ifs.to.order.OrderItem;


public class DCManagerImpl implements DCManager{

	private final static ArrayList<String> DC_PRIORITY = new ArrayList<String>(Arrays.asList("2786","9010","2644","2640","2641","2315","2330"));

	@Autowired
	private DCDao dcDao = null;
	
	public void setDcDao(DCDao dcDao) {
		this.dcDao = dcDao;
	}
	
	@Override
	public DCAvailability findStockAvailability(Order order, String pincode,
			boolean isCod) {
		DCAvailability dcAvailability = new DCAvailability();
		
		//Get orderitems
		List<OrderItem> orderItems = order.getOrderItems();
		
		//Get availability of every orderitem
		for(OrderItem orderItem : orderItems){
			//Get DC availability for this orderitem.
			List<DeliveryCenter> dcs = dcDao.getAvailability(orderItem.getSellerRateChartId(), orderItem.getQuantity());
			
			//Prioritize DCs
			//TO BE CHNAGED LATER
			List<DeliveryCenter> prioritizedDcList = new ArrayList<DeliveryCenter>(); 
			for(DeliveryCenter dc : dcs){
				for (String item : DC_PRIORITY){
					if(dc.getCode() == item && dc.getType() == "physical"){
						prioritizedDcList.add(dc);
					}
				}
			}
			
			for(DeliveryCenter dc : dcs){
				for (String item : DC_PRIORITY){
					if(dc.getCode() == item && dc.getType() != "physical"){
						prioritizedDcList.add(dc);
					}
				}
			}
			
			//Add orderitem availability
			OrderItemAvailability orderItemAvailability = new OrderItemAvailability(orderItem, prioritizedDcList);
			dcAvailability.add(orderItemAvailability);
			
		}
		
		return dcAvailability;
	}
	
}
