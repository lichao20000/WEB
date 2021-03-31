
package com.linkage.module.gtms.itv.dao;

import java.util.List;

import com.linkage.module.gtms.itv.dto.Module;
import com.linkage.module.gtms.itv.dto.SystemTree;


/**
 * 菜单管理数据库接口
 * 
 * @author 陈仲民(5243)
 * @version 1.0
 * @since 2009-12-22
 * @category com.linkage.module.fba.menu.dao<br>
 *           版权：南京联创科技 网管科技部
 */
public interface MenuManagerDAO
{
	
	/**
	 * 初始化所有数据，供配置菜单后调用
	 */
	public void initAll();
	
	/**
	 * 查询模块列表
	 * 
	 * @param roleId
	 *            角色编号
	 * @return
	 */
	public List<Module> getModuleList(long roleId);
	
	/**
	 * 返回指定角色的菜单信息
	 * 
	 * @param roleId
	 * @return
	 */
	public SystemTree getSystemTree(long roleId, String moduleId, String moduleName);
}
