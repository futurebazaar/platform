package com.fb.platform.ifs.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.fb.commons.test.BaseTestCase;
import com.fb.platform.ifs.dao.lsp.DCDao;
import com.fb.platform.ifs.to.lsp.DeliveryCenter;

public class DCDaoTest extends BaseTestCase{
	
	@Autowired
	private DCDao dcDao;
	
	@Test
	public void getAvailability(){
		List<DeliveryCenter> deliveryCenter = dcDao.getAvailability(67437, 4);
		
		assertNotNull(deliveryCenter);
		
		for(DeliveryCenter dc : deliveryCenter){
			assertEquals("2315", dc.getCode());
		}
		
	}

}
