package com.linkage.module.gwms.report.bio;

import java.util.List;
import java.util.Map;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.report.dao.BindDevTypeStatDAO;

/**
 * 
 * @author yaoli (Ailk No.)
 * @version 1.0
 * @since 2019年3月4日
 * @category com.linkage.module.gwms.report.bio
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public class BindDevTypeStatBIO
{
	private BindDevTypeStatDAO bindDevTypeStatDAO;
	
	public String queryBindDevTypeList(UserRes user,String vendorId,String deviceModelId,String startTime,String endTime){
		String table = bindDevTypeStatDAO.queryBindDevTypeList(user,vendorId, deviceModelId, startTime, endTime);
		if(StringUtil.IsEmpty(table)){
			table = "<tr><td colspan='7'>没有查询到相关数据！</td></tr>";
		}
		return table;
	}
	
	public BindDevTypeStatDAO getBindDevTypeStatDAO()
	{
		return bindDevTypeStatDAO;
	}

	public void setBindDevTypeStatDAO(BindDevTypeStatDAO bindDevTypeStatDAO)
	{
		this.bindDevTypeStatDAO = bindDevTypeStatDAO;
	}
}
