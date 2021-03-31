
package com.linkage.module.itms.report.bio;

import com.linkage.module.itms.report.dao.GroupManageDAO;

import java.util.List;
import java.util.Map;

/**
 * @author guankai (Ailk No.300401)
 * @version 1.0
 * @since 2020年4月20日
 * @category com.linkage.module.itms.report.bio
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class GroupManageBIO
{

	private GroupManageDAO dao;
	
	public GroupManageDAO getDao()
	{
		return dao;
	}

	
	public void setDao(GroupManageDAO dao)
	{
		this.dao = dao;
	}


	public List<Map> getGroupManageList(String status, String cityId, int curPage_splitPage, int num_splitPage) {
			return dao.getGroupManageList(status,cityId, curPage_splitPage,num_splitPage);
	}

	public int getGroupManageCount(String status, String cityId) {
		return dao.getGroupManageCount(status,cityId);
	}

	public List<Map> getDetailExcel(String cityId, String status) {
		return dao.getDetailExcel(cityId,status);
	}
}
