package com.linkage.module.gwms.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.LipossGlobals;
import com.linkage.module.bbms.dao.EgwcutomerDAO;
import com.linkage.module.gwms.dao.tabquery.CtTokenDAO;
import com.linkage.module.gwms.obj.tabquery.CtTokenOBJ;
import com.linkage.module.gwms.util.StringUtil;

/**
 * 修改，该接口中的用户帐号改为使用电话号码
 * 
 * @author Jason(3412)
 * @date 2010-3-9
 */
public class CtUserRequestDealServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	/**
	 * 日志记录
	 */
	private static Logger logger = LoggerFactory
			.getLogger(CtUserRequestDealServlet.class);

	/**
	 * GET
	 * 
	 * 单点登录的权限控制中心
	 * 
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String userToken = null;

		// 从Cookie中获取UserToken
//		Cookie[] arrCookie = request.getCookies();
//		int len = 0;
//		if (null != arrCookie && (len = arrCookie.length) > 0) {
//			for (int i = 0; i < len; i++) {
//				if ("UserToken".equals(arrCookie[i].getName())) {
//					userToken = arrCookie[i].getValue();
//					break;
//				}
//			}
//		}
		
		userToken = StringUtil.getStringValue(request.getSession().getAttribute("UserToken_BBMS"));
		if(StringUtil.IsEmpty(userToken)){
			userToken = request.getRemoteAddr();
			request.getSession().setAttribute("UserToken_BBMS", userToken);
		}
		
		// 认证UserToken对应的BsToken
		if (null != userToken) {
			// 处理有UserToken的用户请求
			userDealHasToken(request, response, userToken);
		} else {
			// 重定向到网厅首页面
			logger.warn("用户浏览器Cookie未包含UserToken信息");
			// ctMainPageUrl
			response.sendRedirect(LipossGlobals.getCtMainUrl());
			return;
		}
	}

	/**
	 * 处理有UserToken标记的用户请求
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2010-3-10
	 * @return void
	 */
	private void userDealHasToken(HttpServletRequest request,
			HttpServletResponse response, String userToken)
			throws ServletException, IOException {

		// 从Url的参数中获取BSToken
		String bsToken = request.getParameter("BSTokenResponse");
		// 判断是用户直接请求，还是网厅重定向请求
		// 即：路径是否包含(http://ReturnURL?)BSTokenResponse=BSTokenResponseValue
		if (StringUtil.IsEmpty(bsToken)) { // 未包含，用户请求
			logger.warn("用户浏览器请求的URL中未包含BSTokenResponse信息");
			userRequestDeal(request, response, userToken);
		} else {
			ctRequestDeal(request, response, userToken, bsToken);
		}
	}

	/**
	 * 用户跳转请求处理
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2010-3-10
	 * @return void
	 */
	private void userRequestDeal(HttpServletRequest request,
			HttpServletResponse response, String userToken)
			throws ServletException, IOException {

		// 根据userToken从数据库中获取bsToken
		CtTokenOBJ ctTokenObj = CtTokenDAO.getCtTokenOBJ(userToken);

		// 判断是否为空，并是否超时
		if (null == ctTokenObj
				|| (System.currentTimeMillis() > ctTokenObj.getExpireTime())) {
			logger.warn("用户浏览器Cookie中的UserToken({}), 数据库中无对应的BSToken信息 或 已超时",
					userToken);

			// 请求转发到网厅认证页面
			response.sendRedirect(LipossGlobals.getCtAuthUrl()
					+ "?BSTokenRequest=" + bsTokenRequestValue(request));
		} else {
			// 获取username
			String username = ctTokenObj.getUsername();
			if(false == StringUtil.IsEmpty(username) && username.length() > 11){
				//去除区号
				username = username.substring(4);
			}
			// 未超时，请求转发到返回结果页面
			String customerId = EgwcutomerDAO.getEgwCustIdByTelepone(username);
			request.setAttribute("CustomerId", customerId);
			request.getRequestDispatcher(LipossGlobals.getBbmsMainUrl()).forward(
					request, response);
		}
	}

	/**
	 * 网厅重定向请求
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2010-3-10
	 * @return void
	 */
	private void ctRequestDeal(HttpServletRequest request,
			HttpServletResponse response, String userToken, String bsToken)
			throws ServletException, IOException {

		// 网厅重定向，解析并判断BSToken的合法性
		CtTokenOBJ ctTokenObj = CtTokenOBJ.getBSTokenResponseValue(bsToken);
		if (null == ctTokenObj || 0 != ctTokenObj.getResult()) {
			logger.error("网厅提供的BSToken非法, 或者网厅认证未通过");
			response.sendRedirect(LipossGlobals.getCtMainUrl());
			return;
		}

		// 补全ctTokenObj对象的数据
		ctTokenObj.setUserToken(userToken);
		ctTokenObj.setUpdateTime(System.currentTimeMillis() / 1000);

		// 获取username
		String username = ctTokenObj.getUsername();
		if (StringUtil.IsEmpty(username)) {
			logger.error("BSToken.username is empty");
			request.getRequestDispatcher(LipossGlobals.getBbmsErrUrl()).forward(
					request, response);
		} else {
			
			// 根据userToken从数据库中获取bsToken
			CtTokenOBJ oldCtTokenObj = CtTokenDAO.getCtTokenOBJ(userToken);

			// 判断是否为空，并是否超时
			if (null == oldCtTokenObj){
				// 保存CtToken对象记录
				CtTokenDAO.saveCtToken(ctTokenObj);
			}else{
				// 更新CtToken对象记录
				CtTokenDAO.updateCtToken(ctTokenObj);
			}
			
			// 请求转发到返回结果页面
			String url = null;
			String customerId = EgwcutomerDAO.getEgwCustIdByTelepone(username);
			if(StringUtil.IsEmpty(customerId)){
				logger.warn("无对应客户ID");
				url = LipossGlobals.getBbmsErrUrl();
			}else{
				request.setAttribute("CustomerId", customerId);
				url = LipossGlobals.getBbmsMainUrl();
			}
			//重定向
			request.getRequestDispatcher(url).forward(
					request, response);
		}
	}

	
	
	/**
	 * 方法用于获取BSTokenRquestVlalue值，(生成的规则参见接口文档)
	 * 
	  	BSTokenRequestValue的生成算法如下：
		Digest = Base64(Hash(ReturnURL + “$”+ TimeStamp + “$” + MustLogin))
		其中，Hash算法采用SHA-1。
		BSTokenRequestValue = Base64(BSID + “$$”+  Encrypt(ReturnURL + “$”+ TimeStamp + “$” + MustLogin + “$”+ Digest))
		其中，加密算法采用3DES，Key=SPSecret，SPSecret是网上客服中心颁发给该BS的密钥。
	 * 
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2010-3-10
	 * @return String 返回BSTokenRquestVlalue值
	 */
	String bsTokenRequestValue(HttpServletRequest request) {
		logger.debug("bsTokenRequestValue()");
		// 时间戳
		long time = System.currentTimeMillis();
		// 返回路径
		//获取工程路径
		StringBuffer bufRetUrl = request.getRequestURL();
		String contextName = request.getContextPath();
		String returnUrl = bufRetUrl.substring(0, bufRetUrl.indexOf(contextName) + contextName.length());
		//工程路径+配置相对路径
		returnUrl = returnUrl + LipossGlobals.getBbmsAuthUrl();
		
		// 是否必须登录 MustLogin
		String mustLogin = "true";
		//原始加密字符串
		String originalStr = returnUrl + "$" + time + "$" + mustLogin;
		//返回按规则生成的编码字符串
		return CtTokenOBJ.encodeToken(originalStr);
	}


	
	/**
	 * POST
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
	}
}
