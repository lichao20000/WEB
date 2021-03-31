package com.linkage.module.gwms.blocTest.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.module.gwms.dao.SuperDAO;

@SuppressWarnings("unchecked")
public class BbmsDiagDAO extends SuperDAO
{
	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(BbmsDiagDAO.class);

	
	public List<Map<String, String>> getDeviceWan(String deviceId)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("select a.vpi_id,a.vci_id,a.vlan_id,b.username,b.ip,b.mask,b.gateway,b.dns ")
		.append("from gw_wan_conn_bbms a,gw_wan_conn_session_bbms b ")
		.append(" where a.device_id = b.device_id and a.wan_id = b.wan_id and a.wan_conn_id = b.wan_conn_id and a.device_id='")
		.append(deviceId).append("' and upper(b.serv_list) like '%INTERNET%'");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		List<Map<String, String>> list = jt.queryForList(psql.getSQL());
		
		if(list==null||list.size()==0||list.isEmpty())
		{
			return null;
		}
		for (Map<String,String> map : list)
		{
			if (StringUtil.IsEmpty(map.get("vpi_id")))
			{
				map.put("pvc_vlan", map.get("vlan_id"));
			} else
			{
				map.put("pvc_vlan",
						map.get("vpi_id") + "/"
								+ StringUtil.getStringValue(map.get("vci_id")));
			}
		}
		return list;
	}
	
	/**
	 * 获取设备上行方式
	 * @param deviceId
	 * @return
	 */
	public String getAccessType(String deviceId)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("select access_type from tab_devicetype_config_info a ,tab_gw_device b where a.devicetype_id = b.devicetype_id and b.device_id = '")
		.append(deviceId).append("'");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		List<Map<String, String>> list = jt.queryForList(psql.getSQL());
		
		if(list==null||list.size()==0||list.isEmpty()){
			
			PrepareSQL psql2 = new PrepareSQL(" select access_style_id from tab_egwcustomer where device_id='" + deviceId + "' " );
			
			list = jt.queryForList(psql2.getSQL());
			
			logger.debug("list:"+list.size());
			
			if(list==null||list.size()==0||list.isEmpty()){
				return null;
			}else{
				logger.debug("access_style_id:"+String.valueOf(list.get(0).get("access_style_id")));
				return String.valueOf(list.get(0).get("access_style_id"));
			}
		}else
		{
			return String.valueOf(list.get(0).get("access_type"));
		}
	}


	public Map<String, String> getUserServ(String deviceId)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("select b.vpiid,b.vciid,b.vlanid,b.username,b.ipaddress as ip,b.ipmask as mask,b.gateway,b.adsl_ser as dns,b.wan_type ")
		.append(" from tab_egwcustomer a,egwcust_serv_info b where a.user_id=b.user_id and a.device_id='")
		.append(deviceId).append("' and b.serv_type_id=10");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		List<Map<String, String>> list = jt.queryForList(psql.getSQL());
		Map<String, String> map = new HashMap<String, String>();
		if(list==null||list.size()==0||list.isEmpty()){
			map.put("dataFault", "设备没有绑定用户");
			return null;
		}
		map = list.get(0);
		if(StringUtil.IsEmpty(map.get("vpiid"))){
			map.put("pvc_vlan", map.get("vlanid"));
		}else{
			map.put("pvc_vlan", map.get("vpiid")+"/"+StringUtil.getStringValue(map.get("vciid")));
		}
		return map;
	}
	
}
