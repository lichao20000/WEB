
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

import com.linkage.commons.db.DBOperation;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.system.User;
import com.linkage.litms.system.UserRes;
import com.linkage.litms.system.dbimpl.LogItem;
import com.linkage.module.gtms.system.dao.RecordLogDAO;
import com.linkage.module.gwms.Global;

/**
 * <pre>
 * 记录系统操作日志
 * 根据用户访问的url,判断当前url是否在系统菜单表tab_item中。
 * 如果是则将当前用户访问该菜单信息记录入库。
 * </pre>
 * 
 * @author fangchao (Ailk No.)
 * @version 1.0
 * @since 2014-4-30
 * @category com.linkage.litms.common.filter
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class OperationLogFilter implements Filter
{

	/**
	 * 在web.xml中可以配置该功能是否启用。
	 */
	private boolean isEnable = false;
	private Map<String, ItemEntry> itemMap = null;
	private RecordLogDAO logDAO = null;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException
	{
		String enable = filterConfig.getInitParameter("isEnable");
		isEnable = "true".equals(enable);
		filterConfig.getServletContext().log("enable OperationLogFilter ? " + enable);
		if (isEnable)
		{
			logDAO = new RecordLogDAO();
		}
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException
	{
		if (isEnable)
		{
			
			// 20200716 吉林联通操作日志
			if(Global.instAreaShortName.equals(Global.JLLT)) 
			{
				writeItemLog((HttpServletRequest) request, 1, "Web","","成功");
			}
			else 
			{
				if (itemMap == null)
				{
					initItem();
				}
				String requestPath = getRequestPath((HttpServletRequest) request);
				ItemEntry itemEntry = itemMap.get(requestPath);
				if (itemEntry != null)
				{
					addItemLog(itemEntry, (HttpServletRequest) request);
				}
			}
		}
		chain.doFilter(request, response);
	}

	@Override
	public void destroy()
	{
		itemMap = null;
		logDAO = null;
	}

	private synchronized void initItem()
	{
		if (itemMap == null)
		{
			itemMap = new HashMap<String, ItemEntry>();
			String sql = "select item_id,item_name,item_url from tab_item where item_visual='1'";
			ArrayList<HashMap<String, String>> list = DBOperation.getRecords(sql);
			if (list != null && !list.isEmpty())
			{
				for (int i = 0; i < list.size(); i++)
				{
					Map<String, String> item = list.get(i);
					ItemEntry entry = new ItemEntry();
					entry.itemId = item.get("item_id");
					entry.itemName = item.get("item_name");
					entry.setItemUrl(item.get("item_url"));
					itemMap.put(entry.itemUrl, entry);
				}
			}
		}
	}

	private String getRequestPath(HttpServletRequest request)
	{
		String servletPath = request.getServletPath();
		StringBuilder result = new StringBuilder(servletPath);
		while (result.indexOf("/") == 0)
		{
			result.deleteCharAt(0);
		}
		String queryString = request.getQueryString();
		if (queryString != null)
		{
			result.append("?").append(queryString);
		}
		return result.toString();
	}

	private void addItemLog(ItemEntry entry, HttpServletRequest request)
	{
		UserRes curUser = (UserRes) request.getSession().getAttribute("curUser");
		// 对于session失效的情况，不做日志处理。struts2拦截器会要求用户重新登录。
		if (curUser != null)
		{
			User user = curUser.getUser();
			logDAO.recordOperLog(user.getAccount(), request.getRemoteAddr(),
					request.getRemoteHost(), user.getId(), entry.itemId, entry.itemName);
		}
	}
	/**
	 * 20200717 吉林日志查询改为从这里走
	 * @param request
	 * @param type
	 * @param device
	 * @param content
	 * @param result
	 */
	private void writeItemLog(HttpServletRequest request, int type, String device, String content, String result) 
	{
		UserRes curUser = (UserRes) request.getSession().getAttribute("curUser");
		// 对于session失效的情况，不做日志处理。struts2拦截器会要求用户重新登录。
		if (curUser != null)
		{
			LogItem.getInstance().writeItemLog(request, type, device, content, result);
		}
	}
	
	private class ItemEntry
	{

		private String itemId;
		private String itemName;
		/**
		 * 在过滤用户请求URL时去掉/前缀，该地方也需要去掉/前缀，保持一致
		 */
		private String itemUrl;

		private void setItemUrl(String itemUrl)
		{
			StringBuilder url = new StringBuilder(itemUrl);
			while (url.indexOf("/") == 0)
			{
				url.deleteCharAt(0);
			}
			this.itemUrl = url.toString();
		}

		@Override
		public String toString()
		{
			StringBuilder builder = new StringBuilder();
			builder.append("ItemEntry [itemId=");
			builder.append(itemId);
			builder.append(", itemName=");
			builder.append(itemName);
			builder.append("]");
			return builder.toString();
		}
	}
}
