/**
 * 
 */

package com.linkage.module.itms.resource.act;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.namespace.QName;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.linkage.commons.util.StringUtil;
import com.linkage.commons.util.TimeUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.Global;
import com.linkage.module.itms.resource.bio.DeviceTypeInfoBIO;
import com.linkage.module.itms.resource.enums.AHLTDevVersionTypeEnum;

import action.splitpage.splitPageAction;

/**
 * @author wuchao设备版本查询操作类
 */
public class DeviceTypeInfoACT extends splitPageAction implements SessionAware,
		ServletRequestAware {

	// 日志记录
	private static Logger logger = LoggerFactory
			.getLogger(DeviceTypeInfoACT.class);
	/** 查询的所有数据列表 */

	// request取登陆帐号使用
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
	/** 是否审核 1是经过审核,0未审核 */
	private int is_check = -1;
	/** 设备类型 1是e8-b,2是机卡一体e8-c,3是机卡分离型 e8-c */
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
	/** 设备支持的协议 */
	private String servertype;
	/** 上行方式 */
	private int access_style_relay_id;
	/** 设备Ip支持方式 ipv4 或ipv4和ipv6 */
	private String ipType;
	/** 是否为规范版本 */
	private String isNormal;
	/** 系统类型 1ITMS，2BBMS */
	private String gw_type;
	/**
	 * 设备类型编辑功能页面选择  0：灰化设备类型下拉框,新增编辑设备类型功能（仅修改设备类型）；1：保持现有功能；
	 * 可通过action添加请求参数配置，例：itms/resource/deviceTypeInfo!queryDeviceList.action?gw_type=1&editDeviceType=1
	 * 此控件参数是方式白天通过更新版本设备信息大量更新设备表数据影响数据库，默认为0.
	 */
	private String editDeviceType="0";
	/** 终端规格 */
	private int spec_id;

	private String machineConfig;// 是否支持机顶盒配置
	
	private String is_awifi;// 是否支持awifi开通
	
	private String is_QOE;// 是否支持awifi开通
	
	private String is_esurfing;//是否支持天翼网关
	
	private String is_newVersion = "";//天翼网关最新版本

	private String ipvsix;// 是否支持IPV6

	private String startOpenDate;// 版本定版开始时间

	private String endOpenDate;// 版本定版结束时间

	private String mbBroadband;// 是否支持百兆宽带
	
	private String gbBroadband;// 是否支持百兆宽带

	private long specId;

	private long detailSpecId;

	private List<Map<String, String>> specList = null;

	// 设备类型
	private List<Map<String, String>> devTypeMap;
	
	// 定版原因
	private String reason;
	
	// 新疆，光猫版本是否支持组播：1，是； 2，否；
	private int is_multicast;
	
	//江苏电信，是否支持仿真测速
	private int is_speedtest;

	//江苏电信，是否支持仿真测速
	private int isSupSpeedTest_Query;

	// 旧的版本对应的 devicetype_id字符串，多个用,分割
	private String oldVersionDeviceTypeIds;
	
	/** 新疆新增字段 * */
	
	/** * 设备版本类型* 1.E8C* 2.天翼网关1.0* 3.天翼网关2.0*/
	private String device_version_type="-1";
	/*** 是否支持wifi * 1.是* 0.否*/
	private String wifi="-1";
	/*** wifi支持频率* 1.2.4G* 2.2.4G/5G*/
	private String wifi_frequency="-1";
	/*** wifi支持最大下载速率：MB/S */
	private String download_max_wifi="0";
	/*** 是否有千兆口 1.是 0.否*/
	private String gigabit_port="-1";
	/*** 版本特性 2.全路由 1.测速 0.普通*/
	private String version_feature="-1";
	/**
	 * 千兆口有哪些* 1.lan1/lan1* 2.lan2/lan1* 3.lan2* 4.lan3* 5.lan4*/
	private String gigabit_port_type="-1";
	/*** lan口的最大下载速率：MB/S*/
	private String download_max_lan="0";
	/*** 电源功率*/
	private String power="";
	/*** 终端的入网时间*/
	private String terminal_access_time="";
	/**
	 * 版本升级对应关系选择有三种：
	 * 普通软件升级，参数值为1
	 * 业务相关软件升级，参数值为2
	 * 非业务软件升级，参数值为3
	 */
	private Integer relationType;
	
	/**
	 * 安审插件有三种类型：
	 * 溯源/净网/全部
	 */
	private int is_security_plugin = 0;//是否支持安审插件
	private int security_plugin_type = 0;
	
	/**
	 * wifi业务下发通道实例号
	 * 3,4,7,11
	 */
	private int ssid_instancenum = 0;
	
	/**
	 * H248物理标识口
	 * V0,V1
	 */
	private String hvoip_port = "";
	

	/**
	 * H248语音下发场景
	 * 1,2
	 */
	private String hvoip_type = "";
	
	

	/**
	 * sip语音下发场景
	 * 1,2
	 */
	private String svoip_type = "";
	
	
	private int gigabitNum	= 0; 
	private int mbitNum	=	0;
	private int voipNum	=	0;
	private String is_wifi_double	="";
	private String fusion_ability	=	"";
	private String terminal_access_method	=	"";
	private String devMaxSpeed	=	"";
	private String res_type_id	=	"";
	private String res_vendor	=	"";
	private String res_type	=	"";
	private String remark	=	"";
	// 设备版本类型
	private List<Map<String, String>> devVersionTypeMap;
	private String deviceVersionType;
	private List<Map<String, String>> devVersionListTree;
	private String devVersionTotal;
    private int vendorId = -1;
    private int modelId = -1;
    private JSONObject devTypeInfoMap;
    private String isCheck;

    // 是否支持云网超宽
    private int iscloudnet;
    

	/**
	 * 查询的结果
	 */
	private List dataList = null;
	private List detailDataList = null;
	private List<Map> data; // 需要导出的结果集，必须是处理过的可以直接显示的结果
	private String[] column; // 需要导出的列名，对应data中的键值
	private String[] title; // 显示在导出文档上的列名
	private String fileName; // 导出的文件名（不包括后缀名）
	private String excel;


	public String queryDeviceList() {
		logger.debug("***ACT** queryDeviceList()  editDeviceType:"+editDeviceType);
		devTypeMap = bio.getGwDevType();
		specList = bio.querySpecList();
		if(Global.AHLT.equals(Global.instAreaShortName)){
			devVersionTypeMap = AHLTDevVersionTypeEnum.getAll();
		}
		if ("1".equals(gw_type)) {
			return "itms";
		} else {
			return "bbms";
		}

	}
	/**
	 * 生成前台页面的多选框。
	 */
	public String queryByVendorIdAndDeviceModelId(){
		List<Map> list = bio.queryByVendorIdAndDeviceModelId(this.vendor, this.device_model);
		StringBuffer htmlString = new StringBuffer();
		if(list!=null && !list.isEmpty()){
			for(Map map:list){
				Object version = map.get("softwareversion");
				Object hardVersion = map.get("hardwareversion");
				Object devicetype_id = map.get("devicetype_id");
				htmlString.append("<span style=\"width:24%\"><input name=\"version\" type=\"checkbox\" value=\""+devicetype_id.toString()+"\" />"+hardVersion.toString()+"("+version.toString()+")"+"</span>");
			}
		}
		ajax = htmlString.toString();
		logger.debug("listByVendorIdAndDeviceModelId:ajax = {}",ajax);
		return "ajax";
	}
	
	/**
	 * 审核设备类型做三件事：
	 * 1. update表tab_devicetype_info，将状态改为已审核
	 * 2. 发webservice
	 * 3. 插入表gw_soft_upgrade_temp_map
	 * @return
	 */
	public String checkDeviceType(){
		try {
			
			// 1. webservice的数据准备
			StringBuffer oldVersionsBuffer = new StringBuffer();
			String oldVersions = null;
			logger.warn("页面传过来的旧版本id字符串：{}", oldVersionDeviceTypeIds);
			if(oldVersionDeviceTypeIds!=null){
				String[] split = oldVersionDeviceTypeIds.split(",");
				for(String idStr:split){
					if(idStr != null && !StringUtil.IsEmpty(idStr)){
						long id = Long.parseLong(idStr);
						Map map = bio.queryByDeviceTypeId(id);
						oldVersionsBuffer.append(map.get("softwareversion")).append("、");
						if(id != deviceTypeId){
							// 2. 插入表gw_soft_upgrade_temp_map，插入之前由于有唯一索引的存在，故而要先删除掉
							bio.deleteGwSoftUpgradeTempMapByTempIdAndOldDeviceTypeId(relationType, id);
							bio.insertGwSoftUpgradeTempMap(relationType, id, deviceTypeId);
						}
						else{
							// 说明在策略表：gw_soft_upgrade_temp_map中，新旧版本的devicetype_id相同
							// 此时对于gw_soft_upgrade_temp_map，什么都不做
						}
						
						
					}
				}
			}
			// 前台传过来的是空
			else{
				oldVersionsBuffer = new StringBuffer("");
			}
			oldVersions = (oldVersionsBuffer.toString().endsWith("、"))?oldVersionsBuffer.substring(0, oldVersionsBuffer.length()-1):oldVersionsBuffer.toString();
			
			// 以下拼接VersionDescription 功能描述
			StringBuffer versionDescription = new StringBuffer();
			// 支持的协议
			List<Map> proList= bio.queryDevicetypeInfoServertypeByDeviceTypeId(deviceTypeId);
			if(proList!=null && !proList.isEmpty()){
				Set<String> set = new HashSet<String>();
				for(Map map:proList){
					Object server_type = (Object)map.get("server_type");
					if(server_type!=null){
						set.add(server_type.toString());
					}
				}
				if(!set.isEmpty()){
					if(set.contains("0")){
						versionDescription.append("支持IMS SIP、");
					}
					if(set.contains("1")){
						versionDescription.append("支持软交换SIP、");
					}
					if(set.contains("2")){
						versionDescription.append("支持H248、");
					}
				}
				
			}
			Map map = bio.queryByDeviceTypeId(deviceTypeId);
			if(map!=null){
				Object ip_typeObj = (Object)map.get("ip_type");
				if(ip_typeObj!=null){
					Integer ip_type = Integer.parseInt(ip_typeObj.toString());
					versionDescription.append("支持");
					
					switch(ip_type){
						case 0:
							versionDescription.append("IPV4");
							break;
						case 2:
							versionDescription.append("IPV4和IPV6");
							break;
						case 3:
							versionDescription.append("DS-Lite");
							break;
						case 4:
							versionDescription.append("LAFT6");
							break;
						case 5:
							versionDescription.append("纯IPV6");
							break;
						default:
							versionDescription.append("IPV4");
							break;
					}
				}

				Object mbbroadbandObj = (Object)map.get("mbbroadband");
				if(mbbroadbandObj!=null){
					Integer mbbroadband = Integer.parseInt(mbbroadbandObj.toString());
					if(mbbroadband == 1){
						versionDescription.append("、支持百兆宽带");
					}
					Object zeroconfObj = map.get("zeroconf");
					if(zeroconfObj!=null){
						Integer zeroconf = Integer.parseInt(zeroconfObj.toString());
						versionDescription.append("、支持机顶盒零配置");
					}
				}
				
				// 生成需要发送的xml片段
				String genXML = this.genXML(TimeUtil.getCurrentTimeInMills()+"", "CX_01", "5", (String)map.get("vendor_add"), (String)map.get("device_model"), (String)map.get("specversion"), (String)map.get("hardwareversion"), (String)map.get("softwareversion"), oldVersions, (String)map.get("reason"), map.get("versionttime"), versionDescription.toString());
			
				final String endPointReference = LipossGlobals.getLipossProperty("jsdxVersionRelease");
				logger.warn("websercie URL：{}", endPointReference);
				Service service = new Service();
				Call call = null;
				call = (Call) service.createCall();
				call.setTargetEndpointAddress(new URL(endPointReference));
				QName qn = new QName("http://server.webservice.eoms.zbiti.com", "setVersion");
				call.setOperationName(qn);
				String returnParam = (String) call.invoke(new Object[] { genXML });
				logger.warn("审核设备 webservice 回参：{}", returnParam);
			}
			
			// 3. update表tab_devicetype_info，将状态改为已审核
			bio.updateIsCheck(deviceTypeId);
			
			ajax = "1";
		} catch (Exception e) {
			logger.error("发生异常：{}", e);
			ajax = "-1";
		}
		
		
		
		return "ajax";
	}

	String genXML(String cmdId, String cmdType,String clientType,String vendor,String device_model,String specversion, 
			String hardwareversion, String softwareversion, String softwareversions, String reason, Object setTime, String versionDescription){
		vendor= (vendor != null)?vendor:"";
		device_model = (device_model != null)?device_model:"";
		specversion = (specversion != null)?specversion:"";
		softwareversion = (softwareversion != null)?softwareversion:"";
		softwareversions = (softwareversions != null)?softwareversions:"";
		reason = (reason != null)?reason:"";
		
		
		String xml = "<?xml version=\"1.0\" encoding=\"GBK\"?>"+
		"<root>"+
			"<CmdID>"+cmdId+"</CmdID>"+
			"<CmdType>"+cmdType+"</CmdType>"+
			"<ClientType>"+clientType+"</ClientType>"+
			"<Param>"+
		         "<vendor>"+vendor+"</vendor>"+
				"<device_model>"+device_model+"</device_model>"+
		        "<specversion>"+specversion+"</specversion>"+
		        "<hardwareversion>"+hardwareversion+"</hardwareversion>"+
		        "<softwareversion>"+softwareversion+"</softwareversion>"+
				"<softwareversions>"+softwareversions+"</softwareversions>"+
				"<Reason>"+reason+"</Reason>"+
		        "<SetTime>"+setTime+"</SetTime>"+
				"<VersionDescription>" + versionDescription +"</VersionDescription>"+
			"</Param>"+
		"</root>";
		logger.warn("生成的webservice xml:{}", xml);
		return xml;
	}
	
	
	public String dealTime(String time) {
		SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date str = new Date();
		try {
			str = date.parse(time);
		} catch (ParseException e) {

			logger.warn("选择开始或者结束的时间格式不对:" + time);
		}

		return str.getTime() / 1000 + "";
	}

	public String queryList() {
		logger.debug("***ACT** queryList() editDeviceType:"+editDeviceType);
		if (startTime != null && !"".equals(startTime)) {
			startTime = dealTime(startTime);
		}
		if (endTime != null && !"".equals(endTime)) {
			endTime = dealTime(endTime);
		}
		if (startOpenDate != null && !"".equals(startOpenDate)) {
			startOpenDate = dealTime(startOpenDate);
		}
		if (endOpenDate != null && !"".equals(endOpenDate)) {
			endOpenDate = dealTime(endOpenDate);
		} 

		deviceList = bio.queryDeviceList(vendor, device_model, hard_version,
				soft_version, is_check, rela_dev_type, curPage_splitPage,
				num_splitPage, startTime, endTime, access_style_relay_id,
				spec_id, machineConfig,ipvsix, startOpenDate, endOpenDate,
				mbBroadband,device_version_type,wifi,wifi_frequency,download_max_wifi,gigabit_port,gigabit_port_type,download_max_lan,power,terminal_access_time,deviceVersionType,isSupSpeedTest_Query);
		maxPage_splitPage = bio.getMaxPage_splitPage();
			logger.warn("maxPage_splitPage"+maxPage_splitPage);	
		return "queryList";
		
	}


	public String exportExcel() {
		logger.debug("***ACT** exportExcel() editDeviceType:"+editDeviceType);
		if (startTime != null && !"".equals(startTime)) {
			startTime = dealTime(startTime);
		}
		if (endTime != null && !"".equals(endTime)) {
			endTime = dealTime(endTime);
		}
		if (startOpenDate != null && !"".equals(startOpenDate)) {
			startOpenDate = dealTime(startOpenDate);
		}
		if (endOpenDate != null && !"".equals(endOpenDate)) {
			endOpenDate = dealTime(endOpenDate);
		}

		data = bio.queryDetailForExcel(vendor, device_model, hard_version,
				soft_version, is_check, rela_dev_type, curPage_splitPage,
				num_splitPage, startTime, endTime, access_style_relay_id,
				spec_id, machineConfig,ipvsix, startOpenDate, endOpenDate,
				mbBroadband,device_version_type,wifi,wifi_frequency,download_max_wifi,gigabit_port,gigabit_port_type,download_max_lan,power,terminal_access_time,isSupSpeedTest_Query);
		String excelCol = "vendor_add#device_model#softwareversion#hardwareversion#is_check_name#type_name#devIpType#mbbroadband#device_version_type#is_speedTest";
		String excelTitle = "设备厂商#设备型号#软件版本#硬件版本#是否审核#上行方式#设备ip支持方式#是否支持千兆带宽#设备版本类型#是否支持测速";
		column = excelCol.split("#");
		title = excelTitle.split("#");
		fileName = "设备版本详情";
		return "excel";

	}




	public String getPortAndType() {
		ajax = bio.getPortAndType(deviceTypeId);
		return "ajax";
	}

	public String queryDetail() {
		logger.debug("!!detailSpecId= " + detailSpecId);
		deviceList = bio.queryDeviceDetail(deviceTypeId, detailSpecId);
		logger.debug("deviceList="+deviceList.toString());
		return "detail";
	}

	public String deleteDevice() {
		logger.debug("into deleteDevice()");
		// 不可删除
		if (!bio.canDeleteDevice(deleteID)) {
			ajax = "0";
			return "ajax";
		}
		// 可以删除
		try {
			bio.deleteDevice(gw_type, deleteID);
			ajax = "1";
		} catch (Exception e) {
			ajax = "-1";
		}
		return "ajax";
	}

	/**
	 * JXDX-ITMS-REQ-20180913-WUWF-001(ITMS平台设备版本页面新增维护功能) 新增字段
	 * wifi能力 0：无  1：802.11b 2：802.11b/g 3：802.11b/g/n 4：802.11b/g/n/ac
	 * 
	 */
	private int wifi_ability;
	
	public int getWifi_ability() {
		return wifi_ability;
	}
	public void setWifi_ability(int wifi_ability) {
		this.wifi_ability = wifi_ability;
	}
	/**
	 * add devicetype
	 * 
	 * @return
	 */
	public String addDevType() {
		logger.warn("**ACT addDevType deviceTypeId:"+deviceTypeId+"  editDeviceType:"+editDeviceType);
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		// 定制人
		long acc_oid = curUser.getUser().getId();
		
		if (startOpenDate != null && !"".equals(startOpenDate)) {
			startOpenDate = dealTime(startOpenDate);
		}
		//TODO：添加支持仿真测速的参数
		try
		{
			if (deviceTypeId == -1) {
				ajax = bio.addDevTypeInfo(gw_type, vendor, device_model,
						speversion, hard_version, soft_version, is_check,
						rela_dev_type, typeId, portInfo, servertype, acc_oid,
						ipType, specId, mbBroadband, startOpenDate, machineConfig,
						is_awifi, reason, is_multicast ,is_speedtest, is_QOE,is_esurfing,
						device_version_type,wifi,wifi_frequency,download_max_wifi,gigabit_port,
						gigabit_port_type,download_max_lan,power,terminal_access_time, gbBroadband, 
						wifi_ability,version_feature,is_security_plugin,security_plugin_type,is_newVersion,ssid_instancenum,hvoip_port,hvoip_type,
						svoip_type,gigabitNum,mbitNum,voipNum,devMaxSpeed,is_wifi_double,fusion_ability,terminal_access_method,iscloudnet
						);

			} else {
				ajax = bio.updateDevTypeInfo(gw_type, deviceTypeId, vendor,
						device_model, speversion, hard_version, soft_version,
						is_check, rela_dev_type, typeId, portInfo, servertype,
						acc_oid, ipType, isNormal, specId, mbBroadband,
						startOpenDate, machineConfig,is_awifi, reason,editDeviceType, 
						is_multicast,is_speedtest, is_QOE,is_esurfing,
						device_version_type,wifi,wifi_frequency,download_max_wifi,
						gigabit_port,gigabit_port_type,download_max_lan,power,
						terminal_access_time,gbBroadband, wifi_ability,version_feature,
						is_security_plugin,
						security_plugin_type,
						is_newVersion,
						ssid_instancenum,
						hvoip_port,
						hvoip_type,
						svoip_type,gigabitNum,mbitNum,voipNum,devMaxSpeed,is_wifi_double,
						fusion_ability,terminal_access_method,iscloudnet
						);
			}
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			logger.error("Exception:{}",org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace(e));
		}
		
		logger.warn("**ACT addDevType ajax = {}", ajax);
		return "ajax";
	}

	/**
	 * update is_check
	 * 
	 * @return
	 */
	public String updateIsCheck() {
		logger.debug("info updateIsCheck()");
		try {
			bio.updateIsCheck(deviceTypeId);
			ajax = "1";
		} catch (Exception e) {
			ajax = "-1";
		}
		return "ajax";
	}

	public String isNormalVersion() {
		logger.debug("isNormalVersion()");
		ajax = bio.isNormalVersion(device_model) + "";
		return "ajax";
	}

	public String getTypeNameList() {
		logger.debug("getTypeNameList()");
		ajax = bio.getTypeNameList(typeId);
		return "ajax";
	}
	
	/**
	 * 保存设备类型
	 * @return
	 */
	public String updateDeviceType(){
		logger.debug("***ACT updateDeviceType() deviceTypeId:"+deviceTypeId+"  rela_dev_type:"+rela_dev_type);
		Calendar rightNow = Calendar.getInstance();
		int hours = rightNow.get(Calendar.HOUR_OF_DAY);
		if(22<=hours && hours<24){
			ajax = bio.updateDeviceType(deviceTypeId,rela_dev_type,gw_type);
		}else{
			ajax ="请在闲时(22:00-24:00)编辑设备类型!";
		}
		logger.debug("***ACT updateDeviceType ajax:"+ajax);
		return "ajax";
	}

	/**
	 * 保存设备版本类型
	 * @return
	 */
	public String updateDevVersionType(){
		logger.debug("DeviceTypeInfoACT updateDevVersionType() deviceTypeId:"+deviceTypeId+",devVersionType:"+deviceVersionType);
		int result = bio.updateDevVersionType(deviceTypeId,StringUtil.getIntegerValue(deviceVersionType));
		logger.warn("DeviceTypeInfoACT updateDeviceType result:"+result);
		ajax = result > 0 ? "更新设备版本类型成功！" : "更新设备版本类型失败！";
		return "ajax";
	}


	/**
	 * 安徽联通查询设备版本页面
	 * @return
	 */
	public String queryDevVersionList() {
		logger.debug("queryDevVersionList()");
		devTypeMap = bio.getGwDevType();
		specList = bio.querySpecList();
		devVersionTypeMap = AHLTDevVersionTypeEnum.getAll();
		return "queryTreeList";
	}

	/**
	 * 安徽联通 获取树形设备类型列表
	 * @return
	 */
	public String queryDevVersionTreeList() {
		logger.warn("queryDevVersionTreeList(), vendor:{},device_model:{},hard_version:{},soft_version:{},is_check:{},rela_dev_type:{}," +
				"startTime:{},endTime:{},access_style_relay_id:{},spec_id:{},machineConfig:{},ipvsix:{},startOpenDate:{},endOpenDate:{}," +
				"mbBroadband:{},deviceVersionType:{}" ,vendor, device_model, hard_version, soft_version, is_check, rela_dev_type, startTime,
				endTime, access_style_relay_id, spec_id, machineConfig, ipvsix, startOpenDate, endOpenDate,mbBroadband, deviceVersionType);
		if (startTime != null && !"".equals(startTime)) {
			startTime = dealTime(startTime);
		}
		if (endTime != null && !"".equals(endTime)) {
			endTime = dealTime(endTime);
		}
		if (startOpenDate != null && !"".equals(startOpenDate)) {
			startOpenDate = dealTime(startOpenDate);
		}
		if (endOpenDate != null && !"".equals(endOpenDate)) {
			endOpenDate = dealTime(endOpenDate);
		}

		JSONArray result = bio.devVersionTree(vendor, device_model, hard_version, soft_version, is_check, startTime,
				endTime, access_style_relay_id, spec_id, machineConfig, ipvsix, startOpenDate, endOpenDate,mbBroadband,
				deviceVersionType);
		ajax = result == null || result.size() == 0 ? "" : result.toJSONString();
		return "ajax";

	}

	/**
	 * 安徽联通点击二级 设备型号节点获取对应 设备版本列表
	 * @return
	 */
	public String getDevVersionList(){
		logger.warn("getDevVersionList(), vendor:{},device_model:{},hard_version:{},soft_version:{},is_check:{},rela_dev_type:{}," +
						"startTime:{},endTime:{},access_style_relay_id:{},spec_id:{},machineConfig:{},ipvsix:{},startOpenDate:{},endOpenDate:{}," +
						"mbBroadband:{},deviceVersionType:{},devVersionTotal:{}" ,vendorId, modelId, hard_version, soft_version, isCheck, rela_dev_type, startTime,
				endTime, access_style_relay_id, spec_id, machineConfig, ipvsix, startOpenDate, endOpenDate,mbBroadband, deviceVersionType,devVersionTotal);
		if (startTime != null && !"".equals(startTime)) {
			startTime = dealTime(startTime);
		}
		if (endTime != null && !"".equals(endTime)) {
			endTime = dealTime(endTime);
		}
		if (startOpenDate != null && !"".equals(startOpenDate)) {
			startOpenDate = dealTime(startOpenDate);
		}
		if (endOpenDate != null && !"".equals(endOpenDate)) {
			endOpenDate = dealTime(endOpenDate);
		}
		devVersionListTree = bio.getDevVersionList(vendorId, modelId, hard_version, soft_version, StringUtil.parseInt(isCheck,-1), startTime,
				endTime, access_style_relay_id, spec_id, machineConfig, ipvsix, startOpenDate, endOpenDate,mbBroadband,
				deviceVersionType,curPage_splitPage,num_splitPage);
		totalRowCount_splitPage = StringUtil.getIntegerValue(devVersionTotal);
		logger.warn("getDevVersionList with totalRowCount_splitPage:{}",totalRowCount_splitPage);
		maxPage_splitPage = bio.getMaxPage_splitPage();
		return "devVersionTree";
	}

	/**
	 * 安徽联通新增/修改页面初始化
	 * @return
	 */
	public String addDevVersionAHLT() {
		logger.warn("addDevVersionAHLT(),deviceTypeId:{}",deviceTypeId);
		devTypeMap = bio.getGwDevType();
		specList = bio.querySpecList();
		devVersionTypeMap = AHLTDevVersionTypeEnum.getAll();
		if (deviceTypeId != -1){
			//修改情况获取详情
			devTypeInfoMap = bio.getDetailByDevTypeId(StringUtil.getIntegerValue(deviceTypeId));
		}
		return "devVersionAdd";
	}

	/** getters and setters **/
	public DeviceTypeInfoBIO getBio() {
		return bio;
	}

	public void setBio(DeviceTypeInfoBIO bio) {
		this.bio = bio;
	}

	public int getDevice_model() {
		return device_model;
	}

	public void setDevice_model(int device_model) {
		this.device_model = device_model;
	}

	public List<Map> getDeviceList() {
		return deviceList;
	}

	public void setDeviceList(List<Map> deviceList) {
		this.deviceList = deviceList;
	}

	public String getHard_version() {
		return hard_version;
	}

	public void setHard_version(String hard_version) {
		try {
			this.hard_version = java.net.URLDecoder.decode(hard_version,
					"UTF-8");
		} catch (Exception e) {
			this.hard_version = hard_version;
		}
	}

	public int getIs_check() {
		return is_check;
	}

	public void setIs_check(int is_check) {
		this.is_check = is_check;
	}

	public int getRela_dev_type() {
		return rela_dev_type;
	}

	public void setRela_dev_type(int rela_dev_type) {
		this.rela_dev_type = rela_dev_type;
	}

	public String getSoft_version() {
		return soft_version;
	}

	public void setSoft_version(String soft_version) {
		try {
			this.soft_version = java.net.URLDecoder.decode(soft_version,
					"UTF-8");
		} catch (Exception e) {
			this.soft_version = soft_version;
		}
	}

	public int getVendor() {
		return vendor;
	}

	public void setVendor(int vendor) {
		this.vendor = vendor;
	}

	public Map getSession() {
		return session;
	}

	public String getSpeversion() {
		return speversion;
	}

	public void setSpeversion(String speversion) {
		try {
			this.speversion = java.net.URLDecoder.decode(speversion, "UTF-8");
		} catch (Exception e) {
			this.speversion = speversion;
		}
	}

	public int getDeleteID() {
		return deleteID;
	}

	public void setDeleteID(int deleteID) {
		this.deleteID = deleteID;
	}

	public String getAjax() {
		return ajax;
	}

	public void setAjax(String ajax) {
		this.ajax = ajax;
	}

	public long getDeviceTypeId() {
		return deviceTypeId;
	}

	public void setDeviceTypeId(long deviceTypeId) {
		this.deviceTypeId = deviceTypeId;
	}

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
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

	public void setSession(Map session) {
		this.session = session;
	}

	public String getPortInfo() {
		return portInfo;
	}

	public void setPortInfo(String portInfo) {
		try {
			this.portInfo = java.net.URLDecoder.decode(portInfo, "UTF-8");
		} catch (Exception e) {
			this.portInfo = portInfo;
		}
	}

	public String getServertype() {
		return servertype;
	}

	public void setServertype(String servertype) {
		this.servertype = servertype;
	}

	public void setServletRequest(HttpServletRequest arg0) {
		this.request = arg0;

	}

	public int getAccess_style_relay_id() {
		return access_style_relay_id;
	}

	public void setAccess_style_relay_id(int accessStyleRelayId) {
		access_style_relay_id = accessStyleRelayId;
	}

	public String getGw_type() {
		return gw_type;
	}

	public void setGw_type(String gwType) {
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

	public String getMachineConfig() {
		return machineConfig;
	}

	public void setMachineConfig(String machineConfig) {
		this.machineConfig = machineConfig;
	}

	public String getIpvsix() {
		return ipvsix;
	}

	public void setIpvsix(String ipvsix) {
		this.ipvsix = ipvsix;
	}

	public String getStartOpenDate() {
		return startOpenDate;
	}

	public void setStartOpenDate(String startOpenDate) {
		this.startOpenDate = startOpenDate;
	}

	public String getEndOpenDate() {
		return endOpenDate;
	}

	public void setEndOpenDate(String endOpenDate) {
		this.endOpenDate = endOpenDate;
	}

	public String getMbBroadband() {
		return mbBroadband;
	}

	public void setMbBroadband(String mbBroadband) {
		this.mbBroadband = mbBroadband;
	}
	
	public String getReason()
	{
		return reason;
	}
	
	public void setReason(String reason)
	{
		this.reason = reason;
	}

	public String getOldVersionDeviceTypeIds()
	{
		return oldVersionDeviceTypeIds;
	}
	
	public void setOldVersionDeviceTypeIds(String oldVersionDeviceTypeIds)
	{
		this.oldVersionDeviceTypeIds = oldVersionDeviceTypeIds;
	}
	
	public Integer getRelationType()
	{
		return relationType;
	}
	
	public void setRelationType(Integer relationType)
	{
		this.relationType = relationType;
	}
	
	public String getIs_awifi() {
		return is_awifi;
	}
	public void setIs_awifi(String is_awifi) {
		this.is_awifi = is_awifi;
	}

	public String getEditDeviceType() {
		return editDeviceType;
	}
	public void setEditDeviceType(String editDeviceType) {
		this.editDeviceType = editDeviceType;
	}
	
	public int getIs_multicast() {
		return is_multicast;
	}
	public void setIs_multicast(int is_multicast) {
		this.is_multicast = is_multicast;
	}
	public int getIs_speedtest() {
		return is_speedtest;
	}
	public void setIs_speedtest(int is_speedtest) {
		this.is_speedtest = is_speedtest;
	}
	
	public String getIs_QOE()
	{
		return is_QOE;
	}
	
	public void setIs_QOE(String is_QOE)
	{
		this.is_QOE = is_QOE;
	}
	
	public String getIs_esurfing()
	{
		return is_esurfing;
	}
	
	public void setIs_esurfing(String is_esurfing)
	{
		this.is_esurfing = is_esurfing;
	}
	
	public HttpServletRequest getRequest()
	{
		return request;
	}
	
	public void setRequest(HttpServletRequest request)
	{
		this.request = request;
	}
	
	public String getDevice_version_type()
	{
		return device_version_type;
	}
	
	public void setDevice_version_type(String device_version_type)
	{
		this.device_version_type = device_version_type;
	}
	
	public String getWifi()
	{
		return wifi;
	}
	
	public void setWifi(String wifi)
	{
		this.wifi = wifi;
	}
	
	public String getWifi_frequency()
	{
		return wifi_frequency;
	}
	
	public void setWifi_frequency(String wifi_frequency)
	{
		this.wifi_frequency = wifi_frequency;
	}
	
	public String getDownload_max_wifi()
	{
		return download_max_wifi;
	}
	
	public void setDownload_max_wifi(String download_max_wifi)
	{
		this.download_max_wifi = download_max_wifi;
	}
	
	public String getGigabit_port()
	{
		return gigabit_port;
	}
	
	public void setGigabit_port(String gigabit_port)
	{
		this.gigabit_port = gigabit_port;
	}
	
	public String getGigabit_port_type()
	{
		return gigabit_port_type;
	}
	
	public void setGigabit_port_type(String gigabit_port_type)
	{
		this.gigabit_port_type = gigabit_port_type;
	}
	
	public String getDownload_max_lan()
	{
		return download_max_lan;
	}
	
	public void setDownload_max_lan(String download_max_lan)
	{
		this.download_max_lan = download_max_lan;
	}
	
	public String getPower()
	{
		return power;
	}
	
	public void setPower(String power)
	{
		this.power = power;
	}
	
	public String getTerminal_access_time()
	{
		return terminal_access_time;
	}
	
	public void setTerminal_access_time(String terminal_access_time)
	{
		this.terminal_access_time = terminal_access_time;
	}
	public String getGbBroadband() {
		return gbBroadband;
	}
	public void setGbBroadband(String gbBroadband) {
		this.gbBroadband = gbBroadband;
	}
	public String getVersion_feature()
	{
		return version_feature;
	}
	
	public void setVersion_feature(String version_feature)
	{
		this.version_feature = version_feature;
	}
	public int getSecurity_plugin_type()
	{
		return security_plugin_type;
	}
	public void setSecurity_plugin_type(int security_plugin_type)
	{
		this.security_plugin_type = security_plugin_type;
	}
	public int getIs_security_plugin()
	{
		return is_security_plugin;
	}
	public void setIs_security_plugin(int is_security_plugin)
	{
		this.is_security_plugin = is_security_plugin;
	}
	public String getIs_newVersion() {
		return is_newVersion;
	}
	public void setIs_newVersion(String is_newVersion) {
		this.is_newVersion = is_newVersion;
	}
	public int getSsid_instancenum()
	{
		return ssid_instancenum;
	}
	
	public void setSsid_instancenum(int ssid_instancenum)
	{
		this.ssid_instancenum = ssid_instancenum;
	}
	
	public String getHvoip_port()
	{
		return hvoip_port;
	}
	
	public void setHvoip_port(String hvoip_port)
	{
		this.hvoip_port = hvoip_port;
	}
	

	public String getHvoip_type()
	{
		return hvoip_type;
	}
	
	public void setHvoip_type(String hvoip_type)
	{
		this.hvoip_type = hvoip_type;
	}
	
	public String getSvoip_type()
	{
		return svoip_type;
	}
	
	public void setSvoip_type(String svoip_type)
	{
		this.svoip_type = svoip_type;
	}
	
	public int getGigabitNum()
	{
		return gigabitNum;
	}
	
	public void setGigabitNum(int gigabitNum)
	{
		this.gigabitNum = gigabitNum;
	}
	
	public int getMbitNum()
	{
		return mbitNum;
	}
	
	public void setMbitNum(int mbitNum)
	{
		this.mbitNum = mbitNum;
	}
	
	public int getVoipNum()
	{
		return voipNum;
	}
	
	public void setVoipNum(int voipNum)
	{
		this.voipNum = voipNum;
	}
	
	public String getIs_wifi_double()
	{
		return is_wifi_double;
	}
	
	public void setIs_wifi_double(String is_wifi_double)
	{
		this.is_wifi_double = is_wifi_double;
	}
	
	public String getFusion_ability()
	{
		return fusion_ability;
	}
	
	public void setFusion_ability(String fusion_ability)
	{
		this.fusion_ability = fusion_ability;
	}
	
	public String getTerminal_access_method()
	{
		return terminal_access_method;
	}
	
	public void setTerminal_access_method(String terminal_access_method)
	{
		this.terminal_access_method = terminal_access_method;
	}
	
	public String getDevMaxSpeed()
	{
		return devMaxSpeed;
	}
	
	public void setDevMaxSpeed(String devMaxSpeed)
	{
		this.devMaxSpeed = devMaxSpeed;
	}
	
	public String getRes_type_id()
	{
		return res_type_id;
	}
	
	public void setRes_type_id(String res_type_id)
	{
		this.res_type_id = res_type_id;
	}
	
	public String getRes_vendor()
	{
		return res_vendor;
	}
	
	public void setRes_vendor(String res_vendor)
	{
		this.res_vendor = res_vendor;
	}
	
	public String getRes_type()
	{
		return res_type;
	}
	
	public void setRes_type(String res_type)
	{
		this.res_type = res_type;
	}
	
	public String getRemark()
	{
		return remark;
	}
	
	public void setRemark(String remark)
	{
		this.remark = remark;
	}


	public int getIsSupSpeedTest_Query() {
		return isSupSpeedTest_Query;
	}

	public void setIsSupSpeedTest_Query(int isSupSpeedTest_Query) {
		this.isSupSpeedTest_Query = isSupSpeedTest_Query;
	}


	public List getDataList() {
		return dataList;
	}

	public void setDataList(List dataList) {
		this.dataList = dataList;
	}

	public List getDetailDataList() {
		return detailDataList;
	}

	public void setDetailDataList(List detailDataList) {
		this.detailDataList = detailDataList;
	}

	public List<Map> getData() {
		return data;
	}

	public void setData(List<Map> data) {
		this.data = data;
	}

	public String[] getColumn() {
		return column;
	}

	public void setColumn(String[] column) {
		this.column = column;
	}

	public String[] getTitle() {
		return title;
	}

	public void setTitle(String[] title) {
		this.title = title;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getExcel() {
		return excel;
	}

	public void setExcel(String excel) {
		this.excel = excel;
	}

	public List<Map<String, String>> getDevVersionTypeMap() {
		return devVersionTypeMap;
	}

	public void setDevVersionTypeMap(List<Map<String, String>> devVersionTypeMap) {
		this.devVersionTypeMap = devVersionTypeMap;
	}

	public String getDeviceVersionType() {
		return deviceVersionType;
	}

	public void setDeviceVersionType(String deviceVersionType) {
		this.deviceVersionType = deviceVersionType;
	}

	public List<Map<String, String>> getDevVersionListTree() {
		return devVersionListTree;
	}

	public void setDevVersionListTree(List<Map<String, String>> devVersionListTree) {
		this.devVersionListTree = devVersionListTree;
	}

	public String getDevVersionTotal() {
		return devVersionTotal;
	}

	public void setDevVersionTotal(String devVersionTotal) {
		this.devVersionTotal = devVersionTotal;
	}

    public int getVendorId() {
        return vendorId;
    }

    public void setVendorId(int vendorId) {
        this.vendorId = vendorId;
    }

    public int getModelId() {
        return modelId;
    }

    public void setModelId(int modelId) {
        this.modelId = modelId;
    }

	public Map getDevTypeInfoMap() {
		return devTypeInfoMap;
	}

	public void setDevTypeInfoMap(JSONObject devTypeInfoMap) {
		this.devTypeInfoMap = devTypeInfoMap;
	}

	public String getIsCheck() {
		return isCheck;
	}

	public void setIsCheck(String isCheck) {
		this.isCheck = isCheck;
	}
	public int getIscloudnet() {
		return iscloudnet;
	}
	public void setIscloudnet(int iscloudnet) {
		this.iscloudnet = iscloudnet;
	}
	
}
