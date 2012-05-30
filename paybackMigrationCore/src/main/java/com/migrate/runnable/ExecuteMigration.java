package com.migrate.runnable;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.fb.platform.payback.service.BurnActionCodesEnum;
import com.fb.platform.payback.service.EarnActionCodesEnum;
import com.fb.platform.payback.service.LegacyPointsService;
import com.fb.platform.payback.service.PointsTxnClassificationCodeEnum;



public class ExecuteMigration {
	
	public static void main(String[] args) {
		
		String[] serviceResources = {"applicationContext-dao.xml", "applicationContext-service.xml", 
				"applicationContext-resources.xml"};	

		ApplicationContext orderServiceContext = new ClassPathXmlApplicationContext(serviceResources);
		Object pointsService = orderServiceContext.getBean("legacyPointsService");
		
		//((LegacyPointsService) pointsService).mailBurnData(BurnActionCodesEnum.BURN_REVERSAL, "90012970");
		
		//((LegacyPointsService) pointsService).postEarnData(EarnActionCodesEnum.EARN_REVERSAL, "90012970", "FUTUREBAZAAR");
		//((LegacyPointsService) pointsService).postEarnData(EarnActionCodesEnum.PREALLOC_EARN, "90012970", "FUTUREBAZAAR");
		
		
		
		((LegacyPointsService) pointsService).migratePaybackData();
		
	}
	
}