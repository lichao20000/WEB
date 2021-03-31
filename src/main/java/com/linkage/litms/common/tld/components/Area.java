
package com.linkage.litms.common.tld.components;

import java.io.Writer;

import org.apache.struts2.components.Component;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
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
 * @category com.linkage.litms.common.tld.components
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class Area extends Component
{

	/**
	 * 区域简码，不能为空
	 */
	private String areaCode;
	/**
	 * <pre>
	 * 区域编码查询方式
	 * 查询提供2中方式，
	 * 1.当前配置区域在areaCode当中查询方式，默认方式,即notInMode=false
	 * 2.当前区域不在areaCode当中查询方式。notInMode=true
	 * </pre>
	 */
	private boolean notInMode;

	public Area(ValueStack stack)
	{
		super(stack);
	}

	public boolean start(Writer writer)
	{
		if (StringUtil.IsEmpty(areaCode))
		{
			// areaCode is empty, always return false
			return false;
		}
		boolean inArea = LipossGlobals.inArea(areaCode);
		return notInMode ? !inArea : inArea;
	}

	public void setAreaCode(String areaCode)
	{
		this.areaCode = areaCode;
	}

	public void setNotInMode(boolean notInMode)
	{
		this.notInMode = notInMode;
	}
}
