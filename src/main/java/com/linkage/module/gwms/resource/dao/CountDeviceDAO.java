
package com.linkage.module.gwms.resource.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.report.obj.DevicetypeChildOBJ;
import com.linkage.module.gwms.report.obj.DevicetypeNewestFindReportOBJ;
import com.linkage.module.gwms.util.StringUtil;

/**
 * @author wuchao(工号) Tel:1
 * @version 1.0
 * @since 2012-3-6 上午09:15:42
 * @category com.linkage.module.gwms.resource.dao
 * @copyright 南京联创科技 网管科技部
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class CountDeviceDAO extends SuperDAO
{
	private static Logger logger = LoggerFactory.getLogger(CountDeviceDAO.class);

	public List<Map> queryCityName(String cityId)
	{
		String m_cityList = "select city_id,city_name from tab_city " +
							"where parent_id=? or city_id=? order by city_id";
		PrepareSQL psql = new PrepareSQL(m_cityList);
		psql.setString(1, cityId);
		psql.setString(2, cityId);
		List<Map> cityList = new ArrayList<Map>();
		cityList = jt.queryForList(psql.getSQL());
		return cityList;
	}

	public List<String> filterDevice(String cityId, String vendor, String devicemodel,
			String devicetype, String protocol2, String protocol1, String protocol0)
	{
		List<String> serverType = new ArrayList<String>();
		
		if (null != protocol2 && !"".equals(protocol2) && !"undefined".equals(protocol2))
		{
			serverType.add(protocol2);
		}
		if (null != protocol1 && !"".equals(protocol1) && !"undefined".equals(protocol1))
		{
			serverType.add(protocol1);
		}
		if (null != protocol0 && !"".equals(protocol0) && !"undefined".equals(protocol0))
		{
			serverType.add(protocol0);
		}
		StringBuffer filterSql = new StringBuffer();
		PrepareSQL psqlFilter = new PrepareSQL();
		if (serverType.size() == 0)
		{
			filterSql.append("select count(device_id) as num,devicetype_id " +
					"from tab_gw_device where device_status=1 and gw_type=1");
			if (null != vendor && !"-1".equals(vendor))
			{
				filterSql.append(" and vendor_id='");
				filterSql.append(vendor);
				filterSql.append("'");
			}
			if (null != devicemodel && !"-1".equals(devicemodel))
			{
				filterSql.append(" and device_model_id='");
				filterSql.append(devicemodel);
				filterSql.append("'");
			}
			if (null != devicetype && !"-1".equals(devicetype))
			{
				filterSql.append(" and devicetype_id=");
				filterSql.append(devicetype);
			}
			if (!StringUtil.IsEmpty(cityId) && !"00".equals(cityId)) {
				filterSql.append(" and city_id in(?)");
			}
			filterSql.append(" group by devicetype_id");
			psqlFilter.append(filterSql.toString());
			if (!StringUtil.IsEmpty(cityId) && !"00".equals(cityId))
			{
			psqlFilter.setStringExt(1, StringUtils.weave(CityDAO
					.getAllNextCityIdsByCityPid(cityId)), false);
			}
		}
		else
		{
			filterSql.append("select count(a.device_id) as num,a.devicetype_id " +
					"from tab_gw_device a,tab_devicetype_info_servertype s " +
					"where a.devicetype_id=s.devicetype_id and a.device_status = 1 and a.gw_type=1 ");
			if (null != vendor && !"-1".equals(vendor))
			{
				filterSql.append(" and a.vendor_id='");
				filterSql.append(vendor);
				filterSql.append("'");
			}
			if (null != devicemodel && !"-1".equals(devicemodel))
			{
				filterSql.append(" and a.device_model_id='");
				filterSql.append(devicemodel);
				filterSql.append("'");
			}
			if (null != devicetype && !"-1".equals(devicetype))
			{
				filterSql.append(" and a.devicetype_id=");
				filterSql.append(devicetype);
			}
			if (!StringUtil.IsEmpty(cityId) && !"00".equals(cityId))
			{
				filterSql.append(" and a.city_id in(?)");
			}
			filterSql.append(" and s.server_type in(?)");
			filterSql.append(" group by a.devicetype_id");
			psqlFilter.append(filterSql.toString());
			if (!StringUtil.IsEmpty(cityId) && !"00".equals(cityId))
			{
				psqlFilter.setStringExt(1, StringUtils.weave(CityDAO
						.getAllNextCityIdsByCityPid(cityId)), false);
				psqlFilter.setStringExt(2, StringUtils.weaveNumber(serverType), false);
			}else {
				psqlFilter.setStringExt(1, StringUtils.weaveNumber(serverType), false);
			}
		}
		
		List<Map> resultList = new ArrayList<Map>();
		resultList = jt.queryForList(psqlFilter.getSQL());
		List<String> devicetypeList = new ArrayList<String>();
		if (resultList.size() > 0)
		{
			for (int i = 0; i < resultList.size(); i++)
			{
				if (StringUtil.getIntegerValue(resultList.get(i).get("num")) > 100)
				{
					devicetypeList.add(StringUtil.getStringValue(resultList.get(i).get(
							"devicetype_id")));
				}
			}
		}
		return devicetypeList;
	}

	public List<Map> getDevicetype(List<String> list, String is_check,
			String rela_dev_type, String startTime, String endTime)
	{
	/**	if(DBUtil.GetDB()==3){
			//TODO wait
		}else{
			
		} */
		StringBuffer bf = new StringBuffer();
		bf.append("select a.vendor_id,c.vendor_add,b.device_model,a.devicetype_id," +
				"a.softwareversion,a.is_check,a.add_time,a.hardwareversion " +
				"from tab_devicetype_info a,gw_device_model b, tab_vendor c " +
				"where a.device_model_id=b.device_model_id and  a.vendor_id=c.vendor_id ");
		// "and a.add_time>1330531200 and a.add_time<1333036799 
		//	order by  a.vendor_id,device_model,devicetype_id ")
		// bf.append(" select a.vendor_id,b.device_model,a.devicetype_id from "
		// +
		// "tab_devicetype_info a,gw_device_model b where a.device_model_id=b.device_model_id ");
		if (null != rela_dev_type && !"null".equals(rela_dev_type)
				&& !"-1".equals(rela_dev_type))
		{
			bf.append(" and a.rela_dev_type_id=" + rela_dev_type);
		}
		if (null != is_check && !"null".equals(is_check) && !"-1".equals(is_check))
		{
			bf.append(" and a.is_check=" + is_check);
		}
		if (null != startTime && !"".equals(startTime))
		{
			bf.append(" and a.add_time>");
			bf.append(formatTime(startTime));
		}
		if (null != endTime && !"".equals(endTime))
		{
			bf.append(" and a.add_time<");
			bf.append(formatTime(endTime));
		}
		bf.append(" and a.devicetype_id in(?) order by a.vendor_id,b.device_model,a.devicetype_id ");
		PrepareSQL psql = new PrepareSQL(bf.toString());
		if (null==list || list.size() == 0)
		{
			return null;
		}
		else
		{
			psql.setStringExt(1, StringUtils.weaveNumber(list), false);
		}
		return jt.queryForList(psql.getSQL());
	}

	public List getData(List<Map> devicetypeList, List cityTempList, String userCityId)
	{
		logger.debug("getData()");
		if (null ==devicetypeList ||  devicetypeList.size() == 0)
		{
			return null;
		}
		List rsList = new ArrayList();
		// 取得所有版本的详细信息
		// List devicetypeList = dao.getDevicetype(startTime,endTime);
		// 取得有协议版本的协议
		List<Map> servertypeList = getServertype(devicetypeList);
		// 取出版本ID
		List<String> typeList = new ArrayList<String>();
		Map<String, Map> mapService = new HashMap<String, Map>();
		for (int i = 0; i < devicetypeList.size(); i++)
		{
			String temp_vendor_id = String.valueOf(
					((Map) devicetypeList.get(i)).get("vendor_id")).toString();
			String temp_vendor_name = String.valueOf(
					((Map) devicetypeList.get(i)).get("vendor_add")).toString();
			String temp_device_model = String.valueOf(
					((Map) devicetypeList.get(i)).get("device_model")).toString();
			String temp_devicetype_id = String.valueOf(
					((Map) devicetypeList.get(i)).get("devicetype_id")).toString();
			String temp_softwareversion = String.valueOf(
					((Map) devicetypeList.get(i)).get("softwareversion")).toString();
			String temp_hardwareversion = String.valueOf(
					((Map) devicetypeList.get(i)).get("hardwareversion")).toString();
			String is_check = String.valueOf(
					((Map) devicetypeList.get(i)).get("is_check")).toString();
			String temp_is_check = " ";
			if ("1".equals(is_check))
			{
				temp_is_check = "是";
			}
			if ("0".equals(is_check))
			{
				temp_is_check = "否";
			}
			String temp_add_time = " ";
			String add_time = String.valueOf(
					((Map) devicetypeList.get(i)).get("add_time")).toString();
			if (null != add_time && !"".equals(add_time))
			{
				temp_add_time = getTime(add_time);
			}
			String serverName = "";
			for (int j = 0; j < servertypeList.size(); j++)
			{
				String devicetype_id = StringUtil.getStringValue(servertypeList.get(j)
						.get("devicetype_id"));
				if (temp_devicetype_id.equals(devicetype_id))
				{
					String temp_server_type = StringUtil.getStringValue(servertypeList
							.get(j).get("server_type"));
					if (temp_server_type.equals("2"))
					{
						serverName = serverName + "H248,";
					}
					if (temp_server_type.equals("1"))
					{
						serverName = serverName + "软交换SIP,";
					}
					if (temp_server_type.equals("0"))
					{
						serverName = serverName + "IMS SIP,";
					}
				}
			}
			if (null != serverName && !"".equals(serverName))
			{
				serverName = serverName.substring(0, serverName.length() - 1);
			}
			String temp_vendor_id_name = temp_vendor_name + "|" + temp_vendor_id;
			typeList.add(temp_devicetype_id);
			Map<String, List> modelMap = null;
			if (null == mapService.get(temp_vendor_id_name))
			{
				modelMap = new HashMap<String, List>();
			}
			else
			{
				modelMap = (Map) mapService.get(temp_vendor_id_name);
			}
			List<String> tempdevicetypeList = null;
			if (null == modelMap.get(temp_device_model))
			{
				tempdevicetypeList = new ArrayList<String>();
			}
			else
			{
				tempdevicetypeList = (List) modelMap.get(temp_device_model);
			}
			tempdevicetypeList.add(temp_devicetype_id + "|" + temp_softwareversion + "("
					+ temp_hardwareversion + ")" + "|" + serverName + "|" + temp_add_time
					+ "|" + temp_is_check);
			modelMap.put(temp_device_model, tempdevicetypeList);
			mapService.put(temp_vendor_id_name, modelMap);
		}
		// 根据属地查询版本对应的数目
		List deviceList = getdeviceNumByType(typeList);
		// 处理数据
		Map<String, Integer> deviceMap = new HashMap<String, Integer>();
		if (null != deviceList)
		{
			for (int i = 0; i < deviceList.size(); i++)
			{
				Map tempMap = (Map) deviceList.get(i);
				String temp_devicetype_id = StringUtil.getStringValue(tempMap
						.get("devicetype_id"));
				String temp_city_id = StringUtil.getStringValue(tempMap.get("city_id"));
				int temp_num = StringUtil.getIntegerValue(tempMap.get("num"));
				deviceMap.put(temp_devicetype_id + "_" + temp_city_id, temp_num);
			}
		}
		Iterator itVender = mapService.keySet().iterator();
		while (itVender.hasNext())
		{
			String key = (String) itVender.next();
			Map valueMap = (Map) mapService.get(key);
			long venderSize = 0;
			List childList = new ArrayList();
			Iterator itDevicetype = valueMap.keySet().iterator();
			while (itDevicetype.hasNext())
			{
				String keyModel = (String) itDevicetype.next();
				List tempDevicetypeList = (List) valueMap.get(keyModel);
				venderSize = venderSize + tempDevicetypeList.size();
				DevicetypeChildOBJ typeObj = new DevicetypeChildOBJ();
				typeObj.setDevice_model(keyModel);
				typeObj.setChildInt(tempDevicetypeList.size());
				List num = new ArrayList();
				for (int i = 0; i < tempDevicetypeList.size(); i++)
				{
					// devicetypeId + softwarename
					// 该版本的所有数量
					long total = 0;
					String temp_softwareversion[] = ((String) tempDevicetypeList.get(i))
							.split("\\|");
					List<String> one = new ArrayList<String>();
					one.add(temp_softwareversion[1]);
					one.add(temp_softwareversion[2]);
					one.add(temp_softwareversion[3]);
					one.add(temp_softwareversion[4]);
					for (int j = 0; j < cityTempList.size(); j++)
					{
						String tempCityId = String.valueOf(
								((Map) cityTempList.get(j)).get("city_id")).toString();
						// 页面显示的数目
						long deviceNum = 0;
						if (userCityId.equals(tempCityId))
						{
							deviceNum = StringUtil.getIntegerValue(deviceMap
									.get(temp_softwareversion[0] + "_" + tempCityId));
							if (deviceNum == 0)
							{
								one.add("0");
							}
							else
							{
								one.add("<a href=\"javascript:detail('"
										+ temp_softwareversion[0] + "','" + tempCityId
										+ "')\">" + deviceNum + "</a>");
								total = total + deviceNum;
							}
						}
						else
						{
							ArrayList titleCityIdlist = CityDAO
									.getAllNextCityIdsByCityPid(tempCityId);
							String titleTmpCity = "";
							String titleTmp = "";
							long titleTmpValue = 0;
							long titleTotal = 0;
							Iterator titleCityId = titleCityIdlist.listIterator();
							while (titleCityId.hasNext())
							{
								titleTmpCity = (String) titleCityId.next();
								titleTmp = String
										.valueOf(deviceMap.get(temp_softwareversion[0]
												+ "_" + titleTmpCity));
								if (null != titleTmp && !"".equals(titleTmp)
										&& !"null".equals(titleTmp))
								{
									titleTmpValue = Long.parseLong(titleTmp);
									titleTotal += titleTmpValue;
								}
							}
							deviceNum = StringUtil.getLongValue(titleTotal);
							if (deviceNum == 0)
							{
								one.add("0");
							}
							else
							{
								one.add("<a href=\"javascript:detail('"
										+ temp_softwareversion[0] + "','" + tempCityId
										+ "')\">" + deviceNum + "</a>");
								total = total + deviceNum;
							}
						}
					}
					if (total > 0)
					{
						one.add("<a href='javascript:detail(" + temp_softwareversion[0]
								+ ",-1)'>" + total + "</a>");
					}
					else
					{
						one.add("0");
					}
					num.add(one);
				}
				typeObj.setNum(num);
				childList.add(typeObj);
			}
			DevicetypeNewestFindReportOBJ vendorObj = new DevicetypeNewestFindReportOBJ();
			// key 为厂商名字|厂商id
			vendorObj.setVendor_name(key.split("\\|")[0]);
			// 厂商下所有的版本数
			vendorObj.setChildInt(venderSize);
			// 厂商下 所有的型号对象
			vendorObj.setChildList(childList);
			rsList.add(vendorObj);
		}
		return rsList;
	}

	/**
	 * 根据属地查询指定版本的终端数
	 * 
	 * @param typeList
	 * @return
	 */
	public List getdeviceNumByType(List<String> typeList)
	{
		logger.debug("getdeviceNumByType(typeList:{})", typeList);
		StringBuffer sql = new StringBuffer();
		sql
				.append("select count(device_id) as num,devicetype_id,city_id from tab_gw_device");
		if (null == typeList || typeList.size() < 1)
		{
			return null;
		}
		else
		{
			sql.append(" where devicetype_id in (");
			for (String type : typeList)
			{
				sql.append(type);
				sql.append(",");
			}
			sql.deleteCharAt(sql.length() - 1);
			sql.append(") ");
		}
		sql.append(" and device_status = 1 and gw_type=1 ");
		sql.append(" group by devicetype_id,city_id order by city_id ");
		logger.debug("getdeviceNumByType=>sql:{}", sql);
		PrepareSQL psql = new PrepareSQL(sql.toString());
		return jt.queryForList(psql.getSQL());
	}

	public String formatTime(String time)
	{
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try
		{
			return dateFormat.parse(time).getTime() / 1000 + "";
		}
		catch (ParseException e)
		{
			return "";
		}
	}

	public String getTime(String mills)
	{
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(StringUtil.getLongValue(mills + "000"));
		String date = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
		return date;
	}

	public List<Map> getServertype(List<Map> devicetypeList)
	{
		List<String> devicetypeIdList = new ArrayList<String>();
		for (int i = 0; i < devicetypeList.size(); i++)
		{
			devicetypeIdList.add(StringUtil.getStringValue(devicetypeList.get(i).get(
					"devicetype_id")));
		}
		String sql = "select devicetype_id,server_type from tab_devicetype_info_servertype where devicetype_id in(?)";
		PrepareSQL psql = new PrepareSQL(sql.toString());
		if (devicetypeIdList.size() == 0)
		{
			return null;
			// psql.setStringExt(1,"0", false);
		}
		else
		{
			psql.setStringExt(1, StringUtils.weaveNumber(devicetypeIdList), false);
		}
		return jt.queryForList(psql.getSQL());
	}

	public List<Map> queryDetail(String userCityId, String devicetype, String cityId,
			int curPage_splitPage, int num_splitPage)
	{
	/**	if(DBUtil.GetDB()==3){
			//TODO wait
		}else{
			
		} */
		StringBuffer sql = new StringBuffer();
		sql.append("select a.device_id,a.device_serialnumber,t.softwareversion," +
				"m.device_model,v.vendor_add,c.city_name " +
				"from tab_gw_device a,tab_devicetype_info t,gw_device_model m,tab_vendor v,tab_city c " +
				"where a.device_status=1 and a.gw_type=1 and t.vendor_id=v.vendor_id and "
				+ " a.city_id=c.city_id and a.devicetype_id=t.devicetype_id " +
				"and t.device_model_id=m.device_model_id and a.devicetype_id=? ");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		psql.setInt(1, StringUtil.getIntegerValue(devicetype));
		if (!StringUtil.IsEmpty(cityId) && !"00".equals(cityId))
		{
			sql.append("and a.city_id in(?)");
			
			if ("-1".equals(cityId))
			{
				psql.setStringExt(2, StringUtils.weave(CityDAO
						.getAllNextCityIdsByCityPid(userCityId)), false);
			}
			if (userCityId.equals(cityId))
			{
				psql.setStringExt(2, userCityId, true);
				logger.debug("cityId:" + cityId);
			}
			if (!userCityId.equals(cityId) && !"-1".equals(cityId))
			{
				psql.setStringExt(2, StringUtils.weave(CityDAO
						.getAllNextCityIdsByCityPid(cityId)), false);
			}
		}
		List<Map> list = querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage
				+ 1, num_splitPage, new RowMapper()
		{

			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				map.put("city_name", rs.getString("city_name"));
				map.put("device_serialnumber", rs.getString("device_serialnumber"));
				map.put("vendor_add", rs.getString("vendor_add"));
				map.put("device_model", rs.getString("device_model"));
				map.put("softwareversion", rs.getString("softwareversion"));
				map.put("device_id", rs.getString("device_id"));
				return map;
			}
		});
		return list;
	}

	public int getCountDevice(String userCityId, String devicetype, String cityId,
			int curPage_splitPage, int num_splitPage)
	{
		StringBuffer sql = new StringBuffer();
		if(DBUtil.GetDB()==3){
			//TODO wait
			sql.append("select count(*) ");
		}else{
			sql.append("select count(1) ");
		}
		sql.append("from tab_gw_device a,tab_devicetype_info t,gw_device_model m,tab_vendor v,tab_city c " +
				"where a.device_status=1 and a.gw_type=1 and t.vendor_id=v.vendor_id "
				+ "and a.city_id=c.city_id and a.devicetype_id=t.devicetype_id " +
				"and t.device_model_id=m.device_model_id and a.devicetype_id=? ");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		psql.setInt(1, StringUtil.getIntegerValue(devicetype));
		
		 if (!StringUtil.IsEmpty(cityId) && !"00".equals(cityId))
		{
			sql.append(" and a.city_id in(?)"); 
		if ("-1".equals(cityId))
		{
			psql.setStringExt(2, StringUtils.weave(CityDAO
					.getAllNextCityIdsByCityPid(userCityId)), false);
		}
		if (userCityId.equals(cityId))
		{
			psql.setStringExt(2, userCityId, true);
			logger.debug("cityId:" + cityId);
		}
		if (!userCityId.equals(cityId) && !"-1".equals(cityId))
		{
			psql.setStringExt(2, StringUtils.weave(CityDAO
					.getAllNextCityIdsByCityPid(cityId)), false);
		}
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
		return maxPage;
	}

	public List<Map> queryDetailForExcel(String userCityId, String devicetype,
			String cityId)
	{
		StringBuffer sql = new StringBuffer();
	/**	if(DBUtil.GetDB()==3){
			//TODO wait
		}else{
			
		} */
		sql.append("select a.device_id,a.device_serialnumber,t.softwareversion," +
				"m.device_model,v.vendor_add,c.city_name " +
				"from tab_gw_device a,tab_devicetype_info t,gw_device_model m,tab_vendor v,tab_city c " +
				"where a.device_status=1 and a.gw_type=1 and t.vendor_id=v.vendor_id "
				+ "and a.city_id=c.city_id and a.devicetype_id=t.devicetype_id " +
				"and t.device_model_id=m.device_model_id and a.devicetype_id =?");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		psql.setInt(1, StringUtil.getIntegerValue(devicetype));
		if (!StringUtil.IsEmpty(cityId) && !"00".equals(cityId))
		{
			sql.append(" and a.city_id in(?)");
		
			if ("-1".equals(cityId))
			{
				psql.setStringExt(2, StringUtils.weave(CityDAO
						.getAllNextCityIdsByCityPid(userCityId)), false);
			}
			if (userCityId.equals(cityId))
			{
				psql.setStringExt(2, userCityId, true);
			}
			if (!userCityId.equals(cityId) && !"-1".equals(cityId))
			{
				psql.setStringExt(2, StringUtils.weave(CityDAO
						.getAllNextCityIdsByCityPid(cityId)), false);
			}
		}
		List<Map> list = jt.queryForList(psql.getSQL());
		return list;
	}

	public List<Map> queryDetailAll(String userCityId, String filterDevicetype,
			String cityId, int curPage_splitPage, int num_splitPage)
	{
	/**	if(DBUtil.GetDB()==3){
			//TODO wait
		}else{
			
		} */
       String sql="select a.city_id,a.device_id,a.device_serialnumber," +
       		"t.softwareversion,m.device_model,v.vendor_add " +
       		"from gw_device_model m,tab_vendor v,tab_gw_device a " +
       		"left join tab_devicetype_info t on a.devicetype_id=t.devicetype_id " +
			"where a.device_status=1 and a.gw_type=1 and t.vendor_id=v.vendor_id " +
			"and t.device_model_id=m.device_model_id and a.devicetype_id in("+ filterDevicetype+")";
		PrepareSQL psql = new PrepareSQL(sql.toString());
		if (userCityId.equals(cityId))
		{
			psql.append(" and a.city_id='" + userCityId + "'");
			psql.setStringExt(1, StringUtils.weave(CityDAO
					.getAllNextCityIdsByCityPid(cityId)), false);
		}
		
			
		if (!"-1".equals(cityId) && !userCityId.equals(cityId))
		{
			if (!StringUtil.IsEmpty(cityId) && !"00".equals(cityId))
			{
				psql.append(" and a.city_id in(?)");
				psql.setStringExt(1, StringUtils.weave(CityDAO
						.getAllNextCityIdsByCityPid(cityId)), false);
			}
		}
	final Map<String,String>cityMap = CityDAO.getCityIdCityNameMap();
		List<Map> list = querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage
				+ 1, num_splitPage, new RowMapper()
		{

			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				map.put("city_name",cityMap.get(rs.getString("city_id")));
				map.put("device_serialnumber", rs.getString("device_serialnumber"));
				map.put("vendor_add", rs.getString("vendor_add"));
				map.put("device_model", rs.getString("device_model"));
				map.put("softwareversion", rs.getString("softwareversion"));
				map.put("device_id", rs.getString("device_id"));
				return map;
			}
		});
		return list;
	}

	public int queryDetailAllCount(String userCityId, String filterDevicetype,
			String cityId, int curPage_splitPage, int num_splitPage)
	{
		String sql;
		if(DBUtil.GetDB()==3){
			//TODO wait
			sql="select count(*) ";
		}else{
			sql="select count(1) ";
		}
		sql+="from gw_device_model m,tab_vendor v,tab_gw_device a " +
			"left join tab_devicetype_info t on a.devicetype_id=t.devicetype_id " +
			"where a.device_status=1 and a.gw_type=1 and t.vendor_id=v.vendor_id " +
			"and t.device_model_id=m.device_model_id and a.devicetype_id in("+ filterDevicetype+")";
		PrepareSQL psql = new PrepareSQL(sql.toString());
		if (userCityId.equals(cityId))
		{
			psql.append(" and a.city_id='" + userCityId + "'");
			psql.setStringExt(1, StringUtils.weave(CityDAO
					.getAllNextCityIdsByCityPid(cityId)), false);
		}
		if (!"-1".equals(cityId) && !userCityId.equals(cityId))
		{
			if (!StringUtil.IsEmpty(cityId) && !"00".equals(cityId))
			{
				psql.append(" and a.city_id in(?)");
				psql.setStringExt(1, StringUtils.weave(CityDAO
						.getAllNextCityIdsByCityPid(cityId)), false);
			}
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
		return maxPage;
	}

	public List<Map> queryDetailForExcelAll(String userCityId, String filterDevicetype,
			String cityId)
	{
	/**	if(DBUtil.GetDB()==3){
			//TODO wait
		}else{
			
		} */
		String sql="select a.city_id,a.device_id,a.device_serialnumber," +
				"t.softwareversion,m.device_model,v.vendor_add " +
				"from gw_device_model m,tab_vendor v,tab_gw_device a " +
				"left join tab_devicetype_info t on a.devicetype_id=t.devicetype_id " +
				"where a.device_status=1 and a.gw_type=1 and t.vendor_id=v.vendor_id " +
				"and t.device_model_id=m.device_model_id and a.devicetype_id in("+ filterDevicetype+")";
		PrepareSQL psql = new PrepareSQL(sql.toString());
		if (userCityId.equals(cityId))
		{
			psql.append(" and a.city_id='" + userCityId + "'");
			psql.setStringExt(1, StringUtils.weave(CityDAO
					.getAllNextCityIdsByCityPid(cityId)), false);
		}
		if (!"-1".equals(cityId) && !userCityId.equals(cityId))
		{
			if (!StringUtil.IsEmpty(cityId) && !"00".equals(cityId))
			{
				psql.append(" and a.city_id in(?)");
				psql.setStringExt(1, StringUtils.weave(CityDAO
						.getAllNextCityIdsByCityPid(cityId)), false);
			}
		}
		
		List<Map> list = jt.queryForList(psql.getSQL());
		 Map<String,String>cityMap = CityDAO.getCityIdCityNameMap();
		 for(int i=0;i<list.size();i++){
			 list.get(i).put("city_name", cityMap.get(list.get(i).get("city_id")));
		 }
		return list;
	}
}
