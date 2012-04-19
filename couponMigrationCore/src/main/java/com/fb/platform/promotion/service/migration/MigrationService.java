/**
 * 
 */
package com.fb.platform.promotion.service.migration;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fb.platform.promotion.model.legacy.LegacyPromotion;

/**
 * @author vinayak
 *
 */
@Transactional
public interface MigrationService {

	@Transactional(propagation=Propagation.REQUIRED)
	public void migrate(LegacyPromotion legacyPromotion);
}
