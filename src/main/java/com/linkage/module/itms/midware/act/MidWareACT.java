
package com.linkage.module.itms.midware.act;

import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.module.itms.midware.bio.MidWareBIO;

/**
 * 中间件
 * 
 * @author 王森博
 */
@SuppressWarnings("unchecked")
public class MidWareACT extends splitPageAction implements SessionAware
{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(MidWareACT.class);
	// session
	private Map session;
	private String cityId;
	private String oui;
	private String deviceId;
	private String deviceSn;
	private String deviceModel;
	private String adNumber;
	private String status;
	private String area;
	private String group;
	private String phone;
	private String des;
	private String ajax;
	private MidWareBIO midWareBio;

	/**
	 * 中间件增加设备
	 * 
	 * @author wangsenbo
	 * @date Apr 7, 2010
	 * @param
	 * @return String
	 */
	public String add()
	{
		logger.debug("add");
		ajax = midWareBio.addMidWareDevice(cityId,deviceId, oui, deviceSn, deviceModel, adNumber, status,
				area, group, phone, des);
		return "ajax";
	}

	/**
	 * 中间件更新设备
	 * 
	 * @author wangsenbo
	 * @date Apr 7, 2010
	 * @param
	 * @return String
	 */
	public String update()
	{
		logger.debug("update");
		ajax = midWareBio.update(deviceId, oui, deviceSn, deviceModel, adNumber, status,
				area, group, phone, des);
		return "ajax";
	}

	/**
	 * 中间件删除设备
	 * 
	 * @author wangsenbo
	 * @date Apr 7, 2010
	 * @param
	 * @return String
	 */
	public String delete()
	{
		logger.debug("delete");
		ajax = midWareBio.deleteMidWareDevice(deviceId, oui, deviceSn);
		return "ajax";
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
	 * @return the deviceSn
	 */
	public String getDeviceSn()
	{
		return deviceSn;
	}

	/**
	 * @param deviceSn
	 *            the deviceSn to set
	 */
	public void setDeviceSn(String deviceSn)
	{
		this.deviceSn = deviceSn;
	}

	/**
	 * @return the deviceModel
	 */
	public String getDeviceModel()
	{
		return deviceModel;
	}

	/**
	 * @param deviceModel
	 *            the deviceModel to set
	 */
	public void setDeviceModel(String deviceModel)
	{
		this.deviceModel = deviceModel;
	}

	/**
	 * @return the status
	 */
	public String getStatus()
	{
		return status;
	}

	/**
	 * @param status
	 *            the stset
	 */
	public void setStatus(String status)
	{
		this.status = status;
	}

	/**
	 * @return the area
	 */
	public String getArea()
	{
		return area;
	}

	/**
	 * @param area
	 *            the area to set
	 */
	public void setArea(String area)
	{
		this.area = area;
	}

	/**
	 * @return the group
	 */
	public String getGroup()
	{
		return group;
	}

	/**
	 * @param group
	 *            the group to set
	 */
	public void setGroup(String group)
	{
		this.group = group;
	}

	/**
	 * @return the phone
	 */
	public String getPhone()
	{
		return phone;
	}

	/**
	 * @param phone
	 *            the phone to set
	 */
	public void setPhone(String phone)
	{
		this.phone = phone;
	}

	/**
	 * @return the adNumber
	 */
	public String getAdNumber()
	{
		return adNumber;
	}

	/**
	 * @param adNumber
	 *            the adNumber to set
	 */
	public void setAdNumber(String adNumber)
	{
		this.adNumber = adNumber;
	}

	/**
	 * @return the des
	 */
	public String getDes()
	{
		return des;
	}

	/**
	 * @param des
	 *            the des to set
	 */
	public void setDes(String des)
	{
		this.des = des;
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
	 * @return the midWareBio
	 */
	public MidWareBIO getMidWareBio()
	{
		return midWareBio;
	}

	/**
	 * @param midWareBio
	 *            the midWareBio to set
	 */
	public void setMidWareBio(MidWareBIO midWareBio)
	{
		this.midWareBio = midWareBio;
	}

	/**
	 * @return the cityId
	 */
	public String getCityId() {
		return cityId;
	}

	/**
	 * @param cityId the cityId to set
	 */
	public void setCityId(String cityId) {
		this.cityId = cityId;
	}
	
}
