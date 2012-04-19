package com.fb.platform.promotion.migration;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.fb.platform.promotion.service.migration.MigrationManager;

public class MigrationRunner {
	
	public static void main(String[] args) {
		ApplicationContext appContext = new ClassPathXmlApplicationContext( "migration-applicationContext-service.xml",
																			"migration-applicationContext-dao.xml",
																			"migration-applicationContext-resources.xml");
		MigrationManager migrationManager = (MigrationManager)appContext.getBean("migrationManager");
		migrationManager.migrate();
	}

}
