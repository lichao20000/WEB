package com.linkage.module.gtms.stb.resource.action;

import java.util.List;
import java.util.Map;


import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.system.UserRes;
import com.linkage.module.gtms.stb.resource.serv.StbReportQueryBIO;

import action.splitpage.splitPageAction;

/**
 * 
 * @author hp (Ailk No.)
 * @version 1.0
 * @since 2017-12-6
 * @category com.linkage.module.gtms.stb.resource.action
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public class StbReportQueryACT extends splitPageAction implements SessionAware
{
	private static final long serialVersionUID = 1L;
	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(StbReportQueryACT.class);
	private Map session;
	private StbReportQueryBIO bio;
	//属地
	private String gwShare_cityId;
	//下级属地
	private String citynext;
	//厂商
	private String gwShare_vendorId;
	private String ajax;
	//型号
	private String gwShare_deviceModelId;
	//版本
	private String gwShare_devicetypeId;
	//起始ip
	private String startIp;
	//结束ip
	private String endIp;
	//上线状态
	private String gwShare_onlineStatus;
	private List<Map> data; // 需要导出的结果集，必须是处理过的可以直接显示的结果
	private String[] column; // 需要导出的列名，对应data中的键值
	private String[] title; // 显示在导出文档上的列名
	private String fileName; // 导出的文件名（不包括后缀名）
	/**
	 * 查询属地
	 * 
	 * @return
	 */
	public String getCityNextChild() {
		logger.debug("GwDeviceQueryACT=>getCity()");
		if (null == this.gwShare_cityId) {
			UserRes curUser = (UserRes) session.get("curUser");
			this.gwShare_cityId = curUser.getCityId();
		}
		this.ajax = bio.getCity(gwShare_cityId);
		return "ajax";
	}
	/**
	 * 查询下级属地
	 * 
	 * @return
	 */
	public String getCityNext() {
		logger.debug("GwDeviceQueryACT=>getCity()");
		this.ajax = bio.getCity(citynext);
		return "ajax";
	}

	/**
	 * 查询设备厂商
	 * 
	 * @return
	 */
	public String getVendor() {
		logger.debug("GwDeviceQueryACT=>getVendor()");
		this.ajax = bio.getVendor();
		return "ajax";
	}
	/**
	 * 查询设备型号
	 * 
	 * @param vendorId
	 * @return
	 */
	public String getDeviceModel() {
		logger.debug("GwDeviceQueryACT=>getDeviceModel()");
		this.ajax = bio.getDeviceModel(gwShare_vendorId);
		return "ajax";
	}
	/**
	 * 查询设备版本
	 * 
	 * @return
	 */
	public String getDevicetype() {
		logger.debug("GwDeviceQueryACT=>getDevicetype()");
			this.ajax = bio.getDevicetype(gwShare_deviceModelId);
		return "ajax";
	}
	
	public String getExcel() throws Exception {
		fileName = "机顶盒相关报表查询结果";
		title = new String[] { "设备厂商","型号", "软件版本", "属地", "设备序列号", "业务账号", "MAC",
				"设备IP","最近上报时间"};
		column = new String[] { "vendor_name","device_model", "softwareversion", "city_name","device_serialnumber",
				"serv_account", "cpe_mac", "loopback_ip", "complete_time"};
		data = bio.getExcel(gwShare_vendorId, gwShare_deviceModelId, gwShare_devicetypeId, gwShare_cityId, citynext, startIp, endIp, gwShare_onlineStatus);
		return "excel";
	}
public String query()
{
	data=bio.queryList(curPage_splitPage, num_splitPage, gwShare_vendorId, gwShare_deviceModelId, gwShare_devicetypeId, gwShare_cityId, citynext, startIp, endIp, gwShare_onlineStatus);
	maxPage_splitPage=bio.getMaxPage_splitPage();
	return "success";
}
	public Map getSession()
	{
		return session;
	}

	
	public void setSession(Map session)
	{
		this.session = session;
	}
	
	public StbReportQueryBIO getBio()
	{
		return bio;
	}
	
	public void setBio(StbReportQueryBIO bio)
	{
		this.bio = bio;
	}
	
	public String getGwShare_cityId()
	{
		return gwShare_cityId;
	}
	
	public void setGwShare_cityId(String gwShare_cityId)
	{
		this.gwShare_cityId = gwShare_cityId;
	}
	
	public String getCitynext()
	{
		return citynext;
	}
	
	public void setCitynext(String citynext)
	{
		this.citynext = citynext;
	}
	public String getGwShare_vendorId()
	{
		return gwShare_vendorId;
	}
	
	public void setGwShare_vendorId(String gwShare_vendorId)
	{
		this.gwShare_vendorId = gwShare_vendorId;
	}
	
	public String getAjax()
	{
		return ajax;
	}
	
	public void setAjax(String ajax)
	{
		this.ajax = ajax;
	}
	
	public String getGwShare_deviceModelId()
	{
		return gwShare_deviceModelId;
	}
	
	public void setGwShare_deviceModelId(String gwShare_deviceModelId)
	{
		this.gwShare_deviceModelId = gwShare_deviceModelId;
	}
	
	public String getGwShare_devicetypeId()
	{
		return gwShare_devicetypeId;
	}
	
	public void setGwShare_devicetypeId(String gwShare_devicetypeId)
	{
		this.gwShare_devicetypeId = gwShare_devicetypeId;
	}
	
	public String getStartIp()
	{
		return startIp;
	}
	
	public void setStartIp(String startIp)
	{
		this.startIp = startIp;
	}
	
	public String getEndIp()
	{
		return endIp;
	}
	
	public void setEndIp(String endIp)
	{
		this.endIp = endIp;
	}
	
	public String getGwShare_onlineStatus()
	{
		return gwShare_onlineStatus;
	}
	
	public void setGwShare_onlineStatus(String gwShare_onlineStatus)
	{
		this.gwShare_onlineStatus = gwShare_onlineStatus;
	}
	
	public List<Map> getData()
	{
		return data;
	}
	
	public void setData(List<Map> data)
	{
		this.data = data;
	}
	
	public String[] getColumn()
	{
		return column;
	}
	
	public void setColumn(String[] column)
	{
		this.column = column;
	}
	
	public String[] getTitle()
	{
		return title;
	}
	
	public void setTitle(String[] title)
	{
		this.title = title;
	}
	
	public String getFileName()
	{
		return fileName;
	}
	
	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}
	
	
}
