package com.fb.platform.mom.itemAck.sender;

import java.util.List;

import org.apache.http.NameValuePair;

import com.fb.commons.mom.to.ItemTO;

/**
 * @author nehaga
 *
 */
public interface ItemAckParameters {
	public List<NameValuePair> getParameters(List<NameValuePair> parameters, ItemTO itemAck);
}
