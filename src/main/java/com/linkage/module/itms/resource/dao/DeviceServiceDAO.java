
package com.linkage.module.itms.resource.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.linkage.commons.db.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.StringUtil;

/**
 * @author Administrator (Ailk No.)
 * @version 1.0
 * @since 2013-9-10
 * @category com.linkage.module.itms.resource.dao
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class DeviceServiceDAO extends SuperDAO
{

	private static Logger logger = LoggerFactory.getLogger(DeviceServiceDAO.class);
	private Map<String, String> vendorMap = new HashMap<String, String>();
	private Map<String, String> areaMap = new HashMap<String, String>();
	private Map<String, String> deviceTypeMap = new HashMap<String, String>();
	private Map<String, String> cityMap = new HashMap<String, String>();

	/**
	 * 厂商|设备型号|软件版本 列表
	 * 
	 * @return
	 */
	public Map<String, String> getVersionMap()
	{
		logger.debug("DeviceServiceDAO=>getVersionMap()");
		Map<String, String> versionMap = null;
		String sql = "select a.softwareversion,a.devicetype_id,b.device_model,c.vendor_id,a.hardwareversion "
				+ " from tab_devicetype_info a, gw_device_model b,tab_vendor_oui c "
				+ " where a.device_model_id=b.device_model_id and b.vendor_id=c.vendor_id";
		if (versionMap == null)
		{
			versionMap = new HashMap<String, String>();
			PrepareSQL psql = new PrepareSQL(sql);
			List<Map> list = new ArrayList<Map>();
			list = jt.queryForList(psql.getSQL());
			if (list.size() > 0)
			{
				for (int i = 0; i < list.size(); i++)
				{
					String version = StringUtil.getStringValue(list.get(i).get(
							"vendor_id"))
							+ "|"
							+ StringUtil.getStringValue(list.get(i).get("device_model"))
							+ "|"
							+ StringUtil.getStringValue(list.get(i)
									.get("softwareversion"))
							+ "|"
							+ StringUtil.getStringValue(list.get(i)
									.get("hardwareversion"));
					versionMap.put(
							StringUtil.getStringValue(list.get(i).get("devicetype_id")),
							version);
				}
			}
		}
		return versionMap;
	}

	/**
	 * 厂商列表
	 * 
	 * @return
	 */
	public Map<String, String> getVendorMap()
	{
		logger.debug("DeviceServiceDAO=>getVendorMap()");
		Map<String, String> vendorMap = null;
		if (vendorMap == null)
		{
			vendorMap = new HashMap<String, String>();

			String sql = "select * from tab_vendor order by vendor_add";
			// teledb
			if (DBUtil.GetDB() == 3) {
				sql = "select vendor_add, vendor_id, vendor_name from tab_vendor order by vendor_add";
			}
			PrepareSQL psql = new PrepareSQL(sql);
			List<Map> list = new ArrayList<Map>();
			list = jt.queryForList(psql.getSQL());
			if (list.size() > 0)
			{
				for (int i = 0; i < list.size(); i++)
				{
					String vendor_add = StringUtil.getStringValue(list.get(i).get(
							"vendor_add"));
					if (vendor_add != null && !"".equals(vendor_add))
					{
						vendorMap.put(
								StringUtil.getStringValue(list.get(i).get("vendor_id")),
								vendor_add + "(" + list.get(i).get("vendor_id") + ")");
					}
					else
					{
						vendorMap.put(
								StringUtil.getStringValue(list.get(i).get("vendor_id")),
								list.get(i).get("vendor_name") + "("
										+ list.get(i).get("vendor_id") + ")");
					}
				}
			}
		}
		return vendorMap;
	}

	/**
	 * 软件版本列表
	 * 
	 * @param gw_type
	 * @param city_id
	 * @return
	 */
	public List<String> getVendorList(String gw_type, String city_id)
	{
		logger.debug("DeviceServiceDAO=>getVendorList()");
		List<String> tempList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		sql.append("select count(device_id) as num, devicetype_id from tab_gw_device where device_status = 1");
		if (!StringUtil.IsEmpty(gw_type))
		{
			sql.append(" and gw_type=").append(gw_type);
		}
		if (!StringUtil.IsEmpty(city_id) && !"00".equals(city_id)
				&& !"-1".equals(city_id))
		{
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
			sql.append(" and city_id in (").append(StringUtils.weave(cityIdList))
					.append(")");
			cityIdList = null;
		}
		sql.append("group by devicetype_id ");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		List<Map> list = new ArrayList<Map>();
		list = jt.queryForList(psql.getSQL());
		for (int i = 0; i < list.size(); i++)
		{
			String num = StringUtil.getStringValue(list.get(i).get("num"));
			if (Integer.parseInt(num) > 0)
			{
				String devicetype_id = StringUtil.getStringValue(list.get(i).get(
						"devicetype_id"));
				tempList.add(devicetype_id);
			}
		}
		return tempList;
	}

	/**
	 * 将各地市的下级属地放入MAP中
	 * 
	 * @param cityList
	 */
	public Map getNextCityList(List cityList)
	{
		Map cityListMap = new HashMap();
		Iterator it = cityList.iterator();
		String city_id = "";
		while (it.hasNext())
		{
			city_id = (String) it.next();
			ArrayList cityAll = CityDAO.getAllNextCityIdsByCityPid(city_id);
			cityListMap.put(city_id, cityAll);
			cityAll = null;
		}
		return cityListMap;
	}

	/**
	 * 获取当前用户的下级属地
	 * 
	 * @param request
	 * @return
	 */
	public List<Map<String, String>> getCityList(String city_id)
	{
		String m_cityList = "select city_id,city_name from tab_city where parent_id='"
				+ city_id + "' or city_id='" + city_id + "' order by city_id";
		PrepareSQL psql = new PrepareSQL(m_cityList);
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		list = jt.queryForList(psql.getSQL());
		return list;
	}

	/**
	 * 版本信息列表
	 * 
	 * @param startOpenDate1
	 * @param endOpenDate1
	 * @param vendorId
	 * @param deviceTypeId
	 * @param gw_type
	 * @param city_id
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @return
	 */
	public List<Map> DeviceListByEdition(String startOpenDate1, String endOpenDate1,
			String vendorId, String deviceTypeId, String gw_type, String city_id,
			int curPage_splitPage, int num_splitPage)
	{
		logger.debug("DeviceServiceDAO=>DeviceListByEdition");
		StringBuffer sql = new StringBuffer();
		sql.append("select device_id,vendor_id,devicetype_id,zone_id,city_id,device_serialnumber from tab_gw_device a where a.device_status=1");
		if (!StringUtil.IsEmpty(gw_type))
		{
			sql.append(" and a.gw_type=").append(gw_type);
		}
		if (!StringUtil.IsEmpty(startOpenDate1))
		{
			sql.append(" and a.complete_time >=").append(startOpenDate1);
		}
		if (!StringUtil.IsEmpty(endOpenDate1))
		{
			sql.append(" and a.complete_time <=").append(endOpenDate1);
		}
		if (!StringUtil.IsEmpty(vendorId) && !"-1".equals(vendorId))
		{
			sql.append(" and a.vendor_id ='").append(vendorId).append("' ");
		}
		if (!StringUtil.IsEmpty(deviceTypeId) && !"-1".equals(deviceTypeId))
		{
			sql.append(" and a.devicetype_id=").append(deviceTypeId);
		}
		if ("00".equals(city_id))
		{
			sql.append("and city_id = '00' ");
		}
		else
		{
			if (!StringUtil.IsEmpty(city_id) && !"-1".equals(city_id))
			{
				List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
				sql.append(" and a.city_id in (").append(StringUtils.weave(cityIdList))
						.append(")");
				cityIdList = null;
			}
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
		// 厂商列表信息
		vendorMap = getVendorMap();
		// 地域ID映射表
		areaMap = getAreaIdMapName();
		// 版本映射信息表
		deviceTypeMap = getdeviceTypeMap();
		cityMap = Global.G_CityId_CityName_Map;
		List<Map> list = querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage
				+ 1, num_splitPage, new RowMapper()
		{

			@Override
			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				String vendor_id = StringUtil.getStringValue(rs.getString("vendor_id"));
				String devicetype_id = StringUtil.getStringValue(rs
						.getString("devicetype_id"));
				String devicemodel = "";
				String softwareversion = "";
				if (!devicetype_id.equals("-1"))
				{
					String tmp = StringUtil.getStringValue(deviceTypeMap
							.get(devicetype_id));
					if (tmp != "" && null != tmp)
					{
						String[] aa = tmp.split(",");
						devicemodel = aa[0];
						softwareversion = aa[1];
					}
				}
				map.put("device_id", StringUtil.getStringValue(rs.getString("device_id")));
				String cityid = StringUtil.getStringValue(rs.getString("city_id"));
				map.put("vendor", vendorMap.get(vendor_id));
				map.put("devicemodel", devicemodel);
				map.put("softwareversion", softwareversion);
				map.put("city_name", cityMap.get(cityid));
				map.put("device_serialnumber",
						StringUtil.getStringValue(rs.getString("device_serialnumber")));
				String area_id = StringUtil.getStringValue(rs.getString("zone_id"));
				map.put("area_name", areaMap.get(area_id));
				return map;
			}
		});
		return list;
	}

	/**
	 * 统计版本文件数量
	 * 
	 * @param startOpenDate1
	 * @param endOpenDate1
	 * @param vendorId
	 * @param deviceTypeId
	 * @param gw_type
	 * @param city_id
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @return
	 */
	public int countDeviceListByEdition(String startOpenDate1, String endOpenDate1,
			String vendorId, String deviceTypeId, String gw_type, String city_id,
			int curPage_splitPage, int num_splitPage)
	{
		logger.debug("DeviceServiceDAO=>countDeviceListByEdition");
		StringBuffer sql = new StringBuffer();
		// teledb
		if (DBUtil.GetDB() == 3) {
			sql.append("select count(*) from tab_gw_device a where a.device_status=1");
		}
		else {
			sql.append("select count(1) from tab_gw_device a where a.device_status=1");
		}
		if (!StringUtil.IsEmpty(gw_type))
		{
			sql.append(" and a.gw_type=").append(gw_type);
		}
		if (!StringUtil.IsEmpty(startOpenDate1))
		{
			sql.append(" and a.complete_time >=").append(startOpenDate1);
		}
		if (!StringUtil.IsEmpty(endOpenDate1))
		{
			sql.append(" and a.complete_time <=").append(endOpenDate1);
		}
		if (!StringUtil.IsEmpty(vendorId) && !"-1".equals(vendorId))
		{
			sql.append(" and a.vendor_id ='").append(vendorId).append("' ");
		}
		if (!StringUtil.IsEmpty(deviceTypeId) && !"-1".equals(deviceTypeId))
		{
			sql.append(" and a.devicetype_id=").append(deviceTypeId);
		}
		if ("00".equals(city_id))
		{
			sql.append("and city_id = '00' ");
		}
		else
		{
			if (!StringUtil.IsEmpty(city_id) && !"-1".equals(city_id))
			{
				List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
				sql.append(" and a.city_id in (").append(StringUtils.weave(cityIdList))
						.append(")");
				cityIdList = null;
			}
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
	 * 导出版本文件信息
	 * 
	 * @param startOpenDate1
	 * @param endOpenDate1
	 * @param vendorId
	 * @param deviceTypeId
	 * @param gw_type
	 * @param city_id
	 * @return
	 */
	public List<Map> excelDeviceListByEdition(String startOpenDate1, String endOpenDate1,
			String vendorId, String deviceTypeId, String gw_type, String city_id)
	{
		logger.debug("DeviceServiceDAO=>excelDeviceListByEdition()");
		StringBuffer sql = new StringBuffer();
		sql.append("select device_id,vendor_id,devicetype_id,zone_id,city_id,device_serialnumber from tab_gw_device a where a.device_status=1");
		if (!StringUtil.IsEmpty(gw_type))
		{
			sql.append(" and a.gw_type=").append(gw_type);
		}
		if (!StringUtil.IsEmpty(startOpenDate1))
		{
			sql.append(" and a.complete_time >=").append(startOpenDate1);
		}
		if (!StringUtil.IsEmpty(endOpenDate1))
		{
			sql.append(" and a.complete_time <=").append(endOpenDate1);
		}
		if (!StringUtil.IsEmpty(vendorId) && !"-1".equals(vendorId))
		{
			sql.append(" and a.vendor_id ='").append(vendorId).append("' ");
		}
		if (!StringUtil.IsEmpty(deviceTypeId) && !"-1".equals(deviceTypeId))
		{
			sql.append(" and a.devicetype_id=").append(deviceTypeId);
		}
		if ("00".equals(city_id))
		{
			sql.append("and city_id = '00' ");
		}
		else
		{
			if (!StringUtil.IsEmpty(city_id) && !"-1".equals(city_id))
			{
				List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
				sql.append(" and a.city_id in (").append(StringUtils.weave(cityIdList))
						.append(")");
				cityIdList = null;
			}
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
		// 厂商列表信息
		vendorMap = getVendorMap();
		// 地域ID映射表
		areaMap = getAreaIdMapName();
		// 版本映射信息表
		deviceTypeMap = getdeviceTypeMap();
		cityMap = Global.G_CityId_CityName_Map;
		List<Map> list = new ArrayList<Map>();
		list = jt.queryForList(psql.getSQL());
		if (list.size() > 0)
		{
			for (int i = 0; i < list.size(); i++)
			{
				String vendor_id = StringUtil
						.getStringValue(list.get(i).get("vendor_id"));
				String devicetype_id = StringUtil.getStringValue(list.get(i).get(
						"devicetype_id"));
				String devicemodel = "";
				String softwareversion = "";
				if (!devicetype_id.equals("-1"))
				{
					String tmp = StringUtil.getStringValue(deviceTypeMap
							.get(devicetype_id));
					if (tmp != "" && null != tmp)
					{
						String[] aa = tmp.split(",");
						devicemodel = aa[0];
						softwareversion = aa[1];
					}
				}
				list.get(i).put("device_id",
						StringUtil.getStringValue(list.get(i).get("device_id")));
				String cityid = StringUtil.getStringValue(list.get(i).get("city_id"));
				list.get(i).put("vendor", vendorMap.get(vendor_id));
				list.get(i).put("devicemodel", devicemodel);
				list.get(i).put("softwareversion", softwareversion);
				list.get(i).put("city_name", cityMap.get(cityid));
				list.get(i)
						.put("device_serialnumber",
								StringUtil.getStringValue(list.get(i).get(
										"device_serialnumber")));
				String area_id = StringUtil.getStringValue(list.get(i).get("zone_id"));
				list.get(i).put("area_name", areaMap.get(area_id));
			}
		}
		return list;
	}

	public Map<String, String> getdeviceTypeMap()
	{
		logger.debug("DeviceServiceDAO=>getdeviceTypeMap()");
		Map<String, String> deviceTypeMap = new HashMap<String, String>();
		PrepareSQL psql = new PrepareSQL(
				"select a.devicetype_id,a.softwareversion,b.device_model  from tab_devicetype_info a, gw_device_model b where a.device_model_id = b.device_model_id ");
		List<Map> list = new ArrayList<Map>();
		list = jt.queryForList(psql.getSQL());
		if (list.size() > 0)
		{
			for (int i = 0; i < list.size(); i++)
			{
				String tempValue = StringUtil.getStringValue(list.get(i).get(
						"softwareversion"))
						+ ","
						+ StringUtil.getStringValue(list.get(i).get("device_model"));
				deviceTypeMap.put(
						StringUtil.getStringValue(list.get(i).get("devicetype_id")),
						tempValue);
			}
		}
		return deviceTypeMap;
	}

	/**
	 * 获取域id与名称映射关系
	 * 
	 * @return
	 */
	public Map getAreaIdMapName()
	{
		logger.debug("DeviceServiceDAO=>getAreaIdMapName()");
		Map<String, String> map = new HashMap<String, String>();
		PrepareSQL psql = new PrepareSQL("select area_id,area_name from tab_area");
		List<Map> list = new ArrayList<Map>();
		list = jt.queryForList(psql.getSQL());
		if (list.size() > 0)
		{
			for (int i = 0; i < list.size(); i++)
			{
				map.put(StringUtil.getStringValue(list.get(i).get("area_id")),
						StringUtil.getStringValue(list.get(i).get("area_name")));
			}
		}
		return map;
	}

	/**
	 * @category getDevicetype 获取所有的设备型号
	 * @param vendorId
	 * @return List
	 */
	public Map<String, String> getDeviceModel()
	{
		logger.debug("DeviceServiceDAO=>getDeviceModel()");
		Map<String, String> modelMap = new HashMap<String, String>();
		PrepareSQL psql = new PrepareSQL(
				"select device_model_id,device_model from gw_device_model");
		List<Map> list = new ArrayList<Map>();
		list = jt.queryForList(psql.getSQL());
		if (list.size() > 0)
		{
			for (int i = 0; i < list.size(); i++)
			{
				modelMap.put(
						StringUtil.getStringValue(list.get(i).get("device_model_id")),
						StringUtil.getStringValue(list.get(i).get("device_model")));
			}
		}
		return modelMap;
	}

	/**
	 * 过滤信息查询方法
	 * 
	 * @param vendorId
	 * @param modelId
	 * @param deviceTypeId
	 * @param rela_dev_type_id
	 * @param edtionIdStr
	 * @return
	 */
	public Map<String, Map> getEditionIdNameMap(String vendorId, String modelId,
			String deviceTypeId, String rela_dev_type_id, String edtionIdStr)
	{
		StringBuffer bf = new StringBuffer();
		bf.append(" select distinct a.vendor_id,a.device_model_id,a.softwareversion from "
				+ "tab_devicetype_info a,gw_device_model b where a.device_model_id=b.device_model_id ");
		if (null != vendorId && !"null".equals(vendorId) && !"-1".equals(vendorId))
		{
			bf.append(" and a.vendor_id='" + vendorId + "' ");
		}
		if (null != modelId && !"null".equals(modelId) && !"-1".equals(modelId))
		{
			bf.append(" and b.device_model_id='" + modelId + "' ");
		}
		if (null != deviceTypeId && !"null".equals(deviceTypeId)
				&& !"-1".equals(deviceTypeId))
		{
			bf.append(" and a.devicetype_id=" + deviceTypeId + " ");
		}
		if (null != rela_dev_type_id && !"null".equals(rela_dev_type_id)
				&& !"-1".equals(rela_dev_type_id))
		{
			bf.append(" and a.rela_dev_type_id=" + rela_dev_type_id);
		}
		bf.append(" and a.devicetype_id  in " + edtionIdStr
				+ "  order by a.vendor_id,a.device_model_id ");
		PrepareSQL psql = new PrepareSQL(bf.toString());
		List<Map> list = new ArrayList<Map>();
		list = jt.queryForList(psql.getSQL());
		Map<String, Map> mapService = new HashMap<String, Map>();
		if (list.size() > 0)
		{
			for (int i = 0; i < list.size(); i++)
			{
				String vendor_id = StringUtil
						.getStringValue(list.get(i).get("vendor_id"));
				String device_model_id = StringUtil.getStringValue(list.get(i).get(
						"device_model_id"));
				String softwareversion = StringUtil.getStringValue(list.get(i).get(
						"softwareversion"));
				Map<String, List> modelMap = null;
				if (null == mapService.get(vendor_id))
				{
					modelMap = new HashMap<String, List>();
				}
				else
				{
					modelMap = (Map) mapService.get(vendor_id);
				}
				List<String> devicetypeList = null;
				if (null == modelMap.get(device_model_id))
				{
					devicetypeList = new ArrayList<String>();
				}
				else
				{
					devicetypeList = (List) modelMap.get(device_model_id);
				}
				devicetypeList.add(softwareversion);
				modelMap.put(device_model_id, devicetypeList);
				mapService.put(vendor_id, modelMap);
			}
		}
		return mapService;
	}

	/**
	 * 统计个地市版本文件信息
	 * 
	 * @param gw_type
	 * @param startTime
	 * @param endTime
	 * @param vendorId
	 * @param modelId
	 * @param softwareversion
	 * @param rela_dev_type_id
	 * @param edtionIdStr
	 * @return
	 */
	public List<Map> getServiceCityNum(String gw_type, String startTime, String endTime,
			String vendorId, String modelId, String softwareversion,
			String rela_dev_type_id, String edtionIdStr)
	{
		logger.debug("DeviceServiceDAO=>getServiceCityNum()", gw_type);
		StringBuffer sql = new StringBuffer();
		sql.append("select count(a.device_id) as num,t.softwareversion,a.city_id from tab_gw_device a,tab_devicetype_info t where a.devicetype_id=t.devicetype_id and a.device_status = 1 ");
		if (!StringUtil.IsEmpty(gw_type))
		{
			sql.append(" and a.gw_type=").append(gw_type);
		}
		if (null != startTime && !"".equals(startTime))
		{
			sql.append(" and a.complete_time>").append(startTime);
		}
		if (null != endTime && !"".equals(endTime))
		{
			sql.append(" and a.complete_time<").append(endTime);
		}
		if (null != vendorId && !"null".equals(vendorId) && !"-1".equals(vendorId))
		{
			sql.append(" and a.vendor_id='" + vendorId + "' ");
		}
		if (null != modelId && !"null".equals(modelId) && !"-1".equals(modelId))
		{
			sql.append(" and a.device_model_id='" + modelId + "' ");
		}
		if (null != softwareversion && !"null".equals(softwareversion)
				&& !"-1".equals(softwareversion))
		{
			sql.append(" and t.softwareversion='" + softwareversion + "'");
		}
		if (null != rela_dev_type_id && !"null".equals(rela_dev_type_id)
				&& !"-1".equals(rela_dev_type_id))
		{
			sql.append(" and t.rela_dev_type_id=" + rela_dev_type_id);
		}
		sql.append(" and a.devicetype_id  in " + edtionIdStr
				+ " group by a.city_id,t.softwareversion");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		List<Map> list = new ArrayList<Map>();
		list = jt.queryForList(psql.getSQL());
		return list;
	}

	/**
	 * @category getVendor 获取所有的厂商
	 * @return
	 */
	public Map<String, String> getVendor()
	{
		logger.debug("getVendor()");
		Map<String, String> map = new HashMap<String, String>();
		PrepareSQL pSQL = new PrepareSQL(
				"select vendor_id,vendor_name, vendor_add from tab_vendor");
		List<Map> list = new ArrayList<Map>();
		list = jt.queryForList(pSQL.getSQL());
		if (list.size() > 0)
		{
			for (int i = 0; i < list.size(); i++)
			{
				String tempValue = StringUtil.getStringValue(list.get(i)
						.get("vendor_add"))
						+ "("
						+ StringUtil.getStringValue(list.get(i).get("vendor_name")) + ")";
				map.put(StringUtil.getStringValue(list.get(i).get("vendor_id")),
						tempValue);
			}
		}
		return map;
	}

	/**
	 * 软件版本明细
	 * 
	 * @param vendorId
	 * @param modelId
	 * @param deviceTypeId
	 * @param rela_dev_type_id
	 * @param edtionIdStr
	 * @return
	 */
	public Map<String, ArrayList<String>> getEditionIdNameMapVendor(String vendorId,
			String modelId, String softwareversion, String rela_dev_type_id,
			String edtionIdStr)
	{
		StringBuffer bf = new StringBuffer();
		bf.append(" select distinct a.vendor_id,a.device_model_id,a.softwareversion,a.hardwareversion from "
				+ "tab_devicetype_info a,gw_device_model b where a.device_model_id=b.device_model_id ");
		if (null != vendorId && !"null".equals(vendorId) && !"-1".equals(vendorId))
		{
			bf.append(" and a.vendor_id='" + vendorId + "' ");
		}
		if (null != modelId && !"null".equals(modelId) && !"-1".equals(modelId))
		{
			bf.append(" and b.device_model_id='" + modelId + "' ");
		}
		if (!StringUtil.IsEmpty(softwareversion))
		{
			bf.append(" and a.softwareversion='" + softwareversion + "'");
		}
		if (null != rela_dev_type_id && !"null".equals(rela_dev_type_id)
				&& !"-1".equals(rela_dev_type_id))
		{
			bf.append(" and a.rela_dev_type_id=" + rela_dev_type_id);
		}
		bf.append(" and a.devicetype_id  in " + edtionIdStr
				+ "  order by a.softwareversion,a.hardwareversion ");
		PrepareSQL psql = new PrepareSQL(bf.toString());
		List<Map> list = new ArrayList<Map>();
		list = jt.queryForList(psql.getSQL());
		Map<String, ArrayList<String>> mapService = new HashMap<String, ArrayList<String>>();
		ArrayList<String> hwList = new ArrayList<String>();
		String sw = "";
		if (list.size() > 0)
		{
			sw = StringUtil.getStringValue(list.get(0).get("softwareversion"));
			for (int i = 0; i < list.size(); i++)
			{
				String hw = StringUtil.getStringValue(list.get(i).get("hardwareversion"));
				hwList.add(hw);
			}
		}	
		mapService.put(sw, hwList);
		return mapService;
	}

	public List<Map> getEditionIdNameMapVendorTotal(String gw_type, String startTime,
			String endTime, String vendorId, String modelId, String softwareversion,
			String rela_dev_type_id, String edtionIdStr)
	{
		logger.debug("DeviceServiceDAO=>getServiceCityNum()", gw_type);
		StringBuffer sql = new StringBuffer();
		sql.append("select count(a.device_id) as num,t.softwareversion,t.hardwareversion,a.city_id from tab_gw_device a,tab_devicetype_info t where a.devicetype_id=t.devicetype_id and a.device_status = 1 ");
		if (!StringUtil.IsEmpty(gw_type))
		{
			sql.append(" and a.gw_type=").append(gw_type);
		}
		if (null != startTime && !"".equals(startTime))
		{
			sql.append(" and a.complete_time>").append(startTime);
		}
		if (null != endTime && !"".equals(endTime))
		{
			sql.append(" and a.complete_time<").append(endTime);
		}
		if (null != vendorId && !"null".equals(vendorId) && !"-1".equals(vendorId))
		{
			sql.append(" and a.vendor_id='" + vendorId + "' ");
		}
		if (null != modelId && !"null".equals(modelId) && !"-1".equals(modelId))
		{
			sql.append(" and a.device_model_id='" + modelId + "' ");
		}
		if (null != softwareversion && !"null".equals(softwareversion)
				&& !"-1".equals(softwareversion))
		{
			sql.append(" and t.softwareversion='" + softwareversion + "'");
		}
		if (null != rela_dev_type_id && !"null".equals(rela_dev_type_id)
				&& !"-1".equals(rela_dev_type_id))
		{
			sql.append(" and t.rela_dev_type_id=" + rela_dev_type_id);
		}
		sql.append(" and a.devicetype_id  in " + edtionIdStr
				+ " group by a.city_id,t.softwareversion,t.hardwareversion");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		List<Map> list = new ArrayList<Map>();
		list = jt.queryForList(psql.getSQL());
		return list;
	}

	public List<Map> deviceVendorList(String startOpenDate1, String endOpenDate1,
			String vendorId, String softwareversion, String hardwareversion,
			String gw_type, String city_id, int curPage_splitPage, int num_splitPage)
	{
		logger.debug("DeviceServiceDAO=>excelDeviceListByEdition()");
		StringBuffer sql = new StringBuffer();
		sql.append("select a.device_id,a.vendor_id,a.devicetype_id,a.zone_id,a.city_id,a.device_serialnumber from tab_gw_device a,tab_devicetype_info t where a.device_status=1 and a.devicetype_id=t.devicetype_id");
		if (!StringUtil.IsEmpty(gw_type))
		{
			sql.append(" and a.gw_type=").append(gw_type);
		}
		if (!StringUtil.IsEmpty(startOpenDate1))
		{
			sql.append(" and a.complete_time >=").append(startOpenDate1);
		}
		if (!StringUtil.IsEmpty(endOpenDate1))
		{
			sql.append(" and a.complete_time <=").append(endOpenDate1);
		}
		if (!StringUtil.IsEmpty(vendorId) && !"-1".equals(vendorId))
		{
			sql.append(" and a.vendor_id ='").append(vendorId).append("' ");
		}
		if (!StringUtil.IsEmpty(softwareversion))
		{
			sql.append(" and t.softwareversion='").append(softwareversion).append("' ");
		}
		if (!StringUtil.IsEmpty(hardwareversion))
		{
			sql.append(" and t.hardwareversion='").append(hardwareversion).append("' ");
		}
		if ("00".equals(city_id))
		{
			sql.append("and city_id = '00' ");
		}
		else
		{
			if (!StringUtil.IsEmpty(city_id) && !"-1".equals(city_id))
			{
				List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
				sql.append(" and a.city_id in (").append(StringUtils.weave(cityIdList))
						.append(")");
				cityIdList = null;
			}
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
		// 厂商列表信息
		vendorMap = getVendorMap();
		// 地域ID映射表
		areaMap = getAreaIdMapName();
		// 版本映射信息表
		deviceTypeMap = getdeviceTypeMap();
		cityMap = Global.G_CityId_CityName_Map;
		List<Map> list = new ArrayList<Map>();
		list = querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage + 1,
				num_splitPage, new RowMapper()
				{

					@Override
					public Object mapRow(ResultSet rs, int arg1) throws SQLException
					{
						Map<String, String> map = new HashMap<String, String>();
						String vendor_id = StringUtil.getStringValue(rs
								.getString("vendor_id"));
						String devicetype_id = StringUtil.getStringValue(rs
								.getString("devicetype_id"));
						String devicemodel = "";
						String softwareversion = "";
						if (!devicetype_id.equals("-1"))
						{
							String tmp = StringUtil.getStringValue(deviceTypeMap
									.get(devicetype_id));
							if (tmp != "" && null != tmp)
							{
								String[] aa = tmp.split(",");
								softwareversion = aa[0];
								devicemodel = aa[1];
							}
						}
						map.put("device_id",
								StringUtil.getStringValue(rs.getString("device_id")));
						String cityid = StringUtil.getStringValue(rs.getString("city_id"));
						map.put("vendor", vendorMap.get(vendor_id));
						map.put("devicemodel", devicemodel);
						map.put("softwareversion", softwareversion);
						map.put("city_name", cityMap.get(cityid));
						map.put("device_serialnumber", StringUtil.getStringValue(rs
								.getString("device_serialnumber")));
						String area_id = StringUtil.getStringValue(rs
								.getString("zone_id"));
						map.put("area_name", areaMap.get(area_id));
						return map;
					}
				});
		return list;
	}

	public int countDeviceVendorList(String startOpenDate1, String endOpenDate1,
			String vendorId, String softwareversion, String hardwareversion,
			String gw_type, String city_id, int curPage_splitPage, int num_splitPage)
	{
		logger.debug("DeviceServiceDAO=>countDeviceListByEdition");
		StringBuffer sql = new StringBuffer();
		// teledb
		if (DBUtil.GetDB() == 3) {
			sql.append("select count(*) from tab_gw_device a,tab_devicetype_info t where a.device_status=1 and a.devicetype_id=t.devicetype_id");
		}
		else {
			sql.append("select count(1) from tab_gw_device a,tab_devicetype_info t where a.device_status=1 and a.devicetype_id=t.devicetype_id");
		}
		if (!StringUtil.IsEmpty(gw_type))
		{
			sql.append(" and a.gw_type=").append(gw_type);
		}
		if (!StringUtil.IsEmpty(startOpenDate1))
		{
			sql.append(" and a.complete_time >=").append(startOpenDate1);
		}
		if (!StringUtil.IsEmpty(endOpenDate1))
		{
			sql.append(" and a.complete_time <=").append(endOpenDate1);
		}
		if (!StringUtil.IsEmpty(vendorId) && !"-1".equals(vendorId))
		{
			sql.append(" and a.vendor_id ='").append(vendorId).append("' ");
		}
		if (!StringUtil.IsEmpty(softwareversion))
		{
			sql.append(" and t.softwareversion='").append(softwareversion).append("' ");
		}
		if (!StringUtil.IsEmpty(hardwareversion))
		{
			sql.append(" and t.hardwareversion='").append(hardwareversion).append("' ");
		}
		if ("00".equals(city_id))
		{
			sql.append("and city_id = '00' ");
		}
		else
		{
			if (!StringUtil.IsEmpty(city_id) && !"-1".equals(city_id))
			{
				List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
				sql.append(" and a.city_id in (").append(StringUtils.weave(cityIdList))
						.append(")");
				cityIdList = null;
			}
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

	public List<Map> excelDeviceVendorList(String startOpenDate1, String endOpenDate1,
			String vendorId, String softwareversion, String hardwareversion,
			String gw_type, String city_id)
	{
		logger.debug("DeviceServiceDAO=>excelDeviceListByEdition()");
		StringBuffer sql = new StringBuffer();
		sql.append("select a.device_id,a.vendor_id,a.devicetype_id,a.zone_id,a.city_id,a.device_serialnumber from tab_gw_device a,tab_devicetype_info t where a.device_status=1 and a.devicetype_id=t.devicetype_id");
		if (!StringUtil.IsEmpty(gw_type))
		{
			sql.append(" and a.gw_type=").append(gw_type);
		}
		if (!StringUtil.IsEmpty(startOpenDate1))
		{
			sql.append(" and a.complete_time >=").append(startOpenDate1);
		}
		if (!StringUtil.IsEmpty(endOpenDate1))
		{
			sql.append(" and a.complete_time <=").append(endOpenDate1);
		}
		if (!StringUtil.IsEmpty(vendorId) && !"-1".equals(vendorId))
		{
			sql.append(" and a.vendor_id ='").append(vendorId).append("' ");
		}
		if (!StringUtil.IsEmpty(softwareversion))
		{
			sql.append(" and t.softwareversion='").append(softwareversion).append("' ");
		}
		if (!StringUtil.IsEmpty(hardwareversion))
		{
			sql.append(" and t.hardwareversion='").append(hardwareversion).append("' ");
		}
		if ("00".equals(city_id))
		{
			sql.append("and city_id = '00' ");
		}
		else
		{
			if (!StringUtil.IsEmpty(city_id) && !"-1".equals(city_id))
			{
				List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
				sql.append(" and a.city_id in (").append(StringUtils.weave(cityIdList))
						.append(")");
				cityIdList = null;
			}
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
		// 厂商列表信息
		vendorMap = getVendorMap();
		// 地域ID映射表
		areaMap = getAreaIdMapName();
		// 版本映射信息表
		deviceTypeMap = getdeviceTypeMap();
		cityMap = Global.G_CityId_CityName_Map;
		List<Map> list = new ArrayList<Map>();
		list = jt.queryForList(psql.getSQL());
		if (list.size() > 0)
		{
			for (int i = 0; i < list.size(); i++)
			{
				String vendor_id = StringUtil
						.getStringValue(list.get(i).get("vendor_id"));
				String devicetype_id = StringUtil.getStringValue(list.get(i).get(
						"devicetype_id"));
				String devicemodel = "";
				String sw = "";
				if (!devicetype_id.equals("-1"))
				{
					String tmp = StringUtil.getStringValue(deviceTypeMap
							.get(devicetype_id));
					if (tmp != "" && null != tmp)
					{
						String[] aa = tmp.split(",");
						sw = aa[0];
						devicemodel = aa[1];
					}
				}
				list.get(i).put("device_id",
						StringUtil.getStringValue(list.get(i).get("device_id")));
				String cityid = StringUtil.getStringValue(list.get(i).get("city_id"));
				list.get(i).put("vendor", vendorMap.get(vendor_id));
				list.get(i).put("devicemodel", devicemodel);
				list.get(i).put("softwareversion", sw);
				list.get(i).put("city_name", cityMap.get(cityid));
				list.get(i)
						.put("device_serialnumber",
								StringUtil.getStringValue(list.get(i).get(
										"device_serialnumber")));
				String area_id = StringUtil.getStringValue(list.get(i).get("zone_id"));
				list.get(i).put("area_name", areaMap.get(area_id));
			}
		}
		return list;
	}
}
