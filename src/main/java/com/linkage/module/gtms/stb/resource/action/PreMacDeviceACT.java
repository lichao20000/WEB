package com.linkage.module.gtms.stb.resource.action;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import action.splitpage.splitPageAction;

import com.linkage.litms.LipossGlobals;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gtms.stb.resource.serv.PreMacDeviceBIO;
import com.linkage.module.gwms.Global;

/**
 * MAC前缀反推机顶盒信息ACTION
 * @author jiafh
 *
 */
@SuppressWarnings("rawtypes")
public class PreMacDeviceACT extends splitPageAction  implements SessionAware,ServletRequestAware
{
	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory.getLogger(PreMacDeviceACT.class);
	
	private PreMacDeviceBIO bio;
	
	/* 厂商列表*/
	private List<Map<String,String>> vendorList;
	/* MAC前缀和设备对应关系列表*/
	private List<Map> macDeviceList;
	/* 厂商ID*/
	private String vendorId;
	/* 型号ID*/
	private String deviceModelId;
	/* 型号名称*/
	private String deviceModel;
	/* 软件版本*/
	private String softwareversion;
	/* 硬件版本*/
	private String hardwareversion;
	/* MAC前缀*/
	private String preMac;
	/* MAC前缀和设备信息对应关系ID*/
	private String preMacId;
	
	private String ajax;
	private String instArea=Global.instAreaShortName;
	
	private HttpServletRequest request;
	private Map session;
	
	/**
	 * 初始化MAC前缀反推机顶盒消息页面
	 */
	public String execute(){
		logger.debug("PreMacDeviceACT ==> execute()");
		vendorList = bio.querVentorList();
		return "init";
	}
	
	public String addjsp(){
		logger.debug("PreMacDeviceACT ==> execute()");
		vendorList = bio.querVentorList();
		return "add";
	}
	
	/**
	 * 获取厂商
	 */
	public String getVendors(){
		logger.debug("PreMacDeviceACT ==> getVendors()");
		
		ajax = bio.getVendors();
		return "ajax";
	}
	
	/**
	 * 查询设备型号
	 */
	public String getDeviceModelS(){
		logger.debug("PreMacDeviceACT ==> getDeviceModelS()");
		
		ajax = bio.getDeviceModelS(vendorId);
		return "ajax";
	}
	
	/**
	 * 查询设备软件版本
	 */
	public String getSoftwareversionS(){
		logger.debug("PreMacDeviceACT ==> getSoftwareversionS()");
		
		ajax = bio.getSoftwareversionS(vendorId, deviceModelId);
		return "ajax";
	}
	
	/**
	 * 查询设备硬件版本
	 */
	public String getHardwareversionS(){
		logger.debug("PreMacDeviceACT ==> getHardwareversionS()");
		
		ajax = bio.getHardwareversionS(vendorId, deviceModelId, softwareversion);
		return "ajax";
	}
	
	/**
	 * 查询MAC前缀反推机顶盒信息
	 */
	public String queryMacDeviceTypeList(){
		logger.debug("PreMacDeviceACT ==> queryMacDeviceTypeList()");
		
		macDeviceList = bio.queryMacDevice(curPage_splitPage, num_splitPage, preMac, vendorId, 
				deviceModelId, hardwareversion, softwareversion);
		
		int total = bio.queryMacDeviceCount(preMac,vendorId,deviceModelId,hardwareversion,softwareversion);
		
		maxPage_splitPage = bio.queryMacDeviceCout(curPage_splitPage, num_splitPage, total);
		return "list";
	}
	
	/**
	 * 新增设备信息
	 */
	public String add(){
		logger.debug("PreMacDeviceACT ==> add()");
		
		// 根据设备信息查询设备版本
		ajax = bio.validateMacDeviceType(preMac,null);
		if("1".equals(ajax)){
			long  user_id=((UserRes) session.get("curUser")).getUser().getId();
			String user_ip=request.getRemoteHost();
			bio.addMacDeviceType(user_id,user_ip,vendorId, deviceModelId, softwareversion, hardwareversion, preMac);
		}
		return "ajax";
	}
	
	/**
	 * 删除MAC前缀和设备的关系
	 */
	public String delete(){
		logger.debug("PreMacDeviceACT ==> delete()");
		
		// 根据设备信息查询设备版本
		ajax = bio.validateMacDeviceType(null,preMacId);
		if("2".equals(ajax)){
			long  user_id=((UserRes) session.get("curUser")).getUser().getId();
			String user_ip=request.getRemoteHost();
			bio.deleteMacDeviceType(user_id,user_ip,preMacId);
		}
		return "ajax";
	}
	
	/**
	 * 编辑MAC前缀和设备关系信息
	 */
	public String edit(){
		logger.debug("PreMacDeviceACT ==> edit()");
		
		// 根据设备信息查询设备版本
		ajax = bio.validateMacDeviceType(null,preMacId);
		if("2".equals(ajax)){
			long  user_id=((UserRes) session.get("curUser")).getUser().getId();
			String user_ip=request.getRemoteHost();
			bio.editMacDeviceType(user_id,user_ip,vendorId, deviceModelId, softwareversion, hardwareversion, preMac, preMacId);
		}
		return "ajax";
	}
	

	public List<Map> getMacDeviceList() {
		return macDeviceList;
	}

	public void setMacDeviceList(List<Map> macDeviceList) {
		this.macDeviceList = macDeviceList;
	}

	public List<Map<String, String>> getVendorList() {
		return vendorList;
	}

	public void setVendorList(List<Map<String, String>> vendorList) {
		this.vendorList = vendorList;
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

	public String getDeviceModel() {
		return deviceModel;
	}

	public void setDeviceModel(String deviceModel) {
		this.deviceModel = deviceModel;
	}

	public String getSoftwareversion() {
		return softwareversion;
	}

	public void setSoftwareversion(String softwareversion) {
		this.softwareversion = softwareversion;
	}

	public String getHardwareversion() {
		return hardwareversion;
	}

	public void setHardwareversion(String hardwareversion) {
		this.hardwareversion = hardwareversion;
	}

	public String getPreMac() {
		return preMac;
	}

	public void setPreMac(String preMac) {
		this.preMac = preMac;
	}

	public String getAjax() {
		return ajax;
	}

	public void setAjax(String ajax) {
		this.ajax = ajax;
	}

	public PreMacDeviceBIO getBio() {
		return bio;
	}

	public void setBio(PreMacDeviceBIO bio) {
		this.bio = bio;
	}

	public String getPreMacId() {
		return preMacId;
	}

	public void setPreMacId(String preMacId) {
		this.preMacId = preMacId;
	}

	public String getInstArea() {
		return instArea;
	}

	public void setInstArea(String instArea) {
		this.instArea = instArea;
	}
	
	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
		
	}

	public Map getSession()
	{
		return session;
	}

	public void setSession(Map session)
	{
		this.session = session;
	}
	
}
