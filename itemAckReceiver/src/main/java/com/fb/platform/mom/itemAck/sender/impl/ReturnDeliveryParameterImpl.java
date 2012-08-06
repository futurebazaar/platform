package com.fb.platform.mom.itemAck.sender.impl;

import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.fb.commons.mom.to.ItemTO;
import com.fb.commons.mom.to.ReturnDeliveryTO;
import com.fb.platform.mom.itemAck.sender.ItemAckParameters;

public class ReturnDeliveryParameterImpl implements ItemAckParameters {

	@Override
	public List<NameValuePair> getParameters(List<NameValuePair> parameters, ItemTO itemAck) {
		ReturnDeliveryTO returnDeliveryTO = (ReturnDeliveryTO) itemAck;
		parameters.add(new BasicNameValuePair("returndeliveryno", returnDeliveryTO.getNumber()));
		parameters.add(new BasicNameValuePair("returndeliveryType", returnDeliveryTO.getType()));
		if(returnDeliveryTO.getReturnCreatedDate() != null) {
			parameters.add(new BasicNameValuePair("returnCreatedDate", new SimpleDateFormat("dd-MM-yyyy").format(returnDeliveryTO.getReturnCreatedDate())));
		}
		parameters.add(new BasicNameValuePair("returnCreatedBy", returnDeliveryTO.getReturnCreatedBy()));
		return parameters;
	}

}
