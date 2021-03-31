/**
 * 
 */

package com.linkage.module.gwms.resource.bio;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.resource.dao.UpdateDevCityByIPDAO;
import com.linkage.module.gwms.util.StringUtil;

@SuppressWarnings("unchecked")
public class UpdateDevCityByIPBIO
{

	/** log */
	private static Logger logger = LoggerFactory
			.getLogger(UpdateDevCityByIPBIO.class);
	private UpdateDevCityByIPDAO updateDevCityByIPDao;
	/**
	 * Map<city_id,city_name>
	 */
	private Map<String, String> cityMap = null;

	/**
	 * flag : 0 属地不要修正 1 属地要修正 2 IP错误，设备没上报 3 IP错误，IP没有对应属地
	 * manage : 0 不可以管理 1 可以管理 
	 * @author wangsenbo
	 * @date Apr 13, 2010
	 * @param
	 * @return List
	 */
	public List queryDevice(String device_serialnumber, String loopbackIp, String cityId, String gw_type)
	{
		logger.debug("queryDevice({},{})", device_serialnumber, loopbackIp);
		if (true == StringUtil.IsEmpty(device_serialnumber)
				&& true == StringUtil.IsEmpty(loopbackIp))
		{
			logger.warn("UpdateDevCityByIPBIO==>queryDevice()设备序列号和IP都为空");
			return null;
		}
		List<String> citylist = CityDAO.getAllNextCityIdsByCityPid(cityId);
		List<String> citylist1 = CityDAO.getAllPcityIdByCityId(cityId);
		citylist.addAll(citylist1);
		List<Map> list = updateDevCityByIPDao
				.queryDevice(device_serialnumber, loopbackIp, gw_type);
		List<Map> deviceList = new ArrayList<Map>();
		cityMap = CityDAO.getCityIdCityNameMap();
		for (Map map : list)
		{
			String city_id = StringUtil.getStringValue(map.get("city_id"));
			String city_name = StringUtil.getStringValue(cityMap.get(city_id));
			if (false == StringUtil.IsEmpty(city_name))
			{
				map.put("city_name", city_name);
			}
			else
			{
				map.put("city_name", "");
			}
			String fillIP = null;
			String IpCityId = null;
			String ip = StringUtil.getStringValue(map.get("loopback_ip"));
			if (ip != null)
			{
				fillIP = getFillIP(ip);
				if (fillIP != null)
				{
					IpCityId = updateDevCityByIPDao.getCityIdByIP(fillIP);
					String devbendiCityId = CityDAO.getLocationCityIdByCityId(city_id);
					if ("0000".equals(IpCityId))
					{
						map.put("flag", 3);
					}
					else
					{
						String IPbendiCityId = CityDAO
								.getLocationCityIdByCityId(IpCityId);
						if (IPbendiCityId.equals(devbendiCityId))
						{
							map.put("flag", 0);
						}
						else
						{
							if ("00".equals(IPbendiCityId))
							{
								map.put("flag", 0);
							}
							else
							{
								map.put("flag", 1);
							}
						}
						map.put("IpCity_id", IPbendiCityId);
					}
				}
				else
				{
					map.put("IpCityId", "");
					map.put("flag", 2);
				}
			}
			else
			{
				logger.error("设备没有IP");
				map.put("IpCityId", "");
				map.put("flag", 2);
			}
			Boolean bool = false;
			map.put("manage", "0");
			// 设备属地被管理
			for (String string : citylist)
			{
				if (string.equals(city_id))
				{
					bool = true;
				}
			}
			if (bool)
			{
				map.put("manage", "1");
			}
			else
			{
				bool = false;
				for (String string : citylist)
				{
					if (string.equals(IpCityId))
					{
						bool = true;
					}
				}
				if (bool)
				{
					map.put("manage", "1");
				}
			}
			deviceList.add(map);
		}
		return deviceList;
	}

	public List queryAhDevice(String device_serialnumber, String loopbackIp, String cityId )
	{
		logger.debug("queryDevice({},{})", device_serialnumber, loopbackIp);
		if (true == StringUtil.IsEmpty(device_serialnumber)
				&& true == StringUtil.IsEmpty(loopbackIp))
		{
			logger.warn("UpdateDevCityByIPBIO==>queryAhDevice()设备序列号和IP都为空");
			return null;
		}
		
		List<String> citylist = CityDAO.getAllNextCityIdsByCityPid(cityId);
		List<String> citylist1 = CityDAO.getAllPcityIdByCityId(cityId);
		citylist.addAll(citylist1);
		List<Map> list = updateDevCityByIPDao
				.queryAhDevice(device_serialnumber, loopbackIp );
		List<Map> deviceList = new ArrayList<Map>();
		cityMap = CityDAO.getCityIdCityNameMap();
		for (Map map : list)
		{
			String city_id = StringUtil.getStringValue(map.get("city_id"));
			String city_name = StringUtil.getStringValue(cityMap.get(city_id));
			if (false == StringUtil.IsEmpty(city_name))
			{
				map.put("city_name", city_name);
			}
			else
			{
				map.put("city_name", "");
			}

			deviceList.add(map);
		}
		return deviceList;
	}
	
