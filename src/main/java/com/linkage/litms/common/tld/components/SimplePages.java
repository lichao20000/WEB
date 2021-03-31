
package com.linkage.litms.common.tld.components;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.components.UIBean;
import org.apache.struts2.portlet.context.PortletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.system.extend.struts.splitpage.ExtendUtils;
import com.linkage.system.extend.struts.splitpage.Pages;
import com.linkage.system.extend.struts.splitpage.UrlHelper;
import com.opensymphony.xwork2.util.ValueStack;

/**
 * <pre>
 * 分页标签定制
 * 该标签是Page的简化版，近提供页面显示上一页和下一页
 * </pre>
 * 
 * @see Pages
 * @author fangchao (Ailk No.)
 * @version 1.0
 * @since 2013-12-19
 * @category com.linkage.litms.common.tld.components
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class SimplePages extends UIBean
{

	private static Logger logger = LoggerFactory.getLogger(SimplePages.class);
	private static final String[] PAGE_SIZE_SELECT = { "10", "20", "30", "50" };
	// 当前页
	private String curPage_splitPage;
	// 总页数
	private String maxPage_splitPage;
	// 链接
	private String url_splitPage;
	// 每页最大记录数
	private String num_splitPage;
	// 参数字符串
	private String paramList_splitPage;
	// 是否可选每页行数
	private boolean selectPageSize;

	public SimplePages(ValueStack stack, HttpServletRequest request,
			HttpServletResponse response)
	{
		super(stack, request, response);
	}

	private String getValue(String expr)
	{
		Object obj = getStack().findValue(expr);
		return obj == null ? null : obj.toString();
	}

	/**
	 * 开始生成html
	 */
	public boolean start(Writer writer)
	{
		boolean result = super.start(writer);
		curPage_splitPage = getValue("curPage_splitPage");
		maxPage_splitPage = getValue("maxPage_splitPage");
		num_splitPage = getValue("num_splitPage");
		paramList_splitPage = getValue("paramList_splitPage");
		if (curPage_splitPage == null || maxPage_splitPage == null
				|| num_splitPage == null || paramList_splitPage == null
				|| url_splitPage == null)
		{
			// invalid parameter, ignore tag content
			return false;
		}
		StringBuilder str = new StringBuilder();
		// 解析参数，生成url
		String tempUrl = getFullURL(url_splitPage) + "?num_splitPage=" + num_splitPage
				+ "&paramList_splitPage=" + paramList_splitPage;
		Integer cpageInt = Integer.valueOf(curPage_splitPage);
		str.append("<span> ");
		str.append("[第 " + curPage_splitPage + " 页]");
		// 总页数为0，只显示“[首页] [末页]”
		if ("1".equals(curPage_splitPage))
		{
			// 第一页，显示“[上一页] [下一页]”
			str.append("[上一页]<a href='");
			str.append(tempUrl);
			str.append("&curPage_splitPage=" + (cpageInt + 1) + "&maxPage_splitPage="
					+ maxPage_splitPage);
			str.append("'>[下一页]</a>");
		}
		else
		{
			// 不是第一页，显示“[上一页] [下一页]
			str.append("<a href='");
			str.append(tempUrl);
			str.append("&curPage_splitPage=" + (cpageInt - 1) + "&maxPage_splitPage="
					+ maxPage_splitPage);
			str.append("'>[上一页]</a> <a href='");
			str.append(tempUrl);
			str.append("&curPage_splitPage=" + (cpageInt + 1) + "&maxPage_splitPage="
					+ maxPage_splitPage);
			str.append("'>[下一页]</a>");
		}
		// 选择每页显示行数
		if (selectPageSize)
		{
			// 跳转的url和方法
			String tempUrl1 = getFullURL(url_splitPage) + "?paramList_splitPage="
					+ paramList_splitPage;
			String changePageNum = "changeNumSplit('" + tempUrl1 + "','"
					+ maxPage_splitPage + "')";
			str.append("&nbsp;每页显示<select name='num_splitPage_select' onchange=\""
					+ changePageNum + "\">");
			// 显示选项
			for (int i = 0; i < PAGE_SIZE_SELECT.length; i++)
			{
				if (num_splitPage.equals(PAGE_SIZE_SELECT[i]))
				{
					str.append("<option value='" + PAGE_SIZE_SELECT[i] + "' selected>"
							+ PAGE_SIZE_SELECT[i] + "</option>");
				}
				else
				{
					str.append("<option value='" + PAGE_SIZE_SELECT[i] + "'>"
							+ PAGE_SIZE_SELECT[i] + "</option>");
				}
			}
			str.append("</select>行");
		}
		str.append("</span>");
		try
		{
			writer.write(str.toString());
		}
		catch (IOException e)
		{
			logger.error(e.getMessage(), e);
			return false;
		}
		return result;
	}

	@Override
	protected String getDefaultTemplate()
	{
		return Pages.TEMPLATE;
	}

	public void setUrl_splitPage(String url_splitPage)
	{
		this.url_splitPage = url_splitPage;
	}

	public void setSelectPageSize(boolean selectPageSize)
	{
		this.selectPageSize = selectPageSize;
	}

	/**
	 * 获取完整的url路径
	 * 
	 * @param url
	 * @return
	 */
	private String getFullURL(String url)
	{
		String result = "";
		if (PortletActionContext.isPortletRequest())
		{
			result = ExtendUtils.buildResourceUrl(url, parameters);
		}
		else
		{
			String _value = url;
			if (_value != null && _value.indexOf("?") > 0)
			{
				_value = _value.substring(0, _value.indexOf("?"));
			}
			result = UrlHelper.buildUrl(_value, request, response, parameters,
					request.getScheme(), true, true);
		}
		return result;
	}
}
