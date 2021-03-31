package com.linkage.module.gtms.stb.resource.action;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gtms.stb.resource.serv.DeviceTypeInfoBIO;
import com.linkage.module.gwms.Global;


/**
 * @author wuchao设备版本查询操作类
 */
@SuppressWarnings("rawtypes")
public class DeviceTypeInfoACT extends splitPageAction implements SessionAware,ServletRequestAware
{
	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory.getLogger(DeviceTypeInfoACT.class);

	private HttpServletRequest request;
	private List<Map> deviceList;
	private DeviceTypeInfoBIO bio;
	private Map session;
	/** 厂家 */
	private int vendor = -1;
	/** 设备型号 */
	private int device_model = -1;
	/** 硬件版本 */
	private String hard_version;
	/** 软件型号 */
	private String soft_version;
	/** 是否支持零配置 */
	private String zeroconf;
	/** 是否支持开机广告 */
	private String bootadv;
	/** 是否审核 1是经过审核,0未审核 */
	private int is_check = -1;
	/** 设备类型 1是e8-b,2是机卡一体e8-c,3是机卡分离型 e8-c*/
	private int rela_dev_type = -1;
	/** 上行方式ID */
	private String typeId = null;
	/** 特定编号 */
	private String speversion;
	private String ajax;
	private long deviceTypeId;
	private int deleteID;
	/** 开始时间 */
	private String startTime;
	/** 结束时间 */
	private String endTime;
	/** 端口信息 */
	private String portInfo;
	/** 设备支持的协议*/
	private String servertype;
	/** 上行方式*/
	private int access_style_relay_id;
	/**设备Ip支持方式 ipv4 或ipv4和ipv6*/
	private String ipType;
	/**是否为规范版本*/
	private String isNormal;
	/**系统类型 1ITMS，2BBMS*/
	private String gw_type;
	/**机顶盒种类*/
	private int category;
	/**是否探针版本*/
	private int is_probe;
	/**终端规格*/
	private int spec_id;
	private long specId;
	private long detailSpecId;
	
	/**EPG版本 湖南新增字段*/
	private String epg_version;
	private String epg_version_old;
	/**适用网络类型net_type 湖南新增字段*/
	private String net_type;
	private String net_type_old;

	private List<Map<String, String>> specList = null;
	
	//设备类型
	private List<Map<String, String>> devTypeMap;
	
	
	public String queryDeviceList()
	{ 
		logger.debug("into queryDeviceList()");
		devTypeMap = bio.getGwDevType();
		specList = bio.querySpecList();
		return "stb";
	}
	
    public String dealTime(String time)
    {
    	SimpleDateFormat date=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	Date str=new Date();
		try{
			str = date.parse(time);
		}catch (ParseException e){
			logger.warn("选择开始或者结束的时间格式不对:"+time);
		}
    	  
		return str.getTime()/1000+"";
    }
    
    // chenxj 查找配置版本添加
    public String editionList()
	{
		logger.debug("into editionList()");
	
		if (!StringUtil.IsEmpty(startTime)){
			startTime=dealTime(startTime);
		}
		if (!StringUtil.IsEmpty(endTime)){
			endTime=dealTime(endTime);
		}
		
		deviceList = bio.querystbDeviceList(vendor, device_model, hard_version,soft_version, bootadv, curPage_splitPage, num_splitPage,startTime,endTime);
		maxPage_splitPage = bio.getStbmaxPage_splitPage();
		return "stbEditionList";
	}
    
	public String queryList()
	{
		logger.debug("into queryList()");
	
		if (!StringUtil.IsEmpty(startTime)){
			startTime=dealTime(startTime);
		}
		if (!StringUtil.IsEmpty(endTime)){
			endTime=dealTime(endTime);
		}
		
		//chenxj
		//deviceList = bio.querystbDeviceList(vendor, device_model, hard_version,soft_version, curPage_splitPage, num_splitPage,startTime,endTime);
		
		deviceList = bio.querystbDeviceList(vendor, device_model, hard_version,soft_version, bootadv, curPage_splitPage, num_splitPage,startTime,endTime);
		maxPage_splitPage = bio.getStbmaxPage_splitPage();
		if(LipossGlobals.inArea(Global.JLDX)){
			return "stbqueryList4JL";
		}
		return "stbqueryList";
	}
	
     public String getPortAndType()
     {
	     ajax=bio.getPortAndType(deviceTypeId);
	     return "ajax";
	 }
	
	public String queryDetail()
	{
		logger.warn("!!detailSpecId= " +detailSpecId);
		if("4".equals(gw_type)){
			deviceList = bio.querystbDeviceDetail(deviceTypeId,detailSpecId);
		}else {
			deviceList = bio.queryDeviceDetail(deviceTypeId,detailSpecId);
		}
		return "detail";
	}

