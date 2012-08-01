/**
 * 
 */
package com.fb.commons.mail;

import java.io.File;

import javax.mail.internet.MimeMessage;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;

import com.fb.commons.mom.to.MailTO;
import com.fb.commons.mail.exception.MailNoReceiverException;
import com.fb.commons.mail.exception.MailNoSenderException;
import com.fb.commons.mail.exception.MailerException;

/**
 * @author nehaga
 *
 */
public interface MailSender {

	public void send(final MailTO mailTO);
}
