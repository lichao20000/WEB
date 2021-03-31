package com.linkage.module.gtms.config.action;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.rpc.ServiceException;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.LipossGlobals;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gtms.config.serv.WirelessBusinessCtrlServ;
import com.linkage.module.gwms.Global;

public class WirelessBusinessCtrlACT implements SessionAware {
	private static Logger logger = LoggerFactory
			.getLogger(WirelessBusinessCtrlACT.class);
	// 符合条件的loid
	private static String isSuccess = "1";
	// Session
	private Map session;

	private String deviceIds = "";
	/** 帐号 **/
	private String username;

	private long userId;

	private WirelessBusinessCtrlServ bio;

	private String ajax = "";

	private List list = null;

	private String gw_type = "";
	// 属地
	private String cityId = "00";
	// 关闭还是开通 flag 1:开通，0:关闭
	private String flag = "1";

	private String strategy_type = "";

	private String vlanIdMark = "";

	private String ssid = "";

	private String awifi_type = "1";

	private String message = "";

	private String wireless_port = "";

	private String buss_level = "";

	String gwShare_queryField = "";

	String gwShare_queryParam = "";

	private String do_type = "";

	private List queryList = null;

	private String aflag;
	// itms接口wsdl
	private String url = "";
	// 用户信息类型
	private String userInfoType = "";
	// 用户信息
	private String userInfo = "";
	// SSID号
	private String ssidNum = "";
	// 操作类型 2：关闭
	private String type = "";

	// 无线信道
	private String channel = "";

	public static String getIsSuccess() {
		return isSuccess;
	}

	public static void setIsSuccess(String isSuccess) {
		WirelessBusinessCtrlACT.isSuccess = isSuccess;
	}

	public static Logger getLogger() {
		return logger;
	}

	public WirelessBusinessCtrlServ getBio() {
		return bio;
	}

