/**
 * 
 */
package com.fb.platform.test.util;

import com.fb.commons.util.APIObjectUtil;
import com.fb.platform.promotion._1_0.ApplyCouponRequest;
import com.fb.platform.promotion._1_0.OrderItem;
import com.fb.platform.promotion._1_0.OrderRequest;
import com.fb.platform.promotion._1_0.Product;

/**
 * @author vinayak
 *
 */
public class TestApiObjectUtil {

	public static void main(String[] args) {
		APIObjectUtil objectUtil = new APIObjectUtil();

		ApplyCouponRequest cr = new ApplyCouponRequest();
		cr.setCouponCode("xxxx");
		cr.setSessionToken("xxxxyyyy0000==");
		OrderRequest or = new OrderRequest();
		OrderItem oi1 = new OrderItem();
		oi1.setQuantity(10);
		Product pr1 = new Product();
		//pr1.g
		//or.getOrderItem().
		//com.fb.platform.promotion.to.CouponRequest apiCouponReq = new com.fb.platform.promotion.to.CouponRequest();
		
	}
}
