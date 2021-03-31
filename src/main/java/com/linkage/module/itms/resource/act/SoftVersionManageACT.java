package com.linkage.module.itms.resource.act;

import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.litms.system.UserRes;
import com.linkage.module.itms.resource.bio.SoftVersionManageBIO;

public class SoftVersionManageACT extends splitPageAction implements
		SessionAware {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// 日志记录
	private static Logger logger = LoggerFactory
			.getLogger(SoftVersionManageACT.class);

	private Map session;
	/** 属地ID */
	private String cityId = null;
	
	/** 属地ID新增 */
	private String cityId_add = null;
	
	/** 设备厂商ID */
	private String vendor = null;

	/** 设备型号ID */
	private String device_model = null;

	/** 设备版本ID */
	private String devicetypeId = null;

	/** 设备硬件版本 */
	private String hardwareversion = null;

	/** 设备厂商 新增 */
	private String vendor_add = null;

	/** 设备型号 新增 */
	private String device_model_add = null;

	/** 设备原始版本ID */
	private String devicetypeIdOld = null;

	/** 目标软件版本 */
	private String devicetypeIdNew = null;
	/** 导出文件列标题 */
	private String[] title = null;
	
	/** 导出文件列 */
	private String[] column = null;
	
	/** 导出文件名 */
	private String fileName = null;

	private long acc_id;

	private String ajax = null;

	private String tempId = "";

	private List<Map> softVersionList = null;

	private List<Map> softVersionLogList = null;
	
	private List<Map> data = null;

	private SoftVersionManageBIO softVersionManageBIO;

	/**
	 * 初始化查询页面
	 * 
	 * @return
	 */
	public String init() {

		return "queryForm";
	}

	/**
	 * 查询列表
	 * 
	 * @return
	 */
	public String getVersionList() {
		logger.debug("SoftVersionManageACT-> getVersionList");
		softVersionList = softVersionManageBIO.getVersionList(cityId,vendor,
				device_model, devicetypeId, tempId, hardwareversion,
				curPage_splitPage, num_splitPage);

		maxPage_splitPage = softVersionManageBIO.getMaxPage(cityId,vendor,
				device_model, devicetypeId, tempId, hardwareversion,
				curPage_splitPage, num_splitPage);
		return "queryList";
	}

	/**
	 * 查询日志列表
	 * 
	 * @return
	 */
	public String getVersionLogList() {
		logger.debug("SoftVersionManageACT-> getVersionLogList");
		softVersionLogList = softVersionManageBIO.getVersionLogList(vendor,
				device_model, devicetypeId, tempId, hardwareversion,curPage_splitPage, num_splitPage);
		maxPage_splitPage = softVersionManageBIO.getVersionLogMaxPage(vendor,
				device_model, devicetypeId, tempId, hardwareversion,curPage_splitPage, num_splitPage);
		return "queryLogList";
	}

	/**
	 * 新增
	 * 
	 * @return
	 */
	public String addInfo() {

		logger.warn("addInfo()");
		UserRes curUser = (UserRes) session.get("curUser");
		acc_id = curUser.getUser().getId();
		ajax = softVersionManageBIO.addInfo(devicetypeIdOld, devicetypeIdNew,
				tempId, acc_id,cityId_add);
		return "ajax";
	}

	/**
	 * 删除
	 * 
	 * @return
	 */
	public String deleInfo() {
		logger.warn("deleInfo()");
		UserRes curUser = (UserRes) session.get("curUser");
		acc_id = curUser.getUser().getId();
		ajax = softVersionManageBIO.deleInfo(devicetypeIdOld, devicetypeIdNew,tempId, acc_id,cityId);
		return "ajax";
	}
	
	public String parseExcel()
	{
		logger.debug("parseExcel");


		fileName = "软件管理清单";
		title = new String[6];
		column = new String[6];
		title[0] = "设备厂商";
		title[1] = "设备型号";
		title[2] = "硬件版本";
		title[3] = "原始版本";
		title[4] = "目标版本";
		title[5] = "对应关系类型"; 

		column[0] = "vendorName";
		column[1] = "device_model";
		column[2] = "hardwareversion";
		column[3] = "origSoftWareVersion";
		column[4] = "targetSoftWareVersion";
		column[5] = "tempName"; 

		data = softVersionManageBIO.parseExcel(vendor, device_model, devicetypeId, tempId, hardwareversion);
		return "excel";
	}

	/**
	 * 获取原软件版本的复选框和目标软件版本的下拉框
	 * 
	 * @return
	 */
	public String getVersionCheckList() {
		ajax = softVersionManageBIO.getVersionCheckList(vendor_add,
				device_model_add);
		return "ajax";
	}
	
	public String getVersion() {
		ajax = softVersionManageBIO.getVersion(vendor_add,
				device_model_add,devicetypeIdNew);
		return "ajax";
	}

	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	public String getDevice_model() {
		return device_model;
	}

	public void setDevice_model(String device_model) {
		this.device_model = device_model;
	}

	public String getDevicetypeId() {
		return devicetypeId;
	}

	public void setDevicetypeId(String devicetypeId) {
		this.devicetypeId = devicetypeId;
	}

	public List<Map> getSoftVersionList() {
		return softVersionList;
	}

	public void setSoftVersionList(List<Map> softVersionList) {
		this.softVersionList = softVersionList;
	}

	public SoftVersionManageBIO getSoftVersionManageBIO() {
		return softVersionManageBIO;
	}

	public void setSoftVersionManageBIO(
			SoftVersionManageBIO softVersionManageBIO) {
		this.softVersionManageBIO = softVersionManageBIO;
	}

	public String getAjax() {
		return ajax;
	}

	public void setAjax(String ajax) {
		this.ajax = ajax;
	}

	public String getDevicetypeIdOld() {
		return devicetypeIdOld;
	}

	public void setDevicetypeIdOld(String devicetypeIdOld) {
		this.devicetypeIdOld = devicetypeIdOld;
	}

	public String getVendor_add() {
		return vendor_add;
	}

	public void setVendor_add(String vendor_add) {
		this.vendor_add = vendor_add;
	}

	public String getDevice_model_add() {
		return device_model_add;
	}

	public void setDevice_model_add(String device_model_add) {
		this.device_model_add = device_model_add;
	}

	public String getDevicetypeIdNew() {
		return devicetypeIdNew;
	}

	public void setDevicetypeIdNew(String devicetypeIdNew) {
		this.devicetypeIdNew = devicetypeIdNew;
	}

	public String getTempId() {
		return tempId;
	}

	public void setTempId(String tempId) {
		this.tempId = tempId;
	}

	public String getHardwareversion() {
		return hardwareversion;
	}

	public void setHardwareversion(String hardwareversion) {
		this.hardwareversion = hardwareversion;
	}

	public long getAcc_id() {
		return acc_id;
	}

	public void setAcc_id(long acc_id) {
		this.acc_id = acc_id;
	}

	public Map getSession() {
		return session;
	}

	public void setSession(Map session) {
		this.session = session;
	}

	public List<Map> getSoftVersionLogList() {
		return softVersionLogList;
	}

	public void setSoftVersionLogList(
			List<Map> softVersionLogList) {
		this.softVersionLogList = softVersionLogList;
	}

	
	public String[] getTitle()
	{
		return title;
	}

	
	public void setTitle(String[] title)
	{
		this.title = title;
	}

	
	public String[] getColumn()
	{
		return column;
	}

	
	public void setColumn(String[] column)
	{
		this.column = column;
	}

	
	public String getFileName()
	{
		return fileName;
	}

	
	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}

	
	public List<Map> getData()
	{
		return data;
	}

	
	public void setData(List<Map> data)
	{
		this.data = data;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public String getCityId_add() {
		return cityId_add;
	}

	public void setCityId_add(String cityId_add) {
		this.cityId_add = cityId_add;
	}
	
}
