
package com.linkage.module.bbms.report.bio;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.bbms.report.dao.PortFluxQueryDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

@SuppressWarnings("unchecked")
public class PortFluxQueryBIO
{

	private PortFluxQueryDAO portFluxQueryDao;
	private String message;
	/**
	 * Map<city_id,city_name>
	 */
	private Map<String, String> cityMap = null;

	/**
	 * @author wangsenbo
	 * @date Mar 19, 2010
	 * @param
	 * @return List<Map>
	 */
	public List<Map> portFluxQuery(String reportType, String stat_time, String queryType,
			String device_serialnumber, String loopbackIp, String userName,
			String customerName, String linkphone, String cityId)
	{
		String tableName = "";
		String starttime = "";
		String endtime = "";
		DateTimeUtil dt = new DateTimeUtil(stat_time);
		if ("day".equals(reportType))
		{
			tableName = "flux_day_stat_" + dt.getYear() + "_" + dt.getMonth();
			starttime = StringUtil.getStringValue(dt.getLongTime());
			endtime = StringUtil.getStringValue(dt.getLongTime() + (24 * 60 * 60));
		}
		if ("week".equals(reportType))
		{
			tableName = "flux_week_stat_" + dt.getYear();
			String starttime1 = dt.getFirstDayOfWeek("Am");
			dt = new DateTimeUtil(starttime1);
			starttime = StringUtil.getStringValue(dt.getLongTime());
			String endtime1 = dt.getLastDayOfWeek("Am");
			dt = new DateTimeUtil(endtime1);
			endtime = StringUtil.getStringValue(dt.getLongTime() + (24 * 60 * 60));
		}
		if ("month".equals(reportType))
		{
			tableName = "flux_month_stat_" + dt.getYear();
			String starttime1 = dt.getFirtDayOfMonth();
			dt = new DateTimeUtil(starttime1);
			starttime = StringUtil.getStringValue(dt.getLongTime());
			String endtime1 = dt.getLastDayOfMonth();
			dt = new DateTimeUtil(endtime1);
			endtime = StringUtil.getStringValue(dt.getLongTime() + (24 * 60 * 60));
		}
		List list = new ArrayList();
		List<Map> deviceList = new ArrayList<Map>();
		List<Map> userList = new ArrayList<Map>();
		List<String> deviceIdList = new ArrayList<String>();
		int ret = portFluxQueryDao.isHaveTable(tableName);
		if (ret == 0)
		{
			message = "没有相关报表信息！";
			return list;
		}
		if ("device".equals(queryType))
		{
			deviceList = portFluxQueryDao.getDevice(tableName, cityId,
					device_serialnumber, loopbackIp, starttime, endtime, null);
			if (deviceList.size() > 0)
			{
				for (Map map : deviceList)
				{
					deviceIdList.add(StringUtil.getStringValue(map.get("device_id")));
				}
				userList = portFluxQueryDao.getUser(cityId, null, null, null,
						deviceIdList);
			}
			else
			{
				message = "设备不存在！";
				return list;
			}
		}
		if ("user".equals(queryType))
		{
			userList = portFluxQueryDao.getUser(cityId, userName, null, null, null);
			if (userList.size() > 0)
			{
				for (Map map : userList)
				{
					deviceIdList.add(StringUtil.getStringValue(map.get("device_id")));
				}
				deviceList = portFluxQueryDao
						.getDevice(tableName, cityId, device_serialnumber, loopbackIp,
								starttime, endtime, deviceIdList);
			}
			else
			{
				message = "用户不存在！";
				return list;
			}
		}
		if ("customer".equals(queryType))
		{
			userList = portFluxQueryDao.getUser(cityId, null, customerName, linkphone,
					null);
			if (userList.size() > 0)
			{
				for (Map map : userList)
				{
					deviceIdList.add(StringUtil.getStringValue(map.get("device_id")));
				}
				deviceList = portFluxQueryDao
						.getDevice(tableName, cityId, device_serialnumber, loopbackIp,
								starttime, endtime, deviceIdList);
			}
			else
			{
				message = "客户或用户不存在！";
				return list;
			}
		}
		list = getReport(deviceList, userList);
		return list;
	}

	/**
	 * @author wangsenbo
	 * @date Mar 19, 2010
	 * @param
	 * @return List
	 */
	private List getReport(List<Map> deviceList, List<Map> userList)
	{
		List<Map> list = new ArrayList<Map>();
		if (deviceList.size() > 0)
		{
			cityMap = CityDAO.getCityIdCityNameMap();
			Map<Object, Map> userMap = new HashMap<Object, Map>();
			for (Map map : userList)
			{
				userMap.put(map.get("device_id"), map);
			}
			for (Map map3 : deviceList)
			{
				Map rmap = new HashMap();
				String city_id = StringUtil.getStringValue(map3.get("city_id"));
				String city_name = StringUtil.getStringValue(cityMap.get(city_id));
				if (false == StringUtil.IsEmpty(city_name))
				{
					rmap.put("city_name", city_name);
				}
				else
				{
					rmap.put("city_name", "");
				}
				rmap.put("device_serialnumber", map3.get("device_serialnumber"));
				rmap.put("loopback_ip", map3.get("loopback_ip"));
				rmap.put("port_info", map3.get("port_info"));
				double avg_count = StringUtil.getDoubleValue(map3, "avg_count");
				NumberFormat nf = NumberFormat.getPercentInstance();
				nf.setMinimumFractionDigits(2);
				String str = nf.format(avg_count);
				rmap.put("avg_count", str);
				double max_count = StringUtil.getDoubleValue(map3, "max_count");
				String str2 = nf.format(max_count);
				rmap.put("max_count", str2);
				if (userList.size() > 0)
				{
					rmap.put("username", userMap.get(map3.get("device_id")).get(
							"username"));
					rmap.put("customer_name", userMap.get(map3.get("device_id")).get(
							"customer_name"));
				}
				list.add(rmap);
			}
		}
		else
		{
			message = "用户未绑定设备！";
		}
		return list;
	}

	/**
	 * @return the portFluxQueryDao
	 */
	public PortFluxQueryDAO getPortFluxQueryDao()
	{
		return portFluxQueryDao;
	}

	/**
	 * @param portFluxQueryDao
	 *            the portFluxQueryDao to set
	 */
	public void setPortFluxQueryDao(PortFluxQueryDAO portFluxQueryDao)
	{
		this.portFluxQueryDao = portFluxQueryDao;
	}

	/**
	 * @return the message
	 */
	public String getMessage()
	{
		return message;
	}

	/**
	 * @param message
	 *            the message to set
	 */
	public void setMessage(String message)
	{
		this.message = message;
	}
}
