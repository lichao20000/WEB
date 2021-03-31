
package com.linkage.module.itms.report.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.gw.VendorModelVersionDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

@SuppressWarnings({"rawtypes","unchecked"})
public class ActiveTerminalDAO extends SuperDAO
{
	private static Logger logger = LoggerFactory.getLogger(ActiveTerminalDAO.class);
	private Map<String, String> cityMap = null;
	private HashMap<String, String> vendorMap = null;
	private HashMap<String, String> deviceModelMap = null;
	private HashMap<String, String> devicetypeMap = null;

	public Map getActiveTerminalMap(String city_id, String starttime, String endtime,
			String device_type)
	{
		logger.debug("getActiveTerminalInfo({},{},{},{},{},{},{})", new Object[] {
				city_id, starttime, endtime, device_type });
		StringBuffer sql = new StringBuffer();
		sql.append("select x.city_id,x.ttotal,y.total from (");
		
		if(DBUtil.GetDB()==3){
			sql.append("select m.city_id,count(*) as ttotal ");
		}else{
			sql.append("select m.city_id,count(1) as ttotal ");
		}
		sql.append("from tab_gw_device m,gw_devicestatus n ");
		sql.append("where m.device_id=n.device_id and m.device_status!=-1 and m.gw_type=1 ");
		if (!StringUtil.IsEmpty(city_id) && !"00".equals(city_id)
				&& !"-1".equals(city_id))
		{
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
			sql.append(" and m.city_id in (").append(StringUtils.weave(cityIdList))
					.append(")");
			cityIdList = null;
		}
		if (!StringUtil.IsEmpty(device_type) && !"-1".equals(device_type))
		{
			sql.append(" and m.device_type='").append(device_type).append("'");
		}
		sql.append(" group by m.city_id) x left join (");
		if(DBUtil.GetDB()==3){
			sql.append("select a.city_id,count(*) as total ");
		}else{
			sql.append("select a.city_id,count(1) as total ");
		}
		sql.append("from tab_gw_device a,gw_devicestatus b ");
		sql.append("where a.device_id=b.device_id and a.device_status!=-1 and a.gw_type=1 ");
		if (!StringUtil.IsEmpty(city_id) && !"00".equals(city_id)
				&& !"-1".equals(city_id))
		{
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
			sql.append(" and a.city_id in (").append(StringUtils.weave(cityIdList))
					.append(")");
			cityIdList = null;
		}
		if (!StringUtil.IsEmpty(starttime))
		{
			sql.append(" and b.last_time>").append(starttime);
		}
		if (!StringUtil.IsEmpty(endtime))
		{
			sql.append(" and b.last_time<").append(endtime);
		}
		if (!StringUtil.IsEmpty(device_type) && !"-1".equals(device_type))
		{
			sql.append(" and a.device_type='").append(device_type).append("'");
		}
		sql.append(" group by a.city_id) y on x.city_id=y.city_id");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		Map<String, String> map = new HashMap<String, String>();
		List<Map> list = new ArrayList<Map>();
		list = jt.queryForList(psql.getSQL());
		if (!list.isEmpty())
		{
			for (int i = 0; i < list.size(); i++)
			{
				Map rmap = (Map) list.get(i);
				map.put(StringUtil.getStringValue(rmap.get("city_id")),
						StringUtil.getStringValue(rmap.get("total")) + ":"
								+ StringUtil.getStringValue(rmap.get("ttotal")));
			}
		}
		return map;
	}

