
package com.linkage.module.gtms.stb.config.act;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ailk.tr069.devrpc.obj.rpc.DevRpcCmdOBJ;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.WebUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gtms.stb.config.bio.BaseConfigBIO;
import com.linkage.module.gwms.Global;
import com.opensymphony.xwork2.ActionSupport;

public class BaseConfigACT extends ActionSupport
{

	private static final long serialVersionUID = -1072242108728012475L;
	private static Logger logger = LoggerFactory.getLogger(BaseConfigACT.class);
	// 1:家庭网关，2：企业网管，4：机顶盒
	private String gw_type = Global.GW_TYPE_STB;
	// 接入方式
	private String addressingType;
	// PPPoE账号
	private String PPPoEID;
	// PPPoE密码
	private String PPPoEPassword;
	// DHCP帐号
	private String DHCPID;
	// DHCP密码
	private String DHCPPassword;
	// IP地址
	private String IPAddress;
	// 子网掩码
	private String subnetMask;
	// 网关
	private String defaultGateway;
	// DNS服务器
	private String DNSServers;
	// 业务账号
	private String userID;
	// 业务密码
	private String userPassword;
	// 认证服务器地址
	private String authURL;
	// 更新服务器地址
	private String autoUpdateServer;
	// 接入方式
	private String addressingTypebk;
	// PPPoE账号
	private String PPPoEIDbk;
	// PPPoE密码
	private String PPPoEPasswordbk;
	// DHCP账号
	private String DHCPIDbk;
	// DHCP密码
	private String DHCPPasswordbk;
	// IP地址
	private String IPAddressbk;
	// 子网掩码
	private String subnetMaskbk;
	// 网关
	private String defaultGatewaybk;
	// DNS服务器
	private String DNSServersbk;
	// 业务账号
	private String userIDbk;
	// 业务密码
	private String userPasswordbk;
	// 认证服务器地址
	private String authURLbk;
	// 更新服务器地址
	private String autoUpdateServerbk;
	private String deviceId;
	private BaseConfigBIO bio;
	private String errorMessage;
	private String networkAddress;
	private String ajax;
	private String strategyId;
	// 屏显模式
	private String aspectRatio;
	// 视频输出制式
	private String compositeVideo;
	private String newPassword;
	// 是否明文显示密码
	private String pwd;
	// 是否采集成功,1-成功、0-失败
	// private String gatherSucc;
	// 是否是历史配置信息查询: 1-是、2-不是
	private String isHistory;
	// 采集的时间
	private String time;
	private String spModel;
	private String spzsModel;

	/**
	 * 采集设备基本配置
	 * 
	 * @author wangsenbo
	 * @date Nov 22, 2010
	 * @param
	 * @return String
	 */
	public String getBaseConfig()
	{
		logger.warn("getBaseConfig({})", pwd);
		//
		Map map1 = bio.getLAN(deviceId);
		if (map1 == null)
		{
			errorMessage = bio.getErrorMessage();
			// gatherSucc = "0";
			// return "base";
			// 采集失败,获取最新记录表数据
			map1 = bio.getLANRecent(deviceId);
		}
		if (map1 != null && !map1.isEmpty())
		{
			addressingType = StringUtil.getStringValue(map1.get("address_type"));
			IPAddress = StringUtil.getStringValue(map1.get("ip"));
			subnetMask = StringUtil.getStringValue(map1.get("mask"));
			defaultGateway = StringUtil.getStringValue(map1.get("gateway"));
			DNSServers = StringUtil.getStringValue(map1.get("dns"));
			networkAddress = LipossGlobals.getLipossProperty("networkAddress");
			time = formatTime(StringUtil.getIntegerValue(map1.get("gather_time")));
		}
		//
		Map map2 = bio.getX_CTC_IPTV_ServiceInfo(deviceId);
		if (map2 == null)
		{
			errorMessage = bio.getErrorMessage();
			// gatherSucc = "0";
			// return "base";
			// 采集失败,获取最新记录表数据
			map2 = bio.getX_CTC_IPTV_ServiceInfoRecent(deviceId);
		}
		if (map2 != null && !map2.isEmpty())
		{
			PPPoEID = StringUtil.getStringValue(map2.get("pppoe_id"));
			PPPoEPassword = StringUtil.getStringValue(map2.get("pppoe_pwd"));
			userID = StringUtil.getStringValue(map2.get("user_id"));
			userPassword = StringUtil.getStringValue(map2.get("user_pwd"));
			authURL = StringUtil.getStringValue(map2.get("auth_url"));
			DHCPID = StringUtil.getStringValue(map2.get("iptv_dhcp_username"));
			DHCPPassword = StringUtil.getStringValue(map2.get("iptv_dhcp_password"));
		}
		//
		Map map3 = bio.getUserInterface(deviceId);
		if (map3 == null)
		{
			errorMessage = bio.getErrorMessage();
			// gatherSucc = "0";
			// return "base";
			// 采集失败,获取最新记录表数据
			map3 = bio.getUserInterfaceRecent(deviceId);
		}
		if (map3 != null && !map3.isEmpty())
		{
			autoUpdateServer = StringUtil.getStringValue(map3.get("auto_update_serv"));
		}
		// gatherSucc = "1";
		// Map map4 = bio.getCmpstVideoAndAspRatio(deviceId);
		// if(null == map4){
		// errorMessage = bio.getErrorMessage();
		// map4 = bio.getCmpstVideoAndAspRatioRecent(deviceId);
		// }
		// if(null != map4 && !map4.isEmpty()){
		// compositeVideo =
		// StringUtil.getStringValue(map4.get("composite_video_standard"));
		// aspectRatio = StringUtil.getStringValue(map4.get("aspect_ratio"));
		// }
		isHistory = "0";
		return "base";
	}

	/**
	 * 设备历史配置信息
	 * 
	 * @author chenjie
	 * @date 2012-3-22
	 * @param
	 * @return String
	 */
	public String getHistoryConfig()
	{
		logger.warn("getHistoryConfig()");
		Map map1 = bio.getLANRecent(deviceId);
		if (map1 != null)
		{
			addressingType = StringUtil.getStringValue(map1.get("address_type"));
			IPAddress = StringUtil.getStringValue(map1.get("ip"));
			subnetMask = StringUtil.getStringValue(map1.get("mask"));
			defaultGateway = StringUtil.getStringValue(map1.get("gateway"));
			DNSServers = StringUtil.getStringValue(map1.get("dns"));
			networkAddress = LipossGlobals.getLipossProperty("networkAddress");
			time = formatTime(StringUtil.getIntegerValue(map1.get("gather_time")));
		}
		Map map2 = bio.getX_CTC_IPTV_ServiceInfoRecent(deviceId);
		if (map2 != null)
		{
			PPPoEID = StringUtil.getStringValue(map2.get("pppoe_id"));
			PPPoEPassword = StringUtil.getStringValue(map2.get("pppoe_pwd"));
			userID = StringUtil.getStringValue(map2.get("user_id"));
			userPassword = StringUtil.getStringValue(map2.get("user_pwd"));
			authURL = StringUtil.getStringValue(map2.get("auth_url"));
			DHCPID = StringUtil.getStringValue(map2.get("iptv_dhcp_username"));
			DHCPPassword = StringUtil.getStringValue(map2.get("iptv_dhcp_password"));
		}
		// //
		// Map map3 = bio.getUserInterfaceRecent(deviceId);
		// if (map3 != null)
		// {
		// autoUpdateServer = StringUtil.getStringValue(map3.get("auto_update_serv"));
		// }
		// /**add by zhangsb3*/
		// Map map4 = bio.getCmpstVideoAndAspRatioRecent(deviceId);
		// if(null != map4 && !map4.isEmpty()){
		// compositeVideo =
		// StringUtil.getStringValue(map4.get("composite_video_standard"));
		// aspectRatio = StringUtil.getStringValue(map4.get("aspect_ratio"));
		// }
		isHistory = "1";
		return "base";
	}

	/**
	 * 视频输出制式和屏显采集
	 * 
	 * @author zhangsm
	 * @date 2011-4-11
	 * @param
	 * @return String
	 */
	public String cmpstVideoAndAspRatioGather()
	{
		logger.debug("cmpstVideoAndAspRatioGather()");
		Map map = bio.getCmpstVideoAndAspRatio(deviceId);
		if (map == null)
		{
			errorMessage = bio.getErrorMessage();
			ajax = "-1" + "#" + errorMessage;
			return "ajax";
		}
		compositeVideo = StringUtil.getStringValue(map.get("composite_video_standard"));
		aspectRatio = StringUtil.getStringValue(map.get("aspect_ratio"));
		ajax = "1" + "#" + compositeVideo + "#" + aspectRatio;
		return "ajax";
	}