	/**
	 * 配置
	 * 
	 * @return
	 * @throws IOException
	 */
	public String doConfigAll() throws IOException {
		logger.warn("WirelessBusinessCtrlACT->doConfigAll()");
		UserRes curUser = (UserRes) session.get("curUser");
		cityId = curUser.getCityId();
		userId = curUser.getUser().getId();
		List<String> list = new ArrayList<String>();

		String[] deviceStr = null;
		// 如果loid在用户 表中存在并且绑定了设备
		if (deviceIds.contains(",")) // 多个设备
		{
			deviceStr = deviceIds.split(",");

			for (String deviceId : deviceStr) {
				String[] result = this.getStringValue(deviceId).split(",");
				if (result[0].equals(isSuccess)) {
					list.add(deviceId);
				}
			}
		} else // 单个设备
		{
			String[] result = this.getStringValue(deviceIds).split(",");
			if (!result[0].equals(isSuccess)) {
				ajax = result[1];

				return "ajax";
			} else {
				ajax = result[1];
				list.add(deviceIds);
			}
		}

		// 新疆关闭aWiFi操作 调用itmsService接口
		if ("0".equals(flag) && Global.XJDX.equals(Global.instAreaShortName)) {
			try {
				// 
				StringBuffer sb = new StringBuffer("<?xml version=\"1.0\" encoding=\"GBK\"?><root><CmdID>123456789012345</CmdID>");
				sb.append("<CmdType>CX_01</CmdType><ClientType>3</ClientType><Param><UserInfoType>");
				sb.append(userInfoType);
				sb.append("</UserInfoType><UserInfo>");
				sb.append(userInfo);
				sb.append("</UserInfo><SSID>");
				sb.append(ssidNum);
				sb.append("</SSID><Type>");
				sb.append(type);
				sb.append("</Type></Param></root>");
				logger.warn("关闭wifi入参为：" + sb.toString());
				// 入参：xml字符串
				Service service = new Service();
				Call call = null;
				call = (Call) service.createCall();
				call.setTargetEndpointAddress(new URL(url));

				QName qn = new QName(url, "OpenOrCloseAwifi");
				call.setOperationName(qn);
				String ret = (String) call.invoke(new Object[] { sb.toString() });
				logger.warn("关闭wifi回参为：" + ret);
				String result = ret.substring(ret.indexOf("<RstCode>") + 9, ret.indexOf("<RstCode>") + 10);
				if ("0".equals(result)) {
					ajax = "1";
				}
			} catch (ServiceException e) {
				logger.error("关闭wifi调用接口出错!", e);
				e.printStackTrace();
				ajax = "0";
			}
			
		}
		else {
			ajax = bio.doConfig(userId, list, gw_type, flag, strategy_type,
					vlanIdMark, ssid, wireless_port, buss_level, channel,
					awifi_type);
		}
		return "ajax";
	}


/**
	 * 批量无线专线开通页面 
	 * 
	 * @return
	 * @throws IOException
	 */
	public String doConfigAll4Special() throws IOException {
		logger.warn("WirelessBusinessCtrlACT->doConfigAll4Special()");
		UserRes curUser = (UserRes) session.get("curUser");
		cityId = curUser.getCityId();
		userId = curUser.getUser().getId();
		List<String> list = new ArrayList<String>();
		List<String> vlanidList = new ArrayList<String>();
		List<String> ssidList = new ArrayList<String>();
		List<String> priorityList = new ArrayList<String>();
		List<String> channelList = new ArrayList<String>();
		List<String> wlanportList = new ArrayList<String>();

		String[] deviceStr = null;

		// 如果loid在用户 表中存在并且绑定了设备
		if (deviceIds.contains(",")) // 多个设备
		{
			deviceStr = deviceIds.split(",");

			String[] vlanidStr = vlanIdMark.split(",");
			String[] ssidStr = ssid.split(",");
			String[] priorityStr = buss_level.split(",");
			String[] channelStr = channel.split(",");
			String[] wlanportStr = new String[deviceStr.length];
			if (wireless_port.contains(",")){
				System.arraycopy(wireless_port.split(","), 0, wlanportStr, 0, wlanportStr.length);
			}else if("3".equals(wireless_port)){
				logger.warn("3.equals(wireless_port)");
				for(int i=0;i<wlanportStr.length;i++){
					wlanportStr[i] = "3";
				}
			}
			for (int i = 0; i < deviceStr.length; i ++) {
				String deviceId = deviceStr[i];
				String[] result = this.getStringValue(deviceId).split(",");
				if (result[0].equals(isSuccess)) {
					list.add(deviceId);
					vlanidList.add(vlanidStr[i]);
					ssidList.add(ssidStr[i]);
					priorityList.add(priorityStr[i]);
					channelList.add(channelStr[i]);
					wlanportList.add(wlanportStr[i]);
				}
			}
		} else // 单个设备
		{
			logger.warn("单个设备");
			String[] result = this.getStringValue(deviceIds).split(",");
			if (!result[0].equals(isSuccess)) {
				ajax = result[1];

				return "ajax";
			} else {
				ajax = result[1];
				list.add(deviceIds);
				vlanidList.add(vlanIdMark);
				ssidList.add(ssid);
				priorityList.add(buss_level);
				channelList.add(channel);
				wlanportList.add(wireless_port);
			}
		}

		ajax = bio.doConfig4Special(userId, list, gw_type, flag, strategy_type,
				vlanidList, ssidList, wlanportList, priorityList, channelList, 
				awifi_type);
		return "ajax";
	}
	
