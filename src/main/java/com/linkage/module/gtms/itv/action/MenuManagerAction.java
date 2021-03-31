
package com.linkage.module.gtms.itv.action;

import java.util.List;

import com.linkage.module.gtms.itv.bio.MenuManagerBIO;
import com.linkage.module.gtms.itv.dto.Module;
import com.linkage.module.gtms.itv.dto.SystemTree;
import com.linkage.module.liposs.system.basesupport.BaseSupportAction;

/**
 * 菜单管理类
 * 
 * @author 陈仲民(5243)
 * @version 1.0
 * @since 2009-12-23
 * @category com.linkage.module.fba.menu.action<br>
 *           版权：南京联创科技 网管科技部
 */
public class MenuManagerAction extends BaseSupportAction
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5400127148645192416L;
	
	/**
	 * BIO
	 */
	private MenuManagerBIO menuManager;
	
	/**
	 * 模块编号
	 */
	private String moduleId;
	
	/**
	 * 模块名称
	 */
	private String moduleName;
	
	/**
	 * 模块列表
	 */
	private List<Module> moduleList;
	
	/**
	 * 系统菜单
	 */
	private SystemTree systemTree;
	
	/**
	 * 登陆账号
	 */
	private String userName;
	
	/**
	 * 登陆域
	 */
	private String area;
	
	/**
	 * 获取模块类表页面
	 * 
	 * @return
	 * @throws Exception
	 */
	public String createModule() throws Exception
	{
		moduleList = menuManager.getModuleList(getUser().getRoleId());
		
		userName = getUser().getAccount();
		area = "";
		
		return "module";
	}
	
	/**
	 * 生成菜单树
	 * 
	 * @return
	 * @throws Exception
	 */
	public String createTree() throws Exception
	{
		systemTree = menuManager.getSystemTree(getUser().getRoleId(), moduleId,
				moduleName);
		
		return "treeNew";
	}
	
	/**
	 * 初始化所有菜单
	 * 
	 * @return
	 * @throws Exception
	 */
	public String initAll() throws Exception
	{
		menuManager.initAll();
		
		return "result";
	}
	
	public void setMenuManager(MenuManagerBIO menuManager)
	{
		this.menuManager = menuManager;
	}
	
	public String getModuleId()
	{
		return moduleId;
	}
	
	public void setModuleId(String moduleId)
	{
		this.moduleId = moduleId;
	}
	
	public String getModuleName()
	{
		return moduleName;
	}
	
	public void setModuleName(String moduleName)
	{
		this.moduleName = moduleName;
	}
	
	public List<Module> getModuleList()
	{
		return moduleList;
	}
	
	public void setModuleList(List<Module> moduleList)
	{
		this.moduleList = moduleList;
	}
	
	public SystemTree getSystemTree()
	{
		return systemTree;
	}
	
	public void setSystemTree(SystemTree systemTree)
	{
		this.systemTree = systemTree;
	}

	
	public String getUserName()
	{
		return userName;
	}

	
	public void setUserName(String userName)
	{
		this.userName = userName;
	}

	
	public String getArea()
	{
		return area;
	}

	
	public void setArea(String area)
	{
		this.area = area;
	}
	
}