	public List<Map> getDeviceListForAll(String city_id, String device_type,
			int curPage_splitPage, int num_splitPage)
	{
		StringBuffer sql = new StringBuffer();
		/**	if(DBUtil.GetDB()==3){
			//TODO wait
		}else{
			
		}*/
		sql.append("select a.device_id,a.city_id,a.oui,a.device_model_id,a.devicetype_id,");
		sql.append("a.vendor_id,a.device_serialnumber,a.loopback_ip,b.username,h.username broadband ");
		sql.append("from tab_gw_device a ");
		sql.append("left join tab_hgwcustomer b on (a.device_id=b.device_id and b.user_state in ('1','2')) ");
		sql.append("left join hgwcust_serv_info h on (b.user_id=h.user_id and h.serv_type_id =10)");
		sql.append(",gw_devicestatus c ");
		sql.append("where a.device_id=c.device_id and a.device_status!=-1 and a.gw_type=1 ");
		if (!StringUtil.IsEmpty(city_id) && !"00".equals(city_id)
				&& !"-1".equals(city_id))
		{
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
			sql.append(" and a.city_id in (").append(StringUtils.weave(cityIdList))
					.append(")");
			cityIdList = null;
		}
		if (!StringUtil.IsEmpty(device_type) && !"-1".equals(device_type))
		{
			sql.append(" and a.device_type='").append(device_type).append("'");
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
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
				map.put("city_id", city_id);
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
				map.put("broadband", StringUtil.getStringValue(rs.getString("broadband")));
				return map;
			}
		});
		cityMap = null;
		vendorMap = null;
		deviceModelMap = null;
		devicetypeMap = null;
		return list;
	}

	public int getDevForAllCount(String city_id, String device_type,
			int curPage_splitPage, int num_splitPage)
	{
		StringBuffer sql = new StringBuffer();
		if(DBUtil.GetDB()==3){
			//TODO wait
			sql.append("select count(*) ");
		}else{
			sql.append("select count(1) ");
		}
		
		sql.append("from tab_gw_device a ");
		sql.append("left join tab_hgwcustomer b on (a.device_id=b.device_id and b.user_state in ('1','2')) ");
		sql.append("left join hgwcust_serv_info h on (b.user_id=h.user_id and h.serv_type_id =10)");
		sql.append(",gw_devicestatus c ");
		sql.append("where a.device_id=c.device_id and a.device_status!=-1 and a.gw_type=1 ");
		if (!StringUtil.IsEmpty(city_id) && !"00".equals(city_id)
				&& !"-1".equals(city_id))
		{
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
			sql.append(" and a.city_id in (").append(StringUtils.weave(cityIdList))
					.append(")");
			cityIdList = null;
		}
		if (!StringUtil.IsEmpty(device_type) && !"-1".equals(device_type))
		{
			sql.append(" and a.device_type='").append(device_type).append("'");
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
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
		return maxPage;
	}

	public List<Map> getDevForAllExcel(String city_id, String device_type)
	{
		StringBuffer sql = new StringBuffer();
		/**	if(DBUtil.GetDB()==3){
			//TODO wait
		}else{
			
		}*/
		sql.append("select a.device_id,a.city_id,a.oui,a.device_model_id,a.devicetype_id,");
		sql.append("a.vendor_id,a.device_serialnumber,a.loopback_ip,b.username,h.username broadband ");
		sql.append("from tab_gw_device a ");
		sql.append("left join tab_hgwcustomer b on (a.device_id=b.device_id and b.user_state in ('1','2')) ");
		sql.append("left join hgwcust_serv_info h on (b.user_id=h.user_id and h.serv_type_id =10)");
		sql.append(",gw_devicestatus c ");
		sql.append("where a.device_id=c.device_id and a.device_status!=-1 and a.gw_type=1 ");
		if (!StringUtil.IsEmpty(city_id) && !"00".equals(city_id)
				&& !"-1".equals(city_id))
		{
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
			sql.append(" and a.city_id in (").append(StringUtils.weave(cityIdList))
					.append(")");
			cityIdList = null;
		}
		if (!StringUtil.IsEmpty(device_type) && !"-1".equals(device_type))
		{
			sql.append(" and a.device_type='").append(device_type).append("'");
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
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
				map.put("broadband", StringUtil.getStringValue(rs.getString("broadband")));
				return map;
			}
		});
		cityMap = null;
		vendorMap = null;
		deviceModelMap = null;
		devicetypeMap = null;
		return list;
	}

	public List<Map> getDeviceListForTime(String city_id, String starttime,
			String endtime, String device_type, int curPage_splitPage, int num_splitPage)
	{
		StringBuffer sql = new StringBuffer();
		/**	if(DBUtil.GetDB()==3){
			//TODO wait
		}else{
			
		}*/
		sql.append("select a.device_id,a.city_id,a.oui,a.device_model_id,a.devicetype_id,");
		sql.append("a.vendor_id,a.device_serialnumber,a.loopback_ip,b.username,h.username broadband ");
		sql.append("from tab_gw_device a ");
		sql.append("left join tab_hgwcustomer b on (a.device_id=b.device_id and b.user_state in ('1','2')) ");
		sql.append("left join hgwcust_serv_info h on (b.user_id=h.user_id and h.serv_type_id =10)");
		sql.append(",gw_devicestatus c ");
		sql.append(" where a.device_id=c.device_id  and a.device_status !=-1  and a.gw_type=1 ");
		if (!StringUtil.IsEmpty(city_id) && !"00".equals(city_id)
				&& !"-1".equals(city_id))
		{
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
			sql.append(" and a.city_id in (").append(StringUtils.weave(cityIdList))
					.append(")");
			cityIdList = null;
		}
		if (!StringUtil.IsEmpty(starttime))
		{
			sql.append(" and c.last_time>").append(starttime);
		}
		if (!StringUtil.IsEmpty(endtime))
		{
			sql.append(" and c.last_time<").append(endtime);
		}
		if (!StringUtil.IsEmpty(device_type) && !"-1".equals(device_type))
		{
			sql.append(" and a.device_type='").append(device_type).append("'");
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
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
				map.put("city_id", city_id);
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
				map.put("broadband", StringUtil.getStringValue(rs.getString("broadband")));
				return map;
			}
		});
		cityMap = null;
		vendorMap = null;
		deviceModelMap = null;
		devicetypeMap = null;
		return list;
	}

	public int getDeviceListForTimeCount(String city_id, String starttime,
			String endtime, String device_type, int curPage_splitPage, int num_splitPage)
	{
		StringBuffer sql = new StringBuffer();
		if(DBUtil.GetDB()==3){
			//TODO wait
			sql.append("select count(*) ");
		}else{
			sql.append("select count(1) ");
		}
		
		sql.append("from tab_gw_device a ");
		sql.append("left join tab_hgwcustomer b on (a.device_id=b.device_id and b.user_state in ('1','2')) ");
		sql.append("left join hgwcust_serv_info h on (b.user_id=h.user_id and h.serv_type_id =10)");
		sql.append(",gw_devicestatus c ");
		sql.append(" where a.device_id=c.device_id  and a.device_status !=-1  and a.gw_type=1 ");
		if (!StringUtil.IsEmpty(city_id) && !"00".equals(city_id)
				&& !"-1".equals(city_id))
		{
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
			sql.append(" and a.city_id in (").append(StringUtils.weave(cityIdList))
					.append(")");
			cityIdList = null;
		}
		if (!StringUtil.IsEmpty(starttime))
		{
			sql.append(" and c.last_time>").append(starttime);
		}
		if (!StringUtil.IsEmpty(endtime))
		{
			sql.append(" and c.last_time<").append(endtime);
		}
		if (!StringUtil.IsEmpty(device_type) && !"-1".equals(device_type))
		{
			sql.append(" and a.device_type='").append(device_type).append("'");
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
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
		return maxPage;
	}

	public List<Map> getDeviceListForTimeExcel(String city_id, String starttime,
			String endtime, String device_type)
	{
		StringBuffer sql = new StringBuffer();
		/**	if(DBUtil.GetDB()==3){
			//TODO wait
		}else{
			
		}*/
		sql.append("select a.device_id,a.city_id,a.oui,a.device_model_id,a.devicetype_id,");
		sql.append("a.vendor_id,a.device_serialnumber,a.loopback_ip,b.username,h.username broadband ");
		sql.append("from tab_gw_device a ");
		sql.append("left join tab_hgwcustomer b on (a.device_id=b.device_id and b.user_state in ('1','2')) ");
		sql.append("left join hgwcust_serv_info h on (b.user_id=h.user_id and h.serv_type_id =10)");
		sql.append(",gw_devicestatus c ");
		sql.append("where a.device_id=c.device_id and a.device_status!=-1 and a.gw_type=1 ");
		if (!StringUtil.IsEmpty(city_id) && !"00".equals(city_id)
				&& !"-1".equals(city_id))
		{
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
			sql.append(" and a.city_id in (").append(StringUtils.weave(cityIdList))
					.append(")");
			cityIdList = null;
		}
		if (!StringUtil.IsEmpty(starttime))
		{
			sql.append(" and c.last_time>").append(starttime);
		}
		if (!StringUtil.IsEmpty(endtime))
		{
			sql.append(" and c.last_time<").append(endtime);
		}
		if (!StringUtil.IsEmpty(device_type) && !"-1".equals(device_type))
		{
			sql.append(" and a.device_type='").append(device_type).append("'");
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
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
				map.put("broadband", StringUtil.getStringValue(rs.getString("broadband")));
				return map;
			}
		});
		cityMap = null;
		vendorMap = null;
		deviceModelMap = null;
		devicetypeMap = null;
		return list;
	}
}
