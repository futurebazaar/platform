/**
 * 
 */
package com.fb.platform.promotion.service.migration.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fb.platform.promotion.dao.legacy.LegacyDao;
import com.fb.platform.promotion.model.legacy.LegacyPromotion;
import com.fb.platform.promotion.service.migration.MigrationManager;
import com.fb.platform.promotion.service.migration.MigrationService;

/**
 * @author vinayak
 *
 */
public class MigrationManagerImpl implements MigrationManager {

	private Log infoLog = LogFactory.getLog("LOGINFO");

	private Log errorLog = LogFactory.getLog("LOGERROR");

	@Autowired
	private LegacyDao legacyDao = null;

	@Autowired
	private MigrationService migrationService = null;

	@Override
	public void migrate() {
		int startRecord = 0;
		int batchSize = 10;
		while(true) {
			List<Integer> idsToMigrate = legacyDao.loadIdsToMigrate(startRecord, batchSize);
			if (idsToMigrate != null && idsToMigrate.size() > 0) {

				for (Integer promotionId : idsToMigrate) {
					LegacyPromotion legacyPromotion = legacyDao.loadPromotion(promotionId);
					try {
						migrationService.migrate(legacyPromotion);
					} catch (Exception e) {
						errorLog.error("Error while migrating legacy promotions to new promotions.", e);
					}
				}
			} else {
				//end
				infoLog.info("End of migration. Number of promotions migrated : " + startRecord);
				break;
			}
			startRecord = startRecord + batchSize;
		}
	}

	public void setLegacyDao(LegacyDao legacyDao) {
		this.legacyDao = legacyDao;
	}

	public void setMigrationService(MigrationService migrationService) {
		this.migrationService = migrationService;
	}

}
