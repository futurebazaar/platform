/**
 * 
 */
package com.fb.platform.promotion.dao;

import java.util.List;

import com.fb.platform.promotion.model.Promotion;

/**
 * @author Keith Fernandez
 *
 */
public interface PromotionDao {

	/**
	 * @param promotionId promotion Id of the promotion
	 * @return the promotion object
	 */
	public Promotion get(Integer promotionId);

	/**
	 * @param promotionId promotion Id of the promotion
	 */
	public void delete(Integer promotionId);

	/**
	 * @param promotion promotion object
	 */
	public void update(Promotion promotion);

	/**
	 * @param promotion promotion object
	 */
	public void create(Promotion promotion);

	/**
	 * @param couponCode CouponCode to be applied
	 * @return the promotion object
	 */
	public Promotion getPromotionByCouponCode(String couponCode);

	public Integer getPreviousUsesForUser(Integer userId, Integer promotionId);

	public void updatePreviousUsesForUser(Integer userId, Integer promotionId, Integer uses);

	public List<Promotion> getByOrderID(Integer orderId);

	public Promotion getByProductId(Integer productId);

	public List<Promotion> getByMinAmount(Double orderTotal);

	public List<Promotion> getAll();

	public List<Promotion> getAllActive();

	public List<Promotion> getAllGlobalCoupons();
	
	public List<Promotion> getAllCouponsOnCategory(Integer categoryId);
}
