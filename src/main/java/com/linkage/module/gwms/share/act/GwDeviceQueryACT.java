/**
 * 
 */
package com.linkage.module.gwms.share.act;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.resource.QualityServiceAction;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.share.bio.GwDeviceQueryBIO;

/**
 * @author zxj E-mail：qixq@lianchuang.com
 * @version $Revision$
 * @since 2009-12-25
 * @category com.linkage.module.stb.resource.act
 * 
 */

@SuppressWarnings("rawtypes")
public class GwDeviceQueryACT extends splitPageAction implements ServletRequestAware 
{
	private static final long serialVersionUID = 1L;

	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(GwDeviceQueryACT.class);

	GwDeviceQueryBIO gwDeviceBio = null;
	private Map deviceDetailMap = null;
	private String deviceId = null;
	private String ajax = null;
	private String is_awifi;
	private List deviceList = null;
	private List<Map> awifi;
	private int account;
	private String gwShare_cityId = null;
	
	private String gwShare_nextCityId = null;

	private String gwShare_vendorId = null;

	private String gwShare_deviceModelId = null;

	private String gwShare_devicetypeId = null;

	HttpServletRequest request = null;

	private String gwShare_queryType = null;

	private String currencyType = null;

	private String gwShare_queryResultType = null;

	private String gwShare_queryParam = null;

	private String gwShare_queryField = null;

	private String gwShare_onlineStatus = null;

	private String gwShare_bindType = null;

	private String gwShare_deviceSerialnumber = null;

	private String gwShare_netSpeed = null;

	private String gwShare_fileName = null;

	private String gwShare_msg = null;

	private String gwShare_matchSQL = null;
	
	private String noSplitFlag = null;

	private String stackrefresh = null;
	//回传消息
	private String msg = null;

	/**
	 * 查询文本
	 */
	private String searchTxt;
	/**
	 * 匹配文本的分隔符
	 */
	private String separator;

	//查询结果的合计条数
	private int total = 0;

	private  String  gw_type;

	private String fileName;

	/** 1，代表软件升级需求   */
	private String isBatch;
	
	/** 1，代表重庆软件升级需求，多属地   */
	private String iscqsoft = "";

	/** 1，代表安徽软件升级需求，多属地   */
	private String AHLT_softUp = "";

	//查询条件
	private String importQueryField = "username";
	
	private String instArea=Global.instAreaShortName;
	
	/** IP范围查询，起始IP   */
	private String gwShare_start_ip = "";
	
	/** IP范围查询，结束IP   */
	private String gwShare_end_ip = "";
	
	private String online_time = ""; //在线时长
	/**上传文件行数是否加限制，默认否*/
	private String limitNum="0";

	private String startTime = "";

	private String endTime = "";

	private String timeType ;
	
	//新批量参数下发定制，如果是简单查询，返回设备id，高级查询返回数量
	private String newBatchSetRes = "";
	/**
	 * execute
	 */
	public String execute() throws Exception {
		logger.debug("execute()");
		return "success";
	}

	/**
	 * 模拟匹配提示
	 */
	public String getDeviceSn() throws Exception{

		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");

		ajax = gwDeviceBio.getDeviceSn(curUser.getCityId(), searchTxt, separator,gwShare_queryField);

		return "ajax";
	}

	/**
	 * 查询属地
	 * 
	 * @return
	 */
	public String getCityNextChild() {
		logger.debug("GwDeviceQueryACT=>getCity()");
		UserRes curUser = null;
		if (null == this.gwShare_cityId) {

			HttpSession session = request.getSession();
			curUser = (UserRes) session.getAttribute("curUser");
			this.gwShare_cityId = curUser.getCityId();
		}
		// 山西联通如果不是管理员，只显示操作者属地
		if(Global.SXLT.equals(instArea) && !curUser.getUser().isAdmin()) {
			StringBuffer bf = new StringBuffer();
			bf.append("#");
			bf.append(this.gwShare_cityId);
			bf.append("$");
			bf.append(Global.G_CityId_CityName_Map.get(this.gwShare_cityId));
			this.ajax = bf.toString();
		}else {
			this.ajax = gwDeviceBio.getCity(gwShare_cityId);
		}
		
		return "ajax";
	}
	
	
	/**
	 * 查询属地
	 * 
	 * @return
	 */
	public String getCityNextChildCore() {
		logger.debug("GwDeviceQueryACT=>getCity()");
		if (null == this.gwShare_cityId) {

			HttpSession session = request.getSession();
			UserRes curUser = (UserRes) session.getAttribute("curUser");
			this.gwShare_cityId = curUser.getCityId();
		}
		this.ajax = gwDeviceBio.getCityCore(gwShare_cityId);
		return "ajax";
	}
	

	/**
	 * 查询设备厂商
	 * 
	 * @return
	 */
	public String getVendor() {
		logger.debug("GwDeviceQueryACT=>getVendor()");
		this.ajax = gwDeviceBio.getVendor();
		return "ajax";
	}

	public String getVendorStb() {
		logger.debug("GwDeviceQueryACT=>getVendor()");
		this.ajax = gwDeviceBio.getVendorStb();
		return "ajax";
	}
	
	public String getGwShare_nextCityId()
	{
		return gwShare_nextCityId;
	}

	
	public void setGwShare_nextCityId(String gwShare_nextCityId)
	{
		this.gwShare_nextCityId = gwShare_nextCityId;
	}

	/**
	 * 查询设备型号
	 * 
	 * @param vendorId
	 * @return
	 */
	public String getDeviceModel() {
		logger.debug("GwDeviceQueryACT=>getDeviceModel()");
		if("4".equals(gw_type))
		{
			this.ajax = gwDeviceBio.getDeviceModel1(gwShare_vendorId);
		}else{
			this.ajax = gwDeviceBio.getDeviceModel(gwShare_vendorId);
		}
		return "ajax";
	}

	/**
	 * 查询设备OUI
	 *
	 * @param modelId
	 * @return
	 */
	public String getDeviceOui() {
		this.ajax = gwDeviceBio.getDeviceOui(gwShare_deviceModelId);
		return "ajax";
	}

	/**
	 * 查询设备版本
	 * 
	 * @return
	 */
	public String getDevicetype() {
		logger.debug("GwDeviceQueryACT=>getDevicetype()");
		if("4".equals(gw_type))
		{
			this.ajax = gwDeviceBio.getDevicetype1(gwShare_deviceModelId,isBatch);
		}
		else
		{
			this.ajax = gwDeviceBio.getDevicetype(gwShare_deviceModelId,isBatch);
		}
		return "ajax";
	}
	
	/**
	 * 查询设备型号
	 * 
	 * @param vendorId
	 * @return
	 */
	public String getDeviceModelQuality() {
		logger.debug("GwDeviceQueryACT=>getDeviceModelQuality()");
		this.ajax = new QualityServiceAction().getDeviceModel(gwShare_vendorId);
		return "ajax";
	}

	/**
	 * 查询设备版本
	 * 
	 * @return
	 */
	public String getDevicetypeQuality() {
		logger.debug("GwDeviceQueryACT=>getDevicetypeQuality()");
		this.ajax = new QualityServiceAction().getDevicetype(gwShare_deviceModelId);
		return "ajax";
	}

