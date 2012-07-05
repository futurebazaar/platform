package com.fb.platform.payback.util;

import java.util.Properties;

import org.joda.time.DateTime;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.fb.commons.test.BaseTestCase;

public class PointsUtilTest extends BaseTestCase {
	
	@Autowired
	private PointsUtil pointsUtil;
	
	@Test
	public void getDatesTest(){
		String previousDate = pointsUtil.getPreviousDayDate();
		DateTime dateTime = DateTime.now().minusDays(1);
		String dateToString = pointsUtil.convertDateToFormat(dateTime, "yyyy-MM-dd");
		
		assertEquals(dateToString, previousDate);
		DateTime dateFromString = pointsUtil.getDateTimeFromString(previousDate, "yyyy-MM-dd");
		assertEquals(dateTime.getDayOfMonth(), dateFromString.getDayOfMonth());
		assertEquals(dateTime.getYear(), dateFromString.getYear());
		assertEquals(dateTime.getMonthOfYear(), dateFromString.getMonthOfYear());
		
		String dateString = pointsUtil.convertDateToFormat(previousDate, "yyyyMMdd");
		dateFromString = pointsUtil.getDateTimeFromString(dateString, "yyyyMMdd");
		assertEquals(dateTime.getDayOfMonth(), dateFromString.getDayOfMonth());
		assertEquals(dateTime.getYear(), dateFromString.getYear());
		assertEquals(dateTime.getMonthOfYear(), dateFromString.getMonthOfYear());
		
	}
	
	@Test(expected = NullPointerException.class)
	public void getPropertiesNullTest(){
		Properties prop = pointsUtil.getProperties("abcd.properties");
		assertNull(prop);
	}
	
	@Test
	public void isValidCardTest(){
		String card = "312312313";
		assertTrue(!pointsUtil.isValidLoyaltyCard(card));
		
		card = "";
		assertTrue(!pointsUtil.isValidLoyaltyCard(card));
		
		card = null;
		assertTrue(!pointsUtil.isValidLoyaltyCard(card));
		
		card = "1234567891234567";
		assertTrue(pointsUtil.isValidLoyaltyCard(card));
	}
	

}
