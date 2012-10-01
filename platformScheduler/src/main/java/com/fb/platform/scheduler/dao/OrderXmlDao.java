package com.fb.platform.scheduler.dao;

import java.util.List;

import com.fb.platform.scheduler.to.OrderXmlTO;

/**
 * @author anubhav
 *
 */
public interface OrderXmlDao {
	
	public List<OrderXmlTO> getOrderXmlList();

	public int updateOrderXml(int attempts, String reason, String status, long id);

}
