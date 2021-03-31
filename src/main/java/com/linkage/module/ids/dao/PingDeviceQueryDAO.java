package com.linkage.module.ids.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.module.gwms.dao.SuperDAO;


/**
 * 
 * @author Administrator (Ailk No.)
 * @version 1.0
 * @since 2013-10-18
 * @category com.linkage.module.ids.dao
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public class PingDeviceQueryDAO extends SuperDAO
{
	
	private static Logger logger = LoggerFactory
			.getLogger(PingDeviceQueryDAO.class);
	
	public Map<String,String> getDefaultdiag(){
		
		PrepareSQL psql = new PrepareSQL("select column1,column2,column3,column4 from tab_diag_default where default_type_id=1");
		
		List<Map> list = jt.queryForList(psql.getSQL());
		
		Map<String,String> map = new HashMap<String, String>();
		
		if(null!=list && list.size()>0){
			map.put("column1", StringUtil.getStringValue(list.get(0).get("column1")));
			map.put("column2", StringUtil.getStringValue(list.get(0).get("column2")));
			map.put("column3", StringUtil.getStringValue(list.get(0).get("column3")));
			map.put("column4", StringUtil.getStringValue(list.get(0).get("column4")));
		}
		return map;
	}
	/**
	 * 
	 * @param oui
	 * @param device_serialnumber
	 * @param test_time
	 * @param wan_interface
	 * @param vlanid
	 * @param packet_size
	 * @param test_ip
	 * @param packet_num
	 * @param time_out
	 * @param status
	 * @return
	 */
	public int addPingDiagResult(String oui,String device_serialnumber,String test_time,String wan_interface,String vlanid,String packet_size,String test_ip,String packet_num,String time_out,String status,
			String SuccesNum, String FailNum, String  AvgResponseTime,String MinResponseTime,String MaxResponseTime,String PacketLossRate){
		PrepareSQL psql = new PrepareSQL("insert into tab_ping_diag_result(oui, device_serialnumber, test_time, wan_interface, vlanid, packet_size, test_ip, packet_num, time_out,status,success_count,failure_count,avg_resp_time,min_resp_time,max_resp_time,packet_loss_rate) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		psql.setString(1, StringUtil.getStringValue(oui));
		psql.setString(2, StringUtil.getStringValue(device_serialnumber));
		psql.setLong(3, StringUtil.getLongValue(test_time));
		psql.setInt(4, StringUtil.getIntegerValue(wan_interface));
		psql.setString(5, StringUtil.getStringValue(vlanid));
		psql.setInt(6, StringUtil.getIntegerValue(packet_size));
		psql.setString(7, StringUtil.getStringValue(test_ip));
		psql.setInt(8, StringUtil.getIntegerValue(packet_num));
		psql.setInt(9, StringUtil.getIntegerValue(time_out));
		psql.setInt(10, StringUtil.getIntegerValue(status));
		psql.setInt(11, StringUtil.getIntegerValue(SuccesNum));
		psql.setInt(12, StringUtil.getIntegerValue(FailNum));
		psql.setInt(13, StringUtil.getIntegerValue(AvgResponseTime));
		psql.setInt(14, StringUtil.getIntegerValue(MinResponseTime));
		psql.setInt(15, StringUtil.getIntegerValue(MaxResponseTime));
		psql.setInt(16, StringUtil.getIntegerValue(PacketLossRate));
		
		int num = jt.update(psql.getSQL());
		return num;
	}
}
