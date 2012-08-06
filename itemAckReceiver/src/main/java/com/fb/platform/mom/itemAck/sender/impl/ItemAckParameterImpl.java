package com.fb.platform.mom.itemAck.sender.impl;

import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.fb.commons.mom.to.ItemTO;
import com.fb.platform.mom.itemAck.sender.ItemAckParameters;

public class ItemAckParameterImpl implements ItemAckParameters {

	@Override
	public List<NameValuePair> getParameters(List<NameValuePair> parameters, ItemTO itemAck) {
		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
		parameters.add(new BasicNameValuePair("sapDocumentId", String.valueOf(itemAck.getSapDocumentId())));
		parameters.add(new BasicNameValuePair("orderHeaderDelBlock", itemAck.getOrderHeaderDelBlock()));
		parameters.add(new BasicNameValuePair("header", itemAck.getHeader()));
		if(itemAck.getDeliveryDate() != null) {
			parameters.add(new BasicNameValuePair("deliveryDate", dateFormatter.format(itemAck.getDeliveryDate().toDate())));
		}
		parameters.add(new BasicNameValuePair("UOM", itemAck.getUnitOfMeasurement()));
		parameters.add(new BasicNameValuePair("shipComments", itemAck.getShipmentComments()));
		parameters.add(new BasicNameValuePair("orderType", itemAck.getOrderType()));
		parameters.add(new BasicNameValuePair("deliveryNumber", itemAck.getDeliveryNumber()));
		parameters.add(new BasicNameValuePair("blockMsg", itemAck.getBlockMsg()));
		parameters.add(new BasicNameValuePair("itemCategory", itemAck.getItemCategory()));
		parameters.add(new BasicNameValuePair("orderId", itemAck.getOrderId()));
		parameters.add(new BasicNameValuePair("deliveryType", itemAck.getDeliveryType()));
		parameters.add(new BasicNameValuePair("lspName", itemAck.getLspName()));
		parameters.add(new BasicNameValuePair("awbNumber", itemAck.getAwbNumber()));
		parameters.add(new BasicNameValuePair("createdBy", itemAck.getCreatedBy()));
		if(itemAck.getCreatedDate() != null) {
			parameters.add(new BasicNameValuePair("createdDate", dateFormatter.format(itemAck.getCreatedDate().toDate())));
		}
		parameters.add(new BasicNameValuePair("skuID", itemAck.getSkuID()));
		parameters.add(new BasicNameValuePair("lspUpdDescr", itemAck.getLspUpdateDesc()));
		parameters.add(new BasicNameValuePair("plantId", itemAck.getPlantId()));
		parameters.add(new BasicNameValuePair("itemState", itemAck.getItemState()));
		if(itemAck.getOrderDate() != null) {
			parameters.add(new BasicNameValuePair("orderDate",dateFormatter.format(itemAck.getOrderDate().toDate())));
		}
		parameters.add(new BasicNameValuePair("quantity", String.valueOf(itemAck.getQuantity().intValue())));
		if(itemAck.getSapIdoc() != null) {
			parameters.add(new BasicNameValuePair("idocnumber", itemAck.getSapIdoc().getIdocNumber()));
		}
		return parameters;
	}

}
