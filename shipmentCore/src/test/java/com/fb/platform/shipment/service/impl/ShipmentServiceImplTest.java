/**
 * 
 */
package com.fb.platform.shipment.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.fb.commons.test.BaseTestCase;
import com.fb.commons.to.Money;
import com.fb.platform.shipment.exception.DataNotFoundException;
import com.fb.platform.shipment.service.ShipmentService;
import com.fb.platform.shipment.to.GatePassItem;
import com.fb.platform.shipment.to.ParcelItem;

/**
 * @author nehaga
 *
 */
public class ShipmentServiceImplTest extends BaseTestCase {

	@Autowired
	ShipmentService shipmentService;
	
	@Test
	public void getParcelDetails() {
		GatePassItem deliveryItem = new GatePassItem();
		deliveryItem.setAwbNo("M0653351");
		deliveryItem.setDeece("2786-BHIWANDI");
		deliveryItem.setDelNo("100");
		deliveryItem.setDelWt(new BigDecimal(0.500));
		deliveryItem.setItemDescription("description 0");
		deliveryItem.setOrderReferenceId("20000");
		deliveryItem.setQuantity(1);
		
		ParcelItem parcel = shipmentService.getParcelDetails(deliveryItem);
		
		assertEquals("shipment test 1", parcel.getCustomerName());
		assertEquals("my delivery test add", parcel.getAddress());
		assertEquals("Hyderabad", parcel.getCity());
		assertEquals("Andhra Pradesh", parcel.getState());
		assertEquals("India", parcel.getCountry());
		assertEquals("500072", parcel.getPincode());
		assertEquals(new Long("22222222222").longValue(), parcel.getPhoneNumber());
		assertTrue(parcel.getAmountPayable().eq(new Money(new BigDecimal(33200))));
		assertEquals("card-web", parcel.getPaymentMode());
		assertEquals("M0653351", parcel.getTrackingNumber());
		assertEquals("2786-BHIWANDI", parcel.getDeliverySiteId());
		assertEquals("100", parcel.getDeliveryNumber());
		assertEquals(new BigDecimal(0.500), parcel.getWeight());
		assertEquals("description 0", parcel.getArticleDescription());
		assertEquals(1, parcel.getQuantity());
	}
	
	@Test(expected=DataNotFoundException.class)
	public void invalidParcelDetails() {
		GatePassItem deliveryItem = new GatePassItem();
		deliveryItem.setOrderReferenceId("20001");
		
		ParcelItem parcel = shipmentService.getParcelDetails(deliveryItem);
	}
	
}
