package com.fb.platform.fulfilment.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fb.commons.PlatformException;
import com.fb.platform.fulfilment.model.SellerPincodeServicabilityMap;
import com.fb.platform.fulfilment.service.FulfilmentManager;
import com.fb.platform.fulfilment.service.FulfilmentService;
import com.fb.platform.fulfilment.service.NonServicablePincodeException;
import com.fb.platform.fulfilment.to.SellerByPincodeRequest;
import com.fb.platform.fulfilment.to.SellerByPincodeResponse;
import com.fb.platform.fulfilment.to.SellerByPincodeResponseStatusEnum;

public class FulfilmentManagerImpl implements FulfilmentManager{

	private static Log logger = LogFactory.getLog(FulfilmentManagerImpl.class);

	@Autowired
	private FulfilmentService fulfilmentService = null;

	@Override
	public SellerByPincodeResponse getSellerByPincode(SellerByPincodeRequest request) {
		if(logger.isDebugEnabled()) {
			logger.debug("Getting sellers for pincode : " + request.getPincode());
		}
		
		SellerByPincodeResponse response = new SellerByPincodeResponse();

		SellerPincodeServicabilityMap sellerPincodeMap = null;

		try {
			//find the sellers servicing given pincode.
			sellerPincodeMap = fulfilmentService.getSellersForPincode(request.getPincode());
			
			response.setPincode(sellerPincodeMap.getPincode());
			response.setSellerId(sellerPincodeMap.getSellerId());
			
			response.setStatus(SellerByPincodeResponseStatusEnum.SUCCESS);
			
			if(logger.isDebugEnabled()) {
				logger.debug("For pincode : " + request.getPincode() + ", servicable sellers are : " + sellerPincodeMap);
			}
			
		} catch (NonServicablePincodeException e) {
			//we dont service given pincode
			response.setPincode(request.getPincode());
			response.setStatus(SellerByPincodeResponseStatusEnum.NONSERVICABLE_PINCODE);
		} catch (PlatformException e) {
			logger.error("Error while getting seller for pincode : " + request.getPincode(), e);
			response.setPincode(request.getPincode());
			response.setStatus(SellerByPincodeResponseStatusEnum.INTERNAL_ERROR);
		}

		return response;
	}
	
}
