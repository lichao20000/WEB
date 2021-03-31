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
public class VoiceDeviceQueryDAO extends SuperDAO
{
	
	private static Logger logger = LoggerFactory
			.getLogger(VoiceDeviceQueryDAO.class);
	
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
	public int addVoiceDiagResult(String oui,String device_serialnumber,String test_time,String testtype,String callednumber,String dialdtmfconfirmenable,String dialdtmfconfirmnumber,String dialdtmfconfirmresult,String status,String conclusion,String callerfailreason,String failedresponsecode){
		PrepareSQL psql = new PrepareSQL("insert into tab_voice_diag_result(oui, device_serialnumber, test_time, testtype, callednumber, dialdtmfconfirmenable, dialdtmfconfirmnumber, dialdtmfconfirmresult, status, conclusion, callerfailreason, failedresponsecode) values (?,?,?,?,?,?,?,?,?,?,?,?)");
		psql.setString(1, StringUtil.getStringValue(oui));
		psql.setString(2, StringUtil.getStringValue(device_serialnumber));
		psql.setLong(3, StringUtil.getLongValue(test_time));
		psql.setString(4, StringUtil.getStringValue(testtype));
		psql.setString(5, StringUtil.getStringValue(callednumber));
		psql.setString(6, StringUtil.getStringValue(dialdtmfconfirmenable));
		psql.setString(7, StringUtil.getStringValue(dialdtmfconfirmnumber));
		psql.setString(8, StringUtil.getStringValue(dialdtmfconfirmresult));
		psql.setString(9, StringUtil.getStringValue(status));
		psql.setString(10, StringUtil.getStringValue(conclusion));
		psql.setString(11, StringUtil.getStringValue(callerfailreason));
		psql.setLong(12, StringUtil.getLongValue(failedresponsecode));
		int num = jt.update(psql.getSQL());
		return num;
	}
}
