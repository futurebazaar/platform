/**
 * 
 */
package com.fb.platform.promotion.migration.dao;

import java.sql.Timestamp;

import com.fb.commons.to.Money;

/**
 * @author vinayak
 *
 */
public interface MigrationDao {

	public boolean createPromotion(String name, String description, Timestamp validFrom,
			Timestamp validTill, int maxUses, Money maxAmount,
			int maxUsesPerUser, Money maxAmountPerUser, int rule);

}
