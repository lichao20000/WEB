package com.linkage.module.gwms.config.act;

import java.util.Date;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.ConstantClass;
import com.linkage.module.gwms.config.bio.ServiceManSheetBIO;
import com.linkage.module.gwms.obj.StrategyOBJ;
import com.linkage.module.gwms.obj.tabquery.HgwCustObj;
import com.linkage.module.gwms.obj.tabquery.HgwServUserObj;
import com.linkage.module.gwms.util.StringUtil;
import com.linkage.module.gwms.util.service.WanTypeUtil;
import com.opensymphony.xwork2.ActionSupport;

/**
 * @author Jason(3412)
 * @date 2009-9-22
 */
public class ServiceManSheetACT extends ActionSupport implements SessionAware{

	/** */
	private static final long serialVersionUID = 1L;

	/** log */
	private static Logger logger = LoggerFactory
			.getLogger(ServiceManSheetACT.class);

	// 用户ID
	private String userId;
	// 用户账号
	private String username;
	// 设备ID
	private String deviceId;
	// OUI
	private String oui;
	// 设备序列号
	private String devSn;
	// 属地
	private String cityId;
	private String cityName;
	// 局向
	private String officeId;
	private String officeName;
	// 用户姓名
	private String realname;

	// 业务类型
	private String servTypeId;
	//操作类型 (开户)
	private String operTypeId = "1";
	// 接入方式+上网方式
	private String wanType;
	// 接入方式
	private String accessType;
	// VlanID
	private String vlanid;
	// PVC
	private String vpi;
	private String vci;
	// 上网方式
	private String netType;
	// 路由方式：pppoe拨号用户账号和密码
	private String pppoeUsername;
	private String pppoePasswd;
	// 静态IP方式：IP地址,网关,子网掩码,DNS地址
	private String ip;
	private String gateway;
	private String mask;
	private String dns;
	// VOIP业务认证用户名和密码
	private String voipUsername;
	private String voipPasswd;
	// 绑定端口
	private String bindPort;

	/** ajax */
	private String ajax = "";

	// 初始化信息用到
	private String hasCust;
	private String hasServUser;

	private Map session;
	
	private String gw_type;
	
	/** BIO */
	ServiceManSheetBIO serviceManSheetBio;

	/**
	 * @Deprecated
	 * 该方法已不再使用，被子类覆盖
	 * 
	 * 初始化信息
	 * 
	 */
	public String execute() {
		logger.debug("execute()");
		if (null != servTypeId && null != deviceId) {
			// 如果以超链接的方式连到该页面,需要传递的参数

		} else if (null != servTypeId && null != deviceId) {
			// 如果以超链接的方式连到该页面,需要传递的参数

		}
		return SUCCESS;
	}

