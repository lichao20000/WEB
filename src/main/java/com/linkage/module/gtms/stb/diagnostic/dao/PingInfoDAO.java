package com.linkage.module.gtms.stb.diagnostic.dao;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.module.gtms.stb.obj.tr069.PingOBJ;
import com.linkage.litms.common.util.JdbcTemplateExtend;

/**
 * @author chenjie(67371)
 * ping诊断相关的dao操作
 */
@SuppressWarnings("rawtypes")
public class PingInfoDAO
{
	private static Logger logger = LoggerFactory.getLogger(PingInfoDAO.class);

	private JdbcTemplateExtend jt;

    public  List<Map<String,String>> queryIpMap() {
		PrepareSQL psql = new PrepareSQL("select ip_name,ip_value from stb_tab_ping_ip");
		return jt.queryForList(psql.getSQL());
	}

    public void setDao(DataSource dao) {
		jt = new JdbcTemplateExtend(dao);
	}

	/**
	 * 查询TraceRoute相关表数据
	 */
	@SuppressWarnings("unchecked")
	public PingOBJ queryTraceRouteInfo(PingOBJ pingObj)
	{
		logger.debug("queryTraceRouteInfo()");
		String deviceId = pingObj.getDeviceId();
		// diag表信息
		PrepareSQL psql = new PrepareSQL("select response_time, number_of_route_hops from stb_lan_tracert_diag where device_id=?");
		psql.setString(1,deviceId);
		Map map = jt.queryForMap(psql.getSQL());
		pingObj.setResponseTime((String)map.get("response_time"));
		pingObj.setNumberOfRouteHops((String)map.get("number_of_route_hops"));

		// hops表信息
		PrepareSQL psql2 = new PrepareSQL("select hop_host, max_response_time, min_response_time, avg_response_time from stb_lan_tracert_hops where device_id=?");
		psql2.setString(1,deviceId);
		List<Map> hopsList = jt.queryForList(psql2.getSQL());
		pingObj.setHopHostI(hopsList);
		return null;
	}

	/**
	 * 根据city_id 查询IP类别，IP名称
	 */
	public List getIpTypeByCityId(String cityId)
	{
		PrepareSQL sql = null;

		// 00 表示    省中心
		if ("00".equals(cityId)) {
			sql = new PrepareSQL(
					"select distinct ip_type,ip_name from tab_gw_diagnosticIP");
		}else {
			sql = new PrepareSQL(
					"select distinct ip_type,ip_name from tab_gw_diagnosticIP where city_id=?");
			sql.setString(1, cityId);
		}

		return jt.queryForList(sql.getSQL());
	}


	/**
	 * 根据city_id,ipType   查询IP
	 */
	public List getIpByIpType(String cityId, String ipType)
	{
		PrepareSQL sql = null;

		// 00 表示 省中心
		if ("00".equals(cityId)) {
			sql = new PrepareSQL(
					"select distinct ip from tab_gw_diagnosticIP where ip_type=? ");
			sql.setString(1, ipType);
		}else {
			sql = new PrepareSQL(
					"select distinct ip from tab_gw_diagnosticIP where ip_type=? and city_id=?");
			sql.setString(1, ipType);
			sql.setString(2, cityId);
		}

		return jt.queryForList(sql.getSQL());
	}

	/**
	 * 获取机顶盒设备信息
	 */
	public Map getDevName(String device_id)
	{
		PrepareSQL sql=new PrepareSQL();
		sql.append("select oui,device_serialnumber,city_id from stb_tab_gw_device where device_id=? ");
		sql.setString(1,device_id);

		return jt.queryForMap(sql.getSQL());
	}

	/**
	 * 获取地市对应的IP
	 */
	public Map getCityIp(String city_id)
	{
		PrepareSQL sql=new PrepareSQL();
		sql.append("select ip from tab_stb_file_path_city_ip where city_id=? ");
		sql.setString(1,city_id);

		return jt.queryForMap(sql.getSQL());
	}
}
