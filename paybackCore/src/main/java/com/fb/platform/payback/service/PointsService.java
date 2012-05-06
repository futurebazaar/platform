package com.fb.platform.payback.service;

import java.math.BigDecimal;
import java.util.Properties;

import com.fb.platform.payback.to.EarnActionCodesEnum;
import com.fb.platform.payback.to.PointsTxnClassificationCodeEnum;

public interface PointsService {

	BigDecimal getBurnRatio(String day, Properties props, String clientName);

	String getSequenceNumber();

	BigDecimal getEarnRatio(String day, Properties props, String clientName,
			long orderId, EarnActionCodesEnum txnActionCode);

	int getTxnPoints(BigDecimal amount, String day, Properties props,
			String clientName, String txnCode, long orderId);

	int getBonusPoints(BigDecimal amount, String day, Properties props, String clientName, long orderId, String txnActionCode);

}
