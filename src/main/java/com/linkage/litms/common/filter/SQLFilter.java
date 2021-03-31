package com.linkage.litms.common.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.StrutsStatics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.util.DateTimeUtil;

public class SQLFilter implements Filter {
	private static Logger logger = LoggerFactory.getLogger(SQLFilter.class);
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res,FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpReq=(HttpServletRequest)req;   
        HttpServletResponse httpRes=(HttpServletResponse)res;  
		Map<String, Object>  parameterMap = httpReq.getParameterMap();
		
		Enumeration<?> params = httpReq.getParameterNames();
		String sql = "";
		while (params.hasMoreElements()) {
			// 得到参数名
			String name = params.nextElement().toString();
			// 得到参数对应值
			String[] value = httpReq.getParameterValues(name);
			for (int i = 0; i < value.length; i++) {
				sql = sql + value[i];
			}
		}
		if (sqlValidate(sql)) {	
			String CONTENT_TYPE = "text/html; charset=GBK";
			////GBK或者UTF-8都可以
			httpRes.setContentType(CONTENT_TYPE);
			//httpRes.sendRedirect(httpReq.getContextPath() + "/login.jsp");
			httpRes.getWriter().print("<html><script language='javascript'>alert('非法的参数！');</script></html>");
		} else {
			chain.doFilter(req, res);
		}

	}
			
		      
		// 校验SQL
		protected static boolean sqlValidate(String str) {
			// 统一转为小写
			str = str.toLowerCase();
			// 转换为数组
			logger.warn("str:{}",str);
			// 过滤掉的SQL关键字，可以手动添加
			String[] badStrs = {"insert","update","delete","and","INSERT","UPDATE","DELETE","AND"};
			for (int i = 0; i < badStrs.length; i++) {
				// 检索
				if (str.indexOf(badStrs[i]) > 0) {
					return true;
				}
			}
			return false;
		}
		

	@Override
	public void destroy() {

	}

}
