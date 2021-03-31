
package com.linkage.module.gtms.itv.bio;

import java.util.List;

import com.linkage.module.gtms.itv.dto.Module;
import com.linkage.module.gtms.itv.dto.SystemTree;

/**
 * 菜单管理类
 * 
 * @author 陈仲民(5243)
 * @version 1.0
 * @since 2009-12-23
 * @category com.linkage.module.fba.menu.bio<br>
 *           版权：南京联创科技 网管科技部
 */
public interface MenuManagerBIO
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
