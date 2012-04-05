/**
 * 
 */
package com.fb.platform.ifs.mapper;

import com.fb.platform.ifs.domain.SingleArticleServiceabilityRequestBo;
import com.fb.platform.ifs.manager.model.SingleArticleServiceabilityRequestTO;

/**
 * @author sarvesh
 *
 */
public class IFSToBoMapper {
	
	public SingleArticleServiceabilityRequestBo updateBo(SingleArticleServiceabilityRequestTO serviceabilityRequestTO) {
		SingleArticleServiceabilityRequestBo serviceabilityRequestBo = new SingleArticleServiceabilityRequestBo();
		serviceabilityRequestBo.setClient(serviceabilityRequestTO.getClient());
		serviceabilityRequestBo.setPincode(serviceabilityRequestTO.getPincode());
		serviceabilityRequestBo.setRateChartId(serviceabilityRequestTO.getRateChartId());
		serviceabilityRequestBo.setArticleId(serviceabilityRequestTO.getArticleId());
		serviceabilityRequestBo.setItemPrice(serviceabilityRequestTO.getItemPrice());
		serviceabilityRequestBo.setQty(serviceabilityRequestTO.getQty());
		serviceabilityRequestBo.setCod(serviceabilityRequestTO.getIsCod()==1);
		serviceabilityRequestBo.setVendorId(serviceabilityRequestTO.getVendorId());
		return serviceabilityRequestBo;
	}

}
