package com.fb.platform.ifs.manager;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.fb.commons.test.BaseTestCase;
import com.fb.platform.ifs.manager.model.SingleArticleServiceabilityRequestTO;
import com.fb.platform.ifs.manager.model.SingleArticleServiceabilityResponseTO;


public class IFSManagerTest extends BaseTestCase {

	@Autowired
	private IFSManager ifsManager;
	
	@Test
	public void testIFSManager()
    {
	    SingleArticleServiceabilityRequestTO requestTO = new SingleArticleServiceabilityRequestTO();
	    requestTO.setArticleId("106613786");
	    requestTO.setClient("5");
	    requestTO.setIsCod(0);
	    requestTO.setItemPrice(0);
	    requestTO.setPincode("400060");
	    requestTO.setQty(1);
	    requestTO.setRateChartId("49776");
	    requestTO.setVendorId("87");
	    try {
	    	SingleArticleServiceabilityResponseTO resultTO = ifsManager.getSingleArticleServiceabilityInfo(requestTO);	
		    assertNotNull(resultTO);
	    } catch (Exception e) {
			e.printStackTrace();
		}
    }
}