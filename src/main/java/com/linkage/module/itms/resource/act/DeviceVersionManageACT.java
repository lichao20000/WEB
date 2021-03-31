/**
 * 
 */

package com.linkage.module.itms.resource.act;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.util.ExportExcelUtil;
import com.linkage.module.itms.resource.bio.DeviceVersionManageBIO;

/**
 * 新疆需求：版本库管理
 * @author chenjie
 * @version 1.0
 * @since 2012-12-14
 */
public class DeviceVersionManageACT extends splitPageAction implements ServletRequestAware, ServletResponseAware
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7791709039980338445L;

	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(DeviceVersionManageACT.class);
	
	/** bio **/
	private DeviceVersionManageBIO bio; 

	/** id **/
	private String id;
	/** 厂商 **/
	private String vendor;
	/** 设备型号 **/
	private String device_model;
	/** 硬件版本 **/
	private String hard_version;
	/** 软件版本 **/
	private String soft_version;
	/** 特别版本 **/
	private String spec_version;
	/** 版本添加开始时间 **/
	private String start_time;
	/** 版本添加结束时间 **/
	private String end_time;
	/** 文件路径 **/
	private File file_path = null;
	/** 修改页面的修改对象 **/
	private Map modifyVersion;
	
	/** 查询出来的设备版本库 **/
	List<Map> deviceVersionList = null;
	
	/** 厂商集合 **/
	List<Map> vendorList = null;
	/** 型号集合 **/
	List<Map> deviceModelList = null;
	
	/** 存放在WEB工程的路径 **/
	public static String SYSTEM_PATH = "dev-version-file" +  File.separator;
	
	private String ajax;
	
	private String filepath = null;
	
	private HttpServletRequest request;
	private HttpServletResponse response;
	
	/**
	 * 查询所有设备版本库信息
	 * @return
	 */
	public String queryDeviceVersion()
	{
		logger.debug("queryDeviceVersion()");
		
		if(!StringUtil.IsEmpty(start_time))
		{
			start_time = dealTime(start_time);
		}
		if(!StringUtil.IsEmpty(end_time))
		{
			end_time = dealTime(end_time);
		}
		
		deviceVersionList = bio.queryDeviceVersion(vendor, device_model, hard_version, soft_version, spec_version, start_time, end_time
				, curPage_splitPage, num_splitPage);
		maxPage_splitPage = bio.getMaxPage_splitPage();
		
		return "queryDeviceVersion";
	} 
	
	/**
	 * 添加版本库文件
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public String addDeviceVersion()
	{
		logger.debug("addDeviceVersion()");
		
		ServletRequest servletRequest = (ServletRequest)request;
		
		// 开始时间
		long addTime = System.currentTimeMillis()/1000;
		
		// 是否需要上传文件
		boolean needUploadFile = file_path == null ? false : true;
		
		if(needUploadFile)
		{
			// 保存到WEB系统里面文件名
			String newFileName = "versionfile_" + vendor + "_" + device_model + "_" + hard_version + "_" + soft_version + "_" + spec_version + "_" + addTime;
			// 保存的路径
			String storePath = servletRequest.getRealPath("/") + SYSTEM_PATH; // /export/home/itms/WEB_JS/ + xxx/
			
			// 插入数据库结果
			String dbResult = bio.addDeviceVersion(vendor, device_model, hard_version, soft_version, spec_version, storePath + newFileName, addTime);
			// 上传文件结果
			String fileResult = bio.saveFile(file_path, storePath, newFileName);
			
			// 拼接结果页面展示
			ajax = dbResult + "|" + fileResult;
		}
		else
		{
			// 插入数据库结果
			String dbResult = bio.addDeviceVersion(vendor, device_model, hard_version, soft_version, spec_version, "", addTime);
			
			// 拼接结果页面展示,-1标示不需要上传文件
			ajax = dbResult + "|" + "-1";
		}
		
		return "addDeviceVersion";
	}
	
	/**
	 * 展示修改页面
	 * @return
	 */
	public String queryForModify()
	{
		logger.debug("queryForModify()");
		
		modifyVersion = bio.queryForModify(id);
		vendor = (String)modifyVersion.get("vendor_id");
		device_model = (String)modifyVersion.get("device_model_id");
		
		vendorList = bio.getVendorList();
		deviceModelList = bio.getDeviceModelList(vendor);
		
		return "queryForModify";
	}
	
	/**
	 * 修改版本库
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public String modifyDeviceVersion()
	{
		logger.debug("modifyDeviceVersion()");
		
		ServletRequest servletRequest = (ServletRequest)request;
		
		// 开始时间
		long addTime = System.currentTimeMillis()/1000;
		
		// 是否需要上传文件
		boolean needUploadFile = file_path == null ? false : true;
		
		if(needUploadFile)
		{
			// 保存到WEB系统里面文件名
			String newFileName = "versionfile_" + vendor + "_" + device_model + "_" + hard_version + "_" + soft_version + "_" + spec_version + "_" + addTime;
			// 保存的路径
			String storePath = servletRequest.getRealPath("/") + SYSTEM_PATH; // /export/home/itms/WEB_JS/ + xxx/
			
			// 插入数据库结果
			String dbResult = bio.modifyDeviceVersion(id, vendor, device_model, hard_version, soft_version, spec_version, storePath + newFileName, addTime);
			// 上传文件结果
			String fileResult = bio.saveFile(file_path, storePath, newFileName);
			// 删除原来的版本文件
			bio.deleteFile(filepath);
			
			// 拼接结果页面展示
			ajax = dbResult + "|" + fileResult;
		}
		else
		{
			// 插入数据库结果
			String dbResult = bio.modifyDeviceVersion(id, vendor, device_model, hard_version, soft_version, spec_version, "", addTime);
			
			// 拼接结果页面展示,-1标示不需要上传文件
			ajax = dbResult + "|" + "-1";
		}
		
		return "modifyDeviceVersion";
	}
	
	/**
	 * 删除版本库
	 * @return
	 */
	public String deleteDeviceVersion()
	{
		logger.debug("deleteDeviceVersion()");
		
		String dbResult = bio.deleteDeviceVersion(id);
		bio.deleteFile(filepath);
		ajax = dbResult;
		
		return "ajax";
	}
	
	/**
	 * excel导出
	 * @return
	 */
	public String excel()
	{
		logger.debug("excel()");
		
		deviceModelList = bio.queryDeviceVersion(vendor, device_model, hard_version, soft_version, spec_version, start_time, end_time);
		
		ExportExcelUtil util = new ExportExcelUtil("版本库查询结果统计", new String[]{"厂商", "设备型号", "特定版本", "硬件版本", "软件版本", "入库时间", "版本文件"});
		try {
			util.export(response, deviceModelList, new String[]{"vendor_name", "device_model_name", "specversion", "hardwareversion", "softwareversion", "add_time", "file_path" }, "devversion");
		} catch (Exception e) {
			logger.error("导出版本库excel失败:", e);
		}
		return null;
	}

	/**
	 * 下载版本文件
	 * @return
	 */
	public String download()
	{
		bio.download(filepath,response);
		return null;
	}
	
	/**
	 * 处理事件格式转换成秒形式
	 * @param time
	 * @return
	 */
	public String dealTime(String time){
		logger.debug("dealTime({})", time);
		
    	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	Date date=new Date();
		try
		{
			date = sdf.parse(time);
		}
		catch (ParseException e)
		{
			logger.warn("选择开始或者结束的时间格式不对:"+time);
		}
		return String.valueOf(date.getTime()/1000);
    }

	public DeviceVersionManageBIO getBio() {
		return bio;
	}

	public void setBio(DeviceVersionManageBIO bio) {
		this.bio = bio;
	}

	public String getDevice_model() {
		return device_model;
	}

	public void setDevice_model(String device_model) {
		this.device_model = device_model;
	}

	public List<Map> getDeviceVersionList() {
		return deviceVersionList;
	}

	public void setDeviceVersionList(List<Map> deviceVersionList) {
		this.deviceVersionList = deviceVersionList;
	}

	public String getEnd_time() {
		return end_time;
	}

	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}

	public String getHard_version() {
		return hard_version;
	}

	public void setHard_version(String hard_version) {
		this.hard_version = hard_version;
	}

	public String getSoft_version() {
		return soft_version;
	}

	public void setSoft_version(String soft_version) {
		this.soft_version = soft_version;
	}

	public String getSpec_version() {
		return spec_version;
	}

	public void setSpec_version(String spec_version) {
		this.spec_version = spec_version;
	}

	public String getStart_time() {
		return start_time;
	}

	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}

	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	public File getFile_path() {
		return file_path;
	}

	public void setFile_path(File file_path) {
		this.file_path = file_path;
	}

	public String getAjax() {
		return ajax;
	}

	public void setAjax(String ajax) {
		this.ajax = ajax;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<Map> getDeviceModelList() {
		return deviceModelList;
	}

	public void setDeviceModelList(List<Map> deviceModelList) {
		this.deviceModelList = deviceModelList;
	}

	public List<Map> getVendorList() {
		return vendorList;
	}

	public void setVendorList(List<Map> vendorList) {
		this.vendorList = vendorList;
	}

	public Map getModifyVersion() {
		return modifyVersion;
	}

	public void setModifyVersion(Map modifyVersion) {
		this.modifyVersion = modifyVersion;
	}

	public String getFilepath() {
		return filepath;
	}

	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}

	
}
