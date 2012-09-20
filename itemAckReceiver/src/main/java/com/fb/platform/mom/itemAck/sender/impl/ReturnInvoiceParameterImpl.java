package com.fb.platform.mom.itemAck.sender.impl;

import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.fb.commons.mom.to.ItemTO;
import com.fb.commons.mom.to.ReturnInvoiceTO;
import com.fb.platform.mom.itemAck.sender.ItemAckParameters;

public class ReturnInvoiceParameterImpl implements ItemAckParameters {

	@Override
	public List<NameValuePair> getParameters(List<NameValuePair> parameters, ItemTO itemAck) {
		ReturnInvoiceTO returnInvoiceTO = (ReturnInvoiceTO) itemAck;
		parameters.add(new BasicNameValuePair("retInvoiceNo", returnInvoiceTO.getNumber()));
		parameters.add(new BasicNameValuePair("retID", returnInvoiceTO.getReturnId()));
		parameters.add(new BasicNameValuePair("returnInvoiceType", returnInvoiceTO.getType()));
		if(returnInvoiceTO.getInvoiceDate() != null) {
			parameters.add(new BasicNameValuePair("returnInvoiceDate", new SimpleDateFormat("dd-MM-yyyy").format(returnInvoiceTO.getInvoiceDate().toDate())));
		}
		parameters.add(new BasicNameValuePair("returnInvoiceNet", returnInvoiceTO.getInvoiceNet()));
		return parameters;
	}

}
