package com.fb.commons.mail;

import java.io.File;

import javax.mail.internet.MimeMessage;

import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;

import com.fb.commons.mail.to.MailTO;

/**
 * @author nehaga
 *
 */
public class MailSender {
	
	private JavaMailSender springMailSender;
    
    public void setSpringMailSender(JavaMailSender springMailSender) {
        this.springMailSender = springMailSender;
    }
    
    public void send(final MailTO mailTO) throws MailException{

        MimeMessagePreparator preparator = new MimeMessagePreparator() {
        
            public void prepare(MimeMessage mimeMessage) throws Exception {
            	
            	MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        
            	helper.setTo(mailTO.getTo());
            	helper.setFrom(mailTO.getFrom());
            	helper.setCc(mailTO.getCc());
            	helper.setBcc(mailTO.getBcc());
            	helper.setSubject(mailTO.getSubject());
            	helper.setText(mailTO.getMessage());
            	for(File attachment : mailTO.getAttachments()) {
            		helper.addAttachment(attachment.getName(), attachment);
            	}
            }
        };
        this.springMailSender.send(preparator);
    }
}
