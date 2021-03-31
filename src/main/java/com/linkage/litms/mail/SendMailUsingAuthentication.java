/*
 * @(#)SendMailUsingAuthentication.java	1.00 20/7/2006
 *
 * Copyright 2005 联创科技.版权所有
 */
package com.linkage.litms.mail;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.LipossGlobals;
 
/**
 * 
 * @author wangfeng
 * @version 1.00
 * @since Liposs 2.1
 */
public class SendMailUsingAuthentication {
	/** log */
	private static Logger logger = LoggerFactory.getLogger(SendMailUsingAuthentication.class);
	private static SendMailUsingAuthentication instance = null;
	
	private String SMTP_HOST_NAME = null;
 
	private String SMTP_AUTH_USER = null;
  
	private String SMTP_AUTH_PWD = null;
	
	private String MAIL_FROM_ADDRESS = null;
	
	// 测试使用
	private static final String emailMsgTxt = "邮件的内容（测试用）";
 
	private static final String emailSubjectTxt = "邮件的题目（测试用）";
 
	private static final String[] emailList = { "wangfeng@lianchuang.com" };
    
	/**
	 * 定义一个不带参数的构造方法，并对有些值进行初始化
	 *
	 */
    public SendMailUsingAuthentication(){
    	this.SMTP_HOST_NAME = LipossGlobals.getLipossProperty("Mail.HOSTNAME");
    	this.SMTP_AUTH_USER = LipossGlobals.getLipossProperty("Mail.USER");
    	this.SMTP_AUTH_PWD = LipossGlobals.getLipossProperty("Mail.PASSWORD");
    	this.MAIL_FROM_ADDRESS = LipossGlobals.getLipossProperty("Mail.FROMADDRESS");
    }
	
	public void postMail(String subject, String message,String recipients[]
			) throws MessagingException {
		boolean debug = false;
//		java.security.Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
 
		//Set the host smtp address
		Properties props = new Properties();
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.starttls.enable","true");
		props.put("mail.smtp.host", SMTP_HOST_NAME);
		props.put("mail.smtp.auth", "true");
 
		Authenticator auth = new SMTPAuthenticator();
		Session session = Session.getDefaultInstance(props, auth);
 
		session.setDebug(debug);
 
		// create a message
		Message msg = new MimeMessage(session);
 
		// set the from and to address
		InternetAddress addressFrom = new InternetAddress(MAIL_FROM_ADDRESS);
		msg.setFrom(addressFrom);
 
		InternetAddress[] addressTo = new InternetAddress[recipients.length];
		for (int i = 0; i < recipients.length; i++) {
			addressTo[i] = new InternetAddress(recipients[i]);
		}
		msg.setRecipients(Message.RecipientType.TO, addressTo);
 
		// Setting the Subject and Content Type  
		msg.setSubject(subject);
		msg.setContent(message, "text/plain;charset=gb2312");	
		Transport.send(msg);
	}
	
	/**
	 * SimpleAuthenticator is used to do simple authentication when the SMTP
	 * server requires it.
	 */
	private class SMTPAuthenticator extends javax.mail.Authenticator {
 
		public PasswordAuthentication getPasswordAuthentication() {
			String username = SMTP_AUTH_USER;
			String password = SMTP_AUTH_PWD;
			return new PasswordAuthentication(username, password);
		}
	}
 
	/**
	 * 获得类的实例
	 * @return instance
	 */
    public static SendMailUsingAuthentication getInstance()
    {
        if(instance == null)
        	instance = new SendMailUsingAuthentication();
        return instance;
    }
    
    /**
     * 测试
     * @param args
     * @throws Exception
     */
	public static void main(String args[]) throws Exception {
		SendMailUsingAuthentication smtpMailSender = SendMailUsingAuthentication.getInstance();
		smtpMailSender.postMail(emailSubjectTxt, emailMsgTxt,emailList);
		logger.debug("Sucessfully Sent mail to All Users");
	}
}

