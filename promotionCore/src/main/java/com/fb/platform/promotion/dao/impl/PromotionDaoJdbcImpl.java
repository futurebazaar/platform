/**
 * 
 */
package com.fb.platform.promotion.dao.impl;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;

//import com.fb.bo.promotion.PromotionBundle;
import com.fb.platform.promotion.dao.PromotionBundleDao;
import com.fb.platform.promotion.dao.PromotionDao;
import com.fb.platform.promotion.mapper.PromotionMapper;
import com.fb.platform.promotion.model.Promotion;

/**
 * @author Keith Fernandez
 *
 */
public class PromotionDaoJdbcImpl implements PromotionDao {

	private JdbcTemplate jdbcTemplate;

	private PromotionBundleDao promotionBundleDao;

	private static final String GET_PROMOTION_QUERY = "SELECT " +
			"id," +
			"applies_on," +
			"created_on," +
			"created_by," +
			"valid_from," +
			"valid_till," +
			"last_modified_on," +
			"promotion_name," +
			"display_text," +
			"promotion_description," +
			"last_used_on," +
			"promotion_type," +
			"promotion_uses," +
			"rule_id," +
			"is_coupon," +
			"amount_type," +
			"is_active," +
			"priority" +
			" FROM promotion WHERE id = ?";

	private static final String DELETE_PROMOTION_QUERY = "DELETE FROM promotion  WHERE promotion_id = ?";

	private static final String UPDATE_PROMOTION_QUERY = "UPDATE promotion SET " +
			"applies_on=?," +
			"created_on=?," +
			"created_by=?," +
			"valid_from=?," +
			"valid_till=?," +
			"last_modified_on=?," +
			"promotion_name=?," +
			"display_text=?," +
			"promotion_description=?," +
			"last_used_on=?," +
			"promotion_type=?," +
			"promotion_uses=?," +
			"rule_id=?," +
			"is_coupon=?," +
			"amount_type=?," +
			"is_active=?," +
			"priority=?," +
			" WHERE id = ?";

	/* (non-Javadoc)
	 * @see com.fb.platform.promotion.dao.PromotionDao#update(com.fb.bo.promotion.PromotionBo)
	 */
	@Override
	public void update(Promotion promotion) {
		this.jdbcTemplate.update(UPDATE_PROMOTION_QUERY,
				promotion.getAppliesOn(),
				promotion.getCreatedOn(),
				promotion.getCreatedBy(),
				promotion.getValidFrom(),
				promotion.getValidTill(),
				promotion.getLastModifiedOn(),
				promotion.getPromotionName(),
				promotion.getDisplayText(),
				promotion.getPromotionDescription(),
				promotion.getLastUsedOn(),
				promotion.getPromotionType(),
				promotion.getPromotionUses(),
				promotion.getRuleId(),
				promotion.isCoupon(),
				promotion.getAmountType(),
				promotion.isActive(),
				promotion.getPriority()
				);

	}

	/* (non-Javadoc)
	 * @see com.fb.platform.promotion.dao.PromotionDao#get(java.lang.Integer)
	 */
	@Override
	public Promotion get(Integer promotionId) {
		List<Promotion> queryRes = (List<Promotion>) jdbcTemplate.query(
        		GET_PROMOTION_QUERY, 
        		new Object[]{promotionId}, 
        		new PromotionMapper());
		List<Promotion> promotionBoList = queryRes;
		System.out.println(queryRes);
        Promotion promotionBo = promotionBoList.get(0);
        
//        int bundleId = promotionBo.getBundleId();
//        PromotionBundle promotionBundle = promotionBundleDao.get(bundleId);
//        promotionBo.setPromotionBundle(promotionBundle);
//        
//        
//        int discountBundleId = promotionBo.getDiscountBundleId();
//        PromotionBundle discountBundle = promotionBundleDao.get(discountBundleId);
//        promotionBo.setDiscountBundle(discountBundle);

		return promotionBo;
	}

	/* (non-Javadoc)
	 * @see com.fb.platform.promotion.dao.PromotionDao#delete(java.lang.Integer)
	 */
	@Override
	public void delete(Integer promotionId) {
		jdbcTemplate.update(DELETE_PROMOTION_QUERY, promotionId);
	}

	/* (non-Javadoc)
	 * @see com.fb.platform.promotion.dao.PromotionDao#create(com.fb.bo.promotion.PromotionBo)
	 */
	@Override
	public void create(Promotion promotion) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.fb.platform.promotion.dao.PromotionDao#getByCouponCode(java.lang.String)
	 */
	@Override
	public Promotion getPromotionByCouponCode(String couponCode) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.fb.platform.promotion.dao.PromotionDao#getPreviousUsesForUser(java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public Integer getPreviousUsesForUser(Integer userId, Integer promotionId) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.fb.platform.promotion.dao.PromotionDao#updatePreviousUsesForUser(java.lang.Integer, java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public void updatePreviousUsesForUser(Integer userId, Integer promotionId,
			Integer uses) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.fb.platform.promotion.dao.PromotionDao#geByOrderID(java.lang.Integer)
	 */
	@Override
	public List<Promotion> getByOrderID(Integer orderId) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.fb.platform.promotion.dao.PromotionDao#getByProductId(java.lang.Integer)
	 */
	@Override
	public Promotion getByProductId(Integer productId) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.fb.platform.promotion.dao.PromotionDao#getByMinAmount(java.lang.Double)
	 */
	@Override
	public List<Promotion> getByMinAmount(Double orderTotal) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Promotion> getAllActive() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.fb.platform.promotion.dao.PromotionDao#getAll()
	 */
	@Override
	public List<Promotion> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public void setPromotionBundleDao(PromotionBundleDao promotionBundleDao) {
		this.promotionBundleDao = promotionBundleDao;
	}

	@Override
	public List<Promotion> getAllGlobalCoupons() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Promotion> getAllCouponsOnCategory(Integer categoryId) {
		// TODO Auto-generated method stub
		return null;
	}

}
