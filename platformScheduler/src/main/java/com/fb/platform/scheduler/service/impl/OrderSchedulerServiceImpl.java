package com.fb.platform.scheduler.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fb.platform.scheduler.connector.HttpConnector;
import com.fb.platform.scheduler.dao.OrderXmlDao;
import com.fb.platform.scheduler.service.OrderSchedulerService;
import com.fb.platform.scheduler.to.HttpResponseTO;
import com.fb.platform.scheduler.to.OrderXmlTO;
import com.fb.platform.scheduler.utils.OrderAck;
import com.fb.platform.scheduler.utils.SchedulerConstants;
import com.fb.platform.scheduler.utils.SchedulerUtil;

/**
 * @author anubhav
 *
 */
public class OrderSchedulerServiceImpl implements OrderSchedulerService {
	
	private static Log logger = LogFactory.getLog(OrderSchedulerServiceImpl.class);
	private static Properties schedulerProperties = SchedulerUtil.getProperties(SchedulerConstants.SCHEDULER_PROPERTIES);
	private OrderXmlDao orderXmlDao = null;
	
	public void setOrderXmlDao(OrderXmlDao orderXmlDao) {
		this.orderXmlDao = orderXmlDao;
	}
	
	@Override
	public void postXmlsTOBapi() {
		List<OrderXmlTO> orderXmlTOList = orderXmlDao.getOrderXmlList();
		for(OrderXmlTO orderXmlTO : orderXmlTOList) {
			logger.info("Sending XML: " + orderXmlTO);
			orderXmlDao.updateOrderXml(orderXmlTO.getAttempts() + 1, "", SchedulerConstants.SUBMITTED_STATUS, orderXmlTO.getId());
			try {
				String url  = getOrderUrls(orderXmlTO.getType());
				HttpResponseTO httpResponseTO = HttpConnector.sendXml(url, orderXmlTO.getXml());
				logger.info("Response :" + httpResponseTO);
				postTinlaAck(httpResponseTO, orderXmlTO);
				
			} catch (Exception e) {
				logger.error("Posting to Bapi Failed for : "+ orderXmlTO, e);
				orderXmlDao.updateOrderXml(orderXmlTO.getAttempts() + 1, e.getMessage(), SchedulerConstants.ERROR_STATUS, orderXmlTO.getId());
			}
		}
	}
	
	private String getOrderUrls(String messageType) {
		String urlType = "";
		if (messageType.equals(SchedulerConstants.CANCEL_STATUS) || messageType.equals(SchedulerConstants.NEW_STATUS)) {
			urlType  = "order.common.url" ;
		} else if (messageType.equals(SchedulerConstants.MODIFY_STATUS)) {
			urlType  = "order.modify.url" ;
		} else if (messageType.equals(SchedulerConstants.RETURN_STATUS)) {
			urlType  = "order.return.url" ;
		}
		return schedulerProperties.getProperty(urlType);
	}

	private void postTinlaAck(HttpResponseTO httpResponseTO, OrderXmlTO orderXmlTO) {
		logger.info("Posting Ack to Tinla");
		String sapMessage = httpResponseTO.getMessage();
		String status = getValueBetweenTags(sapMessage, "<status>", "</status>");
		String message = getValueBetweenTags(sapMessage, "<message>", "</message>");
		if (message.equals(SchedulerConstants.FAILED_STATUS)) {
			message = sapMessage;
		}
		Map<String, String> map = new HashMap<String, String>();
		map.put("orderId", String.valueOf(orderXmlTO.getOrderId()));
		map.put("orderState", status);
		map.put("orderDesc", message);
		map.put("header", getHeader(orderXmlTO.getType()));
		map.put("id", String.valueOf(orderXmlTO.getId()));
		try {
			String url = schedulerProperties.getProperty("tinla.orderack.url");
			HttpResponseTO tinlaHttpResponseTO = HttpConnector.sendParameter(url, map);
			logger.info("Tinla Ack Response :" + tinlaHttpResponseTO);
		} catch (Exception e) {
			logger.error("Posting Ack to Tinla failed for : "+ orderXmlTO + " and SAP response: " + httpResponseTO, e);
		}
	}
	
	private String getValueBetweenTags(String message, String openTag, String closeTag) {
		 String value = SchedulerConstants.FAILED_STATUS;
		 try{
	        int openTagIndex = message.indexOf(openTag);
	        int closeTagIndex = message.indexOf(closeTag);
	        int startLen = openTagIndex + openTag.length();
	        value = message.substring(startLen, closeTagIndex);
		 } catch(Exception e){
             logger.error("Unknow Value for tag: " + openTag + " in: " + message);
		 }
		 return value;
	}
	
	private String getHeader(String messageType) {
		if (messageType.equals(SchedulerConstants.CANCEL_STATUS)) {
			return OrderAck.CAN_ACK.toString();
		} else if (messageType.equals(SchedulerConstants.MODIFY_STATUS)) {
			return OrderAck.MOD_ACK.toString();
		} else if (messageType.equals(SchedulerConstants.RETURN_STATUS)) {
			return OrderAck.RET_ACK.toString();
		}
		return OrderAck.ORDER_ACK.toString();
	}

}
