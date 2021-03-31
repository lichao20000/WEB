package com.linkage.module.gtms.stb.resource.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.DateTimeUtil;
import com.linkage.commons.util.StringUtil;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

@SuppressWarnings("rawtypes")
public class StbSetConfParamDAO extends SuperDAO
{
	private static Logger logger = LoggerFactory.getLogger(StbSetConfParamDAO.class);
	private int queryCount;


	/**
	 * 获取设备零配置下发参数
	 */
	public Map getDeviceConfParamInfo(String deviceId)
	{
		PrepareSQL psql = new PrepareSQL();// TODO wait (more table related)
		psql.append("select a.oui,a.device_serialnumber,a.city_id,a.cpe_mac,");
		psql.append("b.vendor_name,b.vendor_add,c.device_model,d.serv_account," +
				"e.auth_server, e.auth_server_bak, e.auth_server_conn_peroid, e.zeroconf_server, " +
				"e.zeroconfig_server_bak, e.cmd_server, e.cmd_server_bak, e.cmd_server_conn_peroid, " +
				"e.ntp_server_main, e.ntp_server_bak, e.auto_sleep_mode, e.auto_sleep_time, " +
				"e.ip_protocol_version_lan, e.ip_protocol_version_wifi, e.update_time, e.set_stb_time ");
		psql.append("from stb_tab_gw_device a ");
		psql.append("left join stb_tab_vendor b on a.vendor_id=b.vendor_id ");
		psql.append("left join stb_gw_device_model c on a.device_model_id=c.device_model_id ");
		psql.append("left join stb_tab_customer d on a.customer_id=d.customer_id ");
		psql.append("left join tab_stb_downparam e on a.device_id=e.device_id ");
		psql.append("where a.device_status=1 and a.device_id=? ");
		psql.setString(1, deviceId);

		return jt.queryForMap(psql.getSQL());
	}

	/**
	 * 保存设备零配置参数数据
	 */
	public int saveConfParam(String deviceId, String newParams)
	{
		logger.debug("saveConfParam:{}",newParams);
		String[] strs=newParams.split("#");
		if(StringUtil.IsEmpty(strs[18])){
			return insertConfParam(deviceId,strs);
		}
		return updateConfParam(deviceId,strs);
	}

	/**
	 * 修改设备零配置下发参数
	 */
	public int updateConfParam(String deviceId,String[] strs)
	{
		StringBuffer sb=new StringBuffer();
		sb.append("update tab_stb_downparam set ");
		sb.append("auth_server="+dealEmpty(strs[4])+",");
		sb.append("auth_server_bak="+dealEmpty(strs[5])+",");
		sb.append("auth_server_conn_peroid="+dealEmptyInt(strs[6])+",");
		sb.append("zeroconf_server="+dealEmpty(strs[7])+",");
		sb.append("zeroconfig_server_bak="+dealEmpty(strs[8])+",");
		sb.append("cmd_server="+dealEmpty(strs[9])+",");
		sb.append("cmd_server_bak="+dealEmpty(strs[10])+",");
		sb.append("cmd_server_conn_peroid="+dealEmptyInt(strs[11])+",");
		sb.append("ntp_server_main="+dealEmpty(strs[12])+",");
		sb.append("ntp_server_bak="+dealEmpty(strs[13])+",");
		sb.append("auto_sleep_mode="+dealEmptyInt(strs[14])+",");
		sb.append("auto_sleep_time="+dealEmptyInt(strs[15])+",");
		sb.append("ip_protocol_version_lan="+dealEmptyInt(strs[16])+",");
		sb.append("ip_protocol_version_wifi="+dealEmptyInt(strs[17])+",");
		sb.append("update_time="+System.currentTimeMillis()/1000L+" ");
		sb.append("where device_id='"+deviceId+"' ");

		logger.info(sb.toString());
		try{
			jt.execute(sb.toString());
		}catch(Exception e){
			logger.error("[{}] deleteConParamInfo err:{}",deviceId,e);
			e.printStackTrace();
			return 0;
		}
		return 1;
	}