	/**
	 * 获取全IP,如果参数为192.168.1.1 返回192168001001
	 * 
	 * @param ip
	 * @return
	 */
	public static String getFillIP(String ip)
	{
		logger.debug("getFillIP({})", ip);
		String fillIP = ip;
		String[] ipArray = new String[4];
		ipArray = ip.split("\\.");
		if (ipArray.length != 4)
		{
			return null;
		}
		for (int i = 0; i < 4; i++)
		{
			if (ipArray[i].length() == 1)
			{
				ipArray[i] = "00" + ipArray[i];
			}
			else if (ipArray[i].length() == 2)
			{
				ipArray[i] = "0" + ipArray[i];
			}
		}
		fillIP = ipArray[0] + ipArray[1] + ipArray[2] + ipArray[3];
		return fillIP;
	}

	/**
	 * @return the updateDevCityByIPDao
	 */
	public UpdateDevCityByIPDAO getUpdateDevCityByIPDao()
	{
		return updateDevCityByIPDao;
	}

	/**
	 * @param updateDevCityByIPDao
	 *            the updateDevCityByIPDao to set
	 */
	public void setUpdateDevCityByIPDao(UpdateDevCityByIPDAO updateDevCityByIPDao)
	{
		this.updateDevCityByIPDao = updateDevCityByIPDao;
	}

	/**
	 * @author wangsenbo
	 * @date Apr 15, 2010
	 * @param
	 * @return String
	 */
	public String updateCityByIP(String deviceId, String deviceCityId, String ipCityId,
			String isBind)
	{
		StringBuffer sbSQL = new StringBuffer();
		if (false == StringUtil.IsEmpty(ipCityId))
		{
			if ("1".equals(isBind))
			{// 绑定了用户，要解绑
				sbSQL.append(updateDevCityByIPDao.release(deviceId));
				sbSQL.append(";");
			}
			sbSQL.append(updateDevCityByIPDao.updateCityByIP(deviceId, ipCityId));
			try
			{
				@SuppressWarnings("unused")
				int[] rs = updateDevCityByIPDao.batchUpdate(sbSQL.toString().split(";"));
				return "属地修正成功";
			}
			catch (Exception e)
			{
				return "属地修正失败";
			}
		}
		else
		{
			return "错误：IP对应属地为空";
		}
	}
	
	/**
	 * 
	 * @author 岩 
	 * @date 2016-9-28
	 * @param deviceId
	 * @param deviceCityId
	 * @param ipCityId
	 * @param isBind
	 * @return
	 */
	public String updateAhCity(String deviceId, String newCityId )
	{
		StringBuffer sbSQL = new StringBuffer();
		if (false == StringUtil.IsEmpty(newCityId))
		{
//			if ("1".equals(isBind))
//			{// 绑定了用户，要解绑
//				sbSQL.append(updateDevCityByIPDao.release(deviceId));
//				sbSQL.append(";");
//			}
			sbSQL.append(updateDevCityByIPDao.updateCityByIP(deviceId, newCityId));
			try
			{
				@SuppressWarnings("unused")
				int[] rs = updateDevCityByIPDao.batchUpdate(sbSQL.toString().split(";"));
				return "属地修正成功";
			}
			catch (Exception e)
			{
				return "属地修正失败";
			}
		}
		else
		{
			return "错误：IP对应属地为空";
		}
	}
	
	public Map<String, String> getSencondCityIdCityNameMap(){		
		String strSQL = "select city_id, city_name from tab_city where parent_id = '00' order by city_id";
		PrepareSQL psql = new PrepareSQL(strSQL);
    	psql.getSQL();
		Map map = DataSetBean.getMap(strSQL);
		Map<String, String> resultMap = new ConcurrentHashMap<String, String>();
		resultMap.putAll(map); 
		return resultMap;
	}
	
	public List<Map> getAhDeviceInfoByDev(String deviceId )
	{
		
		List<Map> list = updateDevCityByIPDao.getAhDeviceInfoByDev(deviceId);
		List<Map> deviceList = new ArrayList<Map>();
		cityMap = CityDAO.getCityIdCityNameMap();
		for (Map map : list)
		{
			String city_id = StringUtil.getStringValue(map.get("city_id"));
			String city_name = StringUtil.getStringValue(cityMap.get(city_id));
			if (false == StringUtil.IsEmpty(city_name))
			{
				map.put("city_name", city_name);
			}
			else
			{
				map.put("city_name", "");
			}
			deviceList.add(map);
		}
		return deviceList;
	}
}
