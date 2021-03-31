
package com.linkage.module.gwms.resource.act;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.LipossGlobals;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.resource.bio.UpdateDevCityByIPBIO;
import com.linkage.module.gwms.util.StringUtil;
import com.opensymphony.xwork2.ActionSupport;

@SuppressWarnings("unchecked")
public class UpdateDevCityByIPACT extends ActionSupport implements SessionAware
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// 日志记录
	private static Logger logger = LoggerFactory
			.getLogger(UpdateDevCityByIPACT.class);
	/** 属地列表集合 */
	private List<Map<String, String>> cityList = new ArrayList<Map<String, String>>();
	/** cityMap */
	private static Map<String, String> cityMap = new HashMap<String, String>();
	private List deviceList;
	private String deviceSN = null;
	private String message;
	// session
	private Map session;
	private String loopbackIp;
	private String deviceId;
	private String deviceCityId;
	private String isBind;
	private String IpCityId;
	private UpdateDevCityByIPBIO updateDevCityByIPBio;
	private String instArea;
	static
	{
		UpdateDevCityByIPBIO bio = new UpdateDevCityByIPBIO();
		cityMap = bio.getSencondCityIdCityNameMap();
	}
	private String gw_type;

	public String getGw_type() {
		return gw_type;
	}

	public void setGw_type(String gw_type) {
		this.gw_type = gw_type;
	}
	private String newCityId;
	
	private String updeviceId;

	/**
	 * @author wangsenbo
	 * @date Apr 14, 2010
	 * @param
	 * @return String
	 */
	public String getDeviceInfo()
	{
		logger.debug("getDeviceInfo()");
		UserRes curUser = (UserRes) session.get("curUser");
		if(curUser == null)
		{
			logger.warn("用户未登录");
			deviceList = null;
			return "device";
		}
		String cityId = curUser.getCityId();
		
		logger.warn(curUser.getUser().getAccount() + ": updateDevCityByIPBio.queryDevice : " + deviceSN);
		
		instArea = Global.instAreaShortName;
		if(!StringUtil.IsEmpty(deviceSN) && deviceSN.length()<6)
		{
			logger.warn("设备序列号少于6");
			deviceList = null;
			return "device";
		}
		deviceList = updateDevCityByIPBio.queryDevice(deviceSN, loopbackIp, cityId, gw_type);
		//返回的List有可能为Null，不能直接使用方法 .fixed by zhangcong@ 2011-07-06
		if (null == deviceList || deviceList.isEmpty())
		{
			deviceList = null;
		}
		return "device";
	}

	/**
	 * 根据设备IP的属地修改设备属地
	 * 
	 * @author wangsenbo
	 * @date Apr 15, 2010
	 * @param
	 * @return String
	 */
	public String updateCity()
	{
		logger.debug("updateCity()");
		message = updateDevCityByIPBio.updateCityByIP(deviceId, deviceCityId, IpCityId,
				isBind);
		return "result";
	}

	/**
	 * 
	 * @author 岩 
	 * @date 2016-9-22
	 * @return
	 */
	public String getAhDeviceInfo()
	{
		logger.debug("getAhDeviceInfo()");
		UserRes curUser = (UserRes) session.get("curUser");
		if(curUser == null)
		{
			logger.warn("用户未登录");
			deviceList = null;
			return "ahdevice";
		}
		String cityId = curUser.getCityId();
		if ("00".equals(cityId))
		{
			cityList = CityDAO.getNextCityListByCityPid(cityId);
		}
		else
		{
			// 获取用户所在属地对应的二级属地 (南京 宿迁之类的)
			Map<String, String> map = new HashMap<String, String>();
			for (Map.Entry<String, String> entry : cityMap.entrySet())
			{
				if (CityDAO.getCityName(cityId).contains(entry.getValue()))
				{
					map.put("city_id", entry.getKey());
					map.put("city_name", entry.getValue());
					cityList.add(map);
					break;
				}
			}
		}
		
		logger.warn(curUser.getUser().getAccount() + ": updateDevCityByIPBio.queryAhDevice : " + deviceSN);
		
		instArea = Global.instAreaShortName;
		if(!StringUtil.IsEmpty(deviceSN) && deviceSN.length()<6)
		{
			logger.warn("设备序列号少于6");
			deviceList = null;
			return "ahdevice";
		}
		deviceList = updateDevCityByIPBio.queryAhDevice(deviceSN, loopbackIp, cityId );
		if (deviceList.size()>1){
			return "ahdevice";
		}else if (deviceList.size()==1){
			return "ahresult";
		}
		//返回的List有可能为Null，不能直接使用方法 .fixed by zhangcong@ 2011-07-06
		if (null == deviceList || deviceList.isEmpty())
		{
			logger.warn("设备为空");
			deviceList = null;
		}
		return "ahdevice";
	}
	
	public String getAhDeviceInfoByDev()
	{
		logger.debug("updateAhCity()");
		UserRes curUser = (UserRes) session.get("curUser");
		if(curUser == null)
		{
			logger.warn("用户未登录");
			deviceList = null;
			return "ahdevice";
		}
		String cityId = curUser.getCityId();
		if ("00".equals(cityId))
		{
			cityList = CityDAO.getNextCityListByCityPid(cityId);
		}
		else
		{
			// 获取用户所在属地对应的二级属地 (南京 宿迁之类的)
			Map<String, String> map = new HashMap<String, String>();
			for (Map.Entry<String, String> entry : cityMap.entrySet())
			{
				if (CityDAO.getCityName(cityId).contains(entry.getValue()))
				{
					map.put("city_id", entry.getKey());
					map.put("city_name", entry.getValue());
					cityList.add(map);
					break;
				}
			}
		}
		deviceList = updateDevCityByIPBio.getAhDeviceInfoByDev(updeviceId);
		
		return "ahresult";
	}
	
	/**
	 * 
	 * @author 岩 
	 * @date 2016-9-28
	 * @return
	 */
	public String updateAhCity()
	{
		logger.debug("updateAhCity()");
		message = updateDevCityByIPBio.updateAhCity(deviceId, newCityId);
		return "ahupresult";
	}

	
	/**
	 * @return the deviceList
	 */
	public List getDeviceList()
	{
		return deviceList;
	}

	/**
	 * @param deviceList
	 *            the deviceList to set
	 */
	public void setDeviceList(List deviceList)
	{
		this.deviceList = deviceList;
	}

	/**
	 * @return the deviceSN
	 */
	public String getDeviceSN()
	{
		return deviceSN;
	}

	/**
	 * @param deviceSN
	 *            the deviceSN to set
	 */
	public void setDeviceSN(String deviceSN)
	{
		this.deviceSN = deviceSN;
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

	/**
	 * @return the session
	 */
	public Map getSession()
	{
		return session;
	}

	/**
	 * @param session
	 *            the session to set
	 */
	public void setSession(Map session)
	{
		this.session = session;
	}

	/**
	 * @return the loopbackIp
	 */
	public String getLoopbackIp()
	{
		return loopbackIp;
	}

	/**
	 * @param loopbackIp
	 *            the loopbackIp to set
	 */
	public void setLoopbackIp(String loopbackIp)
	{
		this.loopbackIp = loopbackIp;
	}

	/**
	 * @return the updateDevCityByIPBio
	 */
	public UpdateDevCityByIPBIO getUpdateDevCityByIPBio()
	{
		return updateDevCityByIPBio;
	}

	/**
	 * @param updateDevCityByIPBio
	 *            the updateDevCityByIPBio to set
	 */
	public void setUpdateDevCityByIPBio(UpdateDevCityByIPBIO updateDevCityByIPBio)
	{
		this.updateDevCityByIPBio = updateDevCityByIPBio;
	}

	/**
	 * @return the deviceId
	 */
	public String getDeviceId()
	{
		return deviceId;
	}

	/**
	 * @param deviceId
	 *            the deviceId to set
	 */
	public void setDeviceId(String deviceId)
	{
		this.deviceId = deviceId;
	}

	/**
	 * @return the deviceCityId
	 */
	public String getDeviceCityId()
	{
		return deviceCityId;
	}

	/**
	 * @param deviceCityId
	 *            the deviceCityId to set
	 */
	public void setDeviceCityId(String deviceCityId)
	{
		this.deviceCityId = deviceCityId;
	}

	/**
	 * @return the isBind
	 */
	public String getIsBind()
	{
		return isBind;
	}

	/**
	 * @param isBind
	 *            the isBind to set
	 */
	public void setIsBind(String isBind)
	{
		this.isBind = isBind;
	}

	/**
	 * @return the ipCityId
	 */
	public String getIpCityId()
	{
		return IpCityId;
	}

	/**
	 * @param ipCityId
	 *            the ipCityId to set
	 */
	public void setIpCityId(String ipCityId)
	{
		IpCityId = ipCityId;
	}

	/**
	 * @return the instArea
	 */
	public String getInstArea()
	{
		return instArea;
	}

	/**
	 * @param instArea
	 *            the instArea to set
	 */
	public void setInstArea(String instArea)
	{
		this.instArea = instArea;
	}

	
	public List<Map<String, String>> getCityList()
	{
		return cityList;
	}

	
	public void setCityList(List<Map<String, String>> cityList)
	{
		this.cityList = cityList;
	}

	
	public static Map<String, String> getCityMap()
	{
		return cityMap;
	}

	
	public static void setCityMap(Map<String, String> cityMap)
	{
		UpdateDevCityByIPACT.cityMap = cityMap;
	}

	
	public String getNewCityId()
	{
		return newCityId;
	}

	
	public void setNewCityId(String newCityId)
	{
		this.newCityId = newCityId;
	}

	
	public String getUpdeviceId()
	{
		return updeviceId;
	}

	
	public void setUpdeviceId(String updeviceId)
	{
		this.updeviceId = updeviceId;
	}
	
	
}
