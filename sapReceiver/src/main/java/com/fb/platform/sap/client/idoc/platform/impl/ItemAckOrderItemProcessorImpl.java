/**
 * 
 */
package com.fb.platform.sap.client.idoc.platform.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fb.commons.mom.to.ItemTO;
import com.fb.commons.mom.to.OrderStateEnum;
import com.fb.platform.sap.client.idoc.exception.NoOrderItemFoundException;
import com.fb.platform.sap.client.idoc.platform.ItemAckOrderItemProcessor;
import com.fb.platform.sap.idoc.generated.zatgflow.ZATGFLOW;
import com.fb.platform.sap.idoc.itemAck.ItemAckIdocMapperFactory;
import com.fb.platform.sap.idoc.itemAck.impl.ItemAckIdocMapperImpl;

/**
 * @author nehaga
 *
 */
public class ItemAckOrderItemProcessorImpl implements ItemAckOrderItemProcessor {
	
	private Map<Integer, ItemTO> cStateOrderItemMap = new HashMap<Integer, ItemTO>();
	private ItemAckIdocMapperFactory mapperFactory = new ItemAckIdocMapperFactory();
	
	private static Log logger = LogFactory.getLog(ItemAckOrderItemProcessorImpl.class);

	@Override
	public List<ItemTO> getOrderItems(List<ZATGFLOW> ackList) {
		return  getHighestPriorityOrderItems(ackList);
	}
	
	public List<ItemTO> getHighestPriorityOrderItems(List<ZATGFLOW> ackList) {
		
		/*
		 * create the map of all the order items for the order in the idoc
		 */
		createOrderMap(ackList);
		
		Map<Integer, ItemTO> highestStateOrderItemMap = new HashMap<Integer, ItemTO>(cStateOrderItemMap);
		
		
		ItemTO itemAck = null;
		OrderStateEnum orderState = null;
		
		for(ZATGFLOW sapItemAck : ackList) {
			itemAck = null;
			orderState = getOrderState(sapItemAck.getVBTYPN());
			if(orderState != OrderStateEnum.C) {
				ItemTO orderItem = getCStateOrderItem(sapItemAck);
				if(orderItem == null) {
					throw new NoOrderItemFoundException("No Order Item found for order number : " + sapItemAck.getSOVBELN() + " , item number : " + sapItemAck.getSOPOSNR());
				}
				ItemTO highestStateOrderItem = highestStateOrderItemMap.get(orderItem.getAtgDocumentId());
				if(isAckHigherPriority(highestStateOrderItem.getOrderState(), sapItemAck.getVBTYPN())) {
					itemAck = mapperFactory.getItemAck(sapItemAck, orderItem);
					if(itemAck != null) {
						logger.info("Order Id : " + sapItemAck.getSOVBELN() + " , item number : " + sapItemAck.getSOPOSNR() + " , order state : " + sapItemAck.getVBTYPN());
						highestStateOrderItemMap.put(itemAck.getAtgDocumentId(), itemAck);
					}
				}
			}
		}
		return new ArrayList<ItemTO>(highestStateOrderItemMap.values());
	}
	
	private boolean isAckHigherPriority(String highOrderState, String newOrderState) {
		OrderStateEnum highOrderStateEnum = OrderStateEnum.getInstance(highOrderState);
		OrderStateEnum newOrderStateEnum = OrderStateEnum.getInstance(newOrderState);
		return (highOrderStateEnum.getPriority() <= newOrderStateEnum.getPriority());
	}
	
	private ItemTO getCStateOrderItem(ZATGFLOW sapItemAck) {
		int orderItemNumber = sapItemAck.getSOPOSNR();
		String orderId = sapItemAck.getSOVBELN();
		ItemTO orderItem = cStateOrderItemMap.get(orderItemNumber);
		if(orderItem != null && !orderId.equals(orderItem.getOrderId())) {
			throw new NoOrderItemFoundException("No Order Item found for order number : " + orderId + " , item number : " + orderItemNumber);
		}
		return orderItem;
	}
	
	private OrderStateEnum getOrderState(String sapOrderState) {
		return OrderStateEnum.getInstance(sapOrderState);
	}
	
	/**
	 * create the map of all the order items for the order in the idoc
	 */
	private void createOrderMap(List<ZATGFLOW> ackList) {
		
		ItemTO itemAck = null;
		OrderStateEnum orderState = null;
		ItemTO orderItem = null;
		
		for(ZATGFLOW sapItemAck : ackList) {
			orderState = getOrderState(sapItemAck.getVBTYPN());
			if(orderState == OrderStateEnum.C && sapItemAck.getSOPOSNR() > 0) {
				orderItem = getCStateOrderItem(sapItemAck);
				if(orderItem == null) {
					orderItem = new ItemTO();
				}
				itemAck = new ItemAckIdocMapperImpl().updateItemAck(sapItemAck, orderItem);
				if(!cStateOrderItemMap.containsKey(itemAck.getAtgDocumentId())) {
					cStateOrderItemMap.put(itemAck.getAtgDocumentId(), itemAck);
				}
			}
		}
	}
}