	/**
	 * 视频输出制式和屏显配置
	 * 
	 * @author zhangsm
	 * @date 2011-4-11
	 * @param
	 * @return String
	 */
	public String configCmpstVideoAndAspRatio()
	{
		UserRes curUser = WebUtil.getCurrentUser();
		ajax = bio.configCmpstVideoAndAspRatio(aspectRatio, compositeVideo, curUser
				.getUser().getId(), deviceId, spModel, spzsModel, gw_type);
		return "ajax";
	}

	/**
	 * 配置设备基本配置
	 * 
	 * @author wangsenbo
	 * @date Nov 22, 2010
	 * @param
	 * @return String
	 */
	public String baseConfig()
	{
		UserRes curUser = WebUtil.getCurrentUser();
		ajax = bio.baseConfig(PPPoEID, PPPoEIDbk, PPPoEPassword, PPPoEPasswordbk, DHCPID,
				DHCPIDbk, DHCPPassword, DHCPPasswordbk, IPAddress, IPAddressbk,
				subnetMask, subnetMaskbk, defaultGateway, defaultGatewaybk, DNSServers,
				DNSServersbk, userID, userIDbk, userPassword, userPasswordbk, authURL,
				authURLbk, autoUpdateServer, autoUpdateServerbk, addressingType,
				addressingTypebk, curUser.getUser().getId(), deviceId, gw_type);
		return "ajax";
	}

	public String resPass()
	{
		List<DevRpcCmdOBJ> list = bio.resPass(deviceId, newPassword, gw_type);
		if (list == null || list.size() == 0 || list.get(0) == null)
		{
			ajax = "设置失败";
		}
		// 一个设备返回的命令
		else if (((DevRpcCmdOBJ) list.get(0)).getStat() == 1)
		{
			ajax = "修改成功";
		}
		return "ajax";
	}

	public String cmpstVideoAndAspRatio()
	{
		return "cmpstVideoAndAspRatio";
	}

	public String getStrategyById()
	{
		logger.debug("getStrategyById()");
		if (true == StringUtil.IsEmpty(strategyId))
		{
			logger.error("策略ID为空！");
		}
		else
		{
			ajax = bio.getStrategyById(strategyId);
		}
		return "ajax";
	}

	public String formatTime(long time)
	{
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return dateFormat.format(time * 1000);
	}

	public String getAddressingType()
	{
		return addressingType;
	}

	public void setAddressingType(String addressingType)
	{
		this.addressingType = addressingType;
	}

	public String getPPPoEID()
	{
		return PPPoEID;
	}

	public void setPPPoEID(String poEID)
	{
		PPPoEID = poEID;
	}

	public String getPPPoEPassword()
	{
		return PPPoEPassword;
	}

	public void setPPPoEPassword(String poEPassword)
	{
		PPPoEPassword = poEPassword;
	}

	public String getIPAddress()
	{
		return IPAddress;
	}

	public void setIPAddress(String address)
	{
		IPAddress = address;
	}

	public String getSubnetMask()
	{
		return subnetMask;
	}

	public void setSubnetMask(String subnetMask)
	{
		this.subnetMask = subnetMask;
	}

	public String getDefaultGateway()
	{
		return defaultGateway;
	}

	public void setDefaultGateway(String defaultGateway)
	{
		this.defaultGateway = defaultGateway;
	}

	public String getDNSServers()
	{
		return DNSServers;
	}

	public void setDNSServers(String servers)
	{
		DNSServers = servers;
	}

	public String getUserID()
	{
		return userID;
	}

	public void setUserID(String userID)
	{
		this.userID = userID;
	}

	public String getUserPassword()
	{
		return userPassword;
	}

	public void setUserPassword(String userPassword)
	{
		this.userPassword = userPassword;
	}

	public String getAuthURL()
	{
		return authURL;
	}

	public void setAuthURL(String authURL)
	{
		this.authURL = authURL;
	}

	public String getSpModel()
	{
		return spModel;
	}

	public void setSpModel(String spModel)
	{
		this.spModel = spModel;
	}

	public String getSpzsModel()
	{
		return spzsModel;
	}

	public void setSpzsModel(String spzsModel)
	{
		this.spzsModel = spzsModel;
	}

	public String getAutoUpdateServer()
	{
		return autoUpdateServer;
	}

	public void setAutoUpdateServer(String autoUpdateServer)
	{
		this.autoUpdateServer = autoUpdateServer;
	}

	public String getDeviceId()
	{
		return deviceId;
	}

	public void setDeviceId(String deviceId)
	{
		this.deviceId = deviceId;
	}

	public BaseConfigBIO getBio()
	{
		return bio;
	}

