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
public class PPPoEDeviceQueryDAO extends SuperDAO
{
	
	private static Logger logger = LoggerFactory
			.getLogger(PPPoEDeviceQueryDAO.class);
	
	public Map<String,String> getDefaultdiag(){
		
		PrepareSQL psql = new PrepareSQL("select column1,column2,column3,column4,column5,column6 from tab_diag_default where default_type_id=2");
		
		List<Map> list = jt.queryForList(psql.getSQL());
		
		Map<String,String> map = new HashMap<String, String>();
		
		if(null!=list && list.size()>0){
			map.put("column1", StringUtil.getStringValue(list.get(0).get("column1")));
			map.put("column2", StringUtil.getStringValue(list.get(0).get("column2")));
			map.put("column3", StringUtil.getStringValue(list.get(0).get("column3")));
			map.put("column4", StringUtil.getStringValue(list.get(0).get("column4")));
			map.put("column5", StringUtil.getStringValue(list.get(0).get("column5")));
			map.put("column6", StringUtil.getStringValue(list.get(0).get("column6")));
		}
		return map;
	}
	
	/**
	 * 
	 * @param oui
	 * @param device_serialnumber
	 * @param status
	 * @param test_time
	 * @param download_url
	 * @param eth_priority
	 * @return
	 */
	public int addPPPoEDiagResult(String oui,String device_serialnumber,String test_time,String username,String password,String auth_protocol,String retry_times,String result_code,String ip_address, String default_gateway,String status){
		PrepareSQL psql = new PrepareSQL("insert into tab_pppoe_diag_result (oui, device_serialnumber,test_time, username, password, auth_protocol, retry_times,result_code, ip_address, default_gateway,status) values(?,?,?,?,?,?,?,?,?,?,?)");
		psql.setString(1, StringUtil.getStringValue(oui));
		psql.setString(2, StringUtil.getStringValue(device_serialnumber));
		psql.setLong(3, StringUtil.getLongValue(test_time));
		psql.setString(4, StringUtil.getStringValue(username));
		psql.setString(5, StringUtil.getStringValue(password));
		psql.setString(6, StringUtil.getStringValue(auth_protocol));
		psql.setInt(7, StringUtil.getIntegerValue(retry_times));
		psql.setString(8, StringUtil.getStringValue(result_code));
		psql.setString(9, StringUtil.getStringValue(ip_address));
		psql.setString(10, StringUtil.getStringValue(default_gateway));
		psql.setString(11, StringUtil.getStringValue(status));
		int num = jt.update(psql.getSQL());
		return num;
	}
}
