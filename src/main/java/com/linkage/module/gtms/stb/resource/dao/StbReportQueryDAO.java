package com.linkage.module.gtms.stb.resource.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.commons.util.DateTimeUtil;
import com.linkage.module.gtms.stb.resource.serv.StbReportQueryBIO;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

/**
 *
 * @author hp (Ailk No.)
 * @version 1.0
 * @since 2017-12-6
 * @category com.linkage.module.gtms.stb.resource.dao
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public class StbReportQueryDAO extends SuperDAO
{
	private static Logger logger = LoggerFactory.getLogger(StbReportQueryBIO.class);
	/**
	 * @category getVendor 获取所有的厂商
	 *
	 * @param city_id
	 *
	 * @return List
	 */
	public List getVendor() {
		logger.debug("getVendor()");
		PrepareSQL pSQL = new PrepareSQL("select vendor_id,vendor_name, vendor_add from stb_tab_vendor order by vendor_name");
		return jt.queryForList(pSQL.getSQL());
	}
	/**
	 * @category getDevicetype 获取所有的设备型号
	 *
	 * @param vendorId
	 *
	 * @return List
	 */
	public List getDeviceModel(String vendorId) {
		logger.debug("getDeviceModel(vendorId:{})",vendorId);
		PrepareSQL pSQL = new PrepareSQL();
		pSQL.append(" select a.device_model_id,a.device_model from stb_gw_device_model a where 1=1 ");
		if (null != vendorId && !"".equals(vendorId) && !"-1".equals(vendorId)) {
			pSQL.append(" and a.vendor_id='");
			pSQL.append(vendorId);
			pSQL.append("'");
		}
		pSQL.append(" order by device_model");
		return jt.queryForList(pSQL.getSQL());
	}
	/**
	 * @category getVersionList 获取所有的设备版本
	 *
	 * @param vendor_id
	 * @param deviceModelId
	 *
	 * @return List
	 */
	public List getVersionList(String deviceModelId) {
		logger.debug("getVersionList(deviceModelId:{})",deviceModelId);
		PrepareSQL pSQL = new PrepareSQL();
			pSQL.append("select a.devicetype_id,a.softwareversion,a.hardwareversion from stb_tab_devicetype_info a where 1=1 ");
			if (null != deviceModelId && !"".equals(deviceModelId) && !"-1".equals(deviceModelId)) {
				pSQL.append(" and a.device_model_id='");
				pSQL.append(deviceModelId);
				pSQL.append("'");
			}
		return jt.queryForList(pSQL.getSQL());
	}
	public List<Map> queryList(int curPage_splitPage, int num_splitPage, String vendorId,String deviceModelId,String devicetypeId,String gwShare_cityId,String cityId,String startIp,String endIp,String gwShare_onlineStatus)
	{
		StringBuffer sql = new StringBuffer();// TODO wait (more table related)
		sql.append("select a.city_id, a.device_serialnumber, a.serv_account, a.cpe_mac, a.loopback_ip, a.complete_time,");
		sql.append(" b.vendor_add,");
		sql.append(" c.device_model,");
		sql.append(" d.softwareversion,");
		sql.append(" b.vendor_name");
		sql.append(" from stb_tab_gw_device a,");
		sql.append(" stb_tab_vendor b,");
		sql.append(" stb_gw_device_model c,");
		sql.append(" stb_tab_devicetype_info d,");
		sql.append(" stb_gw_devicestatus e ");
		sql.append(" where a.device_id = e.device_id ");
		sql.append(" and a.vendor_id = b.vendor_id");
		sql.append(" and a.device_model_id = c.device_model_id");
		sql.append(" and a.devicetype_id = d.devicetype_id ");
		if(!StringUtil.IsEmpty(vendorId)&&!vendorId.equals("-1"))
		{
			sql.append(" and a.vendor_id='"+vendorId+"' ");
		}
		if(!StringUtil.IsEmpty(deviceModelId)&&!deviceModelId.equals("-1"))
		{
			sql.append(" and a.device_model_id='"+deviceModelId+"' ");
		}
		if(!StringUtil.IsEmpty(devicetypeId)&&!devicetypeId.equals("-1"))
		{
			sql.append(" and a.devicetype_id='"+devicetypeId+"' ");
		}

		if(!StringUtil.IsEmpty(gwShare_cityId)&&!gwShare_cityId.equals("-1")&&!StringUtil.IsEmpty(cityId)&&!cityId.equals("-1"))
		{
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and a.city_id in (").append(StringUtils.weave(cityIdList))
			.append(")");
				cityIdList = null;
		}
		else if(!StringUtil.IsEmpty(gwShare_cityId)&&!gwShare_cityId.equals("-1")&&!StringUtil.IsEmpty(cityId)&&cityId.equals("-1"))
		{
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(gwShare_cityId);
			sql.append(" and a.city_id in (").append(StringUtils.weave(cityIdList))
			.append(")");
				cityIdList = null;
			//sql.append(" and a.city_id='"+gwShare_cityId+"' ");
		}
		if(!StringUtil.IsEmpty(startIp))
		{
			sql.append(" and a.loopback_ip >='"+startIp+"' ");
		}
		if(!StringUtil.IsEmpty(endIp))
		{
			sql.append(" and a.loopback_ip <='"+endIp+"' ");
		}if(!StringUtil.IsEmpty(gwShare_onlineStatus)&&!gwShare_onlineStatus.equals("-1"))
		{
			sql.append(" and e.online_status='"+gwShare_onlineStatus+"' ");
		}
		sql.append(" order by a.complete_time desc");
		PrepareSQL psql = new PrepareSQL(sql.toString());

		List<Map> list = querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage+ 1,
				num_splitPage, new RowMapper()
		{

			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				//List<Map<String,String>> city=city(rs.getString("city_id"));
				Map<String, String> map = new HashMap<String, String>();
				map.put("vendor_name", rs.getString("vendor_name"));
				map.put("device_model", rs.getString("device_model"));
				map.put("softwareversion",rs.getString("softwareversion"));
				map.put("city_id",rs.getString("city_id"));
				//map.put("city_name",rs.getString(city.get(0).get("city_name")));
				map.put("device_serialnumber", rs.getString("device_serialnumber"));
				map.put("serv_account", rs.getString("serv_account"));
				map.put("cpe_mac", rs.getString("cpe_mac"));
				map.put("loopback_ip", rs.getString("loopback_ip"));
				map.put("complete_time", new DateTimeUtil(rs.getLong("complete_time") * 1000).getYYYY_MM_DD_HH_mm_ss());
				return map;
			}
		});
		return list;
	}
	/**
	 * 获取详细信息(分页)
	 * @param taskId
	 * @param taskName
	 * @param vendorId
	 * @param deviceModelId
	 * @param queryType       0：成功，1：失败，2：未触发，3:总配置数
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @return
	 */
	public int queryTotalListCount(int curPage_splitPage, int num_splitPage, String vendorId,String deviceModelId,String devicetypeId,String gwShare_cityId,String cityId,String startIp,String endIp,String gwShare_onlineStatus)
	{
		StringBuffer sql = new StringBuffer();// TODO wait (more table related)
		sql.append("select count(*)");
		sql.append(" from stb_tab_gw_device a,");
		sql.append(" stb_tab_vendor b,");
		sql.append(" stb_gw_device_model c,");
		sql.append(" stb_tab_devicetype_info d,");
		sql.append(" stb_gw_devicestatus e ");
		sql.append(" where a.device_id = e.device_id ");
		sql.append(" and a.vendor_id = b.vendor_id");
		sql.append(" and a.device_model_id = c.device_model_id");
		sql.append(" and a.devicetype_id = d.devicetype_id ");
		if(!StringUtil.IsEmpty(vendorId)&&!vendorId.equals("-1"))
		{
			sql.append(" and a.vendor_id='"+vendorId+"'");
		}
		if(!StringUtil.IsEmpty(deviceModelId)&&!deviceModelId.equals("-1"))
		{
			sql.append(" and a.device_model_id='"+deviceModelId+"'");
		}
		if(!StringUtil.IsEmpty(devicetypeId)&&!devicetypeId.equals("-1"))
		{
			sql.append(" and a.devicetype_id='"+devicetypeId+"'");
		}

		if(!StringUtil.IsEmpty(gwShare_cityId)&&!gwShare_cityId.equals("-1")&&!StringUtil.IsEmpty(cityId)&&!cityId.equals("-1"))
		{
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and a.city_id in (").append(StringUtils.weave(cityIdList))
			.append(")");
				cityIdList = null;
		}
		else if(!StringUtil.IsEmpty(gwShare_cityId)&&!gwShare_cityId.equals("-1")&&!StringUtil.IsEmpty(cityId)&&cityId.equals("-1"))
		{
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(gwShare_cityId);
			sql.append(" and a.city_id in (").append(StringUtils.weave(cityIdList))
			.append(")");
				cityIdList = null;
			//sql.append(" and a.city_id='"+gwShare_cityId+"' ");
		}
		if(!StringUtil.IsEmpty(startIp))
		{
			sql.append(" and a.loopback_ip >='"+startIp+"'");
		}
		if(!StringUtil.IsEmpty(endIp))
		{
			sql.append(" and a.loopback_ip <='"+endIp+"'");
		}if(!StringUtil.IsEmpty(gwShare_onlineStatus)&&!gwShare_onlineStatus.equals("-1"))
		{
			sql.append(" and e.online_status='"+gwShare_onlineStatus+"'");
		}
		sql.append(" order by a.complete_time desc");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		int total = jt.queryForInt(psql.getSQL());
		int maxPage = 1;
		if (total % num_splitPage == 0) {
			maxPage = total / num_splitPage;
		} else {
			maxPage = total / num_splitPage + 1;
		}
		return maxPage;
	}
	/**
	 * 导出
	 * @param cityid
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map> getExcel( String vendorId,String deviceModelId,String devicetypeId,String gwShare_cityId,String cityId,String startIp,String endIp,String gwShare_onlineStatus)
	{
		StringBuffer sql = new StringBuffer();// TODO wait (more table related)
		sql.append("select a.city_id, a.device_serialnumber, a.serv_account, a.cpe_mac, a.loopback_ip, a.complete_time,");
		sql.append(" b.vendor_add,");
		sql.append(" c.device_model,");
		sql.append(" d.softwareversion,");
		sql.append(" b.vendor_name");
		sql.append(" from stb_tab_gw_device a,");
		sql.append(" stb_tab_vendor b,");
		sql.append(" stb_gw_device_model c,");
		sql.append(" stb_tab_devicetype_info d,");
		sql.append(" stb_gw_devicestatus e ");
		sql.append(" where a.device_id = e.device_id ");
		sql.append(" and a.vendor_id = b.vendor_id");
		sql.append(" and a.device_model_id = c.device_model_id");
		sql.append(" and a.devicetype_id = d.devicetype_id ");
		if(!StringUtil.IsEmpty(vendorId)&&!vendorId.equals("-1"))
		{
			sql.append(" and a.vendor_id='"+vendorId+"'");
		}
		if(!StringUtil.IsEmpty(deviceModelId)&&!deviceModelId.equals("-1"))
		{
			sql.append(" and a.device_model_id='"+deviceModelId+"'");
		}

		if(!StringUtil.IsEmpty(devicetypeId)&&!devicetypeId.equals("-1"))
		{
			sql.append(" and a.devicetype_id='"+devicetypeId+"'");
		}
		if(!StringUtil.IsEmpty(gwShare_cityId)&&!gwShare_cityId.equals("-1")&&!StringUtil.IsEmpty(cityId)&&!cityId.equals("-1"))
		{
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and a.city_id in (").append(StringUtils.weave(cityIdList))
			.append(")");
				cityIdList = null;
		}
		else if(!StringUtil.IsEmpty(gwShare_cityId)&&!gwShare_cityId.equals("-1")&&!StringUtil.IsEmpty(cityId)&&cityId.equals("-1"))
		{
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(gwShare_cityId);
			sql.append(" and a.city_id in (").append(StringUtils.weave(cityIdList))
			.append(")");
				cityIdList = null;
			//sql.append(" and a.city_id='"+gwShare_cityId+"' ");
		}

		if(!StringUtil.IsEmpty(startIp))
		{
			sql.append(" and a.loopback_ip >='"+startIp+"'");
		}
		if(!StringUtil.IsEmpty(endIp))
		{
			sql.append(" and a.loopback_ip <='"+endIp+"'");
		}if(!StringUtil.IsEmpty(gwShare_onlineStatus)&&!gwShare_onlineStatus.equals("-1"))
		{
			sql.append(" and e.online_status='"+gwShare_onlineStatus+"'");
		}
		sql.append(" order by a.complete_time desc");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		List<Map> list = jt.query(psql.getSQL(), new RowMapper()
		{

			public Object mapRow(ResultSet rs, int rowNum) throws SQLException
			{
				//List<Map<String,String>> city=city(rs.getString("city_id"));
				Map<String, String> map = new HashMap<String, String>();
				map.put("vendor_name", rs.getString("vendor_name"));
				map.put("device_model", rs.getString("device_model"));
				map.put("softwareversion",rs.getString("softwareversion"));
				String city_id = String.valueOf(rs.getString("city_id"));
				map.put("city_name", CityDAO.getCityIdCityNameMap().get(city_id));
				//map.put("city_id",rs.getString("city_id"));
				//map.put("city_name",rs.getString(city.get(0).get("city_name")));
				map.put("device_serialnumber", rs.getString("device_serialnumber"));
				map.put("serv_account", rs.getString("serv_account"));
				map.put("cpe_mac", rs.getString("cpe_mac"));
				map.put("loopback_ip", rs.getString("loopback_ip"));
				map.put("complete_time", new DateTimeUtil(rs.getLong("complete_time") * 1000).getYYYY_MM_DD_HH_mm_ss());
				return map;
			}
		});
		return list;
	}
	public List<Map<String,String>> city(String cityid)
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select city_name from tab_city where city_id='"+cityid+"'");
		List<Map<String,String>> list = jt.queryForList(psql.getSQL());
		return list;
	}
	/* *
	 * /**
	 * 查询厂商
	 * @param gw_type
	 * @return
	 *//*
	public List<Map<String,Object>>  getVendorList(String gw_type)
	{
		PrepareSQL pSQL = new PrepareSQL();
		if("4".equals(gw_type))
		{
		pSQL.append("select vendor_id,vendor_name, vendor_add from stb_tab_vendor");
		}else{
		pSQL.append("select distinct vendor_id,vendor_name,vendor_add from tab_vendor");
		}
		return jt.queryForList(pSQL.toString());
	}*/

	/**
	 * 查询型号
	 * @param gw_type
	 * @param vendor_name
	 * @param vendor_id
	 * @return
	 */
	/*public List<Map<String,Object>> getDeviceModelList(String gw_type,String vendor_name,String vendor_id)
	{
		PrepareSQL pSQL = new PrepareSQL();
		if("4".equals(gw_type))
		{
		pSQL.append("select a.device_model_id,a.device_model from stb_gw_device_model a,stb_tab_vendor b where a.vendor_id=b.vendor_id ");
		}else{
		pSQL.append("select a.device_model_id,a.device_model from gw_device_model a,tab_vendor b where a.vendor_id=b.vendor_id ");
		}
		if(!StringUtil.IsEmpty(vendor_name))
		{
			pSQL.append(" and b.vendor_name="+vendor_name);
		}
		if(!StringUtil.IsEmpty(vendor_id))
		{
			pSQL.append(" and b.vendor_id="+vendor_id);
		}
		return jt.queryForList(pSQL.toString());
	}*/

	/*public List<Map<String,Object>> getDevicetype(String gw_type,String deviceModelId, String isBatch)
	{
		PrepareSQL pSQL = new PrepareSQL();
		if("4".equals(gw_type)){
			pSQL.append("select a.devicetype_id,a.softwareversion from stb_tab_devicetype_info a where");
		}else{
			pSQL.append(" select a.devicetype_id,a.softwareversion from tab_devicetype_info a where  ");
		}
		if(!StringUtil.IsEmpty(deviceModelId))
		{
			pSQL.append("  a.device_model_id="+deviceModelId);
		}
		return jt.queryForList(pSQL.toString());
	}*/
}
