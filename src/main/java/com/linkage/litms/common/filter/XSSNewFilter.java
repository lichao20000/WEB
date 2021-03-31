package com.linkage.litms.common.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

/**
 * XSS攻击过滤器
 * 考虑到之前已有的XSSFilter里面揉合了防SQL注入的逻辑，不易维护
 * 新疆检测漏洞修复单独提取出SQLFilter，此处只做XSS过滤
 * 后续其他省份漏洞修复可以参照新疆方式
 *
 * created by lingmin on 2020/4/23
 */
public class XSSNewFilter implements Filter {

    public static Logger logger = LoggerFactory.getLogger(XSSNewFilter.class);
    private List<String> urlExclusion = null;
    private boolean isEnable = false;

    public void init(FilterConfig filterConfig) throws ServletException {
        String enable = filterConfig.getInitParameter("isEnable");
        isEnable = "true".equals(enable);
        filterConfig.getServletContext().log("enable XSSFilter ? " + enable);
    }

    public void destroy() {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException
    {
        if(!isEnable){
            chain.doFilter(request, response);
            return;
        }
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String requestPath = ((HttpServletRequest)request).getServletPath();
        requestPath = dealUrl(requestPath);
        //logger.warn("XSSFilter|requestPath:[{}]",requestPath);
        String servletPath = httpServletRequest.getServletPath();
        if (urlExclusion != null && urlExclusion.contains(servletPath)) {
            chain.doFilter(request, response);
        } else {
            chain.doFilter(new XssHttpServletRequestWrapper((HttpServletRequest) request), response);
        }
    }

    private String dealUrl(String ori){
        StringBuilder url = new StringBuilder(ori);
        while (url.indexOf("/") == 0)
        {
            url.deleteCharAt(0);
        }

        String urlStr = url.toString();
        if(url.indexOf("?") != -1){
            urlStr = urlStr.substring(0, url.indexOf("?"));
        }
        return urlStr;
    }

    public List<String> getUrlExclusion() {
        return urlExclusion;
    }

    public void setUrlExclusion(List<String> urlExclusion) {
        this.urlExclusion = urlExclusion;
    }



}
