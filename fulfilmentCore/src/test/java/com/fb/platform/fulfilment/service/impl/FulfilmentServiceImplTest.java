package com.fb.platform.fulfilment.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.fb.commons.test.BaseTestCase;
import com.fb.platform.fulfilment.model.SellerPincodeServicabilityMap;
import com.fb.platform.fulfilment.service.FulfilmentService;
import com.fb.platform.fulfilment.service.NonServicablePincodeException;

public class FulfilmentServiceImplTest extends BaseTestCase{
	
	@Autowired
	private FulfilmentService fulfilmentService = null;

	@Test
	public void getSingleSellerForPincode(){
		
		//Fresh request, so will hit database, get the result and also cache it.
		SellerPincodeServicabilityMap sellerPincodeMapFromDB = fulfilmentService.getSellersForPincode("400001");
		
		assertNotNull(sellerPincodeMapFromDB);
		
		assertEquals("400001", sellerPincodeMapFromDB.getPincode());
		List<Integer> seller_list = new ArrayList();
		seller_list.add(-10500);
		assertTrue(seller_list.containsAll(sellerPincodeMapFromDB.getSellerId()));
		
		//Now, as the result is already cached, result will be fetched from cache.
		SellerPincodeServicabilityMap sellerPincodeMapFromCache = fulfilmentService.getSellersForPincode("400001");
		
		assertNotNull(sellerPincodeMapFromCache);
		
		assertEquals("400001", sellerPincodeMapFromCache.getPincode());
		seller_list = new ArrayList();
		seller_list.add(-10500);
		assertTrue(seller_list.containsAll(sellerPincodeMapFromCache.getSellerId()));
	}
	
	@Test
	public void getMultipleSellersForPincode() {
		
		//Fresh request, so will hit database, get the result and also cache it.
		SellerPincodeServicabilityMap sellerPincodeMapFromDB = fulfilmentService.getSellersForPincode("400002");
		
		assertNotNull(sellerPincodeMapFromDB);

		assertEquals("400002", sellerPincodeMapFromDB.getPincode());
		List<Integer> seller_list = new ArrayList();
		seller_list.add(-10501);
		seller_list.add(-10502);
		assertTrue(seller_list.containsAll(sellerPincodeMapFromDB.getSellerId()));

		//Now, as the result is already cached, result will be fetched from cache.
		SellerPincodeServicabilityMap sellerPincodeMapFromCache = fulfilmentService.getSellersForPincode("400002");
		
		assertNotNull(sellerPincodeMapFromCache);

		assertEquals("400002", sellerPincodeMapFromCache.getPincode());
		seller_list = new ArrayList();
		seller_list.add(-10501);
		seller_list.add(-10502);
		assertTrue(seller_list.containsAll(sellerPincodeMapFromCache.getSellerId()));
	}
	
	@Test(expected=NonServicablePincodeException.class)
	public void nonservicablePincode() {
		
		SellerPincodeServicabilityMap sellerPincodeMap = fulfilmentService.getSellersForPincode("400003");
		
	}
}