	/**
	 * 新增设备零配置下发参数
	 */
	public int insertConfParam(String deviceId,String[] strs)
	{
		StringBuffer sb=new StringBuffer();
		sb.append("insert into tab_stb_downparam(device_id,auth_server,auth_server_bak,");
		sb.append("auth_server_conn_peroid,zeroconf_server,zeroconfig_server_bak,");
		sb.append("cmd_server,cmd_server_bak,cmd_server_conn_peroid,ntp_server_main,");
		sb.append("ntp_server_bak,auto_sleep_mode,auto_sleep_time,ip_protocol_version_lan,");
		sb.append("ip_protocol_version_wifi,update_time,set_stb_time) ");
		sb.append("values('"+deviceId+"',");
		sb.append(dealEmpty(strs[4])+",");
		sb.append(dealEmpty(strs[5])+",");
		sb.append(dealEmptyInt(strs[6])+",");
		sb.append(dealEmpty(strs[7])+",");
		sb.append(dealEmpty(strs[8])+",");
		sb.append(dealEmpty(strs[9])+",");
		sb.append(dealEmpty(strs[10])+",");
		sb.append(dealEmptyInt(strs[11])+",");
		sb.append(dealEmpty(strs[12])+",");
		sb.append(dealEmpty(strs[13])+",");
		sb.append(dealEmptyInt(strs[14])+",");
		sb.append(dealEmptyInt(strs[15])+",");
		sb.append(dealEmptyInt(strs[16])+",");
		sb.append(dealEmptyInt(strs[17])+",");
		sb.append(System.currentTimeMillis()/1000L+",0)");

		logger.info(sb.toString());
		try{
			jt.execute(sb.toString());
		}catch(Exception e){
			logger.error("[{}] deleteConParamInfo err:{}",deviceId,e);
			e.printStackTrace();
			return 0;
		}
		return 1;
	}

	/**
	 * 删除设备零配置下发参数
	 */
	public int deleteConParamInfo(String deviceId)
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("delete from tab_stb_downparam where device_id=? ");
		psql.setString(1,deviceId);

