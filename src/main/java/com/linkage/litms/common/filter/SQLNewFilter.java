package com.linkage.litms.common.filter;

import com.linkage.module.gwms.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;

/**
 * SQL注入拦截器
 * 考虑到各省份情况不一样，这里把过滤关键字和白名单Url改为可配的方式
 * 考虑到其他省份可能已上线了sqlFilter
 * 后续其他省份可参考该过滤器
 * created by lingmin on 2020/4/23
 */
public class SQLNewFilter implements Filter {
    private static Logger logger = LoggerFactory.getLogger(SQLNewFilter.class);
    private static ArrayList<String> legalUrlList = new ArrayList<String>();
    private boolean isEnable = false;
    private static ArrayList<String> keyWordList = new ArrayList<String>();
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String enable = filterConfig.getInitParameter("isEnable");
        isEnable = "true".equals(enable);
        filterConfig.getServletContext().log("enable SQLFilter ? " + enable);
        //加载拦截白名单
        String whiteUrlStr = filterConfig.getInitParameter("whiteUrls");
        if(!StringUtil.IsEmpty(whiteUrlStr)){
            Collections.addAll(legalUrlList, whiteUrlStr.split(","));
        }
        filterConfig.getServletContext().log("SQLFilter|legalUrlList:" + legalUrlList);
        //加载拦截关键词
        String keyWordsStr = filterConfig.getInitParameter("keyWords");
        if(!StringUtil.IsEmpty(keyWordsStr)){
            String[] keyWordArray = keyWordsStr.substring(0,keyWordsStr.length() - 1).split(",");
            Collections.addAll(keyWordList, keyWordArray);
        }
        filterConfig.getServletContext().log("SQLFilter|keyWordList:" + keyWordList);
    }


    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        if(!isEnable){
            chain.doFilter(req, res);
            return;
        }
        HttpServletRequest httpReq=(HttpServletRequest)req;
        HttpServletResponse httpRes=(HttpServletResponse)res;
        String requestPath = httpReq.getServletPath();
        requestPath = dealUrl(requestPath);
        //logger.warn("SQLFilter|the requestPath:[{}]",requestPath);
        //若是白名单 则不需要过滤
        if (isLegalUrl(requestPath)){
            chain.doFilter(req, res);
            return;
        }

        Enumeration<?> params = httpReq.getParameterNames();
        StringBuilder paramBuilder = new StringBuilder();
        while (params.hasMoreElements()) {
            // 得到参数名
            String name = params.nextElement().toString();
            if(name.contains("action")){
                continue;
            }
            // 得到参数对应值
            String[] value = httpReq.getParameterValues(name);
            for (String s : value) {
                paramBuilder.append(s);
            }
        }
        if (sqlValidate(paramBuilder.toString())) {
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
    private static boolean sqlValidate(String str) {
        if(StringUtil.IsEmpty(str)){
            return false;
        }
        // 统一转为小写
        str = str.toLowerCase();
        //logger.warn("sqlFilter|the params: {}", str);
        for (String keyword : keyWordList) {
            if (str.contains(keyword)) {
                logger.warn("sqlFilter|this params has sensitive word:{}", str);
                return true;
            }
        }
        return false;
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

    private boolean isLegalUrl(String requestPath){
        boolean flag = false;
        for (String legalUrl : legalUrlList){
            if(requestPath.contains(legalUrl)){
                logger.warn("sqlFilter|this url is the legalUrl,requestPath:{}",requestPath);
                flag = true;
                break;
            }
        }
        return flag;
    }

    @Override
    public void destroy() {}
}
