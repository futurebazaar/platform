package com.fb.platform.payback.service;

import java.math.BigDecimal;
import java.util.Properties;

import com.fb.platform.payback.rule.PointsRule;
import com.fb.platform.payback.to.EarnActionCodesEnum;
import com.fb.platform.payback.to.PointsRequest;
import com.fb.platform.payback.to.PointsResponseCodeEnum;

public interface PointsService {

	String getSequenceNumber();

	PointsResponseCodeEnum doOperation(PointsRequest request, PointsRule rule);

}