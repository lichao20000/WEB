package com.linkage.litms.init;

import java.io.IOException;

import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.mail.SendMailUsingAuthentication;
/**
 * 对外提供发送邮件servlet
 * @author zhangsm (AILK No.)
 * @version 1.0
 * @since 2014-6-17
 * @category com.linkage.litms.init
 * @copyright AILK NBS-Network Mgt. RD Dept.
 */
public class SendMailServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** log */
	private static Logger logger = LoggerFactory.getLogger(SendMailServlet.class);
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException
	{
		this.doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException
	{
		// TODO Auto-generated method stub
		String clientId = req.getParameter("clientId");
		String subject = req.getParameter("subject");
		String message = req.getParameter("message");
		String[] recipients = req.getParameter("recipients").split(",");
		if(StringUtil.IsEmpty(subject)||StringUtil.IsEmpty(message)||recipients.length<1)
		{
			logger.warn("clientId:[{}] send mail Parameter is null",new Object[]{clientId});
			resp.getWriter().write("send mail Parameter is null");
		}
		try
		{
			logger.warn("clientId:[{}]，subject:[{}]，message:[{}]，recipients:[{}] send mail now",new Object[]{clientId,subject,message,recipients});
			new SendMailUsingAuthentication().postMail(subject, message, recipients);
		}
		catch (Exception e)
		{
			logger.error("clientId:[{}] send mail exception:{}",new Object[]{clientId,e.getMessage()});
			resp.getWriter().write("send mail exception.");
			e.printStackTrace();
		}
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		logger.warn("SendMailServlet is start ok!");
	}

}
