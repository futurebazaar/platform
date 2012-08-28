package com.fb.platform.mom.itemAck.sender.impl;

import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.fb.commons.mom.to.ItemTO;
import com.fb.commons.mom.to.PgrCreationItemTO;
import com.fb.platform.mom.itemAck.sender.ItemAckParameters;

public class PgrCreationParameterImpl implements ItemAckParameters {

	@Override
	public List<NameValuePair> getParameters(List<NameValuePair> parameters, ItemTO itemAck) {
		PgrCreationItemTO pgrCreationTO = (PgrCreationItemTO) itemAck;
		if(pgrCreationTO.getPgrCreationDate() != null) {
			parameters.add(new BasicNameValuePair("pgrCreationDate", new SimpleDateFormat("dd-MM-yyyy").format(pgrCreationTO.getPgrCreationDate().toDate())));
		}
		return parameters;
	}

}
