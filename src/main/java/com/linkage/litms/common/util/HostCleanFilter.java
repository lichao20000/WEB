package com.linkage.litms.common.util;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.linkage.module.gwms.util.StringUtil;

/**
 * HOST头攻击拦截白名单
 * @author Administrator
 *
 */
public class HostCleanFilter implements Filter{
	
	protected String hostList = "";

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		this.hostList = filterConfig.getInitParameter("hostList");
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
			FilterChain chain) throws IOException, ServletException {
		 	HttpServletRequest request = (HttpServletRequest)servletRequest;
		 	HttpServletResponse response = (HttpServletResponse)servletResponse;  
	        String host=request.getHeader("Host");
	        
	        System.out.println("request.getContextPath()==" + request.getContextPath());
	        
	        System.out.println("request.getLocalName()==" + request.getLocalName());
	        
	        System.out.println("request.getServletPath()==" + request.getServletPath());
	        
	        System.out.println("request.getRequestURI()==" + request.getRequestURI());
	        
	        System.out.println("request.getScheme()==" + request.getScheme());
	        
	        System.out.println("host======" + host);
	        
	        if(!StringUtil.IsEmpty(host)){
	            if(checkBlankList(host,StringUtil.getStringValue(request.getLocalPort()))){
	            	chain.doFilter(servletRequest,servletResponse); 
	            }else{
	            	
	            	String newHsot =  request.getLocalAddr() + ":" + request.getLocalPort();
	            	response.setHeader("Host",newHsot);
	            	String loginUrl = request.getScheme() + "://" + newHsot + "/stb/itv/login.jsp";
	            	response.sendRedirect(loginUrl);
	            }
	             
	        }
		
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}
	
	  /** 
     * 校验当前host是否在白名单中 
     * @param host 
     * @return 
     */  
    private boolean checkBlankList(String host,String port){
    	
    	String [] hostArr = hostList.split(",");
    	for(String hostP : hostArr){
    		if(hostP.equals(host.split(":")[0]) && port.equals(host.split(":")[1])){
    			return true;
    		}
    	}
        return false;  
    }
}
