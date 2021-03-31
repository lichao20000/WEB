
package com.linkage.module.gtms.itv.bio;

import java.util.List;

import com.linkage.module.gtms.itv.dao.MenuManagerDAO;
import com.linkage.module.gtms.itv.dto.Module;
import com.linkage.module.gtms.itv.dto.SystemTree;


/**
 * @author 陈仲民(5243)
 * @version 1.0
 * @since 2009-12-23
 * @category com.linkage.module.fba.menu.bio<br>
 *           版权：南京联创科技 网管科技部
 */
public class MenuManagerBIOImp implements MenuManagerBIO
{
	
	/**
	 * DAO
	 */
	private MenuManagerDAO menuManager;
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.linkage.module.fba.menu.bio.MenuManagerBIO#getModuleList(long)
	 */
	public List<Module> getModuleList(long roleId)
	{
		return menuManager.getModuleList(roleId);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.linkage.module.fba.menu.bio.MenuManagerBIO#getSystemTree(long,
	 *      java.lang.String, java.lang.String)
	 */
	public SystemTree getSystemTree(long roleId, String moduleId, String moduleName)
	{
		return menuManager.getSystemTree(roleId, moduleId, moduleName);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.linkage.module.fba.menu.bio.MenuManagerBIO#initAll()
	 */
	public void initAll()
	{
		menuManager.initAll();
	}
	
	public void setMenuManager(MenuManagerDAO menuManager)
	{
		this.menuManager = menuManager;
	}
	
}
