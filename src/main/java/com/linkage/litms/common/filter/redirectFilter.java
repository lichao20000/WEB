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
import javax.servlet.http.HttpSession;


import com.linkage.litms.system.UserRes;

public class redirectFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // TODO Auto-generated method stub

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        // 获得在下面代码中要用的request,response,session对象
        HttpServletRequest servletRequest = (HttpServletRequest) request;
        HttpServletResponse servletResponse = (HttpServletResponse) response;
        HttpSession session = servletRequest.getSession();
        UserRes curUser = (UserRes)session.getAttribute("curUser");
        // 获得用户请求的URI
        String path = servletRequest.getRequestURI();
        //System.out.println(path);
        if(curUser != null){
        	if(path.indexOf("login.jsp") > -1 && path.indexOf("newlogin.jsp") < 0){
        		servletResponse.sendRedirect("/itms/redirectInmp.jsp");
        	}else{
        		chain.doFilter(servletRequest, servletResponse);
                return;
        	}
        	
        }else{
        	// 未登陆前处理，
            if(path.indexOf("newlogin.jsp") > -1 || path.indexOf("checkuser.jsp") > -1 || path.indexOf("redirectInmp.jsp") > -1 || path.indexOf("error.jsp") > -1) {
                chain.doFilter(servletRequest, servletResponse);
                return;
            }else{
            	servletResponse.sendRedirect("/itms/redirectInmp.jsp");
            }
        }

    }

    @Override
    public void destroy() {
        // TODO Auto-generated method stub

    }

}