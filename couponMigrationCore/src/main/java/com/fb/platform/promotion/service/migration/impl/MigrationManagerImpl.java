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

	private Log log = LogFactory.getLog(MigrationManagerImpl.class);

	@Autowired
	private LegacyDao legacyDao = null;

	@Autowired
	private MigrationService migrationService = null;

	@Override
	public void migrate() {
		int startRecord = 0;
		int batchSize = 10;

		try {
			while(true) {
				List<Integer> idsToMigrate = legacyDao.loadIdsToMigrate(startRecord, batchSize);
				if (idsToMigrate != null && idsToMigrate.size() > 0) {

					for (Integer promotionId : idsToMigrate) {
						LegacyPromotion legacyPromotion = legacyDao.loadPromotion(promotionId);
						migrationService.migrate(legacyPromotion);
					}
				} else {
					//end
					log.info("End of migration. Number of promotions migrated : " + startRecord);
					break;
				}
				startRecord = startRecord + batchSize;
			}
		} catch (Exception e) {
			log.error("Error while migrating legacy promotions to new promotions.", e);
		}
	}

	public void setLegacyDao(LegacyDao legacyDao) {
		this.legacyDao = legacyDao;
	}

	public void setMigrationService(MigrationService migrationService) {
		this.migrationService = migrationService;
	}

}