	/**
	 * 查询设备硬件版本
	 * 
	 * @return
	 */
	public String getDevicetypeHD(){
		logger.debug("GwDeviceQueryACT=>getDevicetypeHD()");
		this.ajax = gwDeviceBio.getDevicetype(gwShare_deviceModelId);
		return "ajax";
	}
	/**
	 * 查询设备软件版本
	 * 
	 * @return
	 */
	public String getDevicetypeST(){
		this.ajax = gwDeviceBio.getDevicetypeST(gwShare_deviceModelId);
		return "ajax";
	}
	/**
	 * 查询设备（带列表）
	 * 
	 * @return
	 */
	public String queryBatchParamNodeDeviceList() {
		logger.debug("GwDeviceQueryACT=>queryBatchParamNodeDeviceList()");
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		long areaId = curUser.getAreaId();
		if(null!=gwShare_queryType){
			gwShare_queryType.trim();
		}
		if(null!=gwShare_cityId){
			gwShare_cityId.trim();
		}else{
			this.gwShare_cityId = curUser.getCityId();
		}
		if("3".equals(gwShare_queryType)){
			if(null!=gwShare_fileName){
				gwShare_fileName.trim();
			}
			this.deviceList = gwDeviceBio.getDeviceList(gw_type,areaId, gwShare_queryType, gwShare_cityId, gwShare_fileName);
			if(null==this.deviceList || this.deviceList.size()<1){
				this.gwShare_msg = gwDeviceBio.getMsg();
			}
			total = deviceList == null?0:this.deviceList.size();
			return "shareList0";

		}
		if("4".equals(gwShare_queryType)){
			if(null!=gwShare_fileName){
				gwShare_fileName.trim();
			}

			if(gwShare_fileName.length()<4){
				this.msg = "上传的文件名不正确！";
				return null;
			}
			String fileName_ = gwShare_fileName.substring(gwShare_fileName.length()-3, gwShare_fileName.length());
			logger.debug("fileName_;{}",fileName_);
			if(!"xls".equals(fileName_) && !"txt".equals(fileName_)){
				this.msg = "上传的文件格式不正确！";
				return null;
			}
			List<String> dataList = null;
			try{
				if("txt".equals(fileName_)){
					dataList = getParamNodeImportDataByTXT(gwShare_fileName);
				}else{
					dataList = getParamNodeImportDataByXLS(gwShare_fileName);
				}
			}catch(FileNotFoundException e){
				logger.warn("{}文件没找到！",gwShare_fileName);
				this.msg = "文件没找到！";
				return null;
			}catch(IOException e){
				logger.warn("{}文件解析出错！",gwShare_fileName);
				this.msg = "文件解析出错！";
				return null;
			}catch(Exception e){
				logger.warn("{}文件解析出错！",gwShare_fileName);
				this.msg = "文件解析出错！";
				return null;
			}

			if(null==this.deviceList || this.deviceList.size()<1){
				this.gwShare_msg = this.msg;
			}
			this.total = dataList.size();
			ajax = String.valueOf(total);
			return "ajax";
		}
		else{
			if(null!=gwShare_queryParam){
				gwShare_queryParam.trim();
			}
			if(null!=gwShare_queryField){
				gwShare_queryField.trim();
			}
			if(null!=gwShare_onlineStatus){
				gwShare_onlineStatus.trim();
			}
			if(null!=gwShare_vendorId){
				gwShare_vendorId.trim();
			}
			if(null!=gwShare_deviceModelId){
				gwShare_deviceModelId.trim();
			}
			if(null!=gwShare_devicetypeId){
				gwShare_devicetypeId.trim();
			}
			if(null!=gwShare_bindType){
				gwShare_bindType.trim();
			}
			if(null!=gwShare_deviceSerialnumber){
				gwShare_deviceSerialnumber.trim();
			}
			if(null!=gwShare_start_ip){
				gwShare_start_ip.trim();
			}
			if(null!=gwShare_end_ip){
				gwShare_end_ip.trim();
			}
			total = gwDeviceBio.getDeviceListCount(gw_type,areaId, gwShare_queryType,
					gwShare_queryParam, gwShare_queryField,gwShare_cityId, gwShare_onlineStatus,
					gwShare_vendorId,gwShare_deviceModelId,gwShare_devicetypeId, 
					gwShare_bindType, gwShare_deviceSerialnumber,gwShare_start_ip,gwShare_end_ip,null,"");

			if(total<=50){
				this.deviceList = gwDeviceBio.getDeviceList(gw_type,areaId, gwShare_queryType,
						gwShare_queryParam, gwShare_queryField,	gwShare_cityId, 
						gwShare_onlineStatus,gwShare_vendorId,gwShare_deviceModelId,
						gwShare_devicetypeId, gwShare_bindType, gwShare_deviceSerialnumber,gwShare_start_ip,gwShare_end_ip,null,null);
				if(null==this.deviceList || this.deviceList.size()<1){
					this.gwShare_msg = gwDeviceBio.getMsg();
				}
				return "shareList0";
			}else{
				this.deviceList = gwDeviceBio.getDeviceList(gw_type,curPage_splitPage,num_splitPage,
						areaId, gwShare_queryType,gwShare_queryParam, gwShare_queryField,
						gwShare_cityId, gwShare_onlineStatus,gwShare_vendorId,gwShare_deviceModelId,
						gwShare_devicetypeId, gwShare_bindType, gwShare_deviceSerialnumber,gwShare_start_ip,gwShare_end_ip,null,null);
				if (total % num_splitPage == 0) {
					maxPage_splitPage = total / num_splitPage;
				} else {
					maxPage_splitPage = total / num_splitPage + 1;
				}
				if(null==this.deviceList || this.deviceList.size()<1){
					this.gwShare_msg = gwDeviceBio.getMsg();
				}
				return "shareList";
			}
		}
	}
	/**
	 * 查询设备（带列表）
	 * 
	 * @return
	 */
	public String queryDeviceList() {
		logger.warn("GwDeviceQueryACT=>getDeviceList()");
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		long areaId = curUser.getAreaId();
		if(null!=gwShare_queryType){
			gwShare_queryType.trim();
		}
		if(null!=gwShare_nextCityId && !"-1".equals(gwShare_nextCityId) && !"null".equals(gwShare_nextCityId)){
			this.gwShare_cityId = gwShare_nextCityId.trim();
		}
		else{
			if(null!=gwShare_cityId){
				gwShare_cityId.trim();
			}else{
				this.gwShare_cityId = curUser.getCityId();
			}
		}
		
		if("true".equals(iscqsoft)&&gwShare_cityId.substring(gwShare_cityId.length()-1).equals(",")){
			String citys[] = gwShare_cityId.split(",");
			gwShare_cityId = "";
			for(int i=0;i<citys.length;i++){
				gwShare_cityId = gwShare_cityId+"'"+citys[i]+"',";
			}
			//gwShare_cityId = gwShare_cityId.substring(0, gwShare_cityId.length()-1);
		}
		
		if("3".equals(gwShare_queryType)){
			if(null!=gwShare_fileName){
				gwShare_fileName.trim();
			}
			
			if(Global.XJDX.equals(instArea)){
				gwDeviceBio.setLimitNum("1".equals(limitNum));
				gwDeviceBio.setMaxNum(300);
			}
			
			this.deviceList = gwDeviceBio.getDeviceList(gw_type,areaId, gwShare_queryType, gwShare_cityId, gwShare_fileName);
			if(null==this.deviceList || this.deviceList.size()<1){
				this.gwShare_msg = gwDeviceBio.getMsg();
			}
			total = deviceList == null?0:this.deviceList.size();
			
			if(Global.NXDX.equals(instArea)){
				noSplitFlag = "yes";
			}
			
			if(total>50 && Global.NXDX.equals(instArea)){
				gwShare_matchSQL=gwDeviceBio.getDeviceSql(gw_type,gwShare_cityId,gwShare_fileName);
				return "shareList";
			}
			if(total>50 && (Global.NMGDX.equals(instArea) || Global.SDDX.equals(instArea) || Global.AHDX.equals(instArea) || Global.HBDX.equals(instArea)) && "stackrefresh".equals(stackrefresh)){
				gwShare_matchSQL=gwDeviceBio.getDeviceSql(gw_type,gwShare_cityId,gwShare_fileName);
				return "shareList";
			}
			
			return "shareList0";
		}
		
		if("4".equals(gwShare_queryType)){
			if(null!=gwShare_fileName){
				gwShare_fileName.trim();
			}

			if(gwShare_fileName.length()<4){
				this.msg = "上传的文件名不正确！";
				return null;
			}
			String fileName_ = gwShare_fileName.substring(gwShare_fileName.length()-3, gwShare_fileName.length());
			logger.debug("fileName_;{}",fileName_);
			if(!"xls".equals(fileName_) && !"txt".equals(fileName_)){
				this.msg = "上传的文件格式不正确！";
				return null;
			}
			List<String> dataList = null;
			try{
				if("txt".equals(fileName_)){
					dataList = getImportDataByTXT(gwShare_fileName);
				}else{
					dataList = getImportDataByXLS(gwShare_fileName);
				}
			}catch(FileNotFoundException e){
				logger.warn("{}文件没找到！",gwShare_fileName);
				this.msg = "文件没找到！";
				return null;
			}catch(IOException e){
				logger.warn("{}文件解析出错！",gwShare_fileName);
				this.msg = "文件解析出错！";
				return null;
			}catch(Exception e){
				logger.warn("{}文件解析出错！",gwShare_fileName);
				this.msg = "文件解析出错！";
				return null;
			}

			if(null==this.deviceList || this.deviceList.size()<1){
				this.gwShare_msg = this.msg;
			}
			this.total = dataList.size();
			ajax = String.valueOf(total);
			return "ajax";
		}
		else{
			if(null!=gwShare_queryParam){
				gwShare_queryParam.trim();
			}
			if(null!=gwShare_queryField){
				gwShare_queryField.trim();
			}
			if(null!=gwShare_onlineStatus){
				gwShare_onlineStatus.trim();
			}
			if(null!=gwShare_vendorId){
				gwShare_vendorId.trim();
			}
			if(null!=gwShare_deviceModelId){
				gwShare_deviceModelId.trim();
			}
			if(null!=gwShare_devicetypeId){
				gwShare_devicetypeId.trim();
			}
			if(null!=gwShare_bindType){
				gwShare_bindType.trim();
			}
			if(null!=gwShare_deviceSerialnumber){
				gwShare_deviceSerialnumber.trim();
			}
			if(null!=gwShare_start_ip){
				gwShare_start_ip.trim();
			}
			if(null!=gwShare_end_ip){
				gwShare_end_ip.trim();
			}

			if(null!=gwShare_netSpeed){
				gwShare_netSpeed.trim();
			}

			if(Global.GSDX.equals(instArea) && !StringUtil.IsEmpty(online_time)){
				long qryDayMills = Integer.valueOf(online_time) * 24 * 60 * 60;
				long currMills = System.currentTimeMillis() / 1000;
				long time = currMills - qryDayMills;
				online_time = String.valueOf(time);
			}
			logger.warn("queryDeviceList => onlin_time:{}",online_time);
			total = gwDeviceBio.getDeviceListCount(gw_type,areaId, gwShare_queryType,
					gwShare_queryParam, gwShare_queryField,gwShare_cityId, gwShare_onlineStatus,
					gwShare_vendorId,gwShare_deviceModelId,gwShare_devicetypeId, 
					gwShare_bindType, gwShare_deviceSerialnumber,gwShare_start_ip,gwShare_end_ip,online_time,gwShare_netSpeed);

			if(total<=50){
				this.deviceList = gwDeviceBio.getDeviceList(gw_type,areaId, gwShare_queryType,
						gwShare_queryParam, gwShare_queryField,	gwShare_cityId, 
						gwShare_onlineStatus,gwShare_vendorId,gwShare_deviceModelId,
						gwShare_devicetypeId, gwShare_bindType, gwShare_deviceSerialnumber,gwShare_start_ip,gwShare_end_ip,online_time,gwShare_netSpeed);
				if(null==this.deviceList || this.deviceList.size()<1){
					this.gwShare_msg = gwDeviceBio.getMsg();
				}
				if(AHLT_softUp!=null&&"1".equals(AHLT_softUp) ){
					return "shareList0select";
				}
				return "shareList0";

			}else{
				this.deviceList = gwDeviceBio.getDeviceList(gw_type,curPage_splitPage,num_splitPage,
						areaId, gwShare_queryType,gwShare_queryParam, gwShare_queryField,
						gwShare_cityId, gwShare_onlineStatus,gwShare_vendorId,gwShare_deviceModelId,
						gwShare_devicetypeId, gwShare_bindType, gwShare_deviceSerialnumber,gwShare_start_ip,gwShare_end_ip,online_time,gwShare_netSpeed);
				if (total % num_splitPage == 0) {
					maxPage_splitPage = total / num_splitPage;
				} else {
					maxPage_splitPage = total / num_splitPage + 1;
				}
				if(null==this.deviceList || this.deviceList.size()<1){
					this.gwShare_msg = gwDeviceBio.getMsg();
				}
				
				if(Global.NXDX.equals(instArea)||Global.XJDX.equals(instArea)){
					gwShare_matchSQL=gwDeviceBio.getDeviceListSQL2(gw_type,areaId, gwShare_queryType,
							gwShare_queryParam, gwShare_queryField,	gwShare_cityId, 
							gwShare_onlineStatus,gwShare_vendorId,gwShare_deviceModelId,
							gwShare_devicetypeId, gwShare_bindType, gwShare_deviceSerialnumber);
				}
				
				/*
				 * if("xj_dx".equals(instArea)){
				 * gwShare_matchSQL=gwDeviceBio.getDeviceListSQL3(gw_type,areaId,
				 * gwShare_queryType, gwShare_queryParam, gwShare_queryField,
				 * gwShare_cityId,
				 * gwShare_onlineStatus,gwShare_vendorId,gwShare_deviceModelId,
				 * gwShare_devicetypeId, gwShare_bindType, gwShare_deviceSerialnumber); }
				 */
				logger.warn("gwShare_matchSQL:"+gwShare_matchSQL);
				if(AHLT_softUp!=null&&"1".equals(AHLT_softUp) ){
					return "shareListselect";
				}
				return "shareList";
			}
		}
	}
	
