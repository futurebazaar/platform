package com.fb.platform.mom.itemAck.sender.impl;

import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.fb.commons.mom.to.ItemTO;
import com.fb.commons.mom.to.PgiCreationItemTO;
import com.fb.platform.mom.itemAck.sender.ItemAckParameters;

public class PgiCreationParameterImpl implements ItemAckParameters {

	@Override
	public List<NameValuePair> getParameters(List<NameValuePair> parameters, ItemTO itemAck) {
		PgiCreationItemTO pgiCreationItemTO  = (PgiCreationItemTO) itemAck;
		if(pgiCreationItemTO.getPgiCreationDate() != null) {
			parameters.add(new BasicNameValuePair("pgiCreationDate", new SimpleDateFormat("dd-MM-yyyy").format(pgiCreationItemTO.getPgiCreationDate())));
		}
		return parameters;
	}

}
