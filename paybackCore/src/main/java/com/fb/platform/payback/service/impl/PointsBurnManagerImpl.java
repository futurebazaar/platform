package com.fb.platform.payback.service.impl;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Properties;

import javax.mail.MessagingException;

import com.fb.commons.util.MailSender;
import com.fb.platform.payback.model.PointsHeader;
import com.fb.platform.payback.service.PointsBurnManager;
import com.fb.platform.payback.service.PointsBurnService;
import com.fb.platform.payback.service.PointsService;
import com.fb.platform.payback.to.StoreBurnPointsRequest;
import com.fb.platform.payback.util.EarnActionCodesEnum;
import com.fb.platform.payback.util.PointsUtil;

public class PointsBurnManagerImpl implements PointsBurnManager{
	
	private PointsBurnService pointsBurnService;
	private PointsService pointsService;
	private PointsUtil pointsUtil;
	
	public void setPointsBurnService(PointsBurnService pointsBurnService){
		this.pointsBurnService = pointsBurnService;
	}
	
	public void setPointsService(PointsService pointsService){
		this.pointsService = pointsService;
	}
	
	public void setPointsUtil(PointsUtil pointsUtil){
		this.pointsUtil = pointsUtil;
	}

	@Override
	public void mailBurnData() {
		try {
			Properties props = pointsUtil.getProperties("points.properties");
			String[] clients =  props.getProperty("CLIENTS").split(",");
			for(String client : clients){
				String[] partnerIds = props.getProperty(client + "_IDS").split(",");
				String merchantId = partnerIds[0];
				for (EarnActionCodesEnum txnActionCode : EarnActionCodesEnum.values()){
					pointsBurnService.mailBurnData(txnActionCode.name(), merchantId);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public int storeBurnPoints(StoreBurnPointsRequest request){
		pointsBurnService.saveBurnPoints(request);		
		return 0;
	}

}
