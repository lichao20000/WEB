
package com.linkage.module.ids.act;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.ids.bio.VoiceDeviceQueryBIO;
import com.opensymphony.xwork2.ActionSupport;

/**
 * @author Administrator (Ailk No.)
 * @version 1.0
 * @since 2013-10-18
 * @category com.linkage.module.ids.act
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class VoiceDeviceQueryACT extends ActionSupport implements ServletRequestAware
{

	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(VoiceDeviceQueryACT.class);
	private HttpServletRequest request;
	VoiceDeviceQueryBIO bio = null;
	private String column1;
	private String column2;
	private String column3;
	private String column4;
	private String city_id;
	private String device_id;
	private String wanType;
	private String oui;
	private String device_sn;
	private String ip_address;
	private String default_gateway;
	private String code;
	private String ajax;
	private Map<String, String> voiceMap;

	@Override
	public String execute() throws Exception
	{
		// TODO Auto-generated method stub
		return "init";
	}

	public String getDefaultdiag()
	{
		ajax = bio.getDefaultdiag();
		return "ajax";
	}

	public String queryVoiceService()
	{
		voiceMap = bio.queryVoiceService(city_id, oui, device_id, column1, column2,
				column3, column4);
		code = voiceMap.get("code");
		return "list";
	}

	@Override
	public void setServletRequest(HttpServletRequest arg0)
	{
		this.request = arg0;
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

	public String getCity_id()
	{
		return city_id;
	}

	public void setCity_id(String city_id)
	{
		this.city_id = city_id;
	}

	public String getDevivce_id()
	{
		return device_id;
	}

	public void setDevivce_id(String devivce_id)
	{
		this.device_id = devivce_id;
	}

	public String getWanType()
	{
		return wanType;
	}

	public void setWanType(String wanType)
	{
		this.wanType = wanType;
	}

	public String getOui()
	{
		return oui;
	}

	public void setOui(String oui)
	{
		this.oui = oui;
	}

	public VoiceDeviceQueryBIO getBio()
	{
		return bio;
	}

	public void setBio(VoiceDeviceQueryBIO bio)
	{
		this.bio = bio;
	}

	public String getDevice_id()
	{
		return device_id;
	}

	public void setDevice_id(String device_id)
	{
		this.device_id = device_id;
	}

	public String getDevice_sn()
	{
		return device_sn;
	}

	public void setDevice_sn(String device_sn)
	{
		this.device_sn = device_sn;
	}

	public String getIp_address()
	{
		return ip_address;
	}

	public void setIp_address(String ip_address)
	{
		this.ip_address = ip_address;
	}

	public String getDefault_gateway()
	{
		return default_gateway;
	}

	public String getCode()
	{
		return code;
	}

	public void setCode(String code)
	{
		this.code = code;
	}

	public void setDefault_gateway(String default_gateway)
	{
		this.default_gateway = default_gateway;
	}

	public Map<String, String> getVoiceMap()
	{
		return voiceMap;
	}

	public void setVoiceMap(Map<String, String> voiceMap)
	{
		this.voiceMap = voiceMap;
	}
}
