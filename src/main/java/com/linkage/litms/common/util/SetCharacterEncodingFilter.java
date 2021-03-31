package com.linkage.litms.common.util;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * Example filter that sets the character encoding to be used in parsing the
 * 
 * incoming request
 * 
 */

public class SetCharacterEncodingFilter implements Filter {
	public SetCharacterEncodingFilter() {
	}

	protected boolean debug = false;

	protected String encoding = null;

	protected FilterConfig filterConfig = null;

	public void destroy() {
		this.encoding = null;
		this.filterConfig = null;
	}

	public void doFilter(ServletRequest request, ServletResponse sResponse,
			FilterChain chain) throws IOException, ServletException {
		// if (request.getCharacterEncoding() == null)
		// {
		// String encoding = getEncoding();
		// if (encoding != null)
		// request.setCharacterEncoding(encoding);
		//
		// }
		HttpServletResponse response = (HttpServletResponse) sResponse;
		response.addHeader("x-frame-options","SAMEORIGIN");
		request.setCharacterEncoding(encoding);
		if (debug) {
				
		}
		chain.doFilter(request, response);
	}

	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
		this.encoding = filterConfig.getInitParameter("encoding");
		this.debug = "true".equalsIgnoreCase(filterConfig
				.getInitParameter("debug"));
	}

	protected String getEncoding() {
		return (this.encoding);
	}
}
