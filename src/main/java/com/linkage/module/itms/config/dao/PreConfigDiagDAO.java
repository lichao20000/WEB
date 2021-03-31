
package com.linkage.module.itms.config.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.obj.tabquery.HgwServUserObj;
import com.linkage.system.utils.DateTimeUtil;

@SuppressWarnings("unchecked")
public class PreConfigDiagDAO extends SuperDAO
{

	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(PreConfigDiagDAO.class);
	private Map<String, String> status_map = new HashMap<String, String>();
//	private Map<String, String> result_map = new HashMap<String, String>();

	public PreConfigDiagDAO()
	{
		status_map.put("0", "等待执行");
		status_map.put("1", "预读PVC");
		status_map.put("2", "预读绑定端口");
		status_map.put("3", "预读无线");
		status_map.put("4", "业务下发");
		status_map.put("100", "执行完成");
//		result_map = getFaultCodeMap();
	}

	public Map<String, String> getFaultCodeMap()
	{
		String sql = "select fault_code, fault_desc,fault_reason,solutions from tab_cpe_faultcode";
		PrepareSQL psql = new PrepareSQL(sql);
		List<Map> list = jt.queryForList(psql.getSQL());
		Map<String, String> faultCodeMap = new HashMap<String, String>();
		for (Map map : list)
		{
			faultCodeMap.put(StringUtil.getStringValue(map.get("fault_code")),StringUtil.getStringValue(map.get("fault_reason")));
		}

		return faultCodeMap;
	}

	
	public Map getDeviceInfo(String deviceId)
	{
		/**	if(DBUtil.GetDB()==3){
			//TODO wait
		}else{
			
		}*/
		PrepareSQL sql = new PrepareSQL("select a.device_id,a.device_serialnumber," +
				"a.oui,a.loopback_ip,a.cr_port,a.cr_path,a.acs_username," +
				"a.acs_passwd,a.gather_id,b.username,b.userline,c.last_time " +
				"from tab_gw_device a left join tab_hgwcustomer b on a.device_id=b.device_id,gw_devicestatus c " +
				"where a.device_id=c.device_id and a.device_id=?");
		sql.setString(1, deviceId);
		return queryForMap(sql.getSQL());
	}

	public Map getDeviceInfoByEgw(String deviceId)
	{
		/**	if(DBUtil.GetDB()==3){
			//TODO wait
		}else{
			
		}*/
		PrepareSQL sql = new PrepareSQL("select a.device_id,a.device_serialnumber,a.oui," +
				"a.loopback_ip,a.cr_port,a.cr_path,a.acs_username,a.acs_passwd," +
				"a.gather_id,b.username,b.userline,c.last_time " +
				"from tab_gw_device a left join tab_egwcustomer b on a.device_id=b.device_id,gw_devicestatus c " +
				"where a.device_id=c.device_id and a.device_id=?");
		sql.setString(1, deviceId);
		return queryForMap(sql.getSQL());
	}

	public Map<String, List<Map>> getPreConfInfo(String cityId, String shortName,
			String accessType)
	{
		PrepareSQL sql = new PrepareSQL();
		if(DBUtil.GetDB()==3){
			sql.append("select serv_name,vpi_id,vci_id,bind_port,vlan_id ");
		}else{
			sql.append("select * ");
		}
		sql.append("from r_dev_pre_conf where shortname=? and city_id=? and access_type=? ");
		sql.setString(1, shortName);
		sql.setString(2, cityId);
		if("EPON".equals(accessType)||"PON".equals(accessType)||"GPON".equals(accessType)
				||"X_CU_EPON".equals(accessType)||"X_CU_GPON".equals(accessType)){
			accessType = "PON";
		}
		sql.setString(3, accessType);
		List list = jt.queryForList(sql.getSQL());
		Map<String, List<Map>> map = new HashMap<String, List<Map>>();
		for (int i = 0; i < list.size(); i++)
		{
			Map tmap = (Map) list.get(i);
			String serv_name = StringUtil.getStringValue(tmap.get("serv_name"));
			List<Map> tlist = map.get(serv_name);
			if (tlist == null)
			{
				tlist = new ArrayList<Map>();
			}
			tlist.add(tmap);
			map.put(serv_name, tlist);
		}
		return map;
	}

	public String getAccessType(String deviceId, int i)
	{
		PrepareSQL sql = new PrepareSQL(
				"select access_type from gw_wan where device_id=? and wan_id=?");
		sql.setString(1, deviceId);
		sql.setInt(2, i);
		Map map = jt.queryForMap(sql.getSQL());
		return StringUtil.getStringValue(map.get("access_type"));
	}

	public List getInternet(String deviceId)
	{
		PrepareSQL sql = new PrepareSQL(
				"select a.vpi_id,a.vci_id,a.vlan_id,b.bind_port from gw_wan_conn a,gw_wan_conn_session b where a.device_id=b.device_id and a.wan_id=b.wan_id and a.wan_conn_id=b.wan_conn_id and b.serv_list='INTERNET' and a.device_id=?");
		sql.setString(1, deviceId);
		return jt.queryForList(sql.getSQL());
	}
	