	/**
	 * 批量无线开通通用页面 
	 * 
	 * @return
	 * @throws IOException
	 */
	public String doConfigAll4SpecialBatch() throws IOException {
		logger.warn("WirelessBusinessCtrlACT->doConfigAll4Special()");
		UserRes curUser = (UserRes) session.get("curUser");
		cityId = curUser.getCityId();
		userId = curUser.getUser().getId();
		List<String> list = new ArrayList<String>();
		List<String> vlanidList = new ArrayList<String>();
		List<String> ssidList = new ArrayList<String>();
		List<String> priorityList = new ArrayList<String>();
		List<String> channelList = new ArrayList<String>();
		List<String> wlanportList = new ArrayList<String>();

		String[] deviceStr = null;

		// 如果loid在用户 表中存在并且绑定了设备
		if (deviceIds.contains(",")) // 多个设备
		{
			deviceStr = deviceIds.split(",");

			String[] vlanidStr = vlanIdMark.split(",");
			String[] ssidStr = ssid.split(",");
			String[] priorityStr = buss_level.split(",");
			String[] channelStr = channel.split(",");
			String[] wlanportStr = new String[deviceStr.length];
			if (wireless_port.contains(",")){
				System.arraycopy(wireless_port.split(","), 0, wlanportStr, 0, wlanportStr.length);
			}else if("3".equals(wireless_port)){
				logger.warn("3.equals(wireless_port)");
				for(int i=0;i<wlanportStr.length;i++){
					wlanportStr[i] = "3";
				}
			}
			for (int i = 0; i < deviceStr.length; i ++) {
				String deviceId = deviceStr[i];
				String[] result = this.getStringValue(deviceId).split(",");
				if (result[0].equals(isSuccess)) {
					list.add(deviceId);
					vlanidList.add(vlanidStr[i]);
					ssidList.add(ssidStr[i]);
					priorityList.add(priorityStr[i]);
					channelList.add(channelStr[i]);
					wlanportList.add(wlanportStr[i]);
				}
			}
		} else // 单个设备
		{
			logger.warn("单个设备");
			String[] result = this.getStringValue(deviceIds).split(",");
			if (!result[0].equals(isSuccess)) {
				ajax = result[1];

				return "ajax";
			} else {
				ajax = result[1];
				list.add(deviceIds);
				vlanidList.add(vlanIdMark);
				ssidList.add(ssid);
				priorityList.add(buss_level);
				channelList.add(channel);
				wlanportList.add(wireless_port);
			}
		}

		ajax = bio.doConfig4SpecialBatch(userId, list, gw_type, flag, strategy_type,
				vlanidList, ssidList, wlanportList, priorityList, channelList, 
				awifi_type);
		return "ajax";
	}

	/**
	 * 配置
	 * 
	 * @return
	 * @throws IOException
	 */
	public String doConfig() throws IOException {
		if (do_type.equals("1")) {
			logger.warn("WirelessBusinessCtrlACT->doConfig()");
			UserRes curUser = (UserRes) session.get("curUser");
			cityId = curUser.getCityId();
			userId = curUser.getUser().getId();
			List<String> list = new ArrayList<String>();

			String[] deviceStr = null;
			// 如果loid在用户 表中存在并且绑定了设备
			logger.warn("deviceIds = " + deviceIds.contains(","));
			if (deviceIds.contains(",")) // 多个设备
			{
				deviceStr = deviceIds.split(",");

				for (String deviceId : deviceStr) {
					String[] result = this.getStringValue(deviceId).split(",");
					if (result[0].equals(isSuccess)) {
						list.add(deviceId);
					}
				}
			} else // 单个设备
			{
				String[] result = this.getStringValue(deviceIds).split(",");
				if (!result[0].equals(isSuccess)) {
					ajax = result[1];

					return "ajax";
				} else {
					ajax = result[1];
					list.add(deviceIds);
				}
			}

			ajax = bio.doConfig(userId, list, gw_type, flag, strategy_type,
					vlanIdMark, ssid, wireless_port, buss_level, channel, awifi_type);
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			try {
				queryList = bio.getDetailsForPage(deviceIds, flag, awifi_type);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			logger.debug("WirelessBusinessCtrlACT->daoConfig().getDetailsForPage()");
			try {
				queryList = bio.getDetailsForPage(deviceIds, flag, awifi_type);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "list";
	}

	public String isHaveStrategy() {
		logger.debug("WirelessBusinessCtrlACT->isHaveStrategy()");
		ajax = bio.isHaveStrategy(deviceIds);
		return "ajax";
	}

	public String isBindUser() {
		logger.debug("WirelessBusinessCtrlACT->isBindUser()");
		ajax = bio.isBindUser(deviceIds);
		return "ajax";
	}

	public String isBind() {
		logger.warn("WirelessBusinessCtrlACT->isBind()");
		ajax = bio.isBind(gwShare_queryField, gwShare_queryParam);
		return "ajax";
	}

	public String isAwifi() {
		logger.warn("WirelessBusinessCtrlACT->isAwifi()");
		ajax = bio.isAwifi(deviceIds);
		return "ajax";
	}

	public String getStringValue(String deviceId) {
		String msn = bio.checkLoid(deviceId, gw_type);
		return msn;
	}

	public InputStream getExportExcelStream() {
		FileInputStream fio = null;
		String path = ServletActionContext.getServletContext().getRealPath("/");
		String filePath = path + "loid/batchUPtemplate.xls";
		try {
			fio = new FileInputStream(filePath);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			logger.warn("读取文件异常", e);
		}
		return fio;
	}

	public static void main(String[] args) {
		String s = "a,b,c";
		s = "a";
		System.out.println(s.contains(","));
	}

	public String getDeviceIds() {
		return deviceIds;
	}

	public void setDeviceIds(String deviceIds) {
		this.deviceIds = deviceIds;
	}

	public String getAjax() {
		return ajax;
	}

	public void setAjax(String ajax) {
		this.ajax = ajax;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Map getSession() {
		return session;
	}

	public void setSession(Map session) {
		this.session = session;
	}

	public String getGw_type() {
		return gw_type;
	}

	public void setGw_type(String gwType) {
		gw_type = gwType;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getStrategy_type() {
		return strategy_type;
	}

	public void setStrategy_type(String strategy_type) {
		this.strategy_type = strategy_type;
	}

	public String getVlanIdMark() {
		return vlanIdMark;
	}

	public void setVlanIdMark(String vlanIdMark) {
		this.vlanIdMark = vlanIdMark;
	}

	public String getSsid() {
		return ssid;
	}

	public void setSsid(String ssid) {
		this.ssid = ssid;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setBio(WirelessBusinessCtrlServ bio) {
		this.bio = bio;
	}

	public String getWireless_port() {
		return wireless_port;
	}

	public void setWireless_port(String wireless_port) {
		this.wireless_port = wireless_port;
	}

	public String getBuss_level() {
		return buss_level;
	}

	public void setBuss_level(String buss_level) {
		this.buss_level = buss_level;
	}

	public String getGwShare_queryField() {
		return gwShare_queryField;
	}

	public void setGwShare_queryField(String gwShare_queryField) {
		this.gwShare_queryField = gwShare_queryField;
	}

	public String getGwShare_queryParam() {
		return gwShare_queryParam;
	}

	public void setGwShare_queryParam(String gwShare_queryParam) {
		this.gwShare_queryParam = gwShare_queryParam;
	}

	public List getQueryList() {
		return queryList;
	}

	public void setQueryList(List queryList) {
		this.queryList = queryList;
	}

	public List getList() {
		return list;
	}

	public void setList(List list) {
		this.list = list;
	}

	public String getAwifi_type() {
		return awifi_type;
	}

	public void setAwifi_type(String awifi_type) {
		this.awifi_type = awifi_type;
	}

	public String getDo_type() {
		return do_type;
	}

	public void setDo_type(String do_type) {
		this.do_type = do_type;
	}

	public String getAflag() {
		return aflag;
	}

	public void setAflag(String aflag) {
		this.aflag = aflag;
	}


	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUserInfoType() {
		return userInfoType;
	}

	public void setUserInfoType(String userInfoType) {
		this.userInfoType = userInfoType;
	}

	public String getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(String userInfo) {
		this.userInfo = userInfo;
	}

	public String getSsidNum() {
		return ssidNum;
	}

	public void setSsidNum(String ssidNum) {
		this.ssidNum = ssidNum;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
