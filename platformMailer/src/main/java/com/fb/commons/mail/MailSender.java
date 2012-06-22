package com.fb.commons.mail;

import java.io.File;

import javax.mail.internet.MimeMessage;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;

import com.fb.commons.mail.exception.MailNoReceiverException;
import com.fb.commons.mail.exception.MailNoSenderException;
import com.fb.commons.mail.exception.MailerException;
import com.fb.commons.mail.to.MailTO;

/**
 * @author nehaga
 *
 */
public class MailSender {
	
	@Autowired
	private JavaMailSender springMailSender;
    
    public void setSpringMailSender(JavaMailSender springMailSender) {
        this.springMailSender = springMailSender;
    }
    
    public void send(final MailTO mailTO) throws MailException{

        MimeMessagePreparator preparator = new MimeMessagePreparator() {
        
            public void prepare(MimeMessage mimeMessage) throws Exception {
            	
            	MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            	boolean hasReceiver = false;
        
            	if(mailTO.getTo() != null && mailTO.getTo().length > 0) {
            		helper.setTo(mailTO.getTo());
            		hasReceiver = true;
            	}
            	if(mailTO.getCc() != null && mailTO.getCc().length > 0) {
            		helper.setCc(mailTO.getCc());
            		hasReceiver = true;
            	}
            	if(mailTO.getBcc() != null && mailTO.getBcc().length > 0) {
            		helper.setBcc(mailTO.getBcc());
            		hasReceiver = true;
            	}
            	if(!hasReceiver) {
            		throw new MailNoReceiverException("Mail should contain to or cc or bcc.");
            	}
            	if(StringUtils.isBlank(mailTO.getFrom())) {
            		throw new MailNoSenderException("Mail should have a sender.");
            	}
            	helper.setFrom(mailTO.getFrom());
            	helper.setSubject(mailTO.getSubject());
            	helper.setText(mailTO.getMessage());
            	if(mailTO.getAttachments() != null) {
	            	for(File attachment : mailTO.getAttachments()) {
	            		helper.addAttachment(attachment.getName(), attachment);
	            	}
            	}
            }
        };
        try {
        	System.out.println("Problem may lie here");
        	this.springMailSender.send(preparator);
        } catch (MailException e) {
        	System.out.println("Problem !!!");
        	e.printStackTrace();
			throw new MailerException("Error sending mail", e);
		}
    }
}
