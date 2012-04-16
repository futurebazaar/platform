/**
 * 
 */
package com.fb.platform.promotion.migration.dao;

import org.springframework.transaction.annotation.Transactional;

import com.fb.platform.promotion.migration.oldModel.*;


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
//		OldPromo oldPromo = loadOldPromotion(promoId);
//		NewPromotion newPromotion = convertOldPromoToNewPromo(oldPromo);
//		migDaoImpl.createPromotion(newPromotion);
//		** Attach RuleConfigValues **
//		List<OldPromoCoupon> coupons = loadOldCoupons(promoId);
//		for (OldPromoCoupon oldCoupon:coupons){
//			NewCoupon newCoupon = convertOldCouponToNewCoupon(oldCoupon);
//			migDaoImpl.createCoupon(newCoupon);
//			List<OldCouponUser> couponUsers = loadCouponUsers(couponCode);
//			for(OldCouponUser oldCouponUser : couponUsers){
//				NewCouponUser newCouponUser = convertOldtoNewCouponUser(oldCoupon);
//				migDaoImpl.createCouponUser(newCouponUser);
//			}
//		}
//		** QueryOrderTable to get couponUses info
//		insert into couponUses and promotionUses table accordingly **
//		createPromotionUses()
//		createCouponUses();
//		
	}

}
