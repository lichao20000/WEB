
package com.linkage.module.gwms.system.bio;

import java.util.List;
import java.util.Map;

import com.linkage.litms.system.User;
import com.linkage.module.gwms.system.dao.SystemUserDAO;

/**
 * 异常绑定统计
 * 
 * @author 王森博
 */
@SuppressWarnings("unchecked")
public class SystemUserBIO
{

	private SystemUserDAO dao;

	/**
	 * @return the dao
	 */
	public SystemUserDAO getDao()
	{
		return dao;
	}

	/**
	 * @param dao
	 *            the dao to set
	 */
	public void setDao(SystemUserDAO dao)
	{
		this.dao = dao;
	}

	/**
	 * 导出系统列表
	 * 登录用户创建的系统用户
	 *
	 * @author wangsenbo
	 * @date Jul 12, 2010
	 * @param 
	 * @return List<Map>
	 */
	public List<Map> getSystemUserExcel(User user)
	{
		return dao.getSystemUserExcel(user);
	}
}
