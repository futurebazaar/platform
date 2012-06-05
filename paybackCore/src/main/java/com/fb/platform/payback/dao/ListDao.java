package com.fb.platform.payback.dao;

import java.util.List;

import org.joda.time.DateTime;

public interface ListDao {

	public List<Long> getHeroDealSellerRateChart(DateTime orderDate);

}