	public void setBio(BaseConfigBIO bio)
	{
		this.bio = bio;
	}

	public String getErrorMessage()
	{
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage)
	{
		this.errorMessage = errorMessage;
	}

	public String getNetworkAddress()
	{
		return networkAddress;
	}

	public void setNetworkAddress(String networkAddress)
	{
		this.networkAddress = networkAddress;
	}

	public String getAddressingTypebk()
	{
		return addressingTypebk;
	}

	public void setAddressingTypebk(String addressingTypebk)
	{
		this.addressingTypebk = addressingTypebk;
	}

	public String getPPPoEIDbk()
	{
		return PPPoEIDbk;
	}

	public void setPPPoEIDbk(String poEIDbk)
	{
		PPPoEIDbk = poEIDbk;
	}

	public String getPPPoEPasswordbk()
	{
		return PPPoEPasswordbk;
	}

	public void setPPPoEPasswordbk(String poEPasswordbk)
	{
		PPPoEPasswordbk = poEPasswordbk;
	}

	public String getIPAddressbk()
	{
		return IPAddressbk;
	}

	public void setIPAddressbk(String addressbk)
	{
		IPAddressbk = addressbk;
	}

	public String getSubnetMaskbk()
	{
		return subnetMaskbk;
	}

	public void setSubnetMaskbk(String subnetMaskbk)
	{
		this.subnetMaskbk = subnetMaskbk;
	}

	public String getDefaultGatewaybk()
	{
		return defaultGatewaybk;
	}

	public void setDefaultGatewaybk(String defaultGatewaybk)
	{
		this.defaultGatewaybk = defaultGatewaybk;
	}

	public String getDNSServersbk()
	{
		return DNSServersbk;
	}

	public void setDNSServersbk(String serversbk)
	{
		DNSServersbk = serversbk;
	}

	public String getUserIDbk()
	{
		return userIDbk;
	}

	public void setUserIDbk(String userIDbk)
	{
		this.userIDbk = userIDbk;
	}

	public String getUserPasswordbk()
	{
		return userPasswordbk;
	}

	public void setUserPasswordbk(String userPasswordbk)
	{
		this.userPasswordbk = userPasswordbk;
	}

	public String getAuthURLbk()
	{
		return authURLbk;
	}

	public void setAuthURLbk(String authURLbk)
	{
		this.authURLbk = authURLbk;
	}

	public String getAutoUpdateServerbk()
	{
		return autoUpdateServerbk;
	}

	public void setAutoUpdateServerbk(String autoUpdateServerbk)
	{
		this.autoUpdateServerbk = autoUpdateServerbk;
	}

	public String getAjax()
	{
		return ajax;
	}

	public void setAjax(String ajax)
	{
		this.ajax = ajax;
	}

	public String getStrategyId()
	{
		return strategyId;
	}

	public String getAspectRatio()
	{
		return aspectRatio;
	}

	public void setAspectRatio(String aspectRatio)
	{
		this.aspectRatio = aspectRatio;
	}

	public String getCompositeVideo()
	{
		return compositeVideo;
	}

	public void setCompositeVideo(String compositeVideo)
	{
		this.compositeVideo = compositeVideo;
	}

	public void setStrategyId(String strategyId)
	{
		this.strategyId = strategyId;
	}

	public String getDHCPID()
	{
		return DHCPID;
	}

	public void setDHCPID(String dhcpid)
	{
		DHCPID = dhcpid;
	}

	public String getDHCPPassword()
	{
		return DHCPPassword;
	}

	public void setDHCPPassword(String password)
	{
		DHCPPassword = password;
	}

	public String getDHCPIDbk()
	{
		return DHCPIDbk;
	}

	public void setDHCPIDbk(String dbk)
	{
		DHCPIDbk = dbk;
	}

	public String getDHCPPasswordbk()
	{
		return DHCPPasswordbk;
	}

	public void setDHCPPasswordbk(String passwordbk)
	{
		DHCPPasswordbk = passwordbk;
	}

	public String getPwd()
	{
		return pwd;
	}

	public void setPwd(String pwd)
	{
		this.pwd = pwd;
	}

	public String getNewPassword()
	{
		return newPassword;
	}

	public void setNewPassword(String newPassword)
	{
		this.newPassword = newPassword;
	}

	public String getIsHistory()
	{
		return isHistory;
	}

	public void setIsHistory(String isHistory)
	{
		this.isHistory = isHistory;
	}

	public String getTime()
	{
		return time;
	}

	public void setTime(String time)
	{
		this.time = time;
	}
}
