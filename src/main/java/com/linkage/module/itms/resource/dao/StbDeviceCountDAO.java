
package com.linkage.module.itms.resource.dao;

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
import com.linkage.litms.common.util.StringUtils;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.StringUtil;

public class StbDeviceCountDAO extends SuperDAO
{

	// 日志
	private static Logger logger = LoggerFactory.getLogger(StbDeviceCountDAO.class);

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map<String, Map<String, Integer>> getCountMap(String cityId)
	{
		logger.debug("StbDeviceCountDAO=>getCountMap({})", new Object[] { cityId });
		PrepareSQL psql = new PrepareSQL(
				"select count(device_id) as num,vendor_id,city_id "
						+ "from stb_tab_gw_device where device_status = 1 ");
		if (!StringUtil.IsEmpty(cityId) && !"00".equals(cityId))
		{
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			psql.append(" and city_id in (");
			psql.append(StringUtils.weave(cityIdList));
			psql.append(")");
			cityIdList = null;
		}
		psql.append(" group by vendor_id,city_id order by vendor_id ");
		List<Map<String, String>> list = jt.queryForList(psql.getSQL());
		Map<String, Map<String, Integer>> reMap = new HashMap<String, Map<String, Integer>>();
		Map temp = null;
		for (int i = 0; i < list.size(); i++)
		{
			temp = list.get(i);
			Map map1 = reMap.get(StringUtil.getStringValue(temp.get("vendor_id")));
			if (map1 == null || map1.isEmpty())
			{
				map1 = new HashMap<String, Integer>();
			}
			map1.put(StringUtil.getStringValue(temp.get("city_id")),
					StringUtil.getIntegerValue(temp.get("num")));
			reMap.put(StringUtil.getStringValue(temp.get("vendor_id")), map1);
		}
		return reMap;
	}

	@SuppressWarnings("rawtypes")
	public List<Map> getDetailInfo(String vendorId, String cityId, int curPage_splitPage,
			int num_splitPage)
	{
		logger.debug("StbDeviceCountDAO=>getDetailInfo({},{},{},{})", new Object[] {
				vendorId, cityId, curPage_splitPage, num_splitPage });
		StringBuffer sql = new StringBuffer();
		sql.append("select a.device_serialnumber,a.city_id,a.vendor_id,a.device_model_id,a.devicetype_id "
				+ "from stb_tab_gw_device a where a.device_status = 1 and 1=1 ");
		if (!StringUtil.IsEmpty(vendorId))
		{
			sql.append("and a.vendor_id = '" + vendorId + "' ");
		}
		if (null != cityId && !"".equals(cityId))
		{
			ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
			String cityStr = cityId + "','" + StringUtils.weave(cityArray, "','");
			sql.append("and a.city_id in ('" + cityStr + "') ");
		}
		final Map<String, String> vendorMap = getVendorMap();
		final Map<String, String> devModelMap = getModelMap();
		final Map<String, String> softVersionMap = getSoftVersionMap();
		PrepareSQL psql = new PrepareSQL(sql.toString());
		psql.getSQL();
		return querySP(sql.toString(), (curPage_splitPage - 1) * num_splitPage + 1,
				num_splitPage, new RowMapper()
				{

					public Object mapRow(ResultSet rs, int arg1) throws SQLException
					{
						Map<String, String> map = new HashMap<String, String>();
						map.put("vendorName", vendorMap.get(StringUtil.getStringValue(rs
								.getString("vendor_id"))));
						map.put("deviceModel", devModelMap.get(StringUtil
								.getStringValue(rs.getString("device_model_id"))));
						map.put("softwareversion", softVersionMap.get(StringUtil
								.getStringValue(rs.getString("devicetype_id"))));
						map.put("cityName", Global.G_CityId_CityName_Map.get(StringUtil
								.getStringValue(rs.getString("city_id"))));
						map.put("deviceSn", rs.getString("device_serialnumber"));
						return map;
					}
				});
	}

	@SuppressWarnings("rawtypes")
	public int countQueryDetail(String vendorId, String cityId, int curPage_splitPage,
			int num_splitPage)
	{
		logger.debug("StbDeviceCountDAO=>countQueryDetail({},{},{},{})", new Object[] {
				vendorId, cityId, curPage_splitPage, num_splitPage });
		StringBuffer sql = new StringBuffer();
		sql.append("select count(1) from stb_tab_gw_device a where a.device_status = 1 and 1=1 ");
		if (!StringUtil.IsEmpty(vendorId))
		{
			sql.append("and a.vendor_id = '" + vendorId + "' ");
		}
		if (null != cityId && !"".equals(cityId))
		{
			ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
			String cityStr = cityId + "','" + StringUtils.weave(cityArray, "','");
			sql.append("and a.city_id in ('" + cityStr + "') ");
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

	/**
	 * 获取设备厂商map
	 * 
	 * @return Map
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> getVendorMap()
	{
		PrepareSQL psql = new PrepareSQL(
				"select vendor_id,vendor_name,vendor_add from stb_tab_vendor ");
		Map<String, String> map = null;
		List<Map<String, String>> list = jt.queryForList(psql.getSQL());
		if (null != list && list.size() > 0)
		{
			map = new HashMap<String, String>();
			for (int i = 0; i < list.size(); i++)
			{
				String vendor_add = StringUtil.getStringValue(list.get(i), "vendor_add");
				if (false == StringUtil.IsEmpty(vendor_add))
				{
					map.put(StringUtil.getStringValue(list.get(i), "vendor_id"),
							vendor_add);
				}
				else
				{
					map.put(StringUtil.getStringValue(list.get(i), "vendor_id"),
							StringUtil.getStringValue(list.get(i), "vendor_name"));
				}
			}
		}
		return map;
	}

	/**
	 * 获取设备型号map
	 * 
	 * @return Map
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> getModelMap()
	{
		PrepareSQL psql = new PrepareSQL(
				"select device_model_id,device_model from stb_gw_device_model ");
		Map<String, String> map = null;
		List<Map<String, String>> list = jt.queryForList(psql.getSQL());
		if (null != list && list.size() > 0)
		{
			map = new HashMap<String, String>();
			for (int i = 0; i < list.size(); i++)
			{
				map.put(StringUtil.getStringValue(list.get(i), "device_model_id"),
						StringUtil.getStringValue(list.get(i), "device_model"));
			}
		}
		return map;
	}

	/**
	 * 获取软件版本map
	 * 
	 * @return Map
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> getSoftVersionMap()
	{
		PrepareSQL psql = new PrepareSQL(
				"select devicetype_id,softwareversion from stb_tab_devicetype_info ");
		Map<String, String> map = null;
		List<Map<String, String>> list = jt.queryForList(psql.getSQL());
		if (null != list && list.size() > 0)
		{
			map = new HashMap<String, String>();
			for (int i = 0; i < list.size(); i++)
			{
				map.put(StringUtil.getStringValue(list.get(i), "devicetype_id"),
						StringUtil.getStringValue(list.get(i), "softwareversion"));
			}
		}
		return map;
	}
}
