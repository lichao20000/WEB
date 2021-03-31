
package com.linkage.module.gtms.stb.resource.action;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.litms.LipossGlobals;
import com.linkage.module.gtms.stb.resource.serv.StbUpgradeVersionBIO;
import com.linkage.module.gwms.Global;

/**
 * 机顶盒版本升级配置页面
 * @author wangyan10(Ailk NO.76091)
 * @version 1.0
 * @since 2018-1-2
 */
public class StbUpgradeVersionACT extends splitPageAction
{

	/** 序列化 */
	private static final long serialVersionUID = 1L;
	/** 日志 */
	private static Logger logger = LoggerFactory.getLogger(StbUpgradeVersionACT.class);
	/** 厂商集合 */
	@SuppressWarnings("rawtypes")
	private List vendorList;
	/** 文件路径ID */
	private String pathId = "";
	/** 文件路径ID */
	private String pathIdNew = "";
	/** 软件升级类型 */
	private String temp_id = "";
	/** 厂商ID */
	private String vendorId = "";
	/** 型号 */
	private String deviceModelId = "";
	/** 型号 */
	private String source_devicetypeId = "";
	/** 型号 */
	private String goal_devicetypeId = "";
	/** 版本 */
	private String device_version = "";
	/** 版本 */
	private String belong = "";
	private String upgradeType = "";
	private String ajax = "";
	/** 下发结果查询用service */
	private StbUpgradeVersionBIO bio;
	private List<Map<String, String>> tempList;
	private List<Map<String, String>> filePathList;
	/** 查询总数 */
	private int queryCount;
	/** 版本 */
	private String hardwareversion = "";
	/**公网路径*/
	private String version_path="";
	/**专网路径*/
	private String special_path="";
	/**DCN路径*/
	private String dcn_path="";
	/** 所属平台id */
	private String platformId = "";
	private String valid="";
	
	private String instArea=Global.instAreaShortName;
	
	public String init(){
		logger.warn("instArea:"+instArea);
		return "init";
	}

	/**
	 * 机顶盒升级版本对应关系列表
	 * @param
	 * @return
	 */
	public String getStbUpgradeTempList()
	{
		logger.debug("adverResultList");
		if(Global.HNLT.equals(instArea)){
			tempList = bio.getStbUpgradeTempList_hnlt(curPage_splitPage,num_splitPage,vendorId,
					deviceModelId,source_devicetypeId, goal_devicetypeId, upgradeType, 
					platformId,hardwareversion,valid);
			logger.debug(tempList.toString());
			maxPage_splitPage = bio.countAdverResultList_hnlt(curPage_splitPage, num_splitPage, 
					vendorId,deviceModelId, source_devicetypeId, goal_devicetypeId, upgradeType, 
					platformId,hardwareversion,valid);
			queryCount = bio.getQueryCount();
		}else{
			tempList = bio.getStbUpgradeTempList(curPage_splitPage, num_splitPage, vendorId,
					deviceModelId, source_devicetypeId, goal_devicetypeId, temp_id, platformId,hardwareversion);
			logger.debug(tempList.toString());
			maxPage_splitPage = bio.countAdverResultList(curPage_splitPage, num_splitPage, vendorId,
					deviceModelId, source_devicetypeId, goal_devicetypeId, temp_id, platformId,hardwareversion);
			queryCount = bio.getQueryCount();
		}
		return "queryList";
	}
	
	/**
	 * 检查原始版本是否已存在
	 * @param
	 * @return
	 */
	public String checkDeviceTypeId() {
		ajax = bio.checkDeviceTypeId(source_devicetypeId);
		return "ajax";
	}
	
	/**
	 * 新增当前版本对应关系
	 * @param
	 * @return
	 */
	public String addUpgradeTemp() 
	{
		if(Global.HNLT.equals(instArea)){
			ajax = bio.addUpgradeTemp_hnlt(vendorId,deviceModelId,source_devicetypeId,
					goal_devicetypeId,temp_id,platformId,valid);
		}else{
			ajax = bio.addUpgradeTemp(vendorId,deviceModelId,source_devicetypeId,
					goal_devicetypeId,temp_id,platformId);
		}
		return "ajax";
	}
	
	/**
	 * 删除当前版本对应关系
	 * @param
	 * @return
	 */
	public String deleteUpgradeTemp() {
		
		if(Global.HNLT.equals(instArea)){
			ajax = bio.deleteUpgradeTemp_hnlt(platformId,goal_devicetypeId,source_devicetypeId,temp_id);
		}else{
			ajax = bio.deleteUpgradeTemp(source_devicetypeId);
		}
		
		return "ajax";
	}
	
	/**
	 * 修改当前版本对应关系
	 * @param
	 * @return
	 */
	public String modifyUpgradeTemp() {
		if(Global.HNLT.equals(instArea)){
			ajax = bio.modifyUpgradeTemp_hnlt(source_devicetypeId ,goal_devicetypeId, platformId,valid);
		}else{
			ajax = bio.modifyUpgradeTemp(source_devicetypeId ,goal_devicetypeId, platformId);
		}
		
		return "ajax";
	}
	