	/**
	 * 查询设备数量
	 * @return
	 */
	public String queryDeviceCount() {
		logger.warn("GwDeviceQueryACT=>queryDeviceCount()");
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		long areaId = curUser.getAreaId();
		if(null!=gwShare_queryType){
			gwShare_queryType.trim();
		}
		if(null!=gwShare_nextCityId && !"-1".equals(gwShare_nextCityId) && !"null".equals(gwShare_nextCityId)){
			this.gwShare_cityId = gwShare_nextCityId.trim();
		}
		else{
			if(null!=gwShare_cityId){
				gwShare_cityId.trim();
			}else{
				this.gwShare_cityId = curUser.getCityId();
			}
		}
		
		if("true".equals(iscqsoft)&&gwShare_cityId.substring(gwShare_cityId.length()-1).equals(",")){
			String citys[] = gwShare_cityId.split(",");
			gwShare_cityId = "";
			for(int i=0;i<citys.length;i++){
				gwShare_cityId = gwShare_cityId+"'"+citys[i]+"',";
			}
			//gwShare_cityId = gwShare_cityId.substring(0, gwShare_cityId.length()-1);
		}
		
		if("3".equals(gwShare_queryType)){
			if(null!=gwShare_fileName){
				gwShare_fileName.trim();
			}
			gwShare_queryResultType = "checkbox";
			
			this.deviceList = gwDeviceBio.getDeviceList(gw_type,areaId, gwShare_queryType, gwShare_cityId, gwShare_fileName);
			if(null==this.deviceList || this.deviceList.size()<1){
				this.gwShare_msg = gwDeviceBio.getMsg();
			}
			total = deviceList == null?0:this.deviceList.size();
			newBatchSetRes = StringUtil.getStringValue(total);
			return "shareList0_newBatchSet";
		}
		
		if("4".equals(gwShare_queryType)){
			if(null!=gwShare_fileName){
				gwShare_fileName.trim();
			}

			if(gwShare_fileName.length()<4){
				this.msg = "上传的文件名不正确！";
				return null;
			}
			String fileName_ = gwShare_fileName.substring(gwShare_fileName.length()-3, gwShare_fileName.length());
			logger.debug("fileName_;{}",fileName_);
			if(!"xls".equals(fileName_) && !"txt".equals(fileName_)){
				this.msg = "上传的文件格式不正确！";
				return null;
			}
			List<String> dataList = null;
			try{
				if("txt".equals(fileName_)){
					dataList = getImportDataByTXT(gwShare_fileName);
				}else{
					dataList = getImportDataByXLS(gwShare_fileName);
				}
			}catch(FileNotFoundException e){
				logger.warn("{}文件没找到！",gwShare_fileName);
				this.msg = "文件没找到！";
				return null;
			}catch(IOException e){
				logger.warn("{}文件解析出错！",gwShare_fileName);
				this.msg = "文件解析出错！";
				return null;
			}catch(Exception e){
				logger.warn("{}文件解析出错！",gwShare_fileName);
				this.msg = "文件解析出错！";
				return null;
			}

			if(null==this.deviceList || this.deviceList.size()<1){
				this.gwShare_msg = this.msg;
			}
			this.total = dataList.size();
			ajax = String.valueOf(total);
			return "ajax";
		}
		else{
			if(null!=gwShare_queryParam){
				gwShare_queryParam.trim();
			}
			if(null!=gwShare_queryField){
				gwShare_queryField.trim();
			}
			if(null!=gwShare_onlineStatus){
				gwShare_onlineStatus.trim();
			}
			if(null!=gwShare_vendorId){
				gwShare_vendorId.trim();
			}
			if(null!=gwShare_deviceModelId){
				gwShare_deviceModelId.trim();
			}
			if(null!=gwShare_devicetypeId){
				gwShare_devicetypeId.trim();
			}
			if(null!=gwShare_bindType){
				gwShare_bindType.trim();
			}
			if(null!=gwShare_deviceSerialnumber){
				gwShare_deviceSerialnumber.trim();
			}
			
			if("1".equals(gwShare_queryType)){
				this.deviceList = gwDeviceBio.getDeviceList(gw_type,areaId, gwShare_queryType,
						gwShare_queryParam, gwShare_queryField,	gwShare_cityId, 
						"",gwShare_vendorId,gwShare_deviceModelId,
						gwShare_devicetypeId, gwShare_bindType, null,null,null,null,null);
				if(null!=this.deviceList && this.deviceList.size()>=1){
					newBatchSetRes = ((HashMap<String,String>)deviceList.get(0)).get("device_id");
					total = 1;
				}
				else{
					total = 0;
					newBatchSetRes = "0";
				}
				
				return "shareList0_newBatchSet";
			}
			else{
				gwShare_queryResultType = "checkbox";
				total = gwDeviceBio.getDeviceListCount(gw_type,areaId, gwShare_queryType,
						gwShare_queryParam, gwShare_queryField,gwShare_cityId, "",
						gwShare_vendorId,gwShare_deviceModelId,gwShare_devicetypeId, 
						gwShare_bindType, null,null,null,null,"");
				
				if(total<=50){
					this.deviceList = gwDeviceBio.getDeviceList(gw_type,areaId, gwShare_queryType,
							gwShare_queryParam, gwShare_queryField,	gwShare_cityId, 
							"",gwShare_vendorId,gwShare_deviceModelId,
							gwShare_devicetypeId, gwShare_bindType, null,null,null,null,null);
					if(null==this.deviceList || this.deviceList.size()<1){
						this.gwShare_msg = gwDeviceBio.getMsg();
					}
					return "shareList0_newBatchSet";
				}else{
					this.deviceList = gwDeviceBio.getDeviceList(gw_type,curPage_splitPage,num_splitPage,
							areaId, gwShare_queryType,gwShare_queryParam, gwShare_queryField,
							gwShare_cityId, "",gwShare_vendorId,gwShare_deviceModelId,
							gwShare_devicetypeId, gwShare_bindType, null,null,null,null,null);
					if (total % num_splitPage == 0) {
						maxPage_splitPage = total / num_splitPage;
					} else {
						maxPage_splitPage = total / num_splitPage + 1;
					}
					if(null==this.deviceList || this.deviceList.size()<1){
						this.gwShare_msg = gwDeviceBio.getMsg();
					}
					
					if(Global.NXDX.equals(instArea)||Global.XJDX.equals(instArea)){
						gwShare_matchSQL=gwDeviceBio.getDeviceListSQL2(gw_type,areaId, gwShare_queryType,
								gwShare_queryParam, gwShare_queryField,	gwShare_cityId, 
								"",gwShare_vendorId,gwShare_deviceModelId,
								gwShare_devicetypeId, gwShare_bindType, null);
					}
					newBatchSetRes = StringUtil.getStringValue(total);
					return "shareList_newBatchSet";
				}
			}
		}
	}

