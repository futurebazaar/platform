/**
 * 
 */
package com.fb.platform.mom.itemAck.sender;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.NameValuePair;

import com.fb.commons.mom.to.CancelItemTO;
import com.fb.commons.mom.to.ItemInvoiceTO;
import com.fb.commons.mom.to.ItemTO;
import com.fb.commons.mom.to.PgiCreationItemTO;
import com.fb.commons.mom.to.PgrCreationItemTO;
import com.fb.commons.mom.to.ReturnDeliveryTO;
import com.fb.commons.mom.to.ReturnInvoiceTO;
import com.fb.commons.mom.to.ReturnOrderTO;
import com.fb.platform.mom.itemAck.sender.impl.CancelInvoiceParameterImpl;
import com.fb.platform.mom.itemAck.sender.impl.InvoiceParameterImpl;
import com.fb.platform.mom.itemAck.sender.impl.ItemAckParameterImpl;
import com.fb.platform.mom.itemAck.sender.impl.PgiCreationParameterImpl;
import com.fb.platform.mom.itemAck.sender.impl.PgrCreationParameterImpl;
import com.fb.platform.mom.itemAck.sender.impl.ReturnDeliveryParameterImpl;
import com.fb.platform.mom.itemAck.sender.impl.ReturnInvoiceParameterImpl;
import com.fb.platform.mom.itemAck.sender.impl.ReturnOrderParameterImpl;
import com.fb.platform.mom.util.LoggerConstants;

/**
 * @author nehaga
 *
 */
public class ItemAckParameterFactory {
	
	private static Log infoLog = LogFactory.getLog(LoggerConstants.ITEM_ACK_LOG);
	
	public List<NameValuePair> getParameters(ItemTO itemAck) {
		List<NameValuePair> itemParamList = new ItemAckParameterImpl().getParameters(new ArrayList<NameValuePair>(), itemAck);
		List<NameValuePair> paramList = null;
		infoLog.info("itemAck of type : " + itemAck.getClass());
		if (PgrCreationItemTO.class.isInstance(itemAck)) {
			paramList = new PgrCreationParameterImpl().getParameters(itemParamList, itemAck);
		} else if (PgiCreationItemTO.class.isInstance(itemAck)) {
			paramList = new PgiCreationParameterImpl().getParameters(itemParamList, itemAck);
		} else if (ItemInvoiceTO.class.isInstance(itemAck)) {
			paramList = new InvoiceParameterImpl().getParameters(itemParamList, itemAck);
		} else if (CancelItemTO.class.isInstance(itemAck)) {
			paramList = new CancelInvoiceParameterImpl().getParameters(itemParamList, itemAck);
		} else if (ReturnOrderTO.class.isInstance(itemAck)) {
			paramList = new ReturnOrderParameterImpl().getParameters(itemParamList, itemAck);
		} else if (ReturnDeliveryTO.class.isInstance(itemAck)) {
			paramList = new ReturnDeliveryParameterImpl().getParameters(itemParamList, itemAck);
		} else if (ReturnInvoiceTO.class.isInstance(itemAck)) {
			paramList = new ReturnInvoiceParameterImpl().getParameters(itemParamList, itemAck);
		} else {
			paramList = itemParamList;
		}
		
		return paramList;
	}
}
