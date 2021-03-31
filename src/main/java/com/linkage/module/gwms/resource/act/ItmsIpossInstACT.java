
package com.linkage.module.gwms.resource.act;

import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.resource.bio.UserInstReleaseBIO;
import com.linkage.module.gwms.util.StringUtil;
import com.opensymphony.xwork2.ActionSupport;

@SuppressWarnings("unchecked")
public class ItmsIpossInstACT extends ActionSupport implements SessionAware
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(ItmsIpossInstACT.class);
	private UserInstReleaseBIO userInstReleaseBio;
	// session
	private Map session;
	private List userList;
	private String username;
	private String dealstaff;
	private String city_id;
	private String device_serialnumber;
	private List deviceList = null;
	private String message;
	private String deviceCityId;
	private String deviceNo;
	private String userId;
	private String oui;
	private String deviceId;
	private String oldDeviceId;
	private String cityName;
	private String faultId;
	private String dealstaffid;
	private String loopbackIp;
	private String gw_type ="1";

	/**
	 * itmsIpossInst页面初始化
	 * 
	 * @author wangsenbo
	 * @date Nov 11, 2009
	 * @return String
	 */
	public String instInit()
	{
		logger.debug("instInit()");
		userList = userInstReleaseBio.queryUser(gw_type, city_id, username, null,null, "all");
		if (userList.isEmpty() == true)
		{
			userList = null;
			Map<String, String> cityMap = CityDAO.getCityIdCityNameMap();
			cityName = cityMap.get(city_id);
			cityMap = null;
		}
		else
		{
			if (userList.size() == 1)
			{
				Map<String, String> map = (Map) userList.get(0);
				String deviceId = map.get("device_id");
				if (false == StringUtil.IsEmpty(deviceId))
				{
					deviceList = userInstReleaseBio.getDeviceInfo(deviceId);
				}
				else
				{
					deviceList = null;
				}
			}
		}
		return "inst";
	}

	/**
	 * itmsIpossRelease页面初始化
	 * 
	 * @author wangsenbo
	 * @date Nov 12, 2009
	 * @return String
	 */
	public String modifyInstInit()
	{
		logger.debug("modifyInstInit()");
		userList = userInstReleaseBio.queryUser(gw_type,city_id, username, null, null, "all");
		if (userList.isEmpty() == true)
		{
			userList = null;
			Map<String, String> cityMap = CityDAO.getCityIdCityNameMap();
			cityName = cityMap.get(city_id);
			cityMap = null;
		}
		else
		{
			if (userList.size() == 1)
			{
				Map<String, String> map = (Map) userList.get(0);
				String deviceId = map.get("device_id");
				if (false == StringUtil.IsEmpty(deviceId))
				{
					deviceList = userInstReleaseBio.getDeviceInfo(deviceId);
				}
				else
				{
					deviceList = null;
				}
			}
		}
		return "modifyInst";
	}

	/**
	 * itmsIposs查询设备信息 绑定
	 * 
	 * @author wangsenbo
	 * @date Nov 12, 2009
	 * @return String
	 */
	public String getDeviceInfo()
	{
		logger.debug("getDeviceInfo()");
		if (null == device_serialnumber || "".equals(device_serialnumber) || "null".equals(device_serialnumber) || StringUtil.IsEmpty(device_serialnumber)) {
			return "device";
		}
		deviceList = userInstReleaseBio.queryDevice(device_serialnumber, city_id,
				loopbackIp);
		if (deviceList.isEmpty())
		{
			deviceList = null;
		}
		return "device";
	}

	/**
	 * itmsIposs设备绑定
	 * 
	 * @author wangsenbo
	 * @date Nov 12, 2009
	 * @return String
	 */
	public String execute()
	{
		logger.debug("ItmsIpossInstACT=>execute()");
		message = userInstReleaseBio.itmsInst(1, userId, username, city_id, deviceId,
				deviceCityId, oui, deviceNo, dealstaff, 1, 0, gw_type);
		return "success";
	}

	/**
	 * itmsIposs设备修障
	 * 
	 * @author wangsenbo
	 * @date Nov 12, 2009
	 * @return String
	 */
	public String modifyInst()
	{
		logger.debug("ItmsIpossInstACT=>modifyInst()");
		message = userInstReleaseBio.ipossItmsModify(userId, username, city_id,
				oldDeviceId, deviceId, deviceCityId, oui, deviceNo, faultId, dealstaff,
				dealstaffid);
		return "success";
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
	 * @return the username
	 */
	public String getUsername()
	{
		return username.toLowerCase();
	}

	/**
	 * @param username
	 *            the username to set
	 */
	public void setUsername(String username)
	{
		this.username = username.toLowerCase();
	}

	/**
	 * @return the dealstaff
	 */
	public String getDealstaff()
	{
		return dealstaff;
	}

	/**
	 * @param dealstaff
	 *            the dealstaff to set
	 */
	public void setDealstaff(String dealstaff)
	{
		this.dealstaff = dealstaff;
	}

	/**
	 * @return the city_id
	 */
	public String getCity_id()
	{
		return city_id;
	}

	/**
	 * @param city_id
	 *            the city_id to set
	 */
	public void setCity_id(String city_id)
	{
		this.city_id = city_id;
	}

	/**
	 * @return the userInstReleaseBio
	 */
	public UserInstReleaseBIO getUserInstReleaseBio()
	{
		return userInstReleaseBio;
	}

	/**
	 * @param userInstReleaseBio
	 *            the userInstReleaseBio to set
	 */
	public void setUserInstReleaseBio(UserInstReleaseBIO userInstReleaseBio)
	{
		this.userInstReleaseBio = userInstReleaseBio;
	}

	/**
	 * @return the userList
	 */
	public List getUserList()
	{
		return userList;
	}

	/**
	 * @param userList
	 *            the userList to set
	 */
	public void setUserList(List userList)
	{
		this.userList = userList;
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
	 * @return the device_serialnumber
	 */
	public String getDevice_serialnumber()
	{
		return device_serialnumber;
	}

	/**
	 * @param device_serialnumber
	 *            the device_serialnumber to set
	 */
	public void setDevice_serialnumber(String device_serialnumber)
	{
		this.device_serialnumber = device_serialnumber;
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
	 * @return the deviceNo
	 */
	public String getDeviceNo()
	{
		return deviceNo;
	}

	/**
	 * @param deviceNo
	 *            the deviceNo to set
	 */
	public void setDeviceNo(String deviceNo)
	{
		this.deviceNo = deviceNo;
	}

	/**
	 * @return the userId
	 */
	public String getUserId()
	{
		return userId;
	}

	/**
	 * @param userId
	 *            the userId to set
	 */
	public void setUserId(String userId)
	{
		this.userId = userId;
	}

	/**
	 * @return the oui
	 */
	public String getOui()
	{
		return oui;
	}

	/**
	 * @param oui
	 *            the oui to set
	 */
	public void setOui(String oui)
	{
		this.oui = oui;
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
	 * @return the oldDeviceId
	 */
	public String getOldDeviceId()
	{
		return oldDeviceId;
	}

	/**
	 * @param oldDeviceId
	 *            the oldDeviceId to set
	 */
	public void setOldDeviceId(String oldDeviceId)
	{
		this.oldDeviceId = oldDeviceId;
	}

	/**
	 * @return the cityName
	 */
	public String getCityName()
	{
		return cityName;
	}

	/**
	 * @param cityName
	 *            the cityName to set
	 */
	public void setCityName(String cityName)
	{
		this.cityName = cityName;
	}

	/**
	 * @return the faultId
	 */
	public String getFaultId()
	{
		return faultId;
	}

	/**
	 * @param faultId
	 *            the faultId to set
	 */
	public void setFaultId(String faultId)
	{
		this.faultId = faultId;
	}

	/**
	 * @return the dealstaffid
	 */
	public String getDealstaffid()
	{
		return dealstaffid;
	}

	/**
	 * @param dealstaffid
	 *            the dealstaffid to set
	 */
	public void setDealstaffid(String dealstaffid)
	{
		this.dealstaffid = dealstaffid;
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

	public String getGw_type() {
		return gw_type;
	}

	public void setGw_type(String gw_type) {
		this.gw_type = gw_type;
	}
}
