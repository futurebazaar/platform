package com.fb.platform.mom.itemAck.sender.impl;

import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.fb.commons.mom.to.ItemInvoiceTO;
import com.fb.commons.mom.to.ItemTO;
import com.fb.platform.mom.itemAck.sender.ItemAckParameters;

public class InvoiceParameterImpl implements ItemAckParameters {

	@Override
	public List<NameValuePair> getParameters(List<NameValuePair> parameters, ItemTO itemAck) {
		ItemInvoiceTO itemInvoiceTO = (ItemInvoiceTO) itemAck;
		parameters.add(new BasicNameValuePair("invoiceNumber", itemInvoiceTO.getInvoiceNumber()));
		if(itemInvoiceTO.getInvoiceDate() != null) {
			parameters.add(new BasicNameValuePair("invoiceDate", new SimpleDateFormat("dd-MM-yyyy").format(itemInvoiceTO.getInvoiceDate().toDate())));
		}
		return parameters;
	}

}
