
package com.linkage.litms.common.tld.ui;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.components.Component;
import org.apache.struts2.views.jsp.ComponentTagSupport;

import com.linkage.litms.common.tld.components.SimplePages;
import com.linkage.system.extend.struts.splitpage.PageTag;
import com.opensymphony.xwork2.util.ValueStack;

/**
 * <pre>
 * 分页制定标签
 * 该标签是PageTag的简化版，近提供页面显示上一页和下一页
 * </pre>
 * 
 * @see PageTag
 * @author fangchao (Ailk No.)
 * @version 1.0
 * @since 2013-12-19
 * @category com.linkage.litms.common.tld.ui
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class SimplePageTag extends ComponentTagSupport
{

	private static final long serialVersionUID = 3470958001094979887L;
	// 链接
	private String url;
	// 是否可选每页行数
	private String selectPageSize = "true";

	@Override
	public Component getBean(ValueStack stack, HttpServletRequest req,
			HttpServletResponse res)
	{
		return new SimplePages(stack, req, res);
	}

	protected void populateParams()
	{
		super.populateParams();
		SimplePages pages = (SimplePages) component;
		pages.setUrl_splitPage(url);
		pages.setSelectPageSize("true".equals(selectPageSize));
	}

	public void setUrl(String url)
	{
		this.url = url;
	}

	public void setSelectPageSize(String selectPageSize)
	{
		this.selectPageSize = selectPageSize;
	}
}
