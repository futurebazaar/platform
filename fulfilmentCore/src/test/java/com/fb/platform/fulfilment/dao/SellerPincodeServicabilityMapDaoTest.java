package com.fb.platform.fulfilment.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.fb.commons.test.BaseTestCase;
import com.fb.platform.fulfilment.model.SellerPincodeServicabilityMap;
import com.fb.platform.fulfilment.service.NonServicablePincodeException;

public class SellerPincodeServicabilityMapDaoTest extends BaseTestCase{

	@Autowired
	private SellerPincodeServicabilityMapDao sellerPincodeMapDao;
	
	@Test
	public void getSingleSellerForPincode() {
		
		SellerPincodeServicabilityMap sellerPincodeMap = sellerPincodeMapDao.getSellersForPincode("400001");
		
		assertNotNull(sellerPincodeMap);

		assertEquals("400001", sellerPincodeMap.getPincode());
		List<Integer> seller_list = new ArrayList();
		seller_list.add(-10500);
		assertTrue(seller_list.containsAll(sellerPincodeMap.getSellerId()));

	}
	
	@Test
	public void getMultipleSellersForPincode() {
		
		SellerPincodeServicabilityMap sellerPincodeMap = sellerPincodeMapDao.getSellersForPincode("400002");
		
		assertNotNull(sellerPincodeMap);

		assertEquals("400002", sellerPincodeMap.getPincode());
		List<Integer> seller_list = new ArrayList();
		seller_list.add(-10501);
		seller_list.add(-10502);
		assertTrue(seller_list.containsAll(sellerPincodeMap.getSellerId()));

	}
	
	@Test(expected=NonServicablePincodeException.class)
	public void nonservicablePincode() {
		
		SellerPincodeServicabilityMap sellerPincodeMap = sellerPincodeMapDao.getSellersForPincode("400003");
		
	}
}
