package com.linkage.litms.uss;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.UrlEncode;

public class CheckURL extends HttpServlet {
	/** log */
	private static Logger logger = LoggerFactory.getLogger(CheckURL.class);
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
			IOException {
		UssLog.log("[CheckURL]----------:doGet");
		doPost(req, resp);
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		UssLog.log("[CheckURL]----------:doPost");
		
		checkParams(req, resp);
	}

//	protected void service(HttpServletRequest req, HttpServletResponse resp)
//			throws ServletException, IOException {
//		logger.debug("ABC-------------3");
//
//		checkParams(req, resp);
//	}

	/**
	 * 检测各参数
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	private void checkParams(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		PrintWriter pw = null;
		String urlHead = "http://192.168.228.192/bbms/UserSelfServ/";
		String returnPage = "";
		
		// 客户ID
		String CustomerId = req.getParameter("CustomerId");
		// 备注，保留字段，内容可以为空
		String Desc = req.getParameter("Desc");
		// 当前时间戳， yyyy-MM-dd HH:mm:ss
		String TimeStamp = req.getParameter("TimeStamp");
		// 用于标示发起方的唯一标识，编码如下：
		// 网上营业厅：12
		String SysId = req.getParameter("SysId");
		// 为发送方生成的唯一流水，生成规则自定义，接收方需要校验此字段，确保唯一性
		String SeqNbr = req.getParameter("SeqNbr");
		// 认证码。
		// 取值：BASE64(3DES(CustomerId+$+TimeStamp+$+ SeqNbr))
		String Authenticator = req.getParameter("Authenticator");

		HttpSession session = req.getSession();
		
		ServletContext application = getServletContext();
		
		UssLog.log("[CustomerId]----------:" + CustomerId);
		UssLog.log("[TimeStamp]----------:" + TimeStamp);
		UssLog.log("[SysId]----------:" + SysId);
		UssLog.log("[SeqNbr]----------:" + SeqNbr);
		UssLog.log("[Authenticator]----------:" + Authenticator);
		
		
//		if (req.getRequestURI().indexOf("login.jsp") == -1) {
//			//req.getRequestDispatcher("/UserSelfServ/error.jsp?errid=5").forward(req, resp);
//		} else  
		
//		if (null == CustomerId || "".equals(CustomerId)) {
//			
//			UserMap.addCheckedResult("ERROR", "1");
//			
//		} else 

		if (null == TimeStamp
				|| "".equals(TimeStamp) || null == SysId || "".equals(SysId) || null == SeqNbr
				|| "".equals(SeqNbr) || null == Authenticator || "".equals(Authenticator)) {
			// 主要参数为空
			//resp.sendRedirect("error.jsp?errid=0");
			
			//getServletConfig().getServletContext().
			
			//req.getRequestDispatcher("error.jsp?errid=0").forward(req, resp);
			//returnPage = "error.jsp?errid=0";
			
			//returnPage = "index.jsp?type=1";
			
			UserMap.getInstance().addCheckedResult(CustomerId, "1");
		}
		
//		else if (!chkTimeStamp(TimeStamp)) {
//			// 时间格式不对
//			//resp.sendRedirect("error.jsp?errid=1");
//			returnPage = "error.jsp?errid=1";
//		} 
		
//		else if (!"12".equals(SysId)) {
//			// 检查请求发起方是否为网上营业厅
//			//resp.sendRedirect("error.jsp?errid=2");
//			//returnPage = "error.jsp?errid=2";
//			returnPage = "index.jsp?errid=2";
//		} 
		
//		else if (!chkSeqNbr(SeqNbr, application)) {
//			// SeqNbr对应的session不唯一
//			//resp.sendRedirect("error.jsp?errid=3");
//			returnPage = "index.jsp?type=2";
//			
//		} 
		
		else if (!chkAuthenticator(Authenticator, CustomerId, TimeStamp, SeqNbr)) {
			// 检查验证码
			//resp.sendRedirect("error.jsp?errid=4");
			
			//returnPage = "index.jsp?type=2";
			
			UserMap.getInstance().addCheckedResult(CustomerId, "2");
			
		}
		else {
			// 如果登录成功
			//UserMap.getInstance().addOnlineSession(SeqNbr, session);
			
			//application.setAttribute(SeqNbr, SeqNbr);
			
			//getServletConfig().getServletContext().getRequestDispatcher("/index.jsp").forward(req, resp);
			//resp.sendRedirect("index.jsp?CustomerId="+CustomerId+"&SeqNbr="+SeqNbr);
			//req.getRequestDispatcher("index.jsp?CustomerId="+CustomerId+"&SeqNbr="+SeqNbr).forward(req, resp);
			
			//returnPage = "index.jsp?type=0&CustomerId="+CustomerId;//+"&SeqNbr="+SeqNbr;
			
			UserMap.getInstance().addCheckedResult(CustomerId, "0");
			
		}
		
//		try {
//			pw = resp.getWriter();
//			
//			pw.print(urlHead + returnPage);
//			pw.flush();
//		} catch (IOException e) {
//			UssLog.log("ERROR--------response IOException-----------");
//		} finally {
//			pw.close();
//		}
		
		logger.debug("USERMAP------------:" + UserMap.getInstance().getCheckedResult());
		//req.getRequestDispatcher(returnPage).forward(req, resp);
	}

	/**
	 * 检测时间戳格式是否正确
	 * 
	 * @param TimeStamp
	 * @return
	 */
	private boolean chkTimeStamp(String TimeStamp) {

		String pat = "^\\d{4}-\\d{2}-\\d{2}\\s\\d{2}:\\d{2}:\\d{2}$";
		Pattern p = Pattern.compile(pat);
		// "2008-10-10 10:05:10"
		Matcher m = p.matcher(TimeStamp);
		return m.matches();
	}

	/**
	 * 存在该SeqNbr是否已存在Session
	 * 
	 * @return
	 */
	private boolean chkSeqNbr(String SeqNbr, ServletContext application) {

		//HashMap<String, HttpSession> onlineSessions = UserMap.getInstance().getOnlineSession();
		if (null == application.getAttribute(SeqNbr)) {
			return true;
		}
		logger.debug("application.getAttribute(SeqNbr)------------:" + application.getAttribute(SeqNbr));
		return false;
	}

	/**
	 * 检测认证码是否正确
	 * 
	 * @param Authenticator
	 * @param CustomerId
	 * @param TimeStamp
	 * @param SeqNbr
	 * @return
	 */
	private boolean chkAuthenticator(String Authenticator, String CustomerId, String TimeStamp,
			String SeqNbr) {
		if (Authenticator.equals(retEncode(CustomerId, TimeStamp, SeqNbr))) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 获取加密字段
	 * 
	 * @param CustomerId
	 * @param CustomerId
	 * @param SeqNbr
	 * @return
	 */
	private String retEncode(String CustomerId, String TimeStamp, String SeqNbr) {
		String param = CustomerId + "$" + TimeStamp + "$" + SeqNbr;

		String key = "abcdefghijklmnopqrstuvwu";
		//logger.debug("key:" + key);
		UrlEncode url = new UrlEncode();
		String encode = url.urlEncoding(param, key);
		logger.debug("encode:" + encode);
		return encode;
	}
}
