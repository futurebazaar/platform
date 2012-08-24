package com.fb.platform.wallet.util;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fb.commons.communication.to.SmsTO;
import com.fb.commons.mail.MailSender;
import com.fb.commons.mom.to.MailTO;
import com.fb.commons.sms.SmsSender;
import com.fb.platform.user.domain.UserBo;
import com.fb.platform.user.domain.UserEmailBo;
import com.fb.platform.user.domain.UserPhoneBo;
import com.fb.platform.user.manager.interfaces.UserAdminService;

public class GenerateSendWalletPassword {
	
	@Autowired
	private UserAdminService userAdminService = null;
	
	@Autowired
	private MailSender mailSender = null;

	@Autowired
	private SmsSender smsSender = null;

	public void setMailSender(MailSender mailSender) {
		this.mailSender = mailSender;
	}

	public void setSmsSender(SmsSender smsSender) {
		this.smsSender = smsSender;
	}
	
	public void setUserAdminService(UserAdminService userAdminService) {
		this.userAdminService = userAdminService;
	}
	
	public UserAdminService getUserAdminService() {
		return userAdminService;
	}
	public MailSender getMailSender() {
		return mailSender;
	}

	public SmsSender getSmsSender() {
		return smsSender;
	}



	private Log log = LogFactory.getLog(GenerateSendWalletPassword.class);
	
	public String generateSendWalletPassword(long userId){
		String randomPassword = RandomStringUtils.random(4, false, true);
		log.info("Wallet password generated for userId: " + userId + " :::::" + randomPassword);
		try {
			UserBo user = userAdminService.getUserByUserId(safeLongToInt(userId));
			for(UserEmailBo userEmailBo : user.getUserEmail()){
				if(userEmailBo.isVerified()){
					MailTO message = MailHelper.createMailTO(userEmailBo.getEmail(), randomPassword, user.getName());
					mailSender.send(message);
				}
			}
			for(UserPhoneBo userPhoneBo : user.getUserPhone()){
				if(userPhoneBo.isVerified()){
					SmsTO sms = SmsHelper.createSmsTO(userPhoneBo.getPhoneno(), randomPassword, user.getName());
					smsSender.send(sms);
				}
			}
		} catch (Exception e){
			e.printStackTrace();
		}
		return randomPassword;
	}
	private int safeLongToInt(long l) {
	    if (l < Integer.MIN_VALUE || l > Integer.MAX_VALUE) {
	        throw new IllegalArgumentException
	            (l + " cannot be cast to int without changing its value.");
	    }
	    return (int) l;
	}
}
