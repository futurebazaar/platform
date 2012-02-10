/**
 * 
 */
package com.fb.dao.promotion;

import java.util.List;

import com.fb.bo.promotion.PromotionBO;

/**
 * @author Keith Fernandez
 *
 */
public interface PromotionDao {

	public PromotionBO get(Integer promotionId);

	public void delete(Integer promotionId);

	public void update(PromotionBO promotion);

	public void create(PromotionBO promotion);

	public List<PromotionBO> getByCouponCode(String couponCode);

	public Integer getPreviousUsesForUser(Integer userId, Integer promotionId);

	public void updatePreviousUsesForUser(Integer userId, Integer promotionId, Integer uses);

	public List<PromotionBO> getByOrderID(Integer orderId);

	public PromotionBO getByProductId(Integer productId);

	public List<PromotionBO> getByMinAmount(Double orderTotal);

	public List<PromotionBO> getAll();

	public List<PromotionBO> getAllActive();

	public List<PromotionBO> getAllGlobalCoupons();
	
	public List<PromotionBO> getAllCouponsOnCategory(Integer categoryId);
}
