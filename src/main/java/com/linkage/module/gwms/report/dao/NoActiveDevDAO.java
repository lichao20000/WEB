
package com.linkage.module.gwms.report.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.gw.VendorModelVersionDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.StringUtil;

/**
 * 考核所有的用户
 * 
 * @author 王森博
 */
@SuppressWarnings("unchecked")
public class NoActiveDevDAO extends SuperDAO
{

	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(NoActiveDevDAO.class);
	/**
	 * Map<city_id,city_name>
	 */
	private Map<String, String> cityMap = null;
	/**
	 * HashMap<vendor_id,vendor_add>
	 */
	private HashMap<String, String> vendorMap = null;
	/**
	 * HashMap<device_model_id,device_model>
	 */
	private HashMap<String, String> deviceModelMap = null;
	/**
	 * HashMap<devicetype_id,softwareversion>
	 */
	private HashMap<String, String> devicetypeMap = null;

	/**
	 * 统计不活跃设备
	 * 
	 * @author wangsenbo
	 * @date Apr 21, 2010
	 * @param
	 * @return Map
	 */
	public Map count(String activetime1, String cityId,String gw_type)
	{
		logger.debug("count({},{})", new Object[] { activetime1, cityId });
		StringBuffer sql = new StringBuffer();
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			sql.append("select a.city_id,count(*) as total ");
		}else{
			sql.append("select a.city_id,count(1) as total ");
		}
		sql.append("from tab_gw_device a,gw_devicestatus b ");
		sql.append("where a.device_id=b.device_id and a.device_status!=-1");
		if (!StringUtil.IsEmpty(cityId) && !"00".equals(cityId))
		{
			sql.append(" and a.city_id in (?) ");
		}
		sql.append(" and b.last_time<?");
		if(!StringUtil.IsEmpty(gw_type)){
			sql.append(" and a.gw_type = "+gw_type );
		} 
		sql.append(" group by a.city_id" );
		logger.warn("统计不活跃设备sql = "+sql);
		PrepareSQL psql = new PrepareSQL(sql.toString());
		List cityList  = null;
		if (!StringUtil.IsEmpty(cityId) && !"00".equals(cityId))
		{
			cityList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			psql.setStringExt(1, StringUtils.weave(cityList), false);
			psql.setLong(2, StringUtil.getLongValue(activetime1));
		}else {
			psql.setLong(1, StringUtil.getLongValue(activetime1));
		}
		List list = jt.queryForList(psql.getSQL());
		Map<String, String> map = new HashMap<String, String>();
		if (false == list.isEmpty())
		{
			for (int i = 0; i < list.size(); i++)
			{
				Map rmap = (Map) list.get(i);
				map.put(StringUtil.getStringValue(rmap.get("city_id")), StringUtil
						.getStringValue(rmap.get("total")));
			}
		}
		cityList = null;
		return map;
	}

	/**
	 * 不活跃设备列表
	 * 
	 * @author wangsenbo
	 * @date Apr 21, 2010
	 * @param
	 * @return List<Map>
	 */
	public List<Map> getDevList(String activetime1, String cityId, int curPage_splitPage,
			int num_splitPage,String gwType)
	{
		PrepareSQL psql = new PrepareSQL();
	/**	if(DBUtil.GetDB()==Global.DB_MYSQL){
			//TODO wait
		}else{
			
		} */
		
		//如果是ITMS
		if (gwType.equals(Global.GW_TYPE_ITMS))
		{
			if (Global.JSDX.equals(Global.instAreaShortName))
			{
				psql.append("select a.device_id,a.city_id,a.oui,a.device_model_id,a.devicetype_id," +
						"a.vendor_id,a.device_serialnumber,a.loopback_ip,b.username,c.serv_status " +
						"from tab_gw_device a " +
						"left join tab_hgwcustomer b on (a.device_id=b.device_id and b.user_state in ('1','2')) " +
						"left join tab_iptv_user c on b.user_id=c.user_id," +
						"gw_devicestatus e " +
						"where a.device_id=e.device_id ");
				if (!StringUtil.IsEmpty(cityId) && !"00".equals(cityId))
				{
					psql.append(" and a.city_id in (?) ");
				}
				psql.append(" and a.device_status !=-1 and e.last_time<?");
			}
			else
			{
				psql.append("select a.device_id,a.city_id,a.oui,a.device_model_id,a.devicetype_id," +
						"a.vendor_id,a.device_serialnumber,a.loopback_ip,b.username " +
						"from tab_gw_device a " +
						"left join tab_hgwcustomer b on (a.device_id=b.device_id and b.user_state in ('1','2'))," +
						"gw_devicestatus e " +
						"where a.device_id=e.device_id ");
				if (!StringUtil.IsEmpty(cityId) && !"00".equals(cityId))
				{
					psql.append(" and a.city_id in (?) ");
				}
				psql.append(" and a.device_status !=-1 and e.last_time<?");
			}
		}
		else
		{
			psql.append("select a.device_id,a.city_id,a.oui,a.device_model_id,a.devicetype_id," +
					"a.vendor_id,a.device_serialnumber,a.loopback_ip,b.username " +
					"from tab_gw_device a " +
					"left join tab_egwcustomer b on (a.device_id=b.device_id and b.user_state in ('1','2'))," +
					"gw_devicestatus e " +
					"where a.device_id=e.device_id ");
			if (!StringUtil.IsEmpty(cityId) && !"00".equals(cityId))
			{
				psql.append(" and  a.city_id in (?) ");
			}
			psql.append(" and a.device_status !=-1 and e.last_time<?");
		}
		psql.append(" and a.gw_type="+gwType);
		List cityList =null;
		if (!StringUtil.IsEmpty(cityId) && !"00".equals(cityId))
		{
			cityList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			psql.setStringExt(1, StringUtils.weave(cityList), false);
			psql.setLong(2, StringUtil.getLongValue(activetime1));
		}else{
			psql.setLong(1, StringUtil.getLongValue(activetime1));
		}
		cityMap = CityDAO.getCityIdCityNameMap();
		vendorMap = VendorModelVersionDAO.getVendorMap();
		deviceModelMap = VendorModelVersionDAO.getDeviceModelMap();
		devicetypeMap = VendorModelVersionDAO.getDevicetypeMap();
		List<Map> list = querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage
				+ 1, num_splitPage, new RowMapper()
		{

			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				map.put("device_id", rs.getString("device_id"));
				String city_id = rs.getString("city_id");
				String city_name = StringUtil.getStringValue(cityMap.get(city_id));
				if (false == StringUtil.IsEmpty(city_name))
				{
					map.put("city_name", city_name);
				}
				else
				{
					map.put("city_name", "");
				}
				String vendor_id = rs.getString("vendor_id");
				String vendor_add = StringUtil.getStringValue(vendorMap.get(vendor_id));
				if (false == StringUtil.IsEmpty(vendor_add))
				{
					map.put("vendor_add", vendor_add);
				}
				else
				{
					map.put("vendor_add", "");
				}
				String device_model_id = rs.getString("device_model_id");
				String device_model = StringUtil.getStringValue(deviceModelMap
						.get(device_model_id));
				if (false == StringUtil.IsEmpty(device_model))
				{
					map.put("device_model", device_model);
				}
				else
				{
					map.put("device_model", "");
				}
				String devicetype_id = rs.getString("devicetype_id");
				String softwareversion = StringUtil.getStringValue(devicetypeMap
						.get(devicetype_id));
				if (false == StringUtil.IsEmpty(softwareversion))
				{
					map.put("softwareversion", softwareversion);
				}
				else
				{
					map.put("softwareversion", "");
				}
				map.put("device_serialnumber", rs.getString("device_serialnumber"));
				map.put("loopback_ip", rs.getString("loopback_ip"));
				map.put("username", rs.getString("username"));
				// iTV是否开通
				if (LipossGlobals.IsITMS())
				{
					if (Global.JSDX.equals(Global.instAreaShortName))
					{
						String serv_status = rs.getString("serv_status");
						if ("1".equals(serv_status))
						{
							map.put("iTV", "是");
						}
						else
						{
							map.put("iTV", "否");
						}
					}
				}
				return map;
			}
		});
		cityList = null;
		cityMap = null;
		vendorMap = null;
		deviceModelMap = null;
		devicetypeMap = null;
		return list;
	}

	public int getDevCount(String activetime1, String cityId, int curPage_splitPage,
			int num_splitPage,String gwType)
	{
		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			//TODO wait
			psql.append("select count(*) ");
		}else{
			psql.append("select count(1) ");
		}
		
		//如果是ITMS
		if (gwType.equals(Global.GW_TYPE_ITMS))
		{
			if (Global.JSDX.equals(Global.instAreaShortName))
			{
				psql.append("from tab_gw_device a " +
						"left join tab_hgwcustomer b on (a.device_id=b.device_id and b.user_state in ('1','2')) " +
						"left join tab_iptv_user c on b.user_id=c.user_id," +
						"gw_devicestatus e " +
						"where a.device_id=e.device_id");
				if (!StringUtil.IsEmpty(cityId) && !"00".equals(cityId))
				{
					psql.append(" and a.city_id in (?) ");
				}
				psql.append(" and a.device_status !=-1 and e.last_time<? ");
			}
			else
			{
				psql.append("from tab_gw_device a " +
						"left join tab_hgwcustomer b on (a.device_id=b.device_id and b.user_state in ('1','2'))," +
						"gw_devicestatus e " +
						"where a.device_id=e.device_id");
				
				if (!StringUtil.IsEmpty(cityId) && !"00".equals(cityId))
				{
					psql.append(" and a.city_id in (?) ");
				}
				psql.append(" and a.device_status !=-1 and e.last_time<? ");
			}
		}
		////如果是BBMS
		else
		{
			psql.append("from tab_gw_device a " +
					"left join tab_egwcustomer b on (a.device_id=b.device_id and b.user_state in ('1','2'))," +
					" gw_devicestatus e " +
					"where a.device_id=e.device_id");
			if (!StringUtil.IsEmpty(cityId) && !"00".equals(cityId))
			{
				psql.append(" and a.city_id in (?) ");
			}
			psql.append(" and a.device_status !=-1 and e.last_time<? ");
		}
		psql.append("and a.gw_type="+gwType);
		List cityList = null;
		if (!StringUtil.IsEmpty(cityId) && !"00".equals(cityId))
		{
			cityList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			psql.setStringExt(1, StringUtils.weave(cityList), false);
			psql.setLong(2, StringUtil.getLongValue(activetime1));
		}else {
			psql.setLong(1, StringUtil.getLongValue(activetime1));
		}
		int total = jt.queryForInt(psql.getSQL());
		int maxPage = 1;
		if (total % num_splitPage == 0)
		{
			maxPage = total / num_splitPage;
		}
		else
		{
			maxPage = total / num_splitPage + 1;
		}
		cityList = null;
		return maxPage;
	}

	public List<Map> getDevExcel(String activetime1, String cityId, String gwType)
	{
		PrepareSQL psql = null;
		//ITMS
		if (gwType.equals(Global.GW_TYPE_ITMS))
		{
			if (Global.JSDX.equals(Global.instAreaShortName))
			{
			/**	if(DBUtil.GetDB()==Global.DB_MYSQL){
					//TODO wait
				}else{
					
				} */
				psql = new PrepareSQL();
				psql.append("select a.device_id,a.city_id,a.oui,a.device_model_id,a.devicetype_id," +
						"a.vendor_id,a.device_serialnumber,a.loopback_ip,b.username,c.serv_status " +
						"from tab_gw_device a " +
						"left join tab_hgwcustomer b on (a.device_id=b.device_id and b.user_state in ('1','2')) " +
						"left join tab_iptv_user c on b.user_id=c.user_id," +
						"gw_devicestatus e " +
						"where a.device_id=e.device_id");
				if (!StringUtil.IsEmpty(cityId) && !"00".equals(cityId))
				{
					psql.append(" and a.city_id in (?) ");
				}
				psql.append(" and a.device_status !=-1 and e.last_time<?");
			}
			else
			{
			/**	if(DBUtil.GetDB()==Global.DB_MYSQL){
					//TODO wait
				}else{
					
				} */
				psql = new PrepareSQL();
				psql.append("select a.device_id,a.city_id,a.oui,a.device_model_id," +
						"a.devicetype_id,a.vendor_id,a.device_serialnumber,a.loopback_ip,b.username " +
						"from tab_gw_device a " +
						"left join tab_hgwcustomer b on (a.device_id=b.device_id and b.user_state in ('1','2'))," +
						"gw_devicestatus e " +
						"where a.device_id=e.device_id");
				if (!StringUtil.IsEmpty(cityId) && !"00".equals(cityId))
				{
					psql.append(" and a.city_id in (?) ");
				}
				psql.append(" and a.device_status !=-1 and e.last_time<?");
			}
		}
		//BBMS
		else
		{
		/**	if(DBUtil.GetDB()==Global.DB_MYSQL){
				//TODO wait
			}else{
				
			} */
			psql = new PrepareSQL();
			psql.append("select a.device_id,a.city_id,a.oui,a.device_model_id,a.devicetype_id," +
					"a.vendor_id,a.device_serialnumber,a.loopback_ip,b.username " +
					"from tab_gw_device a " +
					"left join tab_egwcustomer b on (a.device_id=b.device_id and b.user_state in ('1','2'))," +
					"gw_devicestatus e " +
					"where a.device_id=e.device_id");
			if (!StringUtil.IsEmpty(cityId) && !"00".equals(cityId))
			{
				psql.append(" and a.city_id in (?) ");
			}
			psql.append(" and a.device_status !=-1 and e.last_time<?");
		}
		psql.append(" and a.gw_type="+gwType);
		List cityList = null;
		if (!StringUtil.IsEmpty(cityId) && !"00".equals(cityId))
		{
			cityList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			psql.setStringExt(1, StringUtils.weave(cityList), false);
			psql.setLong(2, StringUtil.getLongValue(activetime1));
		}else {
			psql.setLong(1, StringUtil.getLongValue(activetime1));
		}
		cityMap = CityDAO.getCityIdCityNameMap();
		vendorMap = VendorModelVersionDAO.getVendorMap();
		deviceModelMap = VendorModelVersionDAO.getDeviceModelMap();
		devicetypeMap = VendorModelVersionDAO.getDevicetypeMap();
		List<Map> list = jt.query(psql.getSQL(), new RowMapper()
		{

			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				map.put("device_id", rs.getString("device_id")); 
				String city_id = rs.getString("city_id");
				String city_name = StringUtil.getStringValue(cityMap.get(city_id));
				if (false == StringUtil.IsEmpty(city_name))
				{
					map.put("city_name", city_name);
				}
				else
				{
					map.put("city_name", "");
				}
				String vendor_id = rs.getString("vendor_id");
				String vendor_add = StringUtil.getStringValue(vendorMap.get(vendor_id));
				if (false == StringUtil.IsEmpty(vendor_add))
				{
					map.put("vendor_add", vendor_add);
				}
				else
				{
					map.put("vendor_add", "");
				}
				String device_model_id = rs.getString("device_model_id");
				String device_model = StringUtil.getStringValue(deviceModelMap
						.get(device_model_id));
				if (false == StringUtil.IsEmpty(device_model))
				{
					map.put("device_model", device_model);
				}
				else
				{
					map.put("device_model", "");
				}
				String devicetype_id = rs.getString("devicetype_id");
				String softwareversion = StringUtil.getStringValue(devicetypeMap
						.get(devicetype_id));
				if (false == StringUtil.IsEmpty(softwareversion))
				{
					map.put("softwareversion", softwareversion);
				}
				else
				{
					map.put("softwareversion", "");
				}
				map.put("device_serialnumber", rs.getString("device_serialnumber"));
				map.put("loopback_ip", rs.getString("loopback_ip"));
				map.put("username", rs.getString("username"));
				// iTV是否开通
				if (LipossGlobals.IsITMS())
				{
					if (Global.JSDX.equals(Global.instAreaShortName))
					{
						String serv_status = rs.getString("serv_status");
						if ("1".equals(serv_status))
						{
							map.put("iTV", "是");
						}
						else
						{
							map.put("iTV", "否");
						}
					}
				}
				return map;
			}
		});
		cityList = null;
		cityMap = null;
		vendorMap = null;
		deviceModelMap = null;
		devicetypeMap = null;
		return list;
	}
}