	/**
	 * @Deprecated
	 * 该方法已不再使用，被子类覆盖
	 * 
	 * 查询业务用户信息,初始化页面的信息
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2009-9-22
	 * @return String
	 */
	public String queryServUserInfo() {
		logger.debug("queryServUserInfo()");
		logger.debug("业务类型:{},序列号：{}，用户账号：{}", new Object[] { servTypeId,
				devSn, username });
		if (StringUtil.IsEmpty(devSn) && StringUtil.IsEmpty(username)) {
			logger.warn("查询时序列号和用户账号都为NULL，出错返回");
			hasCust = "-1";
		} else {
			HgwCustObj[] custObjArr = serviceManSheetBio.getCustObj(username,
					devSn, gw_type);
			if (null == custObjArr || custObjArr.length == 0) {
				logger.debug("null == custObj");
				hasCust = "-1";
			} else if (custObjArr.length > 1) {
				logger.debug("custObjArr.length > 1");
				hasCust = "-2";
			} else {
				HgwCustObj custObj = custObjArr[0];
				// 初始化用户相关信息
				deviceId = custObj.getDeviceId();
				userId = custObj.getUserId();
				username = custObj.getUsername();
				oui = custObj.getOui();
				devSn = custObj.getDeviceSerial();
				cityId = custObj.getCityId();
				cityName = custObj.getCityName();
				officeId = custObj.getOfficeId();
				officeName = custObj.getOfficeName();
				realname = custObj.getRealname();
				// 获取业务用户信息
				HgwServUserObj servUserObj = serviceManSheetBio.getServUserObj(
						userId, servTypeId, gw_type);
				if (null != servUserObj) {
					logger.debug("null != servUserObj");
					// 初始化业务用户信息
					vpi = servUserObj.getVpiid();
					vci = servUserObj.getVciid();
					vlanid = servUserObj.getVlanid();
					wanType = servUserObj.getWanType();
					accessType = StringUtil
							.getStringValue(WanTypeUtil
									.getAccessType(StringUtil
											.getIntegerValue(servUserObj
													.getWanType())));
					netType = StringUtil.getStringValue(WanTypeUtil
							.getNetType(StringUtil.getIntegerValue(servUserObj
									.getWanType())));
					pppoeUsername = servUserObj.getUsername();
					pppoePasswd = servUserObj.getPasswd();

					ip = servUserObj.getIpAddress();
					mask = servUserObj.getIpMask();
					gateway = servUserObj.getGateway();
					dns = servUserObj.getDns();

					voipUsername = servUserObj.getUsername();
					voipPasswd = servUserObj.getPasswd();
				} else {
					logger.debug("null == servUserObj");
					hasServUser = "-1";
				}
			}
		}
		return "sheetInfo";
	}

	/**
	 * @Deprecated
	 * 该方法已不再使用，被子类覆盖
	 * 
	 * 手工工单业务下发
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2009-9-22
	 * @return String
	 */
	public String doService() {
		logger.debug("doService()");
		long nowTime = new Date().getTime() / 1000;
		HgwServUserObj hgwServUserObj = new HgwServUserObj();

		wanType = StringUtil.getStringValue(WanTypeUtil.getWanType(StringUtil
				.getIntegerValue(accessType), StringUtil
				.getIntegerValue(netType)));

		hgwServUserObj.setBindPort(bindPort);
		hgwServUserObj.setDns(dns);
		hgwServUserObj.setGateway(gateway);
		hgwServUserObj.setIpAddress(ip);
		hgwServUserObj.setIpMask(mask);
		hgwServUserObj.setServTypeId(servTypeId);
		hgwServUserObj.setUserId(userId);
		hgwServUserObj.setVciid(vci);
		hgwServUserObj.setVpiid(vpi);
		hgwServUserObj.setVlanid(vlanid);
		hgwServUserObj.setWanType(wanType);
		
		UserRes curUser = (UserRes) session.get("curUser");
		long accOid = curUser.getUser().getId();
		StrategyOBJ strategyObj = new StrategyOBJ();
		strategyObj.createId();
		strategyObj.setAccOid(accOid);
		strategyObj.setDeviceId(deviceId);
		strategyObj.setOui(oui);
		strategyObj.setSn(devSn);
		//需要重做
		strategyObj.setRedo(1);
		//配置时间
		strategyObj.setTime(nowTime);
		//立即执行
		strategyObj.setType(0);
		// 顺序,默认1
		strategyObj.setOrderId(1);
		// 工单类型: 新工单,工单参数为xml串的工单
		strategyObj.setSheetType(2);
		
		int iret = -1;
		if ("10".equals(servTypeId)) {
			hgwServUserObj.setPasswd(pppoePasswd);
			hgwServUserObj.setUsername(pppoeUsername);
			strategyObj.setUsername(pppoeUsername);
			strategyObj.setServiceId(ConstantClass.NET_OPEN);
			iret = serviceManSheetBio.netOpen(hgwServUserObj, hasServUser, strategyObj, gw_type);
		} else if ("11".equals(servTypeId)) {
			
			iret = serviceManSheetBio.iptvOpen(hgwServUserObj);
		} else if ("14".equals(servTypeId)) {
			hgwServUserObj.setPasswd(voipPasswd);
			hgwServUserObj.setUsername(voipUsername);
			strategyObj.setUsername(voipUsername);
			strategyObj.setServiceId(ConstantClass.VOIP_OPEN);
			iret = serviceManSheetBio.voipOpen(hgwServUserObj, officeId, hasServUser,
					strategyObj, gw_type);
		}
		ajax = StringUtil.getStringValue(iret);
		return "ajax";
	}