	private void setTime(String starttime, String endtime)
	{
		logger.debug("setTime()" + starttime);
		DateTimeUtil dt = null;// 定义DateTimeUtil
		if (starttime == null || "".equals(starttime))
		{
			startTime = null;
		}
		else
		{
			dt = new DateTimeUtil(starttime);
			startTime = String.valueOf(dt.getLongTime());
		}
		if (endtime == null || "".equals(endtime))
		{
			endTime = null;
		}
		else
		{
			dt = new DateTimeUtil(endtime);
			endTime = String.valueOf(dt.getLongTime());
		}
	}

	public String queryDeviceListForSxlt() {
		deviceListForSxlt();
		return "shareListForSxlt";
	}
	
	public String goPageDeviceListForSxlt() {
		deviceListForSxlt();
		return "deviceOperateForSxlt";
	}
	
	// 山西联通设备管理
	public void deviceListForSxlt() {
		// 时间转换
		setTime(startTime,endTime);
		logger.warn("GwDeviceQueryACT=>queryDeviceListForSxlt()");
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		long areaId = curUser.getAreaId();
		if(null!=gwShare_queryType){
			gwShare_queryType.trim();
		}
		if(null!=gwShare_nextCityId && !"-1".equals(gwShare_nextCityId) && !"null".equals(gwShare_nextCityId)){
			this.gwShare_cityId = gwShare_nextCityId.trim();
		}
		else{
			if(null!=gwShare_cityId){
				gwShare_cityId.trim();
			}else{
				this.gwShare_cityId = curUser.getCityId();
			}
		}

		if("true".equals(iscqsoft)&&gwShare_cityId.substring(gwShare_cityId.length()-1).equals(",")){
			String citys[] = gwShare_cityId.split(",");
			gwShare_cityId = "";
			for(int i=0;i<citys.length;i++){
				gwShare_cityId = gwShare_cityId+"'"+citys[i]+"',";
			}
		}

		if(null!=gwShare_queryParam){
			gwShare_queryParam.trim();
		}
		if(null!=gwShare_queryField){
			gwShare_queryField.trim();
		}
		if(null!=gwShare_onlineStatus){
			gwShare_onlineStatus.trim();
		}
		if(null!=gwShare_vendorId){
			gwShare_vendorId.trim();
		}
		if(null!=gwShare_deviceModelId){
			gwShare_deviceModelId.trim();
		}
		if(null!=gwShare_devicetypeId){
			gwShare_devicetypeId.trim();
		}
		if(null!=gwShare_bindType){
			gwShare_bindType.trim();
		}
		if(null!=gwShare_deviceSerialnumber){
			gwShare_deviceSerialnumber.trim();
		}
		if(null!=gwShare_start_ip){
			gwShare_start_ip.trim();
		}
		if(null!=gwShare_end_ip){
			gwShare_end_ip.trim();
		}
		logger.warn("queryDeviceList => onlin_time:{}",online_time);
		total = gwDeviceBio.getDeviceListCountForSxlt(gw_type,areaId, gwShare_queryType,
				gwShare_queryParam, gwShare_queryField,gwShare_cityId, gwShare_onlineStatus,
				gwShare_vendorId,gwShare_deviceModelId,gwShare_devicetypeId,
				gwShare_bindType, gwShare_deviceSerialnumber,gwShare_start_ip,
				gwShare_end_ip,online_time,startTime,endTime,timeType);

		this.deviceList = gwDeviceBio.getDeviceListForSxlt(gw_type,curPage_splitPage,num_splitPage,
				areaId, gwShare_queryType,gwShare_queryParam, gwShare_queryField,
				gwShare_cityId, gwShare_onlineStatus,gwShare_vendorId,gwShare_deviceModelId,
				gwShare_devicetypeId, gwShare_bindType, gwShare_deviceSerialnumber,gwShare_start_ip,
				gwShare_end_ip,online_time,startTime,endTime,timeType);
		if (total % num_splitPage == 0) {
			maxPage_splitPage = total / num_splitPage;
		} else {
			maxPage_splitPage = total / num_splitPage + 1;
		}
		if(null==this.deviceList || this.deviceList.size()<1){
			this.gwShare_msg = gwDeviceBio.getMsg();
		}
	}
	

	public static void main(String[] args)
	{
		/*String gwShare_cityId = "aaac,dasda,asdasd,";
		String citys[] = gwShare_cityId.split(",");
		gwShare_cityId = "";
		for(int i=0;i<citys.length;i++){
			gwShare_cityId = gwShare_cityId+"'"+citys[i]+"',";
		}
		System.out.println(gwShare_cityId);*/
		String online_time = "7";
		long qryDayMills = Integer.valueOf(online_time) * 24 * 60 * 60;
		long currMills = System.currentTimeMillis() / 1000;
		long time = currMills - qryDayMills;
		online_time = String.valueOf(time);
		System.out.println(qryDayMills);
		System.out.println(currMills);
		System.out.println(time);
		System.out.println(online_time);
	}
	/**
	 * 查询数量
	 */
	public String querynumber()
	{
			logger.debug("GwDeviceQueryACT=>querynumber()");
			HttpSession session = request.getSession();
			UserRes curUser = (UserRes) session.getAttribute("curUser");
			long areaId = curUser.getAreaId();
			if(null!=gwShare_queryType){
				gwShare_queryType.trim();
			}
			if(null!=gwShare_cityId){
				gwShare_cityId.trim();
			}else{
				this.gwShare_cityId = curUser.getCityId();
			}
			if("3".equals(gwShare_queryType)){
				if(null!=gwShare_fileName){
					gwShare_fileName.trim();
				}
				this.deviceList = gwDeviceBio.getnumber(gw_type,areaId, gwShare_queryType, gwShare_cityId, gwShare_fileName);
				if(null==this.deviceList || this.deviceList.size()<1){
					this.ajax = gwDeviceBio.getMsg();
				}
				total = deviceList.size();
				ajax = String.valueOf(total);
				return "ajax";
			}
			if("4".equals(gwShare_queryType)){
				if(null!=gwShare_fileName){
					gwShare_fileName.trim();
				}

				if(gwShare_fileName.length()<4){
					this.msg = "上传的文件名不正确！";
					return null;
				}
				String fileName_ = gwShare_fileName.substring(gwShare_fileName.length()-3, gwShare_fileName.length());
				logger.debug("fileName_;{}",fileName_);
				if(!"xls".equals(fileName_) && !"txt".equals(fileName_)){
					this.msg = "上传的文件格式不正确！";
					return null;
				}
				List<String> dataList = null;
				try{
					if("txt".equals(fileName_)){
						dataList = getImportDataByTXT(gwShare_fileName);
					}else{
						dataList = getImportDataByXLS(gwShare_fileName);
					}
				}catch(FileNotFoundException e){
					logger.warn("{}文件没找到！",gwShare_fileName);
					this.msg = "文件没找到！";
					return null;
				}catch(IOException e){
					logger.warn("{}文件解析出错！",gwShare_fileName);
					this.msg = "文件解析出错！";
					return null;
				}catch(Exception e){
					logger.warn("{}文件解析出错！",gwShare_fileName);
					this.msg = "文件解析出错！";
					return null;
				}

				if(null==this.deviceList || this.deviceList.size()<1){
					this.gwShare_msg = this.msg;
				}
				this.total = dataList.size();
				ajax = String.valueOf(total);
				return "ajax";
			}
			else{
				if(null!=gwShare_queryParam){
					gwShare_queryParam.trim();
				}
				if(null!=gwShare_queryField){
					gwShare_queryField.trim();
				}
				if(null!=gwShare_onlineStatus){
					gwShare_onlineStatus.trim();
				}
				if(null!=gwShare_vendorId){
					gwShare_vendorId.trim();
				}
				if(null!=gwShare_deviceModelId){
					gwShare_deviceModelId.trim();
				}
				if(null!=gwShare_devicetypeId){
					gwShare_devicetypeId.trim();
				}
				if(null!=gwShare_bindType){
					gwShare_bindType.trim();
				}
				if(null!=gwShare_deviceSerialnumber){
					gwShare_deviceSerialnumber.trim();
				}
				if(null!=gwShare_start_ip){
					gwShare_start_ip.trim();
				}
				if(null!=gwShare_end_ip){
					gwShare_end_ip.trim();
				}
				total = gwDeviceBio.getDeviceListCount(gw_type,areaId, gwShare_queryType,
						gwShare_queryParam, gwShare_queryField,gwShare_cityId, gwShare_onlineStatus,
						gwShare_vendorId,gwShare_deviceModelId,gwShare_devicetypeId, 
						gwShare_bindType, gwShare_deviceSerialnumber,gwShare_start_ip,gwShare_end_ip,null,"");

				ajax = String.valueOf(total);
				return "ajax";
			}
	}
	