	public String deleteDevice()
	{
		logger.debug("into deleteDevice()");
		if ("4".equals(gw_type)) {
			if (!bio.canstbDeleteDevice(deleteID)){
				ajax = "0";
				return "ajax";
			}
			
			try{
				bio.deletestbDevice(gw_type,deleteID);
				ajax = "1";
			}catch (Exception e){
				ajax = "-1";
			}
		}else {
			if (!bio.canDeleteDevice(deleteID)){
				ajax = "0";
				return "ajax";
			}
			
			try{
				bio.deleteDevice(gw_type,deleteID);
				ajax = "1";
			}catch (Exception e){
				ajax = "-1";
			}
		}
		
		return "ajax";
	}

	/**
	 * add devicetype
	 */
	public String addDevType()
	{
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		// 定制人
		long acc_oid = curUser.getUser().getId();

		if ("4".equals(gw_type)) {
			if (deviceTypeId == -1)
			{
				ajax = bio.addstbDevTypeInfo(gw_type,vendor, device_model, speversion, hard_version,
						soft_version, is_check, rela_dev_type, typeId,portInfo,servertype,acc_oid,zeroconf,bootadv);
			}
			else   
			{	
				if(LipossGlobals.inArea(Global.JXDX))
				{
					ajax = bio.updatestbDevTypeInfo(gw_type,deviceTypeId, vendor, device_model, speversion,
							hard_version, soft_version, is_check, rela_dev_type,typeId,portInfo,
							servertype,acc_oid,ipType,isNormal,specId,zeroconf,bootadv,category,is_probe);
				}
				else if(LipossGlobals.inArea(Global.HNLT))
				{
					logger.warn("[{}] update stbDevTypeInfo epg_version:[{}]-->[{}],net_type:[{}]-->[{}]",
							new Object[]{deviceTypeId,epg_version_old,epg_version,net_type_old,net_type});
					ajax = bio.updatestbDevTypeInfo(deviceTypeId,epg_version,net_type);
				}
				else
				{
					ajax = bio.updatestbDevTypeInfo(gw_type,deviceTypeId, vendor, device_model, speversion,
							hard_version, soft_version, is_check, rela_dev_type,typeId,portInfo,
							servertype,acc_oid,ipType,isNormal,specId,zeroconf,bootadv,category);
				}
			}
		}else {
			if (deviceTypeId == -1)
			{
				ajax = bio.addDevTypeInfo(gw_type,vendor, device_model, speversion, hard_version,
						soft_version, is_check, rela_dev_type, typeId,portInfo,servertype,acc_oid,ipType,specId);
			}
			else   
			{	
				ajax = bio.updateDevTypeInfo(gw_type,deviceTypeId, vendor, device_model, speversion,
											hard_version, soft_version, is_check, rela_dev_type, 
											typeId,portInfo,servertype,acc_oid,ipType,isNormal,specId);
			}
		}
		return "ajax";
	}
	
	/**
	 * update is_check
	 */
	public String updateIsCheck()
	{
		logger.debug("info updateIsCheck()");
		try{
			bio.updateIsCheck(deviceTypeId);
			ajax = "1";
		}catch (Exception e){
			ajax = "-1";
		}
		
		return "ajax";
	}
	
	public String isNormalVersion()
	{
		logger.debug("isNormalVersion()");
		ajax = bio.isNormalVersion(device_model)+"";
		return "ajax";
	}
	
	public String getTypeNameList()
	{
		logger.debug("getTypeNameList()");
		ajax = bio.getTypeNameList(typeId);
		return "ajax";
	}

	/** getters and setters **/
	public DeviceTypeInfoBIO getBio(){
		return bio;
	}

	public void setBio(DeviceTypeInfoBIO bio){
		this.bio = bio;
	}

	public int getDevice_model(){
		return device_model;
	}

	public void setDevice_model(int device_model){
		this.device_model = device_model;
	}

	public List<Map> getDeviceList(){
		return deviceList;
	}

	public void setDeviceList(List<Map> deviceList){
		this.deviceList = deviceList;
	}

	public String getHard_version(){
		return hard_version;
	}

	public void setHard_version(String hard_version)
	{
		try{
			this.hard_version = java.net.URLDecoder.decode(hard_version, "UTF-8");
			} catch (Exception e){
			this.hard_version = hard_version;
			}
	}

	public int getIs_check(){
		return is_check;
	}

	public void setIs_check(int is_check){
		this.is_check = is_check;
	}

	public int getRela_dev_type(){
		return rela_dev_type;
	}

	public void setRela_dev_type(int rela_dev_type){
		this.rela_dev_type = rela_dev_type;
	}

