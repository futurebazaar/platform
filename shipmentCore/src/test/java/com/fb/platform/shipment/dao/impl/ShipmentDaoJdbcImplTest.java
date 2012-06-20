/**
 * 
 */
package com.fb.platform.shipment.dao.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;

import com.fb.commons.test.BaseTestCase;
import com.fb.commons.to.Money;
import com.fb.platform.shipment.to.ParcelItem;

/**
 * @author nehaga
 *
 */
public class ShipmentDaoJdbcImplTest extends BaseTestCase {

	@Autowired
	ShipmentDaoJdbcImpl shipmentdao; 
	
	@Test
	public void getParcelDetails() {
		ParcelItem parcel = shipmentdao.getParcelDetails("20000");
		assertEquals("shipment test 1", parcel.getCustomerName());
		assertEquals("my delivery test add", parcel.getAddress());
		assertEquals("Hyderabad", parcel.getCity());
		assertEquals("Andhra Pradesh", parcel.getState());
		assertEquals("India", parcel.getCountry());
		assertEquals("500072", parcel.getPincode());
		assertEquals(new Long("22222222222").longValue(), parcel.getPhoneNumber());
		assertTrue(parcel.getAmountPayable().eq(new Money(new BigDecimal(33200))));
		assertEquals("card-web", parcel.getPaymentMode());
	}
	
	@Test(expected=EmptyResultDataAccessException.class)
	public void invalidOrderRef() {
		ParcelItem parcel = shipmentdao.getParcelDetails("20001");
	}
}