	/**
	 * 查询设备（带列表）
	 * 
	 * @return
	 */
	public String queryDeviceListForDoubleParam() {
		logger.debug("GwDeviceQueryACT=>queryDeviceListForDoubleParam()");
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		long areaId = curUser.getAreaId();
		if(null!=gwShare_queryType){
			gwShare_queryType.trim();
		}
		if(null!=gwShare_cityId){
			gwShare_cityId.trim();
		}else{
			this.gwShare_cityId = curUser.getCityId();
		}
		if("3".equals(gwShare_queryType)){
			if(null!=gwShare_fileName){
				gwShare_fileName.trim();
			}
			this.deviceList = gwDeviceBio.getDeviceListForDoubleParam(gw_type,areaId, gwShare_queryType, gwShare_cityId, gwShare_fileName);
			if(null==this.deviceList || this.deviceList.size()<1){
				this.gwShare_msg = gwDeviceBio.getMsg();
			}
			total = deviceList == null?0:this.deviceList.size();
		}
		return "shareList0";
	}
	/**
	 * 查询设备（带列表）(无线专线批量开通查询)
	 * 
	 * @return
	 */
	public String queryDeviceList4Wireless() {
		logger.debug("GwDeviceQueryACT=>queryDeviceList4Wireless()");
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		long areaId = curUser.getAreaId();
		if(null!=gwShare_queryType){
			gwShare_queryType.trim();
		}
		if(null!=gwShare_cityId){
			gwShare_cityId.trim();
		}else{
			this.gwShare_cityId = curUser.getCityId();
		}
		if("3".equals(gwShare_queryType)){
			if(null!=gwShare_fileName){
				gwShare_fileName.trim();
			}
			if(null!=currencyType){
				currencyType.trim();
			}
			this.deviceList = gwDeviceBio.getDeviceList4Wireless(gw_type,areaId	, gwShare_queryType, gwShare_cityId, gwShare_fileName,currencyType);
			if(null==this.deviceList || this.deviceList.size()<1){
				this.gwShare_msg = gwDeviceBio.getMsg();
			}
			total = deviceList == null?0:this.deviceList.size();
			return "shareList0";

		}
		if("4".equals(gwShare_queryType)){
			if(null!=gwShare_fileName){
				gwShare_fileName.trim();
			}

			if(gwShare_fileName.length()<4){
				this.msg = "上传的文件名不正确！";
				return null;
			}
			String fileName_ = gwShare_fileName.substring(gwShare_fileName.length()-3, gwShare_fileName.length());
			logger.debug("fileName_;{}",fileName_);
			if(!"xls".equals(fileName_) && !"txt".equals(fileName_)){
				this.msg = "上传的文件格式不正确！";
				return null;
			}
			List<String> dataList = null;
			try{
				if("txt".equals(fileName_)){
					dataList = getImportDataByTXT(gwShare_fileName);
				}else{
					dataList = getImportDataByXLS(gwShare_fileName);
				}
			}catch(FileNotFoundException e){
				logger.warn("{}文件没找到！",gwShare_fileName);
				this.msg = "文件没找到！";
				return null;
			}catch(IOException e){
				logger.warn("{}文件解析出错！",gwShare_fileName);
				this.msg = "文件解析出错！";
				return null;
			}catch(Exception e){
				logger.warn("{}文件解析出错！",gwShare_fileName);
				this.msg = "文件解析出错！";
				return null;
			}

			if(null==this.deviceList || this.deviceList.size()<1){
				this.gwShare_msg = this.msg;
			}
			this.total = dataList.size();
			ajax = String.valueOf(total);
			return "ajax";
		}
		else{
			if(null!=gwShare_queryParam){
				gwShare_queryParam.trim();
			}
			if(null!=gwShare_queryField){
				gwShare_queryField.trim();
			}
			if(null!=gwShare_onlineStatus){
				gwShare_onlineStatus.trim();
			}
			if(null!=gwShare_vendorId){
				gwShare_vendorId.trim();
			}
			if(null!=gwShare_deviceModelId){
				gwShare_deviceModelId.trim();
			}
			if(null!=gwShare_devicetypeId){
				gwShare_devicetypeId.trim();
			}
			if(null!=gwShare_bindType){
				gwShare_bindType.trim();
			}
			if(null!=gwShare_deviceSerialnumber){
				gwShare_deviceSerialnumber.trim();
			}
			if(null!=gwShare_start_ip){
				gwShare_start_ip.trim();
			}
			if(null!=gwShare_end_ip){
				gwShare_end_ip.trim();
			}
			total = gwDeviceBio.getDeviceListCount(gw_type,areaId, gwShare_queryType,
					gwShare_queryParam, gwShare_queryField,gwShare_cityId, gwShare_onlineStatus,
					gwShare_vendorId,gwShare_deviceModelId,gwShare_devicetypeId, 
					gwShare_bindType, gwShare_deviceSerialnumber,gwShare_start_ip,gwShare_end_ip,online_time,"");

			if(total<=50){
				this.deviceList = gwDeviceBio.getDeviceList(gw_type,areaId, gwShare_queryType,
						gwShare_queryParam, gwShare_queryField,	gwShare_cityId, 
						gwShare_onlineStatus,gwShare_vendorId,gwShare_deviceModelId,
						gwShare_devicetypeId, gwShare_bindType, gwShare_deviceSerialnumber,gwShare_start_ip,gwShare_end_ip,online_time,null);
				if(null==this.deviceList || this.deviceList.size()<1){
					this.gwShare_msg = gwDeviceBio.getMsg();
				}
				return "shareList0";
			}else{
				this.deviceList = gwDeviceBio.getDeviceList(gw_type,curPage_splitPage,num_splitPage,
						areaId, gwShare_queryType,gwShare_queryParam, gwShare_queryField,
						gwShare_cityId, gwShare_onlineStatus,gwShare_vendorId,gwShare_deviceModelId,
						gwShare_devicetypeId, gwShare_bindType, gwShare_deviceSerialnumber,gwShare_start_ip,gwShare_end_ip,online_time,null);
				if (total % num_splitPage == 0) {
					maxPage_splitPage = total / num_splitPage;
				} else {
					maxPage_splitPage = total / num_splitPage + 1;
				}
				if(null==this.deviceList || this.deviceList.size()<1){
					this.gwShare_msg = gwDeviceBio.getMsg();
				}
				return "shareList";
			}
		}
	}

