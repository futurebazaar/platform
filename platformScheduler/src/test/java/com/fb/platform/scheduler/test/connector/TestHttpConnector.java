package com.fb.platform.scheduler.test.connector;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.HttpException;
import org.junit.Test;

import com.fb.platform.scheduler.connector.HttpConnector;
import com.fb.platform.scheduler.to.HttpResponseTO;

public class TestHttpConnector {
	
	@Test
	public void testSendParameter() {
		try {
			Map<String, String> map = new HashMap<String, String>();
			map.put("name", "Test");
			HttpResponseTO httpResponseTO = HttpConnector.sendParameter("http://localhost/", map);
			assertEquals(503, httpResponseTO.getStatusCode());
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testConnector() {
		try {
			Map<String, String> map = new HashMap<String, String>();
			map.put("name", "Test");
			HttpResponseTO httpResponseTO = HttpConnector.sendParameter("", map);
			assertEquals(503, httpResponseTO.getStatusCode());
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}