	public List getIPTV(String deviceId)
	{
		PrepareSQL sql = new PrepareSQL(
				"select a.vpi_id,a.vci_id,a.vlan_id,b.bind_port from gw_wan_conn a,gw_wan_conn_session b where a.device_id=b.device_id and a.wan_id=b.wan_id and a.wan_conn_id=b.wan_conn_id and (b.serv_list='Other' or b.serv_list='OTHER') and a.device_id=?");
		sql.setString(1, deviceId);
		return jt.queryForList(sql.getSQL());
	}

	/**
	 * 应现场需求在原有SQL中增加以下两个节点 ProxyEnable,SnoopingEnable
	 * 
	 * @param deviceId
	 * @return
	 */
	public Map<String, String> getIGMP(String deviceId)
	{
		Map<String, String> retMap = new HashMap<String, String>();
		
		PrepareSQL sql = new PrepareSQL("select igmp_enab, proxy_enable, snooping_enable from gw_iptv where device_id=?");
		sql.setString(1, deviceId);
		Map map = queryForMap(sql.getSQL());
		
		retMap.put("igmp_enab", StringUtil.getStringValue(map.get("igmp_enab")));
		retMap.put("proxy_enable", StringUtil.getStringValue(map.get("proxy_enable")));
		retMap.put("snooping_enable", StringUtil.getStringValue(map.get("snooping_enable")));
		
		return retMap;
	}

	/**
	 * modify by zhangchy 2011-10-31 
	 * 
	 * 新增了 备用服务器地址(prox_serv_2), 备用服务器端口(prox_port_2)
	 * 
	 * @param deviceId
	 * @return
	 */
	public List getDeviceVOIP(String deviceId)
	{
		PrepareSQL sql = new PrepareSQL(
				"select b.voip_id,b.prox_serv,b.prox_port,b.prox_serv_2, b.prox_port_2,c.line_id,c.username from gw_voip_prof b,gw_voip_prof_line c where b.device_id=c.device_id and b.voip_id=c.voip_id and b.prof_id=c.prof_id and b.device_id=?");
		sql.setString(1, deviceId);
		return jt.queryForList(sql.getSQL());
	}

	
	public List getPVCVOIP(String deviceId)
	{
		PrepareSQL sql = new PrepareSQL(
				"select a.vpi_id,a.vci_id,a.vlan_id,b.bind_port from gw_wan_conn a,gw_wan_conn_session b where a.device_id=b.device_id and a.wan_id=b.wan_id and a.wan_conn_id=b.wan_conn_id and b.serv_list='VOIP' and a.device_id=?");
		sql.setString(1, deviceId);
		return jt.queryForList(sql.getSQL());
	}
    
    /**
     * <p>
     * [根据用户ID，查询用户开户的VOIP工单信息]
     * </p>
     * @param userId
     * @return
     */
    public List getBssVoipSheet(String userId)
    {
        PrepareSQL psql = new PrepareSQL();
        if(DBUtil.GetDB()==3){
        	psql.append("select protocol ");
        }else{
        	psql.append("select * ");
        }
        psql.append("from tab_voip_serv_param where user_id = " + userId);
        return jt.queryForList(psql.getSQL());
    }

	public String getUserByDevice(String deviceId)
	{
		PrepareSQL sql = new PrepareSQL(
				"select user_id from tab_hgwcustomer where device_id=?");
		sql.setString(1, deviceId);
		List list = jt.queryForList(sql.getSQL());
		if(list.size()>0){
			Map map = (Map) list.get(0);
			return StringUtil.getStringValue(map.get("user_id"));
		}else{
			return null;
		}
	}

	public List getUserVOIP(String userId)
	{
		PrepareSQL sql = new PrepareSQL(
				"select a.voip_username, a.line_id, b.prox_serv, b.prox_port, b.stand_prox_serv, b.stand_prox_port from tab_voip_serv_param a, tab_sip_info b where a. sip_id =b. sip_id  and a.user_id=?");
		sql.setLong(1, StringUtil.getLongValue(userId));
		return jt.queryForList(sql.getSQL());
	}

	public Map getQOS(String deviceId)
	{
		PrepareSQL sql = new PrepareSQL(
				"select qos_mode, enable from gw_qos where device_id=?");
		sql.setString(1, deviceId);
		return queryForMap(sql.getSQL());
	}

	public List getPreConfInfo(String cityId, String shortName, String accessType,
			String serv_name)
	{
		PrepareSQL sql = new PrepareSQL();
		if(DBUtil.GetDB()==3){
			sql.append("select vpi_id,vci_id,vlan_id,bind_port ");
		}else{
			sql.append("select * ");
		}
		sql.append("from r_dev_pre_conf ");
		sql.append("where shortname=? and city_id=? and access_type=? and serv_name=? ");
		sql.setString(1, shortName);
		sql.setString(2, cityId);
		if("EPON".equals(accessType)||"PON".equals(accessType)||"GPON".equals(accessType)
				||"X_CU_EPON".equals(accessType)||"X_CU_GPON".equals(accessType)){
			accessType = "PON";
		}
		sql.setString(3, accessType);
		sql.setString(4, serv_name);
		List list = jt.queryForList(sql.getSQL());
		return list;
	}

