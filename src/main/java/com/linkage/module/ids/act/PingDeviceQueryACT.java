
package com.linkage.module.ids.act;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.system.UserRes;
import com.linkage.module.ids.bio.PingDeviceQueryBIO;
import com.opensymphony.xwork2.ActionSupport;

/**
 * @author Administrator (Ailk No.)
 * @version 1.0
 * @since 2013-10-18
 * @category com.linkage.module.ids.act
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class PingDeviceQueryACT extends ActionSupport implements ServletRequestAware
{

	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(PingDeviceQueryACT.class);
	private HttpServletRequest request;
	PingDeviceQueryBIO bio = null;
	private String column1;
	private String column2;
	private String column3;
	private String column4;
	
	private String city_id;
	private String oui;
	private String device_id;
	private String ip;
	private String wanType;
	private String wan_interface;
	
	private String code;
	private String ajax;
	
	private Map<String, String> pingMap = null;
	

	@Override
	public String execute() throws Exception
	{
		return "init";
	}

	public String getDefaultdiag()
	{
		ajax = bio.getDefaultdiag();
		return "ajax";
	}

	public String queryWanService()
	{
		logger.warn("wantype:"+wanType);
		pingMap = bio.queryPingService(city_id, oui, device_id, wanType, column1,
				column2, column3, column4, wan_interface);
		code = pingMap.get("code");
		return "list";
	}
	

	
	@Override
	public void setServletRequest(HttpServletRequest arg0)
	{
		this.request = arg0;
	}

	public PingDeviceQueryBIO getBio()
	{
		return bio;
	}

	public void setBio(PingDeviceQueryBIO bio)
	{
		this.bio = bio;
	}

	public String getAjax()
	{
		return ajax;
	}

	public void setAjax(String ajax)
	{
		this.ajax = ajax;
	}

	public String getColumn1()
	{
		return column1;
	}

	public void setColumn1(String column1)
	{
		this.column1 = column1;
	}

	public String getColumn2()
	{
		return column2;
	}

	public void setColumn2(String column2)
	{
		this.column2 = column2;
	}

	public String getColumn3()
	{
		return column3;
	}

	public void setColumn3(String column3)
	{
		this.column3 = column3;
	}

	public String getColumn4()
	{
		return column4;
	}

	public void setColumn4(String column4)
	{
		this.column4 = column4;
	}


	public String getWanType()
	{
		return wanType;
	}

	public void setWanType(String wanType)
	{
		this.wanType = wanType;
	}

	public String getCity_id()
	{
		return city_id;
	}

	public void setCity_id(String city_id)
	{
		this.city_id = city_id;
	}

	public String getOui()
	{
		return oui;
	}

	public void setOui(String oui)
	{
		this.oui = oui;
	}

	public String getWan_interface()
	{
		return wan_interface;
	}

	public void setWan_interface(String wan_interface)
	{
		this.wan_interface = wan_interface;
	}

	public Map<String, String> getPingMap()
	{
		return pingMap;
	}

	public void setPingMap(Map<String, String> pingMap)
	{
		this.pingMap = pingMap;
	}

	
	
	public String getCode()
	{
		return code;
	}

	
	public void setCode(String code)
	{
		this.code = code;
	}

	public String getIp()
	{
		return ip;
	}

	
	public void setIp(String ip)
	{
		this.ip = ip;
	}

	
	public String getDevice_id()
	{
		return device_id;
	}

	
	public void setDevice_id(String device_id)
	{
		this.device_id = device_id;
	}

	
}
