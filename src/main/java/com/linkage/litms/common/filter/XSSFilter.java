package com.linkage.litms.common.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;

public class XSSFilter implements Filter
{
	public static Logger logger = LoggerFactory.getLogger(XSSFilter.class);
	FilterConfig filterConfig = null;
    private List<String> urlExclusion = null;
    /**敏感词*/
    private static List<String> sensitiveList=null;
	/**白名单url*/
	private static ArrayList<String> legalUrlList=null;
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }
 
    public void destroy() {
        this.filterConfig = null;
    }
 
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
    		throws IOException, ServletException 
    {
    	HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		String requestPath = ((HttpServletRequest)request).getServletPath();
		requestPath = dealUrl(requestPath);
		//logger.warn("requestPath:[{}]",requestPath);
		legalUrlList = initLegalUrlList();
		// 白名单url不拦截
		if(isLegalUrl(requestPath,legalUrlList)){
			if(!sensitiveIsNull())
			{
				Enumeration<?> params = httpServletRequest.getParameterNames();
				StringBuffer sb = new StringBuffer();
				while (params.hasMoreElements())
				{
					// 得到参数名
					String name = params.nextElement().toString();
					// 得到参数对应值
					String[] value = httpServletRequest.getParameterValues(name);
					for (int i = 0; i < value.length; i++) {
						sb.append(value[i]);
					}

					name=null;
					value=null;
				}
//				logger.warn("words :{}",sb.toString());
				//包含敏感词，跳转登陆页
				if (!StringUtil.IsEmpty(sb.toString()) && isSensitive(sb.toString().toLowerCase()))
				{
					logger.warn("ilegal words:[{}]",sb.toString().toLowerCase());
					//GBK或者UTF-8都可以
					response.setContentType("text/html; charset=UTF-8");
//					((HttpServletResponse) response).sendRedirect(httpServletRequest.getContextPath() + "/login.jsp");
					String page="<html><script language='javascript'>alert('非法的参数！');</script></html>";
					((HttpServletResponse) response).getWriter().print(page);

					return;
				}
			}
		}
		String servletPath = httpServletRequest.getServletPath();
        if (urlExclusion != null && urlExclusion.contains(servletPath)) {
            chain.doFilter(request, response);
        } else {
            chain.doFilter(new XssHttpServletRequestWrapper((HttpServletRequest) request), response);
        }
    }
    
    /**
     * 是否有敏感词
     */
    private boolean sensitiveIsNull()
    {
    	if(sensitiveList==null || sensitiveList.isEmpty())
    	{
    		sensitiveList=new ArrayList<String>();
    		// 增加拦截词汇
			sensitiveList.add("alert".toLowerCase());
			// 因为节点参数里面可能包括‘and’,所以增加空格处理
			sensitiveList.add(" and ".toLowerCase());
			sensitiveList.add("insert".toLowerCase());
			sensitiveList.add("delete".toLowerCase());
			sensitiveList.add("update".toLowerCase());
        	logger.warn("sensitiveList:"+sensitiveList);
    	}
    	
    	return (sensitiveList==null || sensitiveList.isEmpty());
    }

    private boolean isLegalUrl(String requestPath, ArrayList<String> legalUrlList){
    	boolean flag = true;
		for (String legalUrl:legalUrlList){
			if(requestPath.indexOf(legalUrl)>-1){
				flag = false;
				break;
			}
		}
		return flag;
	}
	/**
	 *@描述 增加白名单url,包含 .action 或者 .jsp
	 *@参数  []
	 *@返回值  java.util.ArrayList<java.lang.String>
	 *@创建人  lsr
	 *@创建时间  2019/11/1
	 *@throws
	 *@修改人和其它信息
	 */
	private ArrayList<String> initLegalUrlList()
	{
		ArrayList<String> legalUrlList=new ArrayList<String>();
		// 增加白名单action,类似 高级查询
		legalUrlList.add("gwDeviceQuery");
		// 家庭网关批量配置参数
		legalUrlList.add("paramNodeBatchConfigAction");
		legalUrlList.add("alarmConfig");
		legalUrlList.add("batchConfigMaxTerminal");
		legalUrlList.add("paramNodeBatchConfig4QAAction");
		legalUrlList.add("restartDeviceBatch");
		legalUrlList.add("stackRefreshTools");
		legalUrlList.add("batchHttpTest");
		legalUrlList.add("software");
		legalUrlList.add("BatchAddIptvNodeACT");
		legalUrlList.add("modifyVlanTools");
		legalUrlList.add("Stbsoftware");
		legalUrlList.add("setMulticastBatch");
		legalUrlList.add("softwareNew");
		legalUrlList.add("httpDownload");
		legalUrlList.add("httpUpload");
		legalUrlList.add("deviceWhiteList");
		legalUrlList.add("executeSql");
		legalUrlList.add("batchSoftRestartACT");
		legalUrlList.add("system");
		legalUrlList.add("paramConfig");
		legalUrlList.add("fileconfig");
		legalUrlList.add("droitTree");
		legalUrlList.add("Resource");
		legalUrlList.add("jt_sevice_from_save");
		//logger.warn("legalUrlList:[{}]",legalUrlList);
		return legalUrlList;
	}

 
    /**
     * 是否包含敏感词
     */
    private boolean isSensitive(String str)
    {
    	boolean flag=false;
    	
    	if(sensitiveList!=null && !sensitiveList.isEmpty()){
    		for(String word:sensitiveList){
        		if(str.indexOf(word)>-1){
        			flag=true;
        			break;
        		}
        	}
    	}
    	
    	return flag;
    }

	private String dealUrl(String ori){
		StringBuilder url = new StringBuilder(ori);
		while (url.indexOf("/") == 0)
		{
			url.deleteCharAt(0);
		}

		String urlStr = url.toString();
		if(url.indexOf("?")!=-1){
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
