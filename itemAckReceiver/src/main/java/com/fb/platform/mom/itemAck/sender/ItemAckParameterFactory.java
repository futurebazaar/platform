/**
 * 
 */
package com.fb.platform.mom.itemAck.sender;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;

import com.fb.commons.mom.to.AtgDocumentItemTO;
import com.fb.commons.mom.to.ItemTO;
import com.fb.platform.mom.itemAck.sender.impl.AtgDocumentParameterImpl;
import com.fb.platform.mom.itemAck.sender.impl.CancelInvoiceParameterImpl;
import com.fb.platform.mom.itemAck.sender.impl.InvoiceParameterImpl;
import com.fb.platform.mom.itemAck.sender.impl.ItemAckParameterImpl;
import com.fb.platform.mom.itemAck.sender.impl.PgiCreationParameterImpl;
import com.fb.platform.mom.itemAck.sender.impl.PgrCreationParameterImpl;
import com.fb.platform.mom.itemAck.sender.impl.ReturnDeliveryParameterImpl;
import com.fb.platform.mom.itemAck.sender.impl.ReturnOrderParameterImpl;

/**
 * @author nehaga
 *
 */
public class ItemAckParameterFactory {
	
	public List<NameValuePair> getParameters(ItemTO itemAck) {
		List<NameValuePair> itemParamList = new ItemAckParameterImpl().getParameters(new ArrayList<NameValuePair>(), itemAck);
		List<NameValuePair> paramList = null;
		if(itemAck.getClass().isInstance(AtgDocumentItemTO.class)) {
			paramList = new AtgDocumentParameterImpl().getParameters(itemParamList, itemAck);
		} else if (itemAck.getClass().isInstance(PgrCreationParameterImpl.class)) {
			paramList = new PgrCreationParameterImpl().getParameters(itemParamList, itemAck);
		} else if (itemAck.getClass().isInstance(PgiCreationParameterImpl.class)) {
			paramList = new PgiCreationParameterImpl().getParameters(itemParamList, itemAck);
		} else if (itemAck.getClass().isInstance(InvoiceParameterImpl.class)) {
			paramList = new InvoiceParameterImpl().getParameters(itemParamList, itemAck);
		} else if (itemAck.getClass().isInstance(CancelInvoiceParameterImpl.class)) {
			paramList = new CancelInvoiceParameterImpl().getParameters(itemParamList, itemAck);
		} else if (itemAck.getClass().isInstance(ReturnOrderParameterImpl.class)) {
			paramList = new ReturnOrderParameterImpl().getParameters(itemParamList, itemAck);
		} else if (itemAck.getClass().isInstance(ReturnDeliveryParameterImpl.class)) {
			paramList = new ReturnDeliveryParameterImpl().getParameters(itemParamList, itemAck);
		} else if (itemAck.getClass().isInstance(ReturnOrderParameterImpl.class)) {
			paramList = new ReturnOrderParameterImpl().getParameters(itemParamList, itemAck);
		} else {
			paramList = itemParamList;
		}
		
		return paramList;
	}
}