	/**
	 * 查询isawifi
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String queryisawifi()
	{
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		long areaId = curUser.getAreaId();
		if(null!=gwShare_queryType){
			gwShare_queryType.trim();
		}
		if(null!=gwShare_queryParam){
			gwShare_queryParam.trim();
		}
		if(null!=gwShare_queryField){
			gwShare_queryField.trim();
		}
		if(null!=gwShare_cityId){
			gwShare_cityId.trim();
		}
		if(null!=gwShare_onlineStatus){
			gwShare_onlineStatus.trim();
		}
		if(null!=gwShare_vendorId){
			gwShare_vendorId.trim();
		}
		if(null!=gwShare_deviceModelId){
			gwShare_deviceModelId.trim();
		}
		if(null!=gwShare_devicetypeId){
			gwShare_devicetypeId.trim();
		}
		if(null!=gwShare_bindType){
			gwShare_bindType.trim();
		}
		if(null!=gwShare_deviceSerialnumber){
			gwShare_deviceSerialnumber.trim();
		}
		this.awifi=gwDeviceBio.queryisawifi(gw_type, areaId, gwShare_queryType, gwShare_queryParam, gwShare_queryField, gwShare_cityId, gwShare_onlineStatus, gwShare_vendorId, gwShare_deviceModelId, gwShare_devicetypeId, gwShare_bindType, gwShare_deviceSerialnumber);
		this.account=gwDeviceBio.queryaccount_number(gw_type, areaId, gwShare_queryType, gwShare_queryParam, gwShare_queryField, gwShare_cityId, gwShare_onlineStatus, gwShare_vendorId, gwShare_deviceModelId, gwShare_devicetypeId, gwShare_bindType, gwShare_deviceSerialnumber);
		Map<String,String> map=new HashMap<String, String>();
		if(account!=0)
		{
		if(null!=awifi&&awifi.size()>0)
		{
			map=awifi.get(0);
			String wifi=StringUtil.getStringValue(map.get("is_awifi"));
		if((!StringUtil.IsEmpty(wifi))&&(!"2".equals(wifi)))
		{
			this.ajax = "0";
		}
		else
		{
			this.ajax="1";
		}
		}
		else
		{
			this.ajax="1";
		}
		}
		else
		{
			this.ajax="2";
		}
		return "ajax";
	}

	/**
	 * 解析文件（txt格式）
	 * 
	 * @param fileName 文件名
	 * @return
	 * @throws FileNotFoundException 
	 * 		   IOException
	 */
	public List<String> getImportDataByTXT(String fileName) throws FileNotFoundException,IOException{
		logger.debug("getImportDataByTXT{}",fileName);
		List<String> list = new ArrayList<String>();
		BufferedReader in = new BufferedReader(new FileReader(getFilePath()+fileName));
		String line = in.readLine();
		/**
		 * 判断导入文件查询的条件，默认为用户账号，当匹配到设备序列号时，
		 * 则以设备序列号作为条件，否则一律以用户账号作为条件
		 */
		if(null!=line && "设备序列号".equals(line)){
			this.importQueryField = "device_serialnumber";
		}else{
			this.importQueryField = "username";
		}
		//从第二行开始读取数据，并对每行数据去掉首尾空格
		while ((line = in.readLine()) != null) {
			if(!"".equals(line.trim())){
				list.add(line.trim());
			}
		}
		in.close();
		in = null;

		return list;
	}

	/**
	 * 解析文件（xls格式）
	 * 
	 * @param fileName 文件名
	 * @return
	 * @throws IOException 
	 * @throws BiffException 
	 * @throws  
	 * 		   
	 */
	public List<String> getImportDataByXLS(String fileName) throws BiffException, IOException{
		logger.debug("getImportDataByXLS{}",fileName);
		List<String> list = new ArrayList<String>();
		File f = new File(getFilePath()+fileName);
		Workbook wwb = Workbook.getWorkbook(f);
		Sheet ws = null;
		//总sheet数
		//int sheetNumber = wwb.getNumberOfSheets();
		int sheetNumber = 1;
		for (int m=0;m<sheetNumber;m++){
			ws = wwb.getSheet(m);

			//当前页总记录行数和列数
			int rowCount = ws.getRows();
			//int columeCount = ws.getColumns();

			if(null!=ws.getCell(0,0).getContents()){
				String line = ws.getCell(0,0).getContents().trim();
				/**
				 * 判断导入文件查询的条件，默认为用户账号，当匹配到设备序列号时，
				 * 则以设备序列号作为条件，否则一律以用户账号作为条件
				 */
				if(null!=line && "设备序列号".equals(line)){
					this.importQueryField = "device_serialnumber";
				}else{
					this.importQueryField = "username";
				}
			}

			//取当前页所有值放入list中
			for (int i=1;i<rowCount;i++){
				String temp = ws.getCell(0, i).getContents();
				if(null!=temp && !"".equals(temp)){
					if(!"".equals(ws.getCell(0, i).getContents().trim())){
						list.add(ws.getCell(0, i).getContents().trim());
					}
				}
			}
		}
		f = null;
		return list;
	}
	/**
	 * 解析文件（txt格式）
	 * 
	 * @param fileName 文件名
	 * @return
	 * @throws FileNotFoundException 
	 * 		   IOException
	 */
	public List<String> getParamNodeImportDataByTXT(String fileName) throws FileNotFoundException,IOException{
		logger.debug("getImportDataByTXT{}",fileName);
		List<String> list = new ArrayList<String>();
		BufferedReader in = new BufferedReader(new FileReader(getFilePath()+fileName));
		String line = in.readLine();
		/**
		 * 判断导入文件查询的条件，默认为用户账号，当匹配到设备序列号时，
		 * 则以设备序列号作为条件，否则一律以用户账号作为条件
		 */
		if(null!=line && "设备序列号".equals(line)){
			this.importQueryField = "device_serialnumber";
		}else{
			this.importQueryField = "username";
		}
		//从第二行开始读取数据，并对每行数据去掉首尾空格
		while ((line = in.readLine()) != null) {
			if(!"".equals(line.trim())){
				list.add(line.trim());
			}
		}
		in.close();
		in = null;

		return list;
	}

	/**
	 * 解析文件（xls格式）
	 * 
	 * @param fileName 文件名
	 * @return
	 * @throws IOException 
	 * @throws BiffException 
	 * @throws  
	 * 		   
	 */
	public List<String> getParamNodeImportDataByXLS(String fileName) throws BiffException, IOException{
		logger.debug("getImportDataByXLS{}",fileName);
		List<String> list = new ArrayList<String>();
		File f = new File(getFilePath()+fileName);
		Workbook wwb = Workbook.getWorkbook(f);
		Sheet ws = null;
		//总sheet数
		//int sheetNumber = wwb.getNumberOfSheets();
		int sheetNumber = 1;
		for (int m=0;m<sheetNumber;m++){
			ws = wwb.getSheet(m);

			//当前页总记录行数和列数
			int rowCount = ws.getRows();
			//int columeCount = ws.getColumns();

			if(null!=ws.getCell(0,0).getContents()){
				String line = ws.getCell(0,0).getContents().trim();
				/**
				 * 判断导入文件查询的条件，默认为用户账号，当匹配到设备序列号时，
				 * 则以设备序列号作为条件，否则一律以用户账号作为条件
				 */
				if(null!=line && "设备序列号".equals(line)){
					this.importQueryField = "device_serialnumber";
				}else{
					this.importQueryField = "username";
				}
			}

			//取当前页所有值放入list中
			for (int i=1;i<rowCount;i++){
				String temp = ws.getCell(0, i).getContents();
				if(null!=temp && !"".equals(temp)){
					if(!"".equals(ws.getCell(0, i).getContents().trim())){
						list.add(ws.getCell(0, i).getContents().trim());
					}
				}
			}
		}
		f = null;
		return list;
	}
	/**
	 * 获取文件路径
	 * 
	 * @return
	 */
	public String getFilePath() {
		//获取系统的绝对路径
		String lipossHome = "";
		String a = FileUploadAction.class.getResource("/").getPath();
		try{
			lipossHome = java.net.URLDecoder.decode(a.substring(0,a.lastIndexOf("WEB-INF")-1),"UTF-8");
		}catch(Exception e){
			logger.error(e.getMessage());
			lipossHome = null;
		}
		logger.debug("{}待解析的文件路径",lipossHome);
		return lipossHome + "/temp/";
	}

