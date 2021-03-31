
package com.linkage.module.gwms.resource.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.StringUtil;
import com.linkage.commons.util.TimeUtil;
import com.linkage.litms.common.database.DBOperation;
import com.linkage.module.gwms.dao.SuperDAO;

public class DeviceE8CImportDAO extends SuperDAO
{

	private static Logger logger = LoggerFactory.getLogger(DeviceE8CImportDAO.class);

	public String addDevice(ArrayList<Map<String, String>> list) {
		
		ArrayList<String> sqllist = new ArrayList<String>();
		for (Map<String, String> devInfoMap : list)
		{
			sqllist.addAll(addSQL(devInfoMap));
			if (sqllist.size() >= 200)
			{
				DBOperation.executeUpdate(sqllist);
				sqllist.clear();
			}
		}
		if (!sqllist.isEmpty())
		{
			DBOperation.executeUpdate(sqllist);
			sqllist.clear();
		}
		return "导入文件成功";
	}
	
	
	public List<String> addSQL(Map<String, String> devInfoMap )
	{
		ArrayList<String> list = new ArrayList<String>();
		StringBuilder sql = new StringBuilder();
		StringBuilder sql1 = new StringBuilder();
		long currentTime = TimeUtil.getCurrentTime();
		String sn = StringUtil.getStringValue(devInfoMap, "SN");
		String mac = StringUtil.getStringValue(devInfoMap, "MAC");
		String device_model = StringUtil.getStringValue(devInfoMap, "终端型号");
		String remark = StringUtil.getStringValue(devInfoMap, "备注");
		String device_serialnumber=mac;
		String sql0=" select device_serialnumber from tab_gw_device where dev_sub_sn='"+mac.substring(6, mac.length())+"' and device_serialnumber like '%"+mac+"' ";
		@SuppressWarnings("unchecked")
		List<Map<String, String>> listData = jt.queryForList(sql0);
		if (listData!=null &&listData.size()>0) {
			device_serialnumber=listData.get(0).get("device_serialnumber")==null?"":listData.get(0).get("device_serialnumber").toString();
		}
		sql.append("delete from tab_device_e8c_remould where mac='"+mac+"'");
		
		sql1.append("insert into tab_device_e8c_remould (");
		sql1.append("sn,mac,device_serialnumber,device_model,remark,importtime");
		sql1.append(") values ('"+sn+"','"+mac+"','"+device_serialnumber+"','"+device_model+"','"+remark+"',"+currentTime+" )");

		list.add(sql.toString());
		list.add(sql1.toString());
		sql = null;
		sql1 = null;
		return list;
	}
}
