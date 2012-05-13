package com.fb.platform.payback.ws.test;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.fb.commons.test.BaseTestCase;
import com.fb.platform.payback.points.PointsResource;


public class PointsResourceTest extends BaseTestCase{

	@Test
	public void testWS(){
		PointsResource pointsResource = new PointsResource();
		pointsResource.storePoints("");
		assertTrue(true);
	}
}
