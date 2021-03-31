
package com.linkage.litms.common.tld.ui;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.components.Component;
import org.apache.struts2.views.jsp.ComponentTagSupport;

import com.linkage.litms.common.tld.components.Area;
import com.opensymphony.xwork2.util.ValueStack;

/**
 * <pre>
 * 区域判断标签
 * 提供两种区域判断方式
 * 1.当前项目所属区域在请求参数areaCode之中，即notInMode=false模式。
 * 2.当前项目所属区域不在请求参数areaCode之中，即notInMode=true模式。
 * </pre>
 * 
 * @author fangchao (Ailk No.)
 * @version 1.0
 * @since 2013-8-20
 * @category com.linkage.litms.common.tld.ui
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class AreaTag extends ComponentTagSupport
{

	private static final long serialVersionUID = 6362049211166474886L;
	private String areaCode;
	private String notInMode;

	@Override
	public Component getBean(ValueStack stack, HttpServletRequest req,
			HttpServletResponse res)
	{
		return new Area(stack);
	}

	protected void populateParams()
	{
		((Area) getComponent()).setAreaCode(areaCode);
		((Area) getComponent()).setNotInMode("true".equals(notInMode));
	}

	public void setAreaCode(String areaCode)
	{
		this.areaCode = areaCode;
	}

	public void setNotInMode(String notInMode)
	{
		this.notInMode = notInMode;
	}
}
