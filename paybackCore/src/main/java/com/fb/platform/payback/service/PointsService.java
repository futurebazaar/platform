package com.fb.platform.payback.service;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fb.platform.payback.to.PointsRequest;
import com.fb.platform.payback.to.PointsResponseCodeEnum;

public interface PointsService {

	String getSequenceNumber();

	@Transactional(propagation=Propagation.REQUIRED)
	PointsResponseCodeEnum doOperation(PointsRequest request);
	
}