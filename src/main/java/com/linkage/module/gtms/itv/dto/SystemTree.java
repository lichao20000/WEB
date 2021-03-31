
package com.linkage.module.gtms.itv.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * 系统菜单树
 * 
 * @author 陈仲民(5243)
 * @version 1.0
 * @since 2009-12-23
 * @category com.linkage.module.fba.menu.dto<br>
 *           版权：南京联创科技 网管科技部
 */
public class SystemTree
{
	
	/**
	 * 模块名称
	 */
	private String moduleName = "";
	
	/**
	 * 模块编号
	 */
	private String moduleId = "";
	
	/**
	 * 节点列表
	 */
	private List<NodeInfo> nodelList = new ArrayList<NodeInfo>();
	
	public String getModuleName()
	{
		return moduleName;
	}
	
	public void setModuleName(String moduleName)
	{
		this.moduleName = moduleName;
	}
	
	public List<NodeInfo> getNodelList()
	{
		return nodelList;
	}
	
	public void setNodelList(List<NodeInfo> nodelList)
	{
		this.nodelList = nodelList;
	}
	
	public String getModuleId()
	{
		return moduleId;
	}
	
	public void setModuleId(String moduleId)
	{
		this.moduleId = moduleId;
	}
	
}
