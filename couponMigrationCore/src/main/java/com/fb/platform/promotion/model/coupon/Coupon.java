/**
 * 
 */
package com.fb.platform.promotion.model.coupon;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fb.platform.promotion.to.PromotionStatusEnum;

/**
 * @author vinayak
 *
 */
public class Coupon implements Serializable {

	private int id;
	private String code;
	private int promotionId;
	private CouponType type;
	private CouponLimitsConfig limitsConfig;

	private List<Integer> users = new ArrayList<Integer>();

	public PromotionStatusEnum isWithinLimits(GlobalCouponUses globalUses, UserCouponUses userUses) {
		//TODO change the behaviour for different coupon types
		return limitsConfig.isWithinLimits(globalUses, userUses);
	}

	public int getId() {
		return id;
	}
	public CouponType getType() {
		return type;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setPromotionId(int promotionId) {
		this.promotionId = promotionId;
	}
	public void setType(CouponType type) {
		this.type = type;
	}
	public void setLimitsConfig(CouponLimitsConfig limitsConfig) {
		this.limitsConfig = limitsConfig;
	}
	public int getPromotionId() {
		return promotionId;
	}
	public CouponLimitsConfig getLimitsConfig() {
		return limitsConfig;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}

	public List<Integer> getUsers() {
		return users;
	}

	public void setUsers(List<Integer> users) {
		this.users = users;
	}
}