	public Map getStrategyById(String strategyId)
	{
		logger.debug("getStrategyById({})", strategyId);
		Map rmap = null;
		if (false == StringUtil.IsEmpty(strategyId))
		{
			PrepareSQL psql = new PrepareSQL();
			if(DBUtil.GetDB()==3){
				psql.append("select id,status,result_id,start_time,end_time ");
			}else{
				psql.append("select * ");
			}
			psql.append("from gw_serv_strategy where id=?");
			psql.setLong(1, StringUtil.getLongValue(strategyId));
			Map tmap = queryForMap(psql.getSQL());
			if (tmap != null)
			{
				rmap = new HashMap<String, String>();
				rmap.put("strategyId", StringUtil.getStringValue(tmap.get("id")));
				rmap.put("status", status_map.get(StringUtil.getStringValue(tmap
						.get("status"))));
				rmap.put("result", Global.G_Fault_Map.get(StringUtil.getIntegerValue(tmap
						.get("result_id"))).getFaultReason());
				// 将startTime转换成时间
				String startTime = StringUtil.getStringValue(tmap.get("start_time"));
				if (false == StringUtil.IsEmpty(startTime))
				{
					DateTimeUtil dateTimeUtil = new DateTimeUtil(Long
							.parseLong(startTime) * 1000);
					startTime = dateTimeUtil.getLongDate();
					dateTimeUtil = null;
				}
				rmap.put("startTime", startTime);
				// 将endTime转换成时间
				String endTime = StringUtil.getStringValue(tmap.get("end_time"));
				if (false == StringUtil.IsEmpty(endTime))
				{
					DateTimeUtil dateTimeUtil = new DateTimeUtil(
							Long.parseLong(endTime) * 1000);
					endTime = dateTimeUtil.getLongDate();
					dateTimeUtil = null;
				}
				rmap.put("endTime", endTime);
			}
		}
		return rmap;
	}

	public boolean ishavevoip(String deviceId)
	{
		PrepareSQL sql = new PrepareSQL("select voip_id from gw_voip_prof where device_id=?");
		sql.setString(1, deviceId);
		List list = jt.queryForList(sql.getSQL());
		if (list == null || list.size() == 0)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
	/**
	 * 根据用户ID获取对应的业务信息数组
	 * 
	 * @author wangsenbo
	 * @date Nov 25, 2010
	 * @param
	 * @return HgwServUserObj[]
	 */
	public HgwServUserObj queryHgwcustServUserByDevId(long userId,long servTypeId){
		logger.debug("queryHgwcustServUserByDevId({})", userId);
		
		HgwServUserObj hgwServUserObj = null;
		
		String strSQL = "select b.user_id, b.serv_type_id, b.username, b.open_status, b.vlanid, b.bind_port"
			+ " from hgwcust_serv_info b "
			+ " where b.serv_status=1 and b.user_id=? and serv_type_id=?";
		PrepareSQL psql = new PrepareSQL(strSQL);
		psql.setLong(1, userId);
		psql.setLong(2, servTypeId);
		// 执行查询
		List resList = jt.queryForList(psql.getSQL());
		// 查询结果非空
		if(null != resList && false == resList.isEmpty()){
			int size = resList.size();
			hgwServUserObj = new HgwServUserObj();
				
				Map rMap = (Map) resList.get(0);
				hgwServUserObj.setUserId(StringUtil.getStringValue(rMap.get("user_id")));
				hgwServUserObj.setServTypeId(StringUtil.getStringValue(rMap.get("serv_type_id")));
				hgwServUserObj.setUsername(String.valueOf(rMap.get("username")));
				hgwServUserObj.setOpenStatus(String.valueOf(rMap.get("open_status")));
				hgwServUserObj.setVlanid(String.valueOf(rMap.get("vlanid")));
				hgwServUserObj.setBindPort(String.valueOf(rMap.get("bind_port")));
		}
		return hgwServUserObj;
	}
	
	public int updateServOpenStatus(long userId, int servTypeId){
		logger.debug("updateServOpenStatus({}, {})", userId, servTypeId);
		// 更新SQL语句
		String strSQL = "update hgwcust_serv_info set open_status=0 "
			+ " where serv_status=1 and user_id=? ";
		PrepareSQL psql = new PrepareSQL(strSQL);
		psql.setLong(1, userId);
		if(0 != servTypeId){
			psql.append(" and serv_type_id=" + servTypeId);
		}
		
		// 执行查询
		int reti = jt.update(psql.getSQL());
		
		return reti;
	}
}