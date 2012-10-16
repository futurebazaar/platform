package com.fb.platform.payback.points.scheduler;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fb.platform.payback.service.PointsManager;
import com.fb.platform.payback.util.PointsUtil;

@Service
public class PointsTaskScheduler {
	
	@Autowired
	private PointsManager pointsManager = null;
	
	private static Log logger = LogFactory.getLog(PointsTaskScheduler.class);
	
		//Fire at 1 AM everyday
		@Scheduled(cron = "0 0 1 * * ?")
		public void sendToPayback(){
			String isMaster = "False";
			Properties props = PointsUtil.getProperties("payback.properties");
			isMaster = props.getProperty("isMaster");
			
			if (isMaster != null && isMaster.equals("True")){
				logger.info("Cron Started");
				pointsManager.mailBurnData();
				pointsManager.uploadEarnFilesOnSFTP();
			}
			
		}
		
}