	/** getter, setter methods */

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getDevSn() {
		return devSn;
	}

	public void setDevSn(String devSn) {
		this.devSn = devSn;
	}

	public String getServTypeId() {
		return servTypeId;
	}

	public void setServTypeId(String servTypeId) {
		this.servTypeId = servTypeId;
	}

	public String getWanType() {
		return wanType;
	}

	public void setWanType(String wanType) {
		this.wanType = wanType;
	}

	public String getAccessType() {
		return accessType;
	}

	public void setAccessType(String accessType) {
		this.accessType = accessType;
	}

	public String getVlanid() {
		return vlanid;
	}

	public void setVlanid(String vlanid) {
		this.vlanid = vlanid;
	}

	public String getVpi() {
		return vpi;
	}

	public void setVpi(String vpi) {
		this.vpi = vpi;
	}

	public String getVci() {
		return vci;
	}

	public void setVci(String vci) {
		this.vci = vci;
	}

	public String getNetType() {
		return netType;
	}

	public void setNetType(String netType) {
		this.netType = netType;
	}

	public String getPppoeUsername() {
		return pppoeUsername;
	}

	public void setPppoeUsername(String pppoeUsername) {
		this.pppoeUsername = pppoeUsername;
	}

	public String getPppoePasswd() {
		return pppoePasswd;
	}

	public void setPppoePasswd(String pppoePasswd) {
		this.pppoePasswd = pppoePasswd;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getGateway() {
		return gateway;
	}

	public void setGateway(String gateway) {
		this.gateway = gateway;
	}

	public String getMask() {
		return mask;
	}

	public void setMask(String mask) {
		this.mask = mask;
	}

	public String getDns() {
		return dns;
	}

	public void setDns(String dns) {
		this.dns = dns;
	}

	public String getVoipUsername() {
		return voipUsername;
	}

	public void setVoipUsername(String voipUsername) {
		this.voipUsername = voipUsername;
	}

	public String getVoipPasswd() {
		return voipPasswd;
	}

	public void setVoipPasswd(String voipPasswd) {
		this.voipPasswd = voipPasswd;
	}

	public String getBindPort() {
		return bindPort;
	}

	public void setBindPort(String bindPort) {
		this.bindPort = bindPort;
	}

	public String getAjax() {
		return ajax;
	}

	public void setServiceManSheetBio(ServiceManSheetBIO serviceManSheetBio) {
		this.serviceManSheetBio = serviceManSheetBio;
	}

	public String getOui() {
		return oui;
	}

	public void setOui(String oui) {
		this.oui = oui;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getOfficeId() {
		return officeId;
	}

	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}

	public String getOfficeName() {
		return officeName;
	}

	public void setOfficeName(String officeName) {
		this.officeName = officeName;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public String getHasCust() {
		return hasCust;
	}

	public String getHasServUser() {
		return hasServUser;
	}


	@Override
	public void setSession(Map arg0) {
		session = arg0;
	}

	public void setHasCust(String hasCust) {
		this.hasCust = hasCust;
	}

	public void setHasServUser(String hasServUser) {
		this.hasServUser = hasServUser;
	}

	public String getGw_type() {
		return gw_type;
	}

	public void setGw_type(String gw_type) {
		this.gw_type = gw_type;
	}

}