	public String getSoft_version(){
		return soft_version;
	}

	public void setSoft_version(String soft_version)
	{
		try{
			this.soft_version = java.net.URLDecoder.decode(soft_version, "UTF-8");
			} catch (Exception e){
			this.soft_version = soft_version;
			}
	}

	public int getVendor(){
		return vendor;
	}

	public void setVendor(int vendor){
		this.vendor = vendor;
	}

	public Map getSession(){
		return session;
	}

	public String getSpeversion(){
		return speversion;
	}

	public void setSpeversion(String speversion)
	{
		try{
			this.speversion = java.net.URLDecoder.decode(speversion, "UTF-8");
			} catch (Exception e){
			this.speversion = speversion;
			}
	}

	public int getDeleteID(){
		return deleteID;
	}
	public void setDeleteID(int deleteID){
		this.deleteID = deleteID;
	}
	public String getAjax(){
		return ajax;
	}
	public void setAjax(String ajax){
		this.ajax = ajax;
	}
	public long getDeviceTypeId(){
		return deviceTypeId;
	}
	public void setDeviceTypeId(long deviceTypeId){
		this.deviceTypeId = deviceTypeId;
	}
	public String getTypeId(){
		return typeId;
	}
	public void setTypeId(String typeId){
		this.typeId = typeId;
	}
	public String getStartTime(){
		return startTime;
	}
	public void setStartTime(String startTime){
		this.startTime = startTime;
	}
	public String getEndTime(){
		return endTime;
	}
	public void setEndTime(String endTime){
		this.endTime = endTime;
	}
	public void setSession(Map session){
		this.session = session;
	}
	public String getPortInfo(){
		return portInfo;
	}
	public void setPortInfo(String portInfo)
	{
		try{
			this.portInfo = java.net.URLDecoder.decode(portInfo, "UTF-8");
			} catch (Exception e){
			this.portInfo = portInfo;
			}
	}
	public String getServertype(){
		return servertype;
	}
	public void setServertype(String servertype){
		this.servertype = servertype;
	}
	public void setServletRequest(HttpServletRequest arg0){
		this.request = arg0;	
	}
	public int getAccess_style_relay_id(){
		return access_style_relay_id;
	}
	public void setAccess_style_relay_id(int accessStyleRelayId){
		access_style_relay_id = accessStyleRelayId;
	}
	public String getGw_type(){
		return gw_type;
	}
	public void setGw_type(String gwType){
		gw_type = gwType;
	}
	public String getIpType() {
		return ipType;
	}
	public void setIpType(String ipType) {
		this.ipType = ipType;
	}
	public String getIsNormal() {
		return isNormal;
	}
	public void setIsNormal(String isNormal) {
		this.isNormal = isNormal;
	}
	public int getSpec_id() {
		return spec_id;
	}
	public void setSpec_id(int specId) {
		spec_id = specId;
	}
	public List<Map<String, String>> getSpecList() {
		return specList;
	}
	public List<Map<String, String>> getDevTypeMap() {
		return devTypeMap;
	}
	public void setDevTypeMap(List<Map<String, String>> devTypeMap) {
		this.devTypeMap = devTypeMap;
	}
	public void setSpecList(List<Map<String, String>> specList) {
		this.specList = specList;
	}
	public long getSpecId() {
		return specId;
	}
	public void setSpecId(long specId) {
		this.specId = specId;
	}
	public long getDetailSpecId() {
		return detailSpecId;
	}
	public void setDetailSpecId(long detailSpecId) {
		this.detailSpecId = detailSpecId;
	}
	public String getZeroconf() {
		return zeroconf;
	}
	public void setZeroconf(String zeroconf) {
		this.zeroconf = zeroconf;
	}
	public String getBootadv() {
		return bootadv;
	}
	public void setBootadv(String bootadv) {
		this.bootadv = bootadv;
	}
	public int getCategory() {
		return category;
	}
	public void setCategory(int category) {
		this.category = category;
	}
	public int getIs_probe() {
		return is_probe;
	}
	public void setIs_probe(int is_probe) {
		this.is_probe = is_probe;
	}
	public String getEpg_version() {
		return epg_version;
	}
	public void setEpg_version(String epg_version) {
		this.epg_version = epg_version;
	}
	public String getEpg_version_old() {
		return epg_version_old;
	}
	public void setEpg_version_old(String epg_version_old) {
		this.epg_version_old = epg_version_old;
	}
	public String getNet_type() {
		return net_type;
	}
	public void setNet_type(String net_type) {
		this.net_type = net_type;
	}
	public String getNet_type_old() {
		return net_type_old;
	}
	public void setNet_type_old(String net_type_old) {
		this.net_type_old = net_type_old;
	}
	
	
}
