package com.linkage.module.gwms.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.LipossGlobals;
import com.linkage.module.gwms.obj.tabquery.CtTokenOBJ;
import com.linkage.module.gwms.util.StringUtil;

/**
 * @author Jason(3412)
 * @date 2010-3-10
 */
public class CtTestServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	/**
	 * 日志记录
	 */
	private static Logger logger = LoggerFactory
			.getLogger(CtTestServlet.class);

	/**
	 * GET
	 * 
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String userToken = null;
		// 从Cookie中获取UserToken
		Cookie[] arrCookie = request.getCookies();
		int len = 0;
		if (null != arrCookie && (len = arrCookie.length) > 0) {
			for (int i = 0; i < len; i++) {
				if ("UserToken".equals(arrCookie[i].getName())) {
					userToken = arrCookie[i].getValue();
					break;
				}
			}
		}
		logger.debug("userToken:{}", userToken);
		
		if (StringUtil.IsEmpty(userToken)) {
			logger.debug("response.setHeader(Set-Cookie)");
			response
					.setHeader("Set-Cookie",
							"UserToken=bbmsUserToken; Path=/; HTTPOnly");
			
//			//直接用Cookie的对象无法实现HttpOnly
//			Cookie userTokenCookie = new Cookie("userToken", "bbmsUserToken");
//			userTokenCookie.setDomain("ct10000.com");
//			userTokenCookie.setMaxAge(1000000);
//			response.addCookie(userTokenCookie);
			
		}

		String bsTokenRequest = request.getParameter("BSTokenRequest");
		logger.debug("bsTokenRequest:({})", bsTokenRequest);

		if (null != bsTokenRequest) {
			//获取工程路径
			StringBuffer bufRetUrl = request.getRequestURL();
			String contextName = request.getContextPath();
			String retUrl = bufRetUrl.substring(0, bufRetUrl.indexOf(contextName) + contextName.length());
			//工程路径+配置相对路径
			retUrl += retUrl + LipossGlobals.getBbmsAuthUrl();
			

			logger.debug("retUrl:{}", retUrl);
			/**
			 * 
			 * BSTokenResponseValue的生成算法如下： Digest = Base64(Hash(Result + “$” +UserID + “$”+ ClientIP +“$”+ LoginTime +“$”+ LoginType +“$”+ LoginLevel +“$”+ AccNbrList + “$”+ TimeStamp + “$” ＋ ExpireTime))
			 * 其中，Hash算法采用SHA-1。 BSTokenResponseValue = Base64(BSID + “$$” + Encrypt (Result + “$” + UserID + “$”+ ClientIP +“$”＋LoginTime +“$”+ LoginType +“$”+ LoginLevel +“$”+ AccNbrList + “$”+ TimeStamp + “$” ＋ ExpireTime + “$”+ Digest))
			 * 
			 */

			String bsTokenResponseValue = "0$b00023542$"
					+ request.getRemoteHost()
					+ "$2010-03-10 15:30:20$1$1$$1324567890$1324597890";

			retUrl += "?BSTokenResponse="
					+ CtTokenOBJ.encodeToken(bsTokenResponseValue);
			response.sendRedirect(retUrl);
		}

		
		PrintWriter pw = response.getWriter();
		pw.print("OK");
		pw.flush();
		pw.close();

	}

	/**
	 * POST
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);

	}
}



class CookieTest {

	public void test(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		/*
		 * 写cookie
		 */
		Cookie namecookie = new Cookie("name", "zhaixf");
		Cookie passwordcookie = new Cookie("password", "123456789");
		Cookie optioncookie = new Cookie("option", "1");

		// 生命周期
		namecookie.setMaxAge(60 * 60 * 24 * 365);
		passwordcookie.setMaxAge(60 * 60 * 24 * 365);
		optioncookie.setMaxAge(60 * 60 * 24 * 365);

		response.addCookie(namecookie);
		response.addCookie(passwordcookie);
		response.addCookie(optioncookie);

		/*
		 * 读cookie
		 */
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			String name = "";
			String password = "";
			String option = "";
			for (int i = 0; i < cookies.length; i++) {
				Cookie c = cookies[i];
				if (c.getName().equalsIgnoreCase("name")) {
					name = c.getValue();
				} else if (c.getName().equalsIgnoreCase("password")) {
					password = c.getValue();
				} else if (c.getName().equalsIgnoreCase("option")) {
					option = c.getValue();
				}
			}
		}
	}
}