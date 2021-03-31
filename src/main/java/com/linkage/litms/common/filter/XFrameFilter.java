package com.linkage.litms.common.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class XFrameFilter implements Filter{
	FilterConfig filterConfig = null;
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
 
        httpServletResponse.setHeader("x-frame-options","SAMEORIGIN");
        httpServletResponse.addHeader("Set-Cookie", "uid=112; Path=/; Secure; HttpOnly");
        chain.doFilter(httpServletRequest,httpServletResponse);
    }
    
    public void init(FilterConfig filterConfig) throws ServletException {
    }
 
    public void destroy() {
    }

}
