package com.fb.platform.mom.itemAck.sender.impl;

import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.fb.commons.mom.to.AtgDocumentItemTO;
import com.fb.commons.mom.to.ItemTO;
import com.fb.platform.mom.itemAck.sender.ItemAckParameters;

public class AtgDocumentParameterImpl implements ItemAckParameters {

	@Override
	public List<NameValuePair> getParameters(List<NameValuePair> parameters, ItemTO itemAck) {
		AtgDocumentItemTO atgDocumentItemTO  = (AtgDocumentItemTO) itemAck;
		parameters.add(new BasicNameValuePair("atgDocumentId", String.valueOf(atgDocumentItemTO.getAtgDocumentId())));
		return parameters;
	}

}
