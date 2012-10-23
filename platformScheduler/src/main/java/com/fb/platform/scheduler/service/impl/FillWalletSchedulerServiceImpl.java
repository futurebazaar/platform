package com.fb.platform.scheduler.service.impl;

import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fb.platform.scheduler.connector.HttpConnector;
import com.fb.platform.scheduler.dao.FillWalletXmlDao;
import com.fb.platform.scheduler.service.FillWalletSchedulerService;
import com.fb.platform.scheduler.to.FillWalletXmlTO;
import com.fb.platform.scheduler.to.HttpResponseTO;
import com.fb.platform.scheduler.utils.SchedulerConstants;
import com.fb.platform.scheduler.utils.SchedulerUtil;

/**
 * @author kislay
 *
 */
public class FillWalletSchedulerServiceImpl implements FillWalletSchedulerService {
	
	private static Log logger = LogFactory.getLog(FillWalletSchedulerServiceImpl.class);
	private static Properties schedulerProperties = SchedulerUtil.getProperties(SchedulerConstants.SCHEDULER_PROPERTIES);
	private FillWalletXmlDao fillWalletXmlDao = null;
	
	public void setFillWalletXmlDao(FillWalletXmlDao fillWalletXmlDao) {
		this.fillWalletXmlDao = fillWalletXmlDao;
	}
	
	@Override
	public void postXmlsTOBapi() {
		List<FillWalletXmlTO> fillWalletXmlTOList = fillWalletXmlDao.getFillWalletXmlLst();
		for(FillWalletXmlTO fillWalletXmlTO : fillWalletXmlTOList) {
			logger.info("Sending XML: " + fillWalletXmlTO);
			try {
				String url  = getFillWalletUrls();
				HttpResponseTO httpResponseTO = HttpConnector.sendXml(url, getFillWalletXml(fillWalletXmlTO.getWalletFillXml()));
				logger.info("Response :" + httpResponseTO);
				fillWalletXmlDao.updateFillWalletXml(fillWalletXmlTO.getId(), true);
				
			} catch (Exception e) {
				logger.error("Posting to Bapi Failed for : "+ fillWalletXmlTO, e);
				fillWalletXmlDao.updateFillWalletXml(fillWalletXmlTO.getId(), false);
			}
		}
	}
	
	private String getFillWalletUrls() {
		String urlType = "fillwallet.url";
		return schedulerProperties.getProperty(urlType);
	}
	
	private String getFillWalletXml(String databseXml) {
		String xml = "fillwallet.xml";
		return (schedulerProperties.getProperty(xml)).replaceAll("$WALLET_XML", databseXml);
	}
}
