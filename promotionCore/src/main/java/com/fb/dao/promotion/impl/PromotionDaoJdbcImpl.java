/**
 * 
 */
package com.fb.dao.promotion.impl;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;

import com.fb.bo.promotion.PromotionBO;
//import com.fb.bo.promotion.PromotionBundle;
import com.fb.dao.promotion.PromotionBundleDao;
import com.fb.dao.promotion.PromotionDao;
import com.fb.mapper.promotion.PromotionMapper;

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
	 * @see com.fb.dao.promotion.PromotionDao#update(com.fb.bo.promotion.PromotionBo)
	 */
	@Override
	public void update(PromotionBO promotion) {
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
	 * @see com.fb.dao.promotion.PromotionDao#get(java.lang.Integer)
	 */
	@Override
	public PromotionBO get(Integer promotionId) {
		List<PromotionBO> queryRes = (List<PromotionBO>) jdbcTemplate.query(
        		GET_PROMOTION_QUERY, 
        		new Object[]{promotionId}, 
        		new PromotionMapper());
		List<PromotionBO> promotionBoList = queryRes;
		System.out.println(queryRes);
        PromotionBO promotionBo = promotionBoList.get(0);
        
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
	 * @see com.fb.dao.promotion.PromotionDao#delete(java.lang.Integer)
	 */
	@Override
	public void delete(Integer promotionId) {
		jdbcTemplate.update(DELETE_PROMOTION_QUERY, promotionId);
	}

	/* (non-Javadoc)
	 * @see com.fb.dao.promotion.PromotionDao#create(com.fb.bo.promotion.PromotionBo)
	 */
	@Override
	public void create(PromotionBO promotion) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.fb.dao.promotion.PromotionDao#getByCouponCode(java.lang.String)
	 */
	@Override
	public List<PromotionBO> getByCouponCode(String couponCode) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.fb.dao.promotion.PromotionDao#getPreviousUsesForUser(java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public Integer getPreviousUsesForUser(Integer userId, Integer promotionId) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.fb.dao.promotion.PromotionDao#updatePreviousUsesForUser(java.lang.Integer, java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public void updatePreviousUsesForUser(Integer userId, Integer promotionId,
			Integer uses) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.fb.dao.promotion.PromotionDao#geByOrderID(java.lang.Integer)
	 */
	@Override
	public List<PromotionBO> getByOrderID(Integer orderId) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.fb.dao.promotion.PromotionDao#getByProductId(java.lang.Integer)
	 */
	@Override
	public PromotionBO getByProductId(Integer productId) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.fb.dao.promotion.PromotionDao#getByMinAmount(java.lang.Double)
	 */
	@Override
	public List<PromotionBO> getByMinAmount(Double orderTotal) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PromotionBO> getAllActive() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.fb.dao.promotion.PromotionDao#getAll()
	 */
	@Override
	public List<PromotionBO> getAll() {
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
	public List<PromotionBO> getAllGlobalCoupons() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PromotionBO> getAllCouponsOnCategory(Integer categoryId) {
		// TODO Auto-generated method stub
		return null;
	}

}
