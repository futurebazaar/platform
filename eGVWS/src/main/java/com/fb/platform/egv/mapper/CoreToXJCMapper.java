/**
 * 
 */
package com.fb.platform.egv.mapper;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;

/**
 * @author keith
 *
 */
public interface CoreToXJCMapper {

	public com.fb.platform.egv.to.GiftVoucherRequest xmlToCoreRequest(String webRequestXml)  throws JAXBException;
	
	public String coreResponseToXml(com.fb.platform.egv.to.GiftVoucherResponse coreResponse) throws JAXBException,DatatypeConfigurationException;
	
}
