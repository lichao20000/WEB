
package com.linkage.module.ids.act;

import java.io.Serializable;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.ids.bio.HttpQualityCheckBIO;

/**
 * @author yinlei3 (Ailk No.73167)
 * @version 1.0
 * @since 2016年4月27日
 * @category com.linkage.module.ids.act
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class HttpQualityCheckACT implements Serializable
{

	/** 序列化 */
	private static final long serialVersionUID = -8071904431548743610L;
	/** 日志记录 */
	private static Logger logger = LoggerFactory.getLogger(HttpQualityCheckACT.class);
	/** bio */
	private HttpQualityCheckBIO bio = null;
	/** 下载测试用url */
	private String downLoadUrl;
	/** 测试用户 */
	private String userName;
	/** 测试密码 */
	private String password;
	/** 设备序列号 */
	private String deviceSerialnumber;
	/** 设备oui */
	private String oui;
	/** 测试结果map */
	private Map<String, String> httpMap;

	/**
	 * 安徽电信Http拨测初始化
	 * 
	 * @return
	 */
	public String init()
	{
		logger.debug("HttpQualityCheckACT --> init()");
		return "init";
	}

	/**
	 * 安徽电信Http拨测
	 * 
	 * @return
	 */
	public String httpQualityCheck()
	{
		logger.debug("HttpQualityCheckACT --> httpQualityCheck()");
		logger.warn("downLoadUrl : " + downLoadUrl + " userName =  " + userName
				+ " password =  " + password + " deviceSerialnumber = "
				+ deviceSerialnumber + " oui = " + oui);
		httpMap = bio.httpQualityCheck(downLoadUrl, userName, password,
				deviceSerialnumber, oui);
		return "list";
	}

	public String getDownLoadUrl()
	{
		return downLoadUrl;
	}

	public String getUserName()
	{
		return userName;
	}

	public String getPassword()
	{
		return password;
	}

	public Map<String, String> getHttpMap()
	{
		return httpMap;
	}

	public void setBio(HttpQualityCheckBIO bio)
	{
		this.bio = bio;
	}

	public void setDownLoadUrl(String downLoadUrl)
	{
		this.downLoadUrl = downLoadUrl;
	}

	public void setUserName(String userName)
	{
		this.userName = userName;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public void setHttpMap(Map<String, String> httpMap)
	{
		this.httpMap = httpMap;
	}

	public String getDeviceSerialnumber()
	{
		return deviceSerialnumber;
	}

	public void setDeviceSerialnumber(String deviceSerialnumber)
	{
		this.deviceSerialnumber = deviceSerialnumber;
	}

	public String getOui()
	{
		return oui;
	}

	public void setOui(String oui)
	{
		this.oui = oui;
	}
}
