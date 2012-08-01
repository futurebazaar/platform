package com.fb.platform.mom.itemAck.sender.impl;

import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.fb.commons.mom.to.ItemTO;
import com.fb.commons.mom.to.ReturnOrderTO;
import com.fb.platform.mom.itemAck.sender.ItemAckParameters;

public class ReturnOrderParameterImpl implements ItemAckParameters {

	@Override
	public List<NameValuePair> getParameters(List<NameValuePair> parameters, ItemTO itemAck) {
		ReturnOrderTO returnOrderTO = (ReturnOrderTO) itemAck;
		parameters.add(new BasicNameValuePair("returnOrderId", returnOrderTO.getOrderId()));
		if(returnOrderTO.getQuantity() != null) {
			parameters.add(new BasicNameValuePair("returnQuantity", returnOrderTO.getQuantity().toString()));
		}
		parameters.add(new BasicNameValuePair("returnStorageLocation", returnOrderTO.getStorageLocation()));
		parameters.add(new BasicNameValuePair("returnCategory", returnOrderTO.getCategory()));
		return parameters;
	}

}
