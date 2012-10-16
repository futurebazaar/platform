package com.fb.platform.mom.itemAck.sender.impl;

import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.fb.commons.mom.to.CancelItemTO;
import com.fb.commons.mom.to.ItemTO;
import com.fb.platform.mom.itemAck.sender.ItemAckParameters;

public class CancelInvoiceParameterImpl implements ItemAckParameters {

	@Override
	public List<NameValuePair> getParameters(List<NameValuePair> parameters, ItemTO itemAck) {
		CancelItemTO cancelItemTO  = (CancelItemTO) itemAck;
		parameters.add(new BasicNameValuePair("cancelInvoiceNumber", cancelItemTO.getCancelInvoiceNumber()));
		return parameters;
	}

}
