package com.fb.commons.util;


import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;


public class MailSender {
	
	private MimeMessage message = null;
	private Multipart multipart = null;
	BodyPart messageBodyPart = null;
	
	
	public MailSender(String host, int port, final String username, final String password){
		Properties properties = new Properties();
		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.port", port);
		//properties.put("mail.smtp.timeout", "1000");
		properties.put("mail.smtp.connectiontimeout", "5000");
		Session session = Session.getInstance(properties);
		if ((username != null && !username.equals("")) && (password != null && !password.equals(""))){
			properties.put("mail.smtp.auth", "true");
			properties.put("mail.smtp.starttls.enable", "true");
			session = javax.mail.Session.getInstance(properties, 
					new javax.mail.Authenticator(){
						protected javax.mail.PasswordAuthentication getPasswordAuthentication(){
							return new javax.mail.PasswordAuthentication(username, password);
						}
			});
		}
		this.message = new MimeMessage(session);
		this.multipart = new MimeMultipart();
		this.messageBodyPart = new MimeBodyPart();
	}
	
	public void setFrom(String from) throws AddressException, MessagingException{
		this.message.setFrom(new InternetAddress(from));
	}
	
	public void setTO(String to) throws AddressException, MessagingException{
		this.message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
	}
	
	public void setCC(String cc) throws AddressException, MessagingException{
		this.message.addRecipient(Message.RecipientType.CC, new InternetAddress(cc));
	}
	
	public void setBCC(String bcc) throws AddressException, MessagingException{
		this.message.addRecipient(Message.RecipientType.BCC, new InternetAddress(bcc));
	}
	
	public void setSubject(String subject) throws MessagingException{
		this.message.setSubject(subject);
	}
	
	public void setText(String text) throws MessagingException{
        this.messageBodyPart.setText(text);
        this.multipart.addBodyPart(messageBodyPart);
	}
	
	public void setFile(File file, String fileName) throws MessagingException{
         DataSource source = new FileDataSource(file.getAbsolutePath());
         this.messageBodyPart.setDataHandler(new DataHandler(source));
         this.messageBodyPart.setFileName(file.getName());
         this.multipart.addBodyPart(this.messageBodyPart);
	}
	
	public void setFileContent(String fileContent, String fileName) throws MessagingException, UnsupportedEncodingException{
		 MimeBodyPart attachmentPart = new MimeBodyPart();
         DataSource source = new ByteArrayDataSource(fileContent.getBytes("UTF-8"), "text/plain");
         attachmentPart.setDataHandler(new DataHandler(source));
         attachmentPart.setFileName(fileName);
         this.multipart.addBodyPart(attachmentPart);
    	 
	}
	
	
	public void sendMail() throws MessagingException{
		this.message.setContent(this.multipart );
	    Transport.send(this.message);
	};
}
