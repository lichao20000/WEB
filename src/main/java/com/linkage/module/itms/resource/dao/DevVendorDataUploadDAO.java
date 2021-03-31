
package com.linkage.module.itms.resource.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.database.DBOperation;
import com.linkage.module.gwms.dao.SuperDAO;

/**
 * @author Reno (Ailk No.)
 * @version 1.0
 * @since 2015年10月15日
 * @category com.linkage.module.itms.resource.dao
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class DevVendorDataUploadDAO extends SuperDAO
{

	/**
	 * 导入tab_repair_device_info表
	 * 
	 * @param list
	 *            excel中符合条件的数据
	 * @param import_time
	 *            导入时间
	 */
	public void insertRepairDevInfo(List<Map<String, String>> list, long import_time)
	{
		PrepareSQL psql = null;
		ArrayList<String> listSQL = new ArrayList<String>();
		if (null == list || list.isEmpty())
		{
			return;
		}
		else
		{
			for (Map<String, String> map : list)
			{
				psql = new PrepareSQL();
				psql.append(" insert into tab_repair_device_info(repair_vendor,insurance_status,batch_number,device_serialnumber,"
						+ "device_vendor,device_model,hardwareversion,softwareversion,version_check,"
						+ "config_check,serv_issue_check,voice_regist_check,check_result,send_city,attribution_city,"
						+ "manufacture_date,import_date)");
				psql.append(" values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ");
				psql.setString(1, map.get("repair_vendor"));
				psql.setInt(2, StringUtil.getIntValue(map, "insurance_status"));
				psql.setString(3, StringUtil.getStringValue(map, "batch_number"));
				psql.setString(4, StringUtil.getStringValue(map, "device_serialnumber"));
				psql.setString(5, StringUtil.getStringValue(map, "device_vendor"));
				psql.setString(6, StringUtil.getStringValue(map, "device_model"));
				psql.setString(7, StringUtil.getStringValue(map, "hardwareversion"));
				psql.setString(8, StringUtil.getStringValue(map, "softwareversion"));
				psql.setInt(9, StringUtil.getIntValue(map, "version_check"));
				psql.setInt(10, StringUtil.getIntValue(map, "config_check"));
				psql.setInt(11, StringUtil.getIntValue(map, "serv_issue_check"));
				psql.setInt(12, StringUtil.getIntValue(map, "voice_regist_check"));
				psql.setInt(13, StringUtil.getIntValue(map, "check_result"));
				psql.setString(14, StringUtil.getStringValue(map, "send_city"));
				psql.setString(15, StringUtil.getStringValue(map, "attribution_city"));
				psql.setLong(16, StringUtil.getLongValue(map, "manufacture_date"));
				psql.setLong(17, import_time);
				listSQL.add(psql.getSQL());
			}
			DBOperation.executeUpdate(listSQL, "proxool.xml-report");
		}
	}
}