		try{
			jt.execute(psql.getSQL());
		}catch(Exception e){
			logger.error("[{}] deleteConParamInfo err:{}",deviceId,e);
			e.printStackTrace();
			return 0;
		}
		return 1;
	}

	/**
	 * 获取设备零配置下发参数列表
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getConfParamList(int curPage_splitPage,int num_splitPage,
			String deviceSn,String deviceMac,String vendorId,String deviceModelId,String cityId,
			String citynext,long updateStartTime,long updateEndTime,long setLastStartTime,
			long setLastEndTime,String auto_sleep_mode,String auto_sleep_time,
			String ip_protocol_version_lan,String ip_protocol_version_wifi,String servAccount)
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select a.device_id,a.device_serialnumber,a.cpe_mac,a.city_id,b.vendor_name,");
		psql.append("b.vendor_add,c.device_model,d.hardwareversion,d.softwareversion,");
		psql.append("e.serv_account as eserv_account,f.update_time,f.set_stb_time ");
		psql.append(getSql(deviceSn,deviceMac,vendorId,deviceModelId,cityId,citynext,
							updateStartTime,updateEndTime,setLastStartTime,setLastEndTime,
							auto_sleep_mode,auto_sleep_time,ip_protocol_version_lan,
							ip_protocol_version_wifi,servAccount));
		psql.append(" order by b.vendor_id ");

		List list = querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage+1,
				num_splitPage, new RowMapper()
				{
					public Object mapRow(ResultSet rs, int arg1) throws SQLException
					{
						Map<String, String> map = new HashMap<String, String>();
						map.put("device_id", rs.getString("device_id"));
						map.put("sn", rs.getString("device_serialnumber"));
						map.put("serv_account", rs.getString("eserv_account"));
						map.put("mac", rs.getString("cpe_mac"));
						map.put("vendorName",StringUtil.IsEmpty(rs.getString("vendor_add")) ?
												rs.getString("vendor_name"):rs.getString("vendor_add"));
						map.put("device_model", rs.getString("device_model"));
						map.put("hardwareversion", rs.getString("hardwareversion"));
						map.put("softwareversion",rs.getString("softwareversion"));
						map.put("city_name", CityDAO.getCityName(rs.getString("city_id")));
						map.put("update_time", getDate(rs.getLong("update_time")));
						map.put("set_stb_time",getDate(rs.getLong("set_stb_time")));

						return map;
					}
				});
		return list;
	}

	/**
	 * 分页
	 */
	public int countConfParamList(int num_splitPage,String deviceSn,String deviceMac,
			String vendorId,String deviceModelId,String cityId,String citynext,
			long updateStartTime,long updateEndTime,long setLastStartTime,long setLastEndTime,
			String auto_sleep_mode,String auto_sleep_time, String ip_protocol_version_lan,
			String ip_protocol_version_wifi,String servAccount)
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select count(*) count_num ");
		psql.append(getSql(deviceSn,deviceMac,vendorId,deviceModelId,
							cityId,citynext,updateStartTime,updateEndTime,
							setLastStartTime,setLastEndTime,auto_sleep_mode,
							auto_sleep_time,ip_protocol_version_lan,
							ip_protocol_version_wifi,servAccount));
		queryCount = jt.queryForInt(psql.getSQL());
		int maxPage = 1;
		if (queryCount % num_splitPage == 0){
			maxPage = queryCount / num_splitPage;
		}else{
			maxPage = queryCount / num_splitPage + 1;
		}
		return maxPage;
	}

	/**
	 * 拼接sql
	 */
	private String getSql(String deviceSn,String deviceMac,String vendorId,
			String deviceModelId,String cityId,String citynext,long updateStartTime,
			long updateEndTime,long setLastStartTime,long setLastEndTime,
			String auto_sleep_mode,String auto_sleep_time,String ip_protocol_version_lan,
			String ip_protocol_version_wifi,String servAccount)
	{
		StringBuffer sb=new StringBuffer();// TODO wait (more table related)
		sb.append("from tab_stb_downparam f ");
		sb.append("left join stb_tab_gw_device a on a.device_id=f.device_id ");
		sb.append("left join stb_tab_vendor b on a.vendor_id=b.vendor_id ");
		sb.append("left join stb_gw_device_model c on a.device_model_id=c.device_model_id ");
		sb.append("left join stb_tab_devicetype_info d on a.devicetype_id=d.devicetype_id ");
		sb.append("left join stb_tab_customer e on a.customer_id=e.customer_id ");
		sb.append("where 1=1");

		if(!StringUtil.IsEmpty(deviceSn))
		{
			String subsn=deviceSn.substring(deviceSn.length()-6,deviceSn.length());
			sb.append(" and a.dev_sub_sn='"+subsn+"'");
			sb.append(" and a.device_serialnumber like '%"+deviceSn+"'");
		}
		if(!StringUtil.IsEmpty(deviceMac)){
			sb.append(" and a.cpe_mac='"+deviceMac+"'");
		}
		if(!StringUtil.IsEmpty(vendorId) && !"-1".equals(vendorId)){
			sb.append(" and a.vendor_id='"+vendorId+"'");
		}
		if(!StringUtil.IsEmpty(deviceModelId) && !"-1".equals(deviceModelId)){
			sb.append(" and a.device_model_id='"+deviceModelId+"'");
		}
		if(!StringUtil.IsEmpty(citynext) && !"-1".equals(citynext) && !"00".equals(citynext))
		{
			ArrayList<String> cl=CityDAO.getAllNextCityIdsByCityPid(citynext);
			sb.append(" and a.city_id in ('"+cl.get(0)+"'");
			for(int i=1;i<cl.size();i++){
				sb.append(",'"+cl.get(i)+"'");
			}
			sb.append(")");
		}
		else if(!StringUtil.IsEmpty(cityId) && !"-1".equals(cityId) && !"00".equals(cityId))
		{
			ArrayList<String> cl=CityDAO.getAllNextCityIdsByCityPid(cityId);
			sb.append(" and a.city_id in ('"+cl.get(0)+"'");
			for(int i=1;i<cl.size();i++){
				sb.append(",'"+cl.get(i)+"'");
			}
			sb.append(")");
		}
		if(!StringUtil.IsEmpty(servAccount)){
			sb.append(" and e.serv_account='"+servAccount+"'");
		}
		if(updateStartTime>0){
			sb.append(" and f.update_time>="+updateStartTime);
		}
		if(updateEndTime>0){
			sb.append(" and f.update_time<"+updateEndTime);
		}
		if(setLastStartTime>0){
			sb.append(" and f.set_stb_time>="+setLastStartTime);
		}
		if(setLastEndTime>0){
			sb.append(" and f.set_stb_time<"+setLastEndTime);
		}
		if(!StringUtil.IsEmpty(auto_sleep_mode) && !"-1".equals(auto_sleep_mode)){
			sb.append(" and f.auto_sleep_mode="+auto_sleep_mode);
		}
		if(!StringUtil.IsEmpty(auto_sleep_time) && !"-1".equals(auto_sleep_time)){
			sb.append(" and f.auto_sleep_time="+auto_sleep_time);
		}
		if(!StringUtil.IsEmpty(ip_protocol_version_lan) && !"-1".equals(ip_protocol_version_lan)){
			sb.append(" and f.ip_protocol_version_lan="+ip_protocol_version_lan);
		}
		if(!StringUtil.IsEmpty(ip_protocol_version_wifi) && !"-1".equals(ip_protocol_version_wifi)){
			sb.append(" and f.ip_protocol_version_wifi="+ip_protocol_version_wifi);
		}
		return sb.toString();
	}

	/**
	 * 字符串空处理
	 */
	private String dealEmpty(String v)
	{
		if(StringUtil.IsEmpty(v) || "-1".equals(v)){
			return "null";
		}
		return "'"+v.trim()+"'";
	}

	/**
	 * 数字空处理
	 */
	private String dealEmptyInt(String v)
	{
		if(StringUtil.IsEmpty(v) || "-1".equals(v)){
			return "null";
		}
		return StringUtil.getIntegerValue(v)+"";
	}

	/**
	 * 秒转日期
	 */
	private String getDate(long time)
	{
		return time==0 ? "":new DateTimeUtil(time*1000).getYYYY_MM_DD_HH_mm_ss();
	}

	public int getQueryCount() {
		return queryCount;
	}

	public void setQueryCount(int queryCount) {
		this.queryCount = queryCount;
	}
}
