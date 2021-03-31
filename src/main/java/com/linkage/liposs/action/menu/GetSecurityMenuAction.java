package com.linkage.liposs.action.menu;

import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.liposs.action.cst;
import com.linkage.liposs.buss.menu.MenuSecurityGWDAO;
import com.linkage.liposs.buss.menu.SercurityItem;
import com.linkage.litms.system.UserRes;
import com.opensymphony.xwork2.ActionSupport;

/**
 * 该功能主要负责
 * 
 * @author 王志猛(5194) tel：13701409234
 * @version 1.0
 * @since 2008-3-27
 * @category com.linkage.liposs.action.menu 版权：南京联创科技 网管科技部
 * 
 */
public class GetSecurityMenuAction extends ActionSupport implements SessionAware
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 974609642829650773L;
	private MenuSecurityGWDAO msDao;// 获取菜单信息的DAO
	private SercurityItem sgwItem; // 数据源容器
	private Map session;// 会话
	private UserRes res;// 用户资源
	private static Logger log = LoggerFactory.getLogger(GetSecurityMenuAction.class);
	public String execute() throws Exception
	{
		res = (UserRes) session.get("curUser");
		sgwItem = msDao.getSecurityGWMenu(String.valueOf(res.getAreaId()));
		return SUCCESS;
	}
	/**
	 * 重新装载内存中的菜单数据
	 * 
	 * @return
	 * @throws Exception
	 *             数据库异常
	 */
	public String reloadSGWMenu() throws Exception
	{
		msDao.reloadGWMenu();
		return cst.OK;
	}
	public void setMsDao(MenuSecurityGWDAO msDao)
	{
		this.msDao = msDao;
	}
	public void setSession(Map arg0)
	{
		this.session = arg0;
	}
	public SercurityItem getSgwItem()
	{
		return sgwItem;
	}
}