	/**
	 * 查询设备（带列表）
	 * 
	 * @return
	 */
	public String queryDeviceListBySQL() {
		logger.debug("GwDeviceQueryACT=>queryDeviceListBySQL()");
		try{
			if(!gwDeviceBio.checkMatchSQL(gwShare_matchSQL))
			{
				this.gwShare_msg = gwDeviceBio.getMsg();
				return "shareList0";
			}
			gwShare_matchSQL = gwShare_matchSQL.replace("[", "'");
			logger.warn("使用自定义SQL查询："+gwShare_matchSQL);
			total = gwDeviceBio.getDeviceListCountBySQL(gwShare_matchSQL);

			if(total<=50){
				this.deviceList = gwDeviceBio.getDeviceListBySQL(gwShare_matchSQL);
				if(null==this.deviceList || this.deviceList.size()<1){
					this.gwShare_msg = gwDeviceBio.getMsg();
				}
				return "shareList0";
			}else{
				this.deviceList = gwDeviceBio.getDeviceListBySQL(curPage_splitPage,num_splitPage,gwShare_matchSQL);
				if (total % num_splitPage == 0) {
					maxPage_splitPage = total / num_splitPage;
				} else {
					maxPage_splitPage = total / num_splitPage + 1;
				}
				if(null==this.deviceList || this.deviceList.size()<1){
					this.gwShare_msg = gwDeviceBio.getMsg();
				}
				return "shareList";
			}
		}
		catch (Exception e)
		{
			logger.error("queryDeviceListBySQL-->Exception:{}"+new Object[]{e.getMessage()});
			return "shareList0";
		}
	}
	/**
	 * 获取查询SQL
	 * 
	 * @return
	 */
	public String queryDeviceListSQL() {
		logger.debug("GwDeviceQueryACT=>queryDeviceListSQL()");
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		long areaId = curUser.getAreaId();
		if(null!=gwShare_queryType){
			gwShare_queryType.trim();
		}

		if(null!=gwShare_nextCityId && !"-1".equals(gwShare_nextCityId) && !"null".equals(gwShare_nextCityId)){
			this.gwShare_cityId = gwShare_nextCityId.trim();
		}
		else{
			if(null!=gwShare_cityId){
				gwShare_cityId.trim();
			}else{
				this.gwShare_cityId = curUser.getCityId();
			}
		}
		if("true".equals(iscqsoft)&&gwShare_cityId.substring(gwShare_cityId.length()-1).equals(",")){
			String citys[] = gwShare_cityId.split(",");
			gwShare_cityId = "";
			for(int i=0;i<citys.length;i++){
				gwShare_cityId = gwShare_cityId+"'"+citys[i]+"',";
			}
			gwShare_cityId = gwShare_cityId.substring(0, gwShare_cityId.length()-1);
		}
		if(null!=gwShare_queryParam){
			gwShare_queryParam.trim();
		}
		if(null!=gwShare_queryField){
			gwShare_queryField.trim();
		}
		if(null!=gwShare_onlineStatus){
			gwShare_onlineStatus.trim();
		}
		if(null!=gwShare_vendorId){
			gwShare_vendorId.trim();
		}
		if(null!=gwShare_deviceModelId){
			gwShare_deviceModelId.trim();
		}
		if(null!=gwShare_devicetypeId){
			gwShare_devicetypeId.trim();
		}
		if(null!=gwShare_bindType){
			gwShare_bindType.trim();
		}
		if(null!=gwShare_deviceSerialnumber){
			gwShare_deviceSerialnumber.trim();
		}
		if(null!=gwShare_start_ip){
			gwShare_start_ip.trim();
		}
		if(null!=gwShare_end_ip){
			gwShare_end_ip.trim();
		}
		ajax = gwDeviceBio.getDeviceListSQL(gw_type,areaId, gwShare_queryType,
				gwShare_queryParam, gwShare_queryField,	gwShare_cityId, 
				gwShare_onlineStatus,gwShare_vendorId,gwShare_deviceModelId,
				gwShare_devicetypeId, gwShare_bindType, gwShare_deviceSerialnumber,iscqsoft,gwShare_start_ip,gwShare_end_ip,gwShare_netSpeed);

		return "ajax";
	}
	
	/**
	 * 分页查询
	 * @return
	 * @throws Exception
	 */
	public String goPage() throws Exception {
		logger.debug("GwDeviceQueryACT=>getDeviceList()");
		logger.warn("gwShare_cityId="+gwShare_cityId);
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		if(null!=gwShare_queryType){
			gwShare_queryType.trim();
		}
		if(null!=gwShare_queryParam){
			gwShare_queryParam.trim();
		}
		if(null!=gwShare_queryField){
			gwShare_queryField.trim();
		}
		if(null!=gwShare_cityId){
			gwShare_cityId.trim();
		}
		if(null!=gwShare_onlineStatus){
			gwShare_onlineStatus.trim();
		}
		if(null!=gwShare_vendorId){
			gwShare_vendorId.trim();
		}
		if(null!=gwShare_deviceModelId){
			gwShare_deviceModelId.trim();
		}
		if(null!=gwShare_devicetypeId){
			gwShare_devicetypeId.trim();
		}
		if(null!=gwShare_bindType){
			gwShare_bindType.trim();
		}
		if(null!=gwShare_deviceSerialnumber){
			gwShare_deviceSerialnumber.trim();
		}
		if(null!=gwShare_start_ip){
			gwShare_start_ip.trim();
		}
		if(null!=gwShare_end_ip){
			gwShare_end_ip.trim();
		}
		long areaId = curUser.getAreaId();	
		
		if(Global.GSDX.equals(instArea) && !StringUtil.IsEmpty(online_time)){
			long qryDayMills = Integer.valueOf(online_time) * 24 * 60 * 60;
			long currMills = System.currentTimeMillis();
			long time = currMills - qryDayMills;
			online_time = String.valueOf(time);
			logger.warn("goPage => onlin_time:{}",online_time);
		}
		
		this.deviceList = gwDeviceBio.getDeviceList(gw_type,curPage_splitPage,num_splitPage,
				areaId, gwShare_queryType,gwShare_queryParam, gwShare_queryField,
				gwShare_cityId, gwShare_onlineStatus,gwShare_vendorId,gwShare_deviceModelId,
				gwShare_devicetypeId, gwShare_bindType, gwShare_deviceSerialnumber,gwShare_start_ip,gwShare_end_ip,online_time,gwShare_netSpeed);
		total = gwDeviceBio.getDeviceListCount(gw_type,areaId, gwShare_queryType,
				gwShare_queryParam, gwShare_queryField,gwShare_cityId, gwShare_onlineStatus,
				gwShare_vendorId,gwShare_deviceModelId,gwShare_devicetypeId, 
				gwShare_bindType, gwShare_deviceSerialnumber,gwShare_start_ip,gwShare_end_ip,online_time,gwShare_netSpeed);
		if (total % num_splitPage == 0) {
			maxPage_splitPage = total / num_splitPage;
		} else {
			maxPage_splitPage = total / num_splitPage + 1;
		}
		return "shareList";
	}

	/**
	 * @return the ajax
	 */
	public String getAjax() {
		return ajax;
	}

	/**
	 * @param ajax
	 *            the ajax to set
	 */
	public void setAjax(String ajax) {
		this.ajax = ajax;
	}

	/**
	 * @return the gwDeviceBio
	 */
	public GwDeviceQueryBIO getGwDeviceBio() {
		return gwDeviceBio;
	}

	/**
	 * @param gwDeviceBio
	 *            the gwDeviceBio to set
	 */
	public void setGwDeviceBio(GwDeviceQueryBIO gwDeviceBio) {
		this.gwDeviceBio = gwDeviceBio;
	}

	public void setServletRequest(HttpServletRequest arg0) {
		this.request = arg0;
	}

	/**
	 * @param deviceList the deviceList to set
	 */
	public void setDeviceList(List deviceList) {
		this.deviceList = deviceList;
	}

	/**
	 * @return the deviceList
	 */
	public List getDeviceList() {
		return deviceList;
	}

	/**
	 * @return the deviceDetailMap
	 */
	public Map getDeviceDetailMap() {
		return deviceDetailMap;
	}

	/**
	 * @param deviceDetailMap the deviceDetailMap to set
	 */
	public void setDeviceDetailMap(Map deviceDetailMap) {
		this.deviceDetailMap = deviceDetailMap;
	}

	/**
	 * @return the deviceId
	 */
	public String getDeviceId() {
		return deviceId;
	}

	/**
	 * @param deviceId the deviceId to set
	 */
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	/**
	 * @return the gwShare_bindType
	 */
	public String getGwShare_bindType() {
		return gwShare_bindType;
	}

	/**
	 * @param gwShare_bindType the gwShare_bindType to set
	 */
	public void setGwShare_bindType(String gwShare_bindType) {
		this.gwShare_bindType = gwShare_bindType;
	}

	/**
	 * @return the gwShare_cityId
	 */
	public String getGwShare_cityId() {
		return gwShare_cityId;
	}

	/**
	 * @param gwShare_cityId the gwShare_cityId to set
	 */
	public void setGwShare_cityId(String gwShare_cityId) {
		this.gwShare_cityId = gwShare_cityId;
	}

	/**
	 * @return the gwShare_deviceModelId
	 */
	public String getGwShare_deviceModelId() {
		return gwShare_deviceModelId;
	}

