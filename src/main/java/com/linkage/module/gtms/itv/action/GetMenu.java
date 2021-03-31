
package com.linkage.module.gtms.itv.action;

import java.util.ArrayList;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.linkage.litms.system.UserRes;
import com.linkage.module.gtms.itv.dao.MenuInit;
import com.linkage.module.gtms.itv.dto.MenuItem;
import com.opensymphony.xwork2.ActionSupport;

/**
 * @author 王志猛(5194) tel:13701409234
 * @version 1.0
 * @since 2007-07-30
 * @category 通过role_id获取菜单的aciton类
 */
public class GetMenu extends ActionSupport implements SessionAware
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8276841015053982387L;
	private Map session;
	private UserRes res;
	private ArrayList<MenuItem> menu;
	private MenuInit menuInit;
	// 菜单编号
	private String menuId;
	// ajax
	private String ajax;
	
	@Override
	public String execute() throws Exception
	{
		res = (UserRes) session.get("curUser");
		menu = menuInit.getMenuByRole(String.valueOf(res.getUser().getRoleId()));
		return SUCCESS;
	}
	
	/**
	 * 获取菜单导航信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public String initBar() throws Exception
	{
		ajax = menuInit.initBar(menuId);
		return "ajax";
	}
	
	public void setMenuId(String menuId)
	{
		this.menuId = menuId;
	}
	
	public String getAjax()
	{
		return ajax;
	}
	
	public ArrayList<MenuItem> getMenu()
	{
		return menu;
	}
	
	public void setMenuInit(MenuInit menuInit)
	{
		this.menuInit = menuInit;
	}
	
	public void setSession(Map session)
	{
		this.session = session;
	}
}
