package com.linkage.litms.common.filter;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.linkage.litms.system.UserRes;

/**
 * 
 * @author Administrator
 *
 */
public class StaticResourcesFilter implements Filter {

	/**
	 * 在web.xml中可以配置该功能是否启用。
	 */
	private boolean isEnable = false;
	private Set<String> prefixIignores = new HashSet<String>();

	@Override
	public void init(FilterConfig config) throws ServletException {
		String enable = config.getInitParameter("isEnable");
		isEnable = "true".equals(enable);
		String cp = config.getServletContext().getContextPath();
		String ignoresParam = config.getInitParameter("needFilter");
		String[] ignoreArray = ignoresParam.split(",");
		for (String s : ignoreArray) {
			prefixIignores.add(cp + s);
		}
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		if (isEnable) {
			HttpServletRequest request = ServletActionContext.getRequest();  
			HttpServletResponse response = ServletActionContext.getResponse();  
			// 不需要过滤的资源返回
			if (!needFilter(request)) {
				chain.doFilter(request, response);// 已经登录
				return;
			}
			
			HttpSession session=request.getSession();
			//取出名为user的session属性
			UserRes user = (UserRes)session.getAttribute("curUser");
			if (user != null) {
				chain.doFilter(request, response);// 已经登录
				return;
			}
			String path = request.getContextPath();
			String basePath = request.getScheme() + "://"
					+ request.getServerName() + ":"
					+ request.getServerPort() + path + "/login.jsp";
			response.sendRedirect(basePath);
			return;
		}

	}

	@Override
	public void destroy() {
		prefixIignores = null;
	}

	private boolean needFilter(HttpServletRequest request) {
		String url = request.getRequestURI();
		for (String filter : prefixIignores) {
			if (url.startsWith(filter)) {
				return true;
			}
		}
		return false;
	}
}
