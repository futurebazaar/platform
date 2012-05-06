package com.fb.platform.payback.dao;

import com.fb.platform.payback.model.PaymentDetail;

public interface PointsBurnDao {
	
	public PaymentDetail getPaymentDetails(long orderId);

}
