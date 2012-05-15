/**
 * 
 */
package com.fb.platform.promotion.admin.to;

import java.io.Serializable;

import com.fb.platform.promotion.model.coupon.CouponType;

/**
 * @author ashish
 *
 */
public class CouponBasicDetails implements Serializable{

	private int couponId;
	private String couponCode;
	private CouponType couponType;
	
	public CouponType getCouponType() {
		return couponType;
	}
	public void setCouponType(CouponType couponType) {
		this.couponType = couponType;
	}
	public int getCouponId() {
		return couponId;
	}
	public void setCouponId(int couponId) {
		this.couponId = couponId;
	}
	public String getCouponCode() {
		return couponCode;
	}
	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((couponCode == null) ? 0 : couponCode.hashCode());
		result = prime * result + couponId;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof CouponBasicDetails)) {
			return false;
		}
		CouponBasicDetails other = (CouponBasicDetails) obj;
		if (couponCode == null) {
			if (other.couponCode != null) {
				return false;
			}
		} else if (!couponCode.equals(other.couponCode)) {
			return false;
		}
		if (couponId != other.couponId) {
			return false;
		}
		return true;
	}
}
