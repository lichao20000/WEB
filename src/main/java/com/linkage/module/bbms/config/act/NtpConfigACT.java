package com.linkage.module.bbms.config.act;

import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.system.UserRes;
import com.linkage.module.bbms.config.bio.NtpConfigBIO;
import com.linkage.module.gwms.obj.gw.TimeNtpOBJ;
import com.linkage.module.gwms.util.StringUtil;

/**
 * @author Jason(3412)
 * @date 2009-10-20
 */
public class NtpConfigACT implements SessionAware{

	/** log */
	private static Logger logger = LoggerFactory
			.getLogger(NtpConfigACT.class);
	// 设备ID
	private String deviceId;
	// NTPserver1
	private String ntpServer1;
	// NTPserver2
	private String ntpServer2;
	// 时区名称 (Beijing, Chongquing, Hong Kong, Urumqi)
	private String timeZoneName;
	// 时区偏移 (+08:00)
	private String timeZone;
	//是否启用(思科私有)
	private String apply;
	//配置或采集方式 1:tr069 2:snmp
	private String configType;
	//是否启用(规范)
	private String enable;
	//BIO
	private NtpConfigBIO ntpConfigBio;
	//AJAX
	private String ajax;
	
	private Map session;
	
	/**
	 * 页面入口 do nothing
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2009-10-20
	 * @return String "success"
	 */
	public String execute() {
		logger.debug("execute()");
		// 页面加载

		return "success";
	}

	/**
	 * 采集NTP服务器，时区信息
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2009-10-20
	 * @return String
	 */
	public String gatherNtp() {
		logger.debug("gatherNtp()");
		// 采集NTP/时区
		ajax = ntpConfigBio.gatherNtp(deviceId, configType);
		
		return "ajax";
	}

	
	/**
	 * 
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2009-10-20
	 * @return String
	 */
	public String configNtp() {
		logger.debug("configNtp()");
		UserRes curUser = (UserRes) session.get("curUser");
		if("1".equals(configType)){
			// 配置NTP/时区(TR069)
			long accoid = curUser.getUser().getId();
			TimeNtpOBJ timeNtpObj = new TimeNtpOBJ();
			timeNtpObj.setDeviceId(deviceId);
			timeNtpObj.setEnable(StringUtil.getIntegerValue(enable));
			timeNtpObj.setNtpServer1(ntpServer1);
			timeNtpObj.setNtpServer2(ntpServer2);
			timeNtpObj.setTimezone(timeZone);
			ajax = ntpConfigBio.configNtpTr069(accoid, timeNtpObj);	
		}else{
			// 配置NTP/时区(SNMP)
			ajax = ntpConfigBio.configNtpSnmp(deviceId, enable, ntpServer1, ntpServer2);
		}
		return "ajax";
	}

	
	
	/** getter, setter methods */
	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getNtpServer1() {
		return ntpServer1;
	}

	public void setNtpServer1(String ntpServer1) {
		this.ntpServer1 = ntpServer1;
	}

	public String getNtpServer2() {
		return ntpServer2;
	}

	public void setNtpServer2(String ntpServer2) {
		this.ntpServer2 = ntpServer2;
	}

	public String getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

	public String getTimeZoneName() {
		return timeZoneName;
	}

	public void setTimeZoneName(String timeZoneName) {
		this.timeZoneName = timeZoneName;
	}

	public String getApply() {
		return apply;
	}

	public void setApply(String apply) {
		this.apply = apply;
	}

	public void setNtpConfigBio(NtpConfigBIO ntpConfigBio) {
		this.ntpConfigBio = ntpConfigBio;
	}

	public String getConfigType() {
		return configType;
	}

	public void setConfigType(String configType) {
		this.configType = configType;
	}

	public String getEnable() {
		return enable;
	}

	public void setEnable(String enable) {
		this.enable = enable;
	}

	public String getAjax() {
		return ajax;
	}

	public void setSession(Map session) {
		this.session = session;
	}

}
