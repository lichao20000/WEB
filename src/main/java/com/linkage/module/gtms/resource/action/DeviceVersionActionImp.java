package com.linkage.module.gtms.resource.action;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.module.gtms.resource.serv.DeviceVersionServ;

/**
 * 
 * @author Administrator(工号) Tel:
 * @version 1.0
 * @since Apr 19, 2012 10:58:32 AM
 * @category com.linkage.module.gtms.resource.action
 * @copyright 南京联创科技 网管科技部
 *
 */
public class DeviceVersionActionImp extends splitPageAction implements DeviceVersionAction{

	private static Logger logger = LoggerFactory.getLogger(DeviceVersionActionImp.class);

	/** 设备厂商ID */
	private String vendorId = null;

	/** 设备型号ID */
	private String deviceModelId = null;

	/** 设备版本ID */
	private String devicetypeId = null;

	/** 设备类型  1表示ITMS    2表示BBMS */
	private String gw_type = null;

	/** 设备类型 1是e8-b,2是机卡一体e8-c,3是机卡分离型 e8-c*/
	private int rela_dev_type = -1;

	private String bssDevPort = null;

	private String ajax = null;

	private List<Map> deviceVersionList = null;

	private List<Map> bssDevPortLsit = null;

	private DeviceVersionServ bio;

	private List<Map> data;
	
	private String fileName;
	
	private String[] column;
	
	private String[] title;

	/**
	 * 初始化查询界面
	 * 
	 * @return
	 */
	public String queryForm() {

		logger.debug("into queryForm()");
		bssDevPortLsit = bio.querySpecName();

		if ("1".equals(gw_type)) {
			return "itms";
		} else {
			return "bbms";  
		}
	}





	/**
	 * 查询列表
	 * 
	 * @return
	 */
	public String queryList() {

		logger.debug("into queryList()");

		deviceVersionList = bio.queryDeviceList(vendorId, deviceModelId, devicetypeId, rela_dev_type, bssDevPort,
				curPage_splitPage, num_splitPage);

		maxPage_splitPage = bio.queryDeviceCount(vendorId, deviceModelId, devicetypeId, rela_dev_type, bssDevPort,
				curPage_splitPage, num_splitPage);

		return "queryVersionList";
	}

	public String toExcel() {

		logger.debug("into toExcel()");

		deviceVersionList = bio.queryDeviceList(vendorId, deviceModelId, devicetypeId, rela_dev_type, bssDevPort);
		
		column = new String[]{"vendor_add","device_model","softwareversion","spec_name"};
		title=new String[]{"设备厂商","设备型号","软件版本","设备终端规格"};
		fileName = "StandardVersion";
		data = deviceVersionList;
		return "excel";
	}

	public String getVendorId() {
		return vendorId;
	}


	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}


	public String getDeviceModelId() {
		return deviceModelId;
	}


	public void setDeviceModelId(String deviceModelId) {
		this.deviceModelId = deviceModelId;
	}


	public String getDevicetypeId() {
		return devicetypeId;
	}


	public void setDevicetypeId(String devicetypeId) {
		this.devicetypeId = devicetypeId;
	}


	public List<Map> getDeviceVersionList() {
		return deviceVersionList;
	}


	public void setDeviceVersionList(List<Map> deviceVersionList) {
		this.deviceVersionList = deviceVersionList;
	}


	public String getGw_type() {
		return gw_type;
	}


	public void setGw_type(String gw_type) {
		this.gw_type = gw_type;
	}


	public String getAjax() {
		return ajax;
	}


	public void setAjax(String ajax) {
		this.ajax = ajax;
	}


	public DeviceVersionServ getBio() {
		return bio;
	}


	public void setBio(DeviceVersionServ bio) {
		this.bio = bio;
	}




	public int getRela_dev_type() {
		return rela_dev_type;
	}




	public void setRela_dev_type(int rela_dev_type) {
		this.rela_dev_type = rela_dev_type;
	}






	public List<Map> getBssDevPortLsit()
	{
		return bssDevPortLsit;
	}






	public void setBssDevPortLsit(List<Map> bssDevPortLsit)
	{
		this.bssDevPortLsit = bssDevPortLsit;
	}






	public String getBssDevPort()
	{
		return bssDevPort;
	}






	public void setBssDevPort(String bssDevPort)
	{
		this.bssDevPort = bssDevPort;
	}






	public List<Map> getData()
	{
		return data;
	}






	public void setData(List<Map> data)
	{
		this.data = data;
	}





	
	public String getFileName()
	{
		return fileName;
	}





	
	public void setFileName(String fileName)
	{
		this.fileName = fileName;
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

}
