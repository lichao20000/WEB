
package com.linkage.module.bbms.config.act;

import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.system.UserRes;
import com.linkage.module.bbms.config.bio.SNMPConfigBIO;
import com.linkage.module.gwms.obj.tabquery.SgwSecurityOBJ;
import com.opensymphony.xwork2.ActionSupport;

public class SNMPConfigACT extends ActionSupport implements SessionAware
{

	/**
	 * 日志记录
	 */
	private static Logger logger = LoggerFactory.getLogger(SNMPConfigACT.class);
	private String deviceId;
	private int is_enable;
	private String snmp_version;
	private String security_username;
	private int security_level;
	private String auth_protocol;
	private String auth_passwd;
	private String privacy_protocol;
	private String privacy_passwd;
	private String snmp_r_passwd;
	private String snmp_w_passwd;
	private SNMPConfigBIO sumpConfigBio;
	private String ajax;
	// session
	private Map session;
	
	/**
	 * @return the session
	 */
	public Map getSession()
	{
		return session;
	}

	
	/**
	 * @param session the session to set
	 */
	public void setSession(Map session)
	{
		this.session = session;
	}

	/**
	 * 获取设备SNMP配置信息
	 * 
	 * @author wangsenbo
	 * @date 2009-10-23
	 * @return String
	 */
	public String execute()
	{
		logger.debug("execute()");
		ajax = sumpConfigBio.getSNMPConfigStatus(deviceId);
		return "ajax";
	}

	/**
	 * 设置设备SNMP配置信息
	 * 
	 * @author wangsenbo
	 * @date 2009-10-23
	 * @return String
	 */
	public String SNMPConfigSave()
	{
		logger.debug("execute()");
		UserRes curUser = (UserRes) session.get("curUser");
		long accoid = curUser.getUser().getId();
		SgwSecurityOBJ sgwSecurityObj = new SgwSecurityOBJ();
		sgwSecurityObj.setIsEnable(is_enable);
		sgwSecurityObj.setDeviceId(deviceId);
		sgwSecurityObj.setSnmpVersion(snmp_version);
		sgwSecurityObj.setSecurityUsername(security_username);
		sgwSecurityObj.setSecurityLevel(security_level);
		sgwSecurityObj.setAuthProtocol(auth_protocol);
		sgwSecurityObj.setAuthPasswd(auth_passwd);
		sgwSecurityObj.setPrivacyProtocol(privacy_protocol);
		sgwSecurityObj.setPrivacyPasswd(privacy_passwd);
		sgwSecurityObj.setSnmprPasswd(snmp_r_passwd);
		sgwSecurityObj.setSnmpwPasswd(snmp_w_passwd);
		ajax = sumpConfigBio.SNMPConfigSave(accoid,sgwSecurityObj);
		return "ajax";
	}

	/**
	 * @return the snmp_version
	 */
	public String getSnmp_version()
	{
		return snmp_version;
	}

	/**
	 * @param snmp_version
	 *            the snmp_version to set
	 */
	public void setSnmp_version(String snmp_version)
	{
		this.snmp_version = snmp_version;
	}

	/**
	 * @return the security_username
	 */
	public String getSecurity_username()
	{
		return security_username;
	}

	/**
	 * @param security_username
	 *            the security_username to set
	 */
	public void setSecurity_username(String security_username)
	{
		this.security_username = security_username;
	}

	/**
	 * @return the is_enable
	 */
	public int getIs_enable()
	{
		return is_enable;
	}

	/**
	 * @param is_enable
	 *            the is_enable to set
	 */
	public void setIs_enable(int is_enable)
	{
		this.is_enable = is_enable;
	}

	/**
	 * @return the security_level
	 */
	public int getSecurity_level()
	{
		return security_level;
	}

	/**
	 * @param security_level
	 *            the security_level to set
	 */
	public void setSecurity_level(int security_level)
	{
		this.security_level = security_level;
	}

	/**
	 * @return the auth_protocol
	 */
	public String getAuth_protocol()
	{
		return auth_protocol;
	}

	/**
	 * @param auth_protocol
	 *            the auth_protocol to set
	 */
	public void setAuth_protocol(String auth_protocol)
	{
		this.auth_protocol = auth_protocol;
	}

	/**
	 * @return the auth_passwd
	 */
	public String getAuth_passwd()
	{
		return auth_passwd;
	}

	/**
	 * @param auth_passwd
	 *            the auth_passwd to set
	 */
	public void setAuth_passwd(String auth_passwd)
	{
		this.auth_passwd = auth_passwd;
	}

	/**
	 * @return the privacy_protocol
	 */
	public String getPrivacy_protocol()
	{
		return privacy_protocol;
	}

	/**
	 * @param privacy_protocol
	 *            the privacy_protocol to set
	 */
	public void setPrivacy_protocol(String privacy_protocol)
	{
		this.privacy_protocol = privacy_protocol;
	}

	/**
	 * @return the privacy_passwd
	 */
	public String getPrivacy_passwd()
	{
		return privacy_passwd;
	}

	/**
	 * @param privacy_passwd
	 *            the privacy_passwd to set
	 */
	public void setPrivacy_passwd(String privacy_passwd)
	{
		this.privacy_passwd = privacy_passwd;
	}

	/**
	 * @return the snmp_r_passwd
	 */
	public String getSnmp_r_passwd()
	{
		return snmp_r_passwd;
	}

	/**
	 * @param snmp_r_passwd
	 *            the snmp_r_passwd to set
	 */
	public void setSnmp_r_passwd(String snmp_r_passwd)
	{
		this.snmp_r_passwd = snmp_r_passwd;
	}

	/**
	 * @return the snmp_w_passwd
	 */
	public String getSnmp_w_passwd()
	{
		return snmp_w_passwd;
	}

	/**
	 * @param snmp_w_passwd
	 *            the snmp_w_passwd to set
	 */
	public void setSnmp_w_passwd(String snmp_w_passwd)
	{
		this.snmp_w_passwd = snmp_w_passwd;
	}

	/**
	 * @return the ajax
	 */
	public String getAjax()
	{
		return ajax;
	}

	/**
	 * @param ajax
	 *            the ajax to set
	 */
	public void setAjax(String ajax)
	{
		this.ajax = ajax;
	}

	/**
	 * @param sumpConfigBio
	 *            the sumpConfigBio to set
	 */
	public void setSumpConfigBio(SNMPConfigBIO sumpConfigBio)
	{
		this.sumpConfigBio = sumpConfigBio;
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
}
