package com.linkage.litms.common.util;

import java.util.List;

import com.linkage.litms.resource.DeviceAct;

/**
 * 系统的常用工具方法。
 * 
 * @author 王志猛(5194) tel:13701409234
 * @version 1.0
 * @since 2007-11-18
 * @category utils
 */
public class SysResouceUtils
{
	/**
	 * 根据当前用户获取和他关联的gatherid
	 * 
	 * @param areaid
	 *            用户的域id
	 * @param devA
	 *            属性资源类，应该通过spring注入给调用该方法的dao类，然后传入即可
	 * @return gatherid的字符串，类似于'1','2','3','4','5'
	 */
	public static String getGatherIdForSql(String areaid, DeviceAct devA)
	{
		String gathers = null;// 域的对应gatherid
		List m_Gathers = devA.getGathersWithAreaId(areaid);
		if (m_Gathers.size() > 0)
			{
				gathers = "'" + m_Gathers.get(0) + "'";
			}
		for (int k = 1; k < m_Gathers.size(); k++)
			{
			
				gathers += ",'" + m_Gathers.get(k) + "'";
			}
		return gathers;
	}
}