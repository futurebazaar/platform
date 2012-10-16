package com.fb.platform.payback.util;

import java.util.HashMap;
import java.util.Properties;
import java.util.StringTokenizer;

import org.joda.time.DateTime;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.fb.commons.test.BaseTestCase;

public class PointsUtilTest extends BaseTestCase {
	
	@Test
	public void getDatesTest(){
		String previousDate = PointsUtil.getPreviousDayDate();
		DateTime dateTime = DateTime.now().minusDays(1);
		String dateToString = PointsUtil.convertDateToFormat(dateTime, "yyyy-MM-dd");
		
		assertEquals(dateToString, previousDate);
		DateTime dateFromString = PointsUtil.getDateTimeFromString(previousDate, "yyyy-MM-dd");
		assertEquals(dateTime.getDayOfMonth(), dateFromString.getDayOfMonth());
		assertEquals(dateTime.getYear(), dateFromString.getYear());
		assertEquals(dateTime.getMonthOfYear(), dateFromString.getMonthOfYear());
		
		String dateString = PointsUtil.convertDateToFormat(previousDate, "yyyyMMdd");
		dateFromString = PointsUtil.getDateTimeFromString(dateString, "yyyyMMdd");
		assertEquals(dateTime.getDayOfMonth(), dateFromString.getDayOfMonth());
		assertEquals(dateTime.getYear(), dateFromString.getYear());
		assertEquals(dateTime.getMonthOfYear(), dateFromString.getMonthOfYear());
		
	}
	
	@Test(expected = NullPointerException.class)
	public void getPropertiesNullTest(){
		Properties prop = PointsUtil.getProperties("abcd.properties");
		assertNull(prop);
	}
	
	@Test
	public void isValidCardTest(){
		String card = "312312313";
		assertTrue(!PointsUtil.isValidLoyaltyCard(card));
		
		card = "";
		assertTrue(!PointsUtil.isValidLoyaltyCard(card));
		
		card = null;
		assertTrue(!PointsUtil.isValidLoyaltyCard(card));
		
		card = "1234567891234567";
		assertTrue(PointsUtil.isValidLoyaltyCard(card));
	}
	
	@Test
	public void getMapValueTest() {
		String map = "gv=5, electronics=2";
		HashMap<String, String> generatedMap = new HashMap<String, String>();
		StringTokenizer mapTokenizer = new StringTokenizer(map, ",");
		while (mapTokenizer.hasMoreTokens()){
			String singleMap = mapTokenizer.nextToken().replaceAll(" ", "");
			generatedMap.put(singleMap.split("=")[0], singleMap.split("=")[1]);
		}
		assertEquals("5", generatedMap.get("gv"));
		assertEquals("2", generatedMap.get("electronics"));
	}
	
	@Test
	public void testGetStackTrace() {
		Throwable e = new Throwable();
		assertTrue(PointsUtil.getStackTrace(e).contains("at com.fb.platform.payback.util.PointsUtilTest.testGetStackTrace"));
	}

}