	/**
	 * @param gwShare_deviceModelId the gwShare_deviceModelId to set
	 */
	public void setGwShare_deviceModelId(String gwShare_deviceModelId) {
		this.gwShare_deviceModelId = gwShare_deviceModelId;
	}

	/**
	 * @return the gwShare_deviceSerialnumber
	 */
	public String getGwShare_deviceSerialnumber() {
		return gwShare_deviceSerialnumber;
	}

	/**
	 * @param gwShare_deviceSerialnumber the gwShare_deviceSerialnumber to set
	 */
	public void setGwShare_deviceSerialnumber(String gwShare_deviceSerialnumber) {
		this.gwShare_deviceSerialnumber = gwShare_deviceSerialnumber;
	}

	/**
	 * @return the gwShare_devicetypeId
	 */
	public String getGwShare_devicetypeId() {
		return gwShare_devicetypeId;
	}

	/**
	 * @param gwShare_devicetypeId the gwShare_devicetypeId to set
	 */
	public void setGwShare_devicetypeId(String gwShare_devicetypeId) {
		this.gwShare_devicetypeId = gwShare_devicetypeId;
	}

	/**
	 * @return the gwShare_onlineStatus
	 */
	public String getGwShare_onlineStatus() {
		return gwShare_onlineStatus;
	}

	/**
	 * @param gwShare_onlineStatus the gwShare_onlineStatus to set
	 */
	public void setGwShare_onlineStatus(String gwShare_onlineStatus) {
		this.gwShare_onlineStatus = gwShare_onlineStatus;
	}

	/**
	 * @return the gwShare_queryField
	 */
	public String getGwShare_queryField() {
		return gwShare_queryField;
	}

	/**
	 * @param gwShare_queryField the gwShare_queryField to set
	 */
	public void setGwShare_queryField(String gwShare_queryField) {
		this.gwShare_queryField = gwShare_queryField;
	}

	/**
	 * @return the gwShare_queryParam
	 */
	public String getGwShare_queryParam() {
		return gwShare_queryParam;
	}

	/**
	 * @param gwShare_queryParam the gwShare_queryParam to set
	 */
	public void setGwShare_queryParam(String gwShare_queryParam) {
		this.gwShare_queryParam = gwShare_queryParam;
	}

	/**
	 * @return the gwShare_queryResultType
	 */
	public String getGwShare_queryResultType() {
		return gwShare_queryResultType;
	}

	/**
	 * @param gwShare_queryResultType the gwShare_queryResultType to set
	 */
	public void setGwShare_queryResultType(String gwShare_queryResultType) {
		this.gwShare_queryResultType = gwShare_queryResultType;
	}

	/**
	 * @return the gwShare_queryType
	 */
	public String getGwShare_queryType() {
		return gwShare_queryType;
	}

	/**
	 * @param gwShare_queryType the gwShare_queryType to set
	 */
	public void setGwShare_queryType(String gwShare_queryType) {
		this.gwShare_queryType = gwShare_queryType;
	}

	/**
	 * @return the currencyType
	 */
	public String getCurrencyType()
	{
		return currencyType;
	}

	/**
	 * @param currencyType the currencyType to set
	 */
	public void setCurrencyType(String currencyType)
	{
		this.currencyType = currencyType;
	}

	/**
	 * @return the gwShare_vendorId
	 */
	public String getGwShare_vendorId() {
		return gwShare_vendorId;
	}

	/**
	 * @param gwShare_vendorId the gwShare_vendorId to set
	 */
	public void setGwShare_vendorId(String gwShare_vendorId) {
		this.gwShare_vendorId = gwShare_vendorId;
	}

	public String getSearchTxt()
	{
		return searchTxt;
	}

	public String getSeparator()
	{
		return separator;
	}

	public void setSeparator(String separator)
	{
		this.separator = separator;

	}

	/**
	 * @return the total
	 */
	public int getTotal() {
		return total;
	}

	/**
	 * @param total the total to set
	 */
	public void setTotal(int total) {
		this.total = total;
	}

	public void setSearchTxt(String searchTxt)
	{
		try
		{
			this.searchTxt = URLDecoder.decode(searchTxt, "UTF-8");
			if (searchTxt != null)
			{
				this.searchTxt = this.searchTxt.trim();
			}
		} catch (UnsupportedEncodingException e)
		{
			logger.error(e.getMessage());
		}
	}

	/**
	 * @return the gwShare_fileName
	 */
	public String getGwShare_fileName() {
		return gwShare_fileName;
	}

	/**
	 * @param gwShare_fileName the gwShare_fileName to set
	 */
	public void setGwShare_fileName(String gwShare_fileName) {
		this.gwShare_fileName = gwShare_fileName;
	}

	/**
	 * @return the gwShare_msg
	 */
	public String getGwShare_msg() {
		return gwShare_msg;
	}

	/**
	 * @param gwShare_msg the gwShare_msg to set
	 */
	public void setGwShare_msg(String gwShare_msg) {
		this.gwShare_msg = gwShare_msg;
	}
	public String getGw_type()
	{
		return gw_type;
	}


	public void setGw_type(String gwType)
	{
		gw_type = gwType;
	}


	public String getGwShare_matchSQL()
	{
		return gwShare_matchSQL;
	}


	public void setGwShare_matchSQL(String gwShare_matchSQL)
	{
		this.gwShare_matchSQL = gwShare_matchSQL;
	}
	/**
	 * @return the msg
	 */
	public String getMsg() {
		return msg;
	}

	/**
	 * @param msg the msg to set
	 */
	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}


	public String getIsBatch()
	{
		return isBatch;
	}


	public void setIsBatch(String isBatch)
	{
		this.isBatch = isBatch;
	}


	public String getImportQueryField()
	{
		return importQueryField;
	}


	public void setImportQueryField(String importQueryField)
	{
		this.importQueryField = importQueryField;
	}

	
	public List<Map> getAwifi()
	{
		return awifi;
	}

	
	public void setAwifi(List<Map> awifi)
	{
		this.awifi = awifi;
	}

	
	public String getIs_awifi()
	{
		return is_awifi;
	}

	
	public void setIs_awifi(String is_awifi)
	{
		this.is_awifi = is_awifi;
	}

	
	public int getAccount()
	{
		return account;
	}

	
	public void setAccount(int account)
	{
		this.account = account;
	}

	public String getInstArea() {
		return instArea;
	}

	public void setInstArea(String instArea) {
		this.instArea = instArea;
	}

	
	public String getIscqsoft()
	{
		return iscqsoft;
	}

	
	public void setIscqsoft(String iscqsoft)
	{
		this.iscqsoft = iscqsoft;
	}

	public String getNoSplitFlag() {
		return noSplitFlag;
	}

	public void setNoSplitFlag(String noSplitFlag) {
		this.noSplitFlag = noSplitFlag;
	}

	
	public String getStackrefresh() {
		return stackrefresh;
	}

	
	public void setStackrefresh(String stackrefresh) {
		this.stackrefresh = stackrefresh;
	}

	
	/**
	 * @return the gwShare_start_ip
	 */
	public String getGwShare_start_ip()
	{
		return gwShare_start_ip;
	}

	
	/**
	 * @param gwShare_start_ip the gwShare_start_ip to set
	 */
	public void setGwShare_start_ip(String gwShare_start_ip)
	{
		this.gwShare_start_ip = gwShare_start_ip;
	}

	
	/**
	 * @return the gwShare_end_ip
	 */
	public String getGwShare_end_ip()
	{
		return gwShare_end_ip;
	}

	
	/**
	 * @param gwShare_end_ip the gwShare_end_ip to set
	 */
	public void setGwShare_end_ip(String gwShare_end_ip)
	{
		this.gwShare_end_ip = gwShare_end_ip;
	}
	public String getOnline_time()
	{
		return online_time;
	}

	
	public void setOnline_time(String online_time)
	{
		this.online_time = online_time;
	}
	
	public String getLimitNum() {
		return limitNum;
	}

	public void setLimitNum(String limitNum) {
		this.limitNum = limitNum;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getTimeType() {
		return timeType;
	}

	public void setTimeType(String timeType) {
		this.timeType = timeType;
	}

	
	public String getNewBatchSetRes()
	{
		return newBatchSetRes;
	}

	
	public void setNewBatchSetRes(String newBatchSetRes)
	{
		this.newBatchSetRes = newBatchSetRes;
	}

	public String getAHLT_softUp() {
		return AHLT_softUp;
	}

	public void setAHLT_softUp(String AHLT_softUp) {
		this.AHLT_softUp = AHLT_softUp;
	}

	public String getGwShare_netSpeed() {
		return gwShare_netSpeed;
	}

	public void setGwShare_netSpeed(String gwShare_netSpeed) {
		this.gwShare_netSpeed = gwShare_netSpeed;
	}
}
