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
import com.fb.platform.payback.util.PartnerMerchantIdEnum;
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
		String settlementDate = pointsUtil.getPreviousDayDate();;
		String header = "Transaction ID, Transaction Date, Merchant ID, Points, Reason";
		String fileBody = header + "\n";
		String txnActionCode = "BURN_REVERSAL";
		for(PartnerMerchantIdEnum partnerMerchantId : PartnerMerchantIdEnum.values()){
			String[] partnerIds = partnerMerchantId.toString().split(",");
			String merchantId = partnerIds[0];
			Collection<PointsHeader> burnList = pointsBurnService.loadBurnData(txnActionCode, settlementDate, merchantId);
			Iterator<PointsHeader> burnIterator = burnList.iterator();
			while(burnIterator.hasNext()){
				PointsHeader burnValues = burnIterator.next();
				String transactionID = burnValues.getTransactionId();
				String transactionDate = burnValues.getTxnDate();
				String points = String.valueOf(burnValues.getTxnPoints());
				String reason = burnValues.getReason();
				
				fileBody += transactionID + ", " + transactionDate + ", " + merchantId + ", " +
						points + ", " + reason + "\n";
				System.out.println(fileBody);
				
			}
			if (fileBody != null && !fileBody.equals("")){
					
				try{
					Properties props = pointsUtil.getProperties("payback.properties");					
					String host = props.getProperty("mailHost");
					int port = Integer.parseInt(props.getProperty("mailPort"));
					String username = props.getProperty("mailUsername");
					String password = props.getProperty("mailPassword");
					MailSender mailSender = new MailSender(host, port, username, password);
					mailSender.setFrom(props.getProperty("burnFROM"));
					mailSender.setTO(props.getProperty("burnTO"));
					mailSender.setCC(props.getProperty("burnCC"));
					mailSender.setFileContent(fileBody, "BurnReversal_" + settlementDate + ".csv");
					mailSender.sendMail();
					//pointsService.setStatusDone(txnActionCode, settlementDate, merchantId);
				}catch(IOException ioException){
					ioException.printStackTrace();
				} catch (MessagingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
	}
	
	public int storeBurnPoints(StoreBurnPointsRequest request){
		pointsBurnService.saveBurnPoints(request);		
		return 0;
	}

}
