/**
 * 
 */
package com.fb.platform.promotion.migration.dao;

import org.springframework.transaction.annotation.Transactional;

import platform.fb.com.migration.model.OldPromo;
import platform.fb.com.migration.model.*;

/**
 * @author keith
 *
 */
public class SinglePromotionMigrate {
	
	MigrationDao migDaoImpl =  new MigrationDaoJdbcImpl();
	
	/**
	 * As of now just snippet of logic is written in commented format.
	 * 
	 * @throws Exception
	 */
	@Transactional
	public void migrate() throws Exception{
//		OldPromo oldPromo = readOldPromo(promoId);
//		NewPromotion newPromotion = convertOldPromoToNewPromo(oldPromo);
//		migDaoImpl.createPromotion(newPromotion);
//		List<OldPromoCoupon> coupons = getOldCoupons(promoId);
//		for (OldPromoCoupon oldCoupon:coupons){
//			NewCoupon newCoupon = convertOldCouponToNewCoupon(oldCoupon);
//			migDaoImpl.createCoupon(newCoupon);
//			List<OldCouponUser> couponUsers = getOldUsersForCoupon(couponCode);
//			for(OldCouponUser oldCouponUser : couponUsers){
//				NewCouponUser newCouponUser = convertOldtoNewCouponUser(oldCoupon);
//				migDaoImpl.createCouponUser(newCouponUser);
//			}
//		}
//		
	}

}