	/**
	 * 查询设备版本
	 * 
	 * @return
	 */
	public String getPathByModelId() {
		logger.debug("GwDeviceQueryACT=>getDevicetype()");
		this.ajax = bio.getPathByModelId(deviceModelId);
		return "ajax";
	}
	
	
	/**
	 * 机顶盒文件升级路径配置列表
	 * @param
	 * @return
	 */
	public String getStbUpgradeFilePathList()
	{
		logger.debug("getStbUpgradeFilePathList");
		filePathList = bio.getStbUpgradeFilePathList(curPage_splitPage, num_splitPage, vendorId,
				deviceModelId, goal_devicetypeId);
		logger.warn(filePathList.toString());
		maxPage_splitPage = bio.countStbUpgradeFilePathList(curPage_splitPage, num_splitPage, vendorId,
				deviceModelId, goal_devicetypeId);
		queryCount = bio.getQueryCount();
		
		if(Global.HNLT.equals(Global.instAreaShortName)){
			return "filePathList_hnlt";
		}else{
			return "filePathList";
		}
	}
	
	/**
	 * 新增文件升级路径配置
	 * @param
	 * @return
	 */
	public String addUpgradeFilePath() {
		
		if(Global.HNLT.equals(Global.instAreaShortName)){
			ajax = bio.addUpgradeFilePath_hnlt(vendorId,deviceModelId,goal_devicetypeId,pathId);
		}else{
			ajax = bio.addUpgradeFilePath(pathId,vendorId,deviceModelId, goal_devicetypeId);
		}
		return "ajax";
	}
	
	/**
	 * 删除当前文件升级路径配置
	 * @param
	 * @return
	 */
	public String deleteUpgradeFilePath() {
		if(Global.HNLT.equals(Global.instAreaShortName)){
			ajax = bio.deleteUpgradeFilePath(pathId,deviceModelId,goal_devicetypeId,vendorId);
		}else{
			ajax = bio.deleteUpgradeFilePath(pathId,deviceModelId, goal_devicetypeId);
		}
		
		
		return "ajax";
	}
	
	/**
	 * 修改当前文件升级路径配置
	 * @param
	 * @return
	 */
	public String modifyUpgradeFilePath() {
		ajax = bio.modifyUpgradeFilePath(vendorId,pathId,deviceModelId, goal_devicetypeId,pathIdNew);
		return "ajax";
	}

	@SuppressWarnings("rawtypes")
	public List getVendorList()
	{
		return vendorList;
	}

	@SuppressWarnings("rawtypes")
	public void setVendorList(List vendorList)
	{
		this.vendorList = vendorList;
	}

	public String getVendorId()
	{
		return vendorId;
	}

	public void setVendorId(String vendorId)
	{
		this.vendorId = vendorId;
	}

	public String getDevice_version()
	{
		return device_version;
	}

	public void setDevice_version(String device_version)
	{
		this.device_version = device_version;
	}
	
	public String getAjax()
	{
		return ajax;
	}

	public void setAjax(String ajax)
	{
		this.ajax = ajax;
	}

	public String getDeviceModelId() {
		return deviceModelId;
	}

	public void setDeviceModelId(String deviceModelId) {
		this.deviceModelId = deviceModelId;
	}

	public String getSource_devicetypeId() {
		return source_devicetypeId;
	}

	public void setSource_devicetypeId(String source_devicetypeId) {
		this.source_devicetypeId = source_devicetypeId;
	}

	public String getGoal_devicetypeId() {
		return goal_devicetypeId;
	}

	public void setGoal_devicetypeId(String goal_devicetypeId) {
		this.goal_devicetypeId = goal_devicetypeId;
	}

	public String getBelong() {
		return belong;
	}

	public void setBelong(String belong) {
		this.belong = belong;
	}

	public StbUpgradeVersionBIO getBio() {
		return bio;
	}

	public void setBio(StbUpgradeVersionBIO bio) {
		this.bio = bio;
	}

	public List<Map<String, String>> getTempList() {
		return tempList;
	}

	public void setTempList(List<Map<String, String>> tempList) {
		this.tempList = tempList;
	}

	public String getUpgradeType() {
		return upgradeType;
	}

	public void setUpgradeType(String upgradeType) {
		this.upgradeType = upgradeType;
	}

	
	public String getTemp_id()
	{
		return temp_id;
	}

	
	public void setTemp_id(String temp_id)
	{
		this.temp_id = temp_id;
	}

	public int getQueryCount() {
		return queryCount;
	}

	public void setQueryCount(int queryCount) {
		this.queryCount = queryCount;
	}

	public String getPathId() {
		return pathId;
	}

	public void setPathId(String pathId) {
		this.pathId = pathId;
	}

	public String getPathIdNew() {
		return pathIdNew;
	}

	public void setPathIdNew(String pathIdNew) {
		this.pathIdNew = pathIdNew;
	}

	public List<Map<String, String>> getFilePathList() {
		return filePathList;
	}

	public void setFilePathList(List<Map<String, String>> filePathList) {
		this.filePathList = filePathList;
	}

	public String getHardwareversion() {
		return hardwareversion;
	}

	public void setHardwareversion(String hardwareversion) {
		this.hardwareversion = hardwareversion;
	}

	public String getPlatformId() {
		return platformId;
	}

	public void setPlatformId(String platformId) {
		this.platformId = platformId;
	}

	public String getInstArea() {
		return instArea;
	}

	public void setInstArea(String instArea) {
		this.instArea = instArea;
	}

	public String getVersion_path() {
		return version_path;
	}

	public void setVersion_path(String version_path) {
		this.version_path = version_path;
	}

	public String getSpecial_path() {
		return special_path;
	}

	public void setSpecial_path(String special_path) {
		this.special_path = special_path;
	}

	public String getDcn_path() {
		return dcn_path;
	}

	public void setDcn_path(String dcn_path) {
		this.dcn_path = dcn_path;
	}

	public String getValid() {
		return valid;
	}

	public void setValid(String valid) {
		this.valid = valid;
	}
	
	

}
