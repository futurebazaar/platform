package com.fb.platform.fulfilment.manager.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.fb.commons.test.BaseTestCase;
import com.fb.platform.fulfilment.cache.SellerPincodeMapCacheAccess;
import com.fb.platform.fulfilment.model.SellerPincodeServicabilityMap;
import com.fb.platform.fulfilment.service.FulfilmentManager;
import com.fb.platform.fulfilment.to.SellerByPincodeRequest;
import com.fb.platform.fulfilment.to.SellerByPincodeResponse;
import com.fb.platform.fulfilment.to.SellerByPincodeResponseStatusEnum;

public class FulfilmentManagerImplTest extends BaseTestCase{
	
	@Autowired
	private FulfilmentManager fulfilmentManager = null;
	
	@Autowired
	SellerPincodeMapCacheAccess sellerPincodeMapCache = null;
	
	@Test
	public void getSingleSellerForPincode(){
		
		//Fresh request, so will hit database, get the result and also cache it.
		SellerByPincodeRequest sellerByPincodeRequest = new SellerByPincodeRequest();
		
		sellerByPincodeRequest.setPincode("400001");
		
		//Clear the cache if any
		SellerPincodeServicabilityMap sellerPincodeMap = sellerPincodeMapCache.get("400001");
		if(sellerPincodeMap != null){
			sellerPincodeMapCache.clear("400001");
		}
		
		SellerByPincodeResponse sellerByPincodeResponse = fulfilmentManager.getSellerByPincode(sellerByPincodeRequest);
		
		assertNotNull(sellerByPincodeResponse);
		
		assertEquals(SellerByPincodeResponseStatusEnum.SUCCESS.getStatus(), sellerByPincodeResponse.getStatusCode());
		
		assertEquals("400001", sellerByPincodeResponse.getPincode());
		List<Integer> seller_list = new ArrayList();
		seller_list.add(-10500);
		assertTrue(seller_list.containsAll(sellerByPincodeResponse.getSellerId()));
		
		//Now, as the result is already cached, result will be fetched from cache.
		sellerByPincodeResponse = fulfilmentManager.getSellerByPincode(sellerByPincodeRequest);
		
		assertNotNull(sellerByPincodeResponse);
		
		assertEquals(SellerByPincodeResponseStatusEnum.SUCCESS.getStatus(), sellerByPincodeResponse.getStatusCode());
		
		assertEquals("400001", sellerByPincodeResponse.getPincode());
		seller_list = new ArrayList();
		seller_list.add(-10500);
		assertTrue(seller_list.containsAll(sellerByPincodeResponse.getSellerId()));
		
		//Clear the cache
		sellerPincodeMap = sellerPincodeMapCache.get("400001");
		if(sellerPincodeMap != null){
			sellerPincodeMapCache.clear("400001");
		}
	}
	
	@Test
	public void getMultipleSellersForPincode() {
		
		//Fresh request, so will hit database, get the result and also cache it.
		SellerByPincodeRequest sellerByPincodeRequest = new SellerByPincodeRequest();
		
		sellerByPincodeRequest.setPincode("400002");
		
		SellerByPincodeResponse sellerByPincodeResponse = fulfilmentManager.getSellerByPincode(sellerByPincodeRequest);
		
		assertNotNull(sellerByPincodeResponse);
		
		assertEquals(SellerByPincodeResponseStatusEnum.SUCCESS.getStatus(), sellerByPincodeResponse.getStatusCode());
		
		assertEquals("400002", sellerByPincodeResponse.getPincode());
		List<Integer> seller_list = new ArrayList();
		seller_list.add(-10501);
		seller_list.add(-10502);
		assertTrue(seller_list.containsAll(sellerByPincodeResponse.getSellerId()));
		
		//Now, as the result is already cached, result will be fetched from cache.
		sellerByPincodeResponse = fulfilmentManager.getSellerByPincode(sellerByPincodeRequest);
		
		assertNotNull(sellerByPincodeResponse);
		
		assertEquals(SellerByPincodeResponseStatusEnum.SUCCESS.getStatus(), sellerByPincodeResponse.getStatusCode());
		
		assertEquals("400002", sellerByPincodeResponse.getPincode());
		seller_list = new ArrayList();
		seller_list.add(-10501);
		seller_list.add(-10502);
		assertTrue(seller_list.containsAll(sellerByPincodeResponse.getSellerId()));
		
	}
	
	@Test
	public void nonservicablePincode() {
		
		SellerByPincodeRequest sellerByPincodeRequest = new SellerByPincodeRequest();
		
		sellerByPincodeRequest.setPincode("400003");
		
		SellerByPincodeResponse sellerByPincodeResponse = fulfilmentManager.getSellerByPincode(sellerByPincodeRequest);
		
		assertNotNull(sellerByPincodeResponse);
		
		assertEquals(SellerByPincodeResponseStatusEnum.NONSERVICABLE_PINCODE.getStatus(), sellerByPincodeResponse.getStatusCode());
	}
	
}
