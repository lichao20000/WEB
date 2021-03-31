
package com.linkage.module.gwms.resource.act;

import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.LipossGlobals;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.resource.bio.UserInstReleaseBIO;
import com.opensymphony.xwork2.ActionSupport;

/**
 * @author 王森博
 * @date 2009-11-13
 */
@SuppressWarnings("unchecked")
public class BbmsInstACT extends ActionSupport implements SessionAware
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(BbmsInstACT.class);
	private List deviceList;
	private String device_serialnumber;
	private String userName;
	private List userList;
	private String message;
	private UserInstReleaseBIO userInstReleaseBio;
	// session
	private Map session;
	private String username;
	private String userId;
	private String userCityId;
	private String deviceId;
	private String deviceCityId;
	private String oui;
	private String deviceNo;
	private String customerId;
	private String oldDeviceId;
	private String instArea;
	private String loopbackIp;
	private String gw_type = null;

	/**
	 * bbms查询设备信息
	 * 
	 * @author wangsenbo
	 * @date Nov 13, 2009
	 * @return String
	 */
	public String getDeviceInfo()
	{
		logger.debug("getDeviceInfo()");
		UserRes curUser = (UserRes) session.get("curUser");
		String cityId = curUser.getCityId();
		deviceList = userInstReleaseBio.queryDevice(device_serialnumber, cityId,
				loopbackIp,2);
		if (deviceList.isEmpty())
		{
			deviceList = null;
		}
		return "device";
	}

	/**
	 * bbms查询用户信息 绑定
	 * 
	 * @author wangsenbo
	 * @date Nov 13, 2009
	 * @return String
	 */
	public String getInstUserCustomerInfo()
	{
		logger.debug("getInstUserCustomerInfo()");
		UserRes curUser = (UserRes) session.get("curUser");
		String cityId = curUser.getCityId();
		userList = userInstReleaseBio.queryUser(gw_type, cityId, userName, null, device_serialnumber,
				"all");
		instArea = Global.instAreaShortName;
		if (userList.isEmpty())
		{
			userList = null;
		}
		return "userInst";
	}

	/**
	 * bbms查询用户信息 解绑
	 * 
	 * @author wangsenbo
	 * @date Nov 13, 2009
	 * @return String
	 */
	public String getReleaseUserCustomerInfo()
	{
		logger.debug("getReleaseUserCustomerInfo()");
		UserRes curUser = (UserRes) session.get("curUser");
		String cityId = curUser.getCityId();
		userList = userInstReleaseBio.queryUser(gw_type,cityId, userName,null, device_serialnumber,
				"bind");
		if (userList.isEmpty())
		{
			userList = null;
		}
		return "userRelease";
	}

	/**
	 * bbms设备绑定
	 * 
	 * @author wangsenbo
	 * @date Nov 13, 2009
	 * @return String
	 */
	public String execute()
	{
		logger.debug("execute()");
		UserRes curUser = (UserRes) session.get("curUser");
		message = userInstReleaseBio.bbmsInst(curUser.getUser().getId(), userId,
				username, userCityId, deviceId, deviceCityId, oui, deviceNo, customerId,
				curUser.getUser().getAccount(), 1, curUser.getUser().getPasswd());
		return "success";
	}

	/**
	 * bbms解绑
	 * 
	 * @author wangsenbo
	 * @date Nov 13, 2009
	 * @return String
	 */
	public String release()
	{
		logger.debug("release()");
		UserRes curUser = (UserRes) session.get("curUser");
		message = userInstReleaseBio.bbmsRelease(userId, username, userCityId, deviceId,
				curUser.getUser().getAccount(), curUser.getUser().getPasswd());
		return "success";
	}

	/**
	 * 新疆bbms设备强制绑定
	 * 
	 * @author wangsenbo
	 * @date Nov 13, 2009
	 * @return String
	 */
	public String imposeInst()
	{
		logger.debug("execute()");
		UserRes curUser = (UserRes) session.get("curUser");
		message = userInstReleaseBio
				.bbmsImposeInst(curUser.getUser().getId(), userId, username, userCityId,
						oldDeviceId, deviceId, deviceCityId, oui, deviceNo, customerId,
						curUser.getUser().getAccount(), curUser.getUser().getPasswd());
		return "success";
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
	 * @return the userName
	 */
	public String getUserName()
	{
//		return userName.toLowerCase();
		return userName;
	}

	/**
	 * @param userName
	 *            the userName to set
	 */
	public void setUserName(String userName)
	{
//		this.userName = userName.toLowerCase();
		this.userName = userName;
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
		return username;
	}

	/**
	 * @param username
	 *            the username to set
	 */
	public void setUsername(String username)
	{
		this.username = username;
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
	 * @return the userCityId
	 */
	public String getUserCityId()
	{
		return userCityId;
	}

	/**
	 * @param userCityId
	 *            the userCityId to set
	 */
	public void setUserCityId(String userCityId)
	{
		this.userCityId = userCityId;
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
	 * @return the customerId
	 */
	public String getCustomerId()
	{
		return customerId;
	}

	/**
	 * @param customerId
	 *            the customerId to set
	 */
	public void setCustomerId(String customerId)
	{
		this.customerId = customerId;
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
