package com.fb.platform.payback.points.scheduler;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fb.platform.payback.service.PointsManager;

@Service
public class PointsTaskScheduler {
	
	@Autowired
	private PointsManager pointsManager = null;
	
	private static Log logger = LogFactory.getLog(PointsTaskScheduler.class);
	
		//Fire at 1 AM everyday
		@Scheduled(cron = "0 0 1 * * ?")
		public void sendToPayback(){
			String isMaster = "False";
			InputStream inStream = this.getClass().getClassLoader().getResourceAsStream("payback.properties");
			Properties props = new Properties();
			try {
				props.load(inStream);
				isMaster = props.getProperty("isMaster");
				inStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			if (isMaster != null && isMaster.equals("True")){
				logger.info("Cron Started");
				pointsManager.mailBurnData();
				pointsManager.uploadEarnFilesOnSFTP();
			}
			
		}
		
}
