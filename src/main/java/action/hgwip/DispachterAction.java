package action.hgwip;

import com.opensymphony.xwork2.ActionSupport;

/**
 * ip地址管理菜单的分发入口
 * 
 * @author 王志猛(5194) tel:13701409234
 * @version 1.0
 * @since 2007-11-12
 * @category ipmg
 */
public class DispachterAction extends ActionSupport
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3590541507265618942L;
	private String attr;// 状态属性字符串
	// 格式：userstat/subnet/subnetgrp/inetmask/assign/leaf
	private String actionName = "getSubDetail";// 转向的action名字
	private String nameSpace = "/hgwipMgSys";// action的空间域名
	@Override
	public String execute() throws Exception
	{
		String[] attrs = attr.split("/");
		// 如果出现字符一场，则功能无法继续下去，因为不需要判空
		if ("0".equals(attrs[4]) && "true".equals(attrs[5]))
			{
				// 未划分的ip需要划分
				actionName = "unAssignIP";
			} else
			{
				// 已经划分的ip，可以收回或者取消
				actionName = "assignIP";
			}
		return SUCCESS;
	}
	public void setAttr(String attr)
	{
		this.attr = attr;
	}
	public String getAttr()
	{
		return attr;
	}
	public String getActionName()
	{
		return actionName;
	}
	public String getNameSpace()
	{
		return nameSpace;
	}
}
