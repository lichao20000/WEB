
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
import com.linkage.module.gwms.util.StringUtil;
import com.opensymphony.xwork2.ActionSupport;

@SuppressWarnings("unchecked")
public class ItmsInstACT extends ActionSupport implements SessionAware
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(ItmsInstACT.class);
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
	private String oldDeviceId;
	private String deviceCityId;
	private String oui;
	private String deviceNo;
	private String customerId;
	private String instArea;
	private String loopbackIp;
	private String servUserName = null;
	private String chkinstuser;
	private String nameListHtml;
	private String nameType = null;
	private String gw_type;

	
	

	/**
	 * itms现场安装页面初始化
	 * 
	 * @author wangsenbo
	 * @date Jul 22, 2010
	 * @param
	 * @return String
	 */
	public String init()
	{
		this.setNameListHtml();
		return "init";
	}

	/**
	 * itms查询设备信息 绑定
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
				loopbackIp);
		if (deviceList.isEmpty())
		{
			deviceList = null;
		}
		return "device";
	}

	/**
	 * itms查询用户信息 绑定
	 * 
	 * @author wangsenbo
	 * @date Nov 13, 2009
	 * @return String
	 */
	public String getInstUserInfo()
	{
		UserRes curUser = (UserRes) session.get("curUser");
		String cityId = curUser.getCityId();
		gw_type="1";
		userList = userInstReleaseBio.queryUser(gw_type,cityId, userName, nameType,
				device_serialnumber, "all");
		instArea = Global.instAreaShortName;
		chkinstuser = LipossGlobals.getLipossProperty("chkinstuser");
		if (true == StringUtil.IsEmpty(servUserName))
		{
			servUserName = null;
		}
		if (userList.isEmpty())
		{
			userList = null;
		}
		return "userInst";
	}

	/**
	 * itms查询用户信息 解绑
	 * 
	 * @author wangsenbo
	 * @date Nov 13, 2009
	 * @return String
	 */
	public String getReleaseUserInfo()
	{
		logger.debug("getReleaseUserInfo()");
		UserRes curUser = (UserRes) session.get("curUser");
		String cityId = curUser.getCityId();
		userList = userInstReleaseBio.queryUser(gw_type,cityId, userName, nameType,
				device_serialnumber, "bind");
		if (userList.isEmpty())
		{
			userList = null;
		}
		return "userRelease";
	}

	/**
	 * itms设备绑定
	 * 
	 * @author wangsenbo
	 * @date Nov 13, 2009
	 * @return String
	 */
	public String execute()
	{
		logger.debug("execute()");
		UserRes curUser = (UserRes) session.get("curUser");
		if (true == StringUtil.IsEmpty(userCityId))
		{
			userCityId = curUser.getCityId();
		}
		message = userInstReleaseBio.itmsInst(curUser.getUser().getId(), userId,
				username, userCityId, deviceId, deviceCityId, oui, deviceNo, curUser
						.getUser().getAccount(), 1, 1 ,gw_type);
		return "success";
	}

	/**
	 * 新疆itms设备强制绑定
	 * 
	 * @author wangsenbo
	 * @date Nov 13, 2009
	 * @return String
	 */
	public String imposeInst()
	{
		logger.debug("execute()");
		UserRes curUser = (UserRes) session.get("curUser");
		message = userInstReleaseBio.itmsImposeInst(curUser.getUser().getId(), userId,
				username, userCityId, oldDeviceId, deviceId, deviceCityId, oui, deviceNo,
				curUser.getUser().getAccount());
		return "success";
	}

	/**
	 * itms解绑
	 * 
	 * @author wangsenbo
	 * @date Nov 13, 2009
	 * @return String
	 */
	public String release()
	{
		logger.debug("release()");
		UserRes curUser = (UserRes) session.get("curUser");
		message = userInstReleaseBio.itmsRelease(userId, username, userCityId, deviceId,
				curUser.getUser().getAccount(), 1);
		logger.info(message);
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
		return userName;
	}

	/**
	 * @param userName
	 *            the userName to set
	 */
	public void setUserName(String userName)
	{
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
	 * @return the servUserName
	 */
	public String getServUserName()
	{
		return servUserName;
	}

	/**
	 * @param servUserName
	 *            the servUserName to set
	 */
	public void setServUserName(String servUserName)
	{
		this.servUserName = servUserName;
	}

	/**
	 * @return the chkinstuser
	 */
	public String getChkinstuser()
	{
		return chkinstuser;
	}

	/**
	 * @param chkinstuser
	 *            the chkinstuser to set
	 */
	public void setChkinstuser(String chkinstuser)
	{
		this.chkinstuser = chkinstuser;
	}

	/**
	 * @param nameList
	 *            the nameList to set
	 */
	public void setNameListHtml()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("<SELECT name='nameType' class='bk' style='width:260px'>");
		
		// 山西联通 "LOID"变更为"唯一标识"
		if(Global.SXLT.equals(Global.instAreaShortName)) {
			sb.append("<option value='1'>唯一标识</option>");
		}else {
			sb.append("<option value='1'>LOID</option>");
		}
		
		sb.append("<option value='2' selected>上网宽带账号</option>");
		sb.append("<option value='3'>IPTV宽带账号</option>");
		//sb.append("<option value='4'>VoIP认证号码</option>");
		//sb.append("<option value='5'>VoIP电话号码</option>");
		sb.append("</SELECT>");
		nameListHtml = sb.toString();
	}

	/**
	 * @return the nameListHtml
	 */
	public String getNameListHtml()
	{
		return nameListHtml;
	}

	/**
	 * @param nameListHtml
	 *            the nameListHtml to set
	 */
	public void setNameListHtml(String nameListHtml)
	{
		this.nameListHtml = nameListHtml;
	}

	/**
	 * @return the nameType
	 */
	public String getNameType()
	{
		return nameType;
	}

	/**
	 * @param nameType
	 *            the nameType to set
	 */
	public void setNameType(String nameType)
	{
		this.nameType = nameType;
	}
	public String getGw_type()
	{
		return gw_type;
	}

	
	public void setGw_type(String gwType)
	{
		gw_type = gwType;
	}
}
