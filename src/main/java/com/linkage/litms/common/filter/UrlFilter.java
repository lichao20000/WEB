
package com.linkage.litms.common.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.DBOperation;
import com.linkage.litms.system.UserRes;



/**
 * <pre>
 * 根据用户访问的url,判断当前url是否在非当前用户角色权限内，是则进index.jsp
 * </pre>
 * 
 * @author fangchao (Ailk No.)
 * @version 1.0
 * @since 2014-4-30
 * @category com.linkage.litms.common.filter
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class UrlFilter implements Filter
{
	private static Logger logger = LoggerFactory.getLogger(UrlFilter.class);
	/**
	 * 在web.xml中可以配置该功能是否启用。
	 */
	private boolean isEnable = false;
	
	/**
	 * 全部可见菜单
	 */
	private Map<String, String> itemMap = null;
	private Map<String, String> legalitemMap = null;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException
	{
		String enable = filterConfig.getInitParameter("isEnable");
		isEnable = "true".equals(enable);
		filterConfig.getServletContext().log("enable UrlFilter ? " + enable);
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException
	{
		if (isEnable)
		{
			String requestPath = ((HttpServletRequest)request).getServletPath();
			requestPath = dealUrl(requestPath);
			/*if(requestPath.indexOf("login.jsp")==-1){*/
				HttpSession session = ((HttpServletRequest)request).getSession();
				
				//从会话中取非法路径map
				itemMap = (Map<String, String>) session.getAttribute("illegalUrl");
				legalitemMap = (Map<String, String>) session.getAttribute("legalUrl");
				//没取到，根据roleid从重新获取
				if(null == itemMap){
					UserRes curUser = (UserRes) session.getAttribute("curUser");
					if(null!=curUser){
						long roleid = curUser.getUser().getRoleId();
						initItem(roleid);
					}
				}
				
				if(null == legalitemMap){
					UserRes curUser = (UserRes) session.getAttribute("curUser");
					if(null!=curUser){
						long roleid = curUser.getUser().getRoleId();
						initItemLegal(roleid);
					}
				}
				
			/*}*/
			//System.out.println("\nitemMap="+itemMap);
			UserRes curUser = (UserRes) session.getAttribute("curUser");
			if(null!=curUser){
				if(!legalitemMap.containsKey(requestPath) && itemMap.containsKey(requestPath)){
					//((HttpServletResponse)response).sendRedirect("/index.jsp");
					logger.warn("illegal url: {}", requestPath);
					return ;
				}
			}
		}
		chain.doFilter(request, response);
	}

	@Override
	public void destroy()
	{
		itemMap = null;
	}

	private void initItem(long roleId)
	{
		itemMap = new HashMap<String, String>();
		String sql = "select item_url,item_id from tab_item where item_id not in (select item_id from tab_item_role where role_id="+roleId+")";
		ArrayList<HashMap<String, String>> list = DBOperation.getRecords(sql);
		if (list != null && !list.isEmpty())
		{
			StringBuilder url = new StringBuilder("");
			String urlStr = "";
			for (int i = 0; i < list.size(); i++)
			{
				Map<String, String> item = list.get(i);
				url = new StringBuilder(item.get("item_url"));
				while (url.indexOf("/") == 0)
				{
					url.deleteCharAt(0);
				}
				
				urlStr = url.toString();
				if(url.indexOf("?")!=-1){
					urlStr = urlStr.substring(0, url.indexOf("?"));
				}
				itemMap.put(urlStr, item.get("item_id"));
			}
		}
	}
	
	
	private void initItemLegal(long roleId)
	{
		legalitemMap = new HashMap<String, String>();
		String sql = "select item_url,item_id from tab_item where item_id in (select item_id from tab_item_role where role_id="+roleId+")";
		ArrayList<HashMap<String, String>> list = DBOperation.getRecords(sql);
		if (list != null && !list.isEmpty())
		{
			StringBuilder url = new StringBuilder("");
			String urlStr = "";
			for (int i = 0; i < list.size(); i++)
			{
				Map<String, String> item = list.get(i);
				url = new StringBuilder(item.get("item_url"));
				while (url.indexOf("/") == 0)
				{
					url.deleteCharAt(0);
				}
				
				urlStr = url.toString();
				if(url.indexOf("?")!=-1){
					urlStr = urlStr.substring(0, url.indexOf("?"));
				}
				legalitemMap.put(urlStr, item.get("item_id"));
			}
		}
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


}
