
package com.linkage.module.ids.util;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.jasig.cas.client.validation.Assertion;

//import com.linkage.litms.system.UserMap;
import com.linkage.litms.system.UserMap;
import com.linkage.system.utils.Encoder;

/**
 * @author dongwei (Ailk No.69991)
 * @version 1.0
 * @since 2013-10-10
 * @category com.linkage.module.ims.system.common.filter
 * @copyright Ailk NBS-Network Mgt. RD Dept. 用来做单点登录的过滤器
 */
public class AutoSetUserAdapterFilter implements Filter
{

	// log4j日志
	private static Logger log = Logger.getLogger(AutoSetUserAdapterFilter.class);

	@Override
	public void init(FilterConfig filterConfig) throws ServletException
	{
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException
	{
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		HttpSession session = httpRequest.getSession();
		
		/*
		 * if (httpRequest.getQueryString().indexOf("logout?service=") > -1) {
		 * chain.doFilter(httpRequest, httpResponse); return; }
		 */
		// _const_cas_assertion_是CAS中存放登录用户名的session标志
		//此处用的是全局的seesion,要本系统自己的session区分开
		try {
			// AbstractCasFilter.CONST_CAS_ASSERTION =_const_cas_assertion;
			Object object = session.getAttribute("_const_cas_assertion_"); 
			Enumeration en = session.getAttributeNames();
			while (en.hasMoreElements()) {
			     Object o= en.nextElement();
			     log.warn(o.toString());
			}
			
			if (object != null)
			{
				
				Assertion assertion = (Assertion) object;
				String str_username = assertion.getPrincipal().getName();
				//str_username = Encoder.getBase64(str_username);
				// admin.com
				//String domain = Encoder.getBase64("admin.com");
				// 这个request里面必须有 ：acc_loginname area_name
				log.warn("str_username = "+str_username);
				log.warn("domain = admin.com");
				
				httpRequest.setAttribute("acc_loginname", str_username);
				// 暂时写死 admin.com，系统暂时只有这个域名
				httpRequest.setAttribute("area_name", "admin.com");
				//暂时不做权限控制
			    DbAuthorizationCAS auth = new DbAuthorizationCAS(httpRequest, httpResponse);
				//这个方法用来将curUser写入session，同时加载权限等信息
				auth.initUserRes();
				session.setAttribute("IsLogin", "true");// 标示登陆成功
				UserMap.getInstance().addOnlineSession(Encoder.getFromBase64(str_username),
						session);
				// 向session写入信息
				session.setAttribute("rand", "rand");
			}
			log.warn("进入单点登录后");
			chain.doFilter(httpRequest, httpResponse);
			return;
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	public void destroy()
	{
		// TODO Auto-generated method stub
	}
}
