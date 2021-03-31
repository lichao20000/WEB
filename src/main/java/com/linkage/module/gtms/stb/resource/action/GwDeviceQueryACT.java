/**
 *
 */
package com.linkage.module.gtms.stb.resource.action;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletResponseAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.WebUtil;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gtms.stb.fault.serv.CertusDataBio;
import com.linkage.module.gtms.stb.resource.serv.DeviceXinnengBIO;
import com.linkage.module.gtms.stb.resource.serv.GwDeviceQueryBIO;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.corba.ACSCorba;

/**
 * @author zxj E-mail：qixq@lianchuang.com
 * @version $Revision$
 * @since 2009-12-25
 * @category com.linkage.module.stb.resource.act
 *
 */
public class GwDeviceQueryACT extends splitPageAction implements ServletResponseAware
{
	private static final long serialVersionUID = 1L;

	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(GwDeviceQueryACT.class);

	GwDeviceQueryBIO gwDeviceBio = null;
	DeviceXinnengBIO xinnengBio = null ;
	CertusDataBio certusDataBio = null;
	private HttpServletResponse response;
	private Map deviceDetailMap = null;

	private String deviceId = null;

	private String ajax = null;

	private List deviceList = null;

	private String cityId = "";

	private String vendorId = null;

	private String deviceModelId = null;

	private String devicetypeId = null;

	private String queryType = null;

	private String queryResultType = null;

	private String queryParam = null;

	private String queryField = null;

	private String onlineStatus = null;

	private String bindType = null;

	private String deviceSerialnumber = null;

	private String startQuery = "false";

	private String username = null;

	private String deviceIp = null;

	private String cpeMac = null;

	private String servAccount = null;

	//设备上报开始时间
	private String completeStartTime = null;
	//设备上报截止时间
	private String completeEndTime = null;
	//最近上线时间
	private String startLastTime = null;

	private String endLastTime = null;

	private String startLastTime1 = null;

	private String endLastTime1 = null;

	private List<Map<String,String>> cityList = null;

	private List<Map<String,String>> statusList = null;

	/**
	 * 接入类型
	 */
	private String addressingType = null;

	/**
	 * 最近一次上报IP
	 */
	private String loopbackIp = null;

	/**
	 * 最近一次上报IP(V6)
	 */
	private String loopbackIpSix = null;

	/**
	 * 零配置状态
	 */
	private String status = null;

	private Map<String,String> deviceNetMap = null;

	private Map<String,String> deviceEpgMap = null;

	//局点名称
	private String instAreaName;

	//针对待查询的属地，是否查询该属地下所有设备
	//“true” 不查询；“false”查询
	private String noChildCity = null;
	//判断是否是操作还是查询列表
	private String type;

	//判断是否是操作还是查询列表【机顶盒信息查询】
	private String infoQueryType ;

	private List<Map> zerogwInfoList;

	private List<Map> failReasonList;

	private String failReason;

	//终端状态
	private String device_status=null;

	private String instArea=Global.instAreaShortName;

	private List<Map> data; // 需要导出的结果集，必须是处理过的可以直接显示的结果
	private String[] column; // 需要导出的列名，对应data中的键值
	private String[] title; // 显示在导出文档上的列名
	private String fileName; // 导出的文件名（不包括后缀名）

	private int total;

	/**
	 * execute
	 */
	public String execute() throws Exception {
		logger.debug("execute()");
		initData();
		return "success";
	}

	/**
	 * 零配置用查询
	 */
	public String zeroQuery() throws Exception {
		logger.debug("zeroQuery()");
		initData();
		if(!Global.NXDX.equals(Global.instAreaShortName))
		{
			return "zeroSuccess";
		}
		else
		{
			return "nxSuccess";
		}
	}

	/**
	 * 查询零配置设备（带列表）
	 *
	 * @return
	 */
	public String zeroqueryDeviceList() {
		logger.debug("GwDeviceQueryACT=>zeroqueryDeviceList()");
		this.startQuery = "true";
		UserRes curUser = WebUtil.getCurrentUser();
		this.setTime();
		if(!StringUtil.IsEmpty(cityId)){
			cityId = cityId.trim();
		}else{
			cityId = curUser.getCityId();
		}
		boolean tempNoChildCity = false;
		if("true".equals(noChildCity)){
			tempNoChildCity = true;
		}
		this.deviceList = gwDeviceBio.getZeroDeviceList(curPage_splitPage,num_splitPage,
				cityId, deviceSerialnumber,loopbackIp,username,servAccount,
				tempNoChildCity,startLastTime1,endLastTime1,addressingType,status,cpeMac, failReason,device_status,loopbackIpSix);
		int total = gwDeviceBio.getZeroDeviceListCount(cityId, deviceSerialnumber,loopbackIp,username,
				servAccount,tempNoChildCity,startLastTime1,endLastTime1,addressingType,status,cpeMac, failReason,device_status,loopbackIpSix);
		this.total = total;
		if (total % num_splitPage == 0) {
			maxPage_splitPage = total / num_splitPage;
		} else {
			maxPage_splitPage = total / num_splitPage + 1;
		}

		if(Global.NXDX.equals(Global.instAreaShortName))
		{
			totalRowCount_splitPage = total;
		}

		logger.info("queryResultType---------------->>"+queryResultType);
		if("none".equals(queryResultType)){
			//type为0没有删除功能 1 有
			if("0".equals(type)){
				return "noDeleteZerolist";
			}
			else{
				return "zerolist";
			}
		}else{
			return "zeroshareList";
		}
	}

	/**
	 * 分页查询
	 * @return
	 * @throws Exception
	 */
	public String zerogoPage() throws Exception {
		this.startQuery = "true";
		UserRes curUser = WebUtil.getCurrentUser();
		this.setTime();
		if(!StringUtil.IsEmpty(cityId)){
			cityId = cityId.trim();
		}else{
			cityId = curUser.getCityId();
		}
		boolean tempNoChildCity = false;
		if("true".equals(noChildCity)){
			tempNoChildCity = true;
		}
		this.deviceList = gwDeviceBio.getZeroDeviceList(curPage_splitPage,num_splitPage,
				cityId,deviceSerialnumber,loopbackIp,username,servAccount,
				tempNoChildCity, startLastTime1,endLastTime1,addressingType,status,cpeMac, failReason,device_status,loopbackIpSix);
		int total = gwDeviceBio.getZeroDeviceListCount(cityId, deviceSerialnumber,loopbackIp,username,
				servAccount,tempNoChildCity,startLastTime1,endLastTime1,addressingType,status,cpeMac, failReason,device_status,loopbackIpSix);
		if (total % num_splitPage == 0) {
			maxPage_splitPage = total / num_splitPage;
		} else {
			maxPage_splitPage = total / num_splitPage + 1;
		}
		if("none".equals(queryResultType)){
			return "zerolist";
		}else{
			return "zeroshareList";
		}
	}

	/**
	 * 设备详细
	 * @return
	 */
	public String queryZeroDeviceDetail(){
		UserRes curUser = WebUtil.getCurrentUser();
		boolean tempNoChildCity = false;
		if("true".equals(noChildCity)){
			tempNoChildCity = true;
		}
		this.deviceDetailMap = gwDeviceBio.queryDeviceDetail(deviceId, curUser.getCityId(),tempNoChildCity);
		//局点名称
		instAreaName = Global.instAreaShortName;
		if(Global.HNLT.equals(instAreaName)){
			return "zerodetail";
		}
		if(Global.SDLT.equals(instAreaName)){
			zerogwInfoList = gwDeviceBio.querygwWorkDetail(deviceId);
		}else if(Global.JXDX.equals(instAreaName)){
			zerogwInfoList = gwDeviceBio.querygwZeroDetailTo3(deviceId);
		}else{
			zerogwInfoList = gwDeviceBio.querygwZeroDetail(deviceId);
		}
		return "zerodetail";
	}

	public String querygwZeroDetailPage(){
		this.zerogwInfoList = gwDeviceBio.querygwZeroDetailPage(curPage_splitPage,num_splitPage,deviceId);;
		int total = gwDeviceBio.querygwZeroDetailCount(deviceId);
		if (total % num_splitPage == 0) {
			maxPage_splitPage = total / num_splitPage;
		} else {
			maxPage_splitPage = total / num_splitPage + 1;
		}
		return "zeroDetailPage";
	}

	/**
	 * 初始化数据
	 */
	private void initData()
	{
		UserRes curUser = WebUtil.getCurrentUser();
		this.cityList = CityDAO.getNextCityListByCityPid(curUser.getCityId());
		initStatusList();
		DateTimeUtil dt = new DateTimeUtil();
		startLastTime = dt.getDate();
		dt = new DateTimeUtil(startLastTime);
		long start_time = dt.getLongTime();
		dt = new DateTimeUtil(start_time * 1000);
		startLastTime = dt.getLongDate();
		dt = new DateTimeUtil((start_time + 24 * 3600 - 1) * 1000);
		endLastTime = dt.getLongDate();
		failReasonList = gwDeviceBio.queryFailReason();
	}

	private void initStatusList()
	{
		this.statusList = gwDeviceBio.getStatusList();
	}

	/**
	 * 时间转化
	 */
	private void setTime()
	{
		logger.debug("setTime()" + startLastTime);
		DateTimeUtil dt = null;// 定义DateTimeUtil
		if (StringUtil.IsEmpty(startLastTime))
		{
			startLastTime1 = null;
		}
		else
		{
			dt = new DateTimeUtil(startLastTime);
			startLastTime1 = String.valueOf(dt.getLongTime());
		}
		if (StringUtil.IsEmpty(endLastTime))
		{
			endLastTime1 = null;
		}
		else
		{
			dt = new DateTimeUtil(endLastTime);
			endLastTime1 = String.valueOf(dt.getLongTime());
		}
	}

	/**
	 * 设备详细
	 * @return
	 */
	public String queryDeviceDetail(){
		logger.debug("queryDeviceDetail()");
		UserRes curUser = WebUtil.getCurrentUser();
		boolean tempNoChildCity = false;
		if("true".equals(noChildCity)){
			tempNoChildCity = true;
		}
		this.deviceDetailMap = gwDeviceBio.queryDeviceDetail(deviceId, curUser.getCityId(),tempNoChildCity);
		return "detail";
	}

	/**
	 * 查询属地
	 *
	 * @return
	 */
	public String getCityNextChild() {
		logger.debug("GwDeviceQueryACT=>getCity()");
		if (StringUtil.IsEmpty(cityId)) {
			UserRes curUser = WebUtil.getCurrentUser();
			this.cityId = curUser.getCityId();
		}
		this.ajax = gwDeviceBio.getCity(cityId);
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

	/**
	 * 查询设备型号
	 *
	 * @param vendorId
	 * @return
	 */
	public String getDeviceModel() {
		logger.debug("GwDeviceQueryACT=>getDeviceModel()");
		this.ajax = gwDeviceBio.getDeviceModel(vendorId);
		return "ajax";
	}

	/**
	 * 查询设备版本
	 *
	 * @return
	 */
	public String getDevicetype() {
		logger.debug("GwDeviceQueryACT=>getDevicetype()");
		this.ajax = gwDeviceBio.getDevicetype(deviceModelId);
		return "ajax";
	}

	/**
	 * 删除设备
	 * @return
	 */
	public String delete(){
		logger.debug("GwDeviceQueryACT=>delete()");
		if (StringUtil.IsEmpty(cityId)) {
			UserRes curUser = WebUtil.getCurrentUser();
			this.cityId = curUser.getCityId();
		}
		boolean tempNoChildCity = false;
		if("true".equals(noChildCity)){
			tempNoChildCity = true;
		}
		this.ajax = gwDeviceBio.delete(deviceId, cityId, tempNoChildCity, Global.GW_TYPE_STB);
		return "ajax";
	}

	public String editInit(){
		UserRes curUser = WebUtil.getCurrentUser();
		this.cityList = CityDAO.getAllNextCityListByCityPid(curUser.getCityId());
		boolean tempNoChildCity = false;
		if("true".equals(noChildCity)){
			tempNoChildCity = true;
		}
		this.deviceDetailMap = gwDeviceBio.queryDeviceDetail(deviceId, curUser.getCityId(),tempNoChildCity);
		return "edit";
	}

	/**
	 * 目前机顶盒查询列表页面没有编辑设备的功能
	 * @return
	 */
//	public String edit(){
//		logger.debug("GwDeviceQueryACT=>editInit()");
//		if(null==this.cityId){
//			HttpSession session = request.getSession();
//			UserRes curUser = (UserRes) session.getAttribute("curUser");
//			this.cityId = curUser.getCityId();
//		}
//		this.ajax = gwDeviceBio.editDevice(deviceId, cityId, cpeMac, deviceIp);
//		return "ajax";
//	}

	public String xinneng(){
		UserRes curUser = WebUtil.getCurrentUser();
		boolean tempNoChildCity = false;
		if("true".equals(noChildCity)){
			tempNoChildCity = true;
		}
		this.deviceDetailMap = gwDeviceBio.queryDeviceDetail(deviceId, curUser.getCityId(),tempNoChildCity);
		this.deviceNetMap = xinnengBio.queryDeviceXinneng(deviceId);
		this.deviceEpgMap = certusDataBio.requestSTBInfo(String.valueOf(deviceDetailMap.get("serv_account")),
										String.valueOf(deviceDetailMap.get("device_serialnumber")),
										String.valueOf(deviceDetailMap.get("cpe_mac")),
										String.valueOf(deviceDetailMap.get("city_id")));
		return "xinneng";
	}

	/**
	 * 查询设备（带列表）
	 *
	 * @return
	 */
	public String queryDeviceList() {
		logger.debug("GwDeviceQueryACT=>getDeviceList()");
		this.startQuery = "true";
		UserRes curUser = WebUtil.getCurrentUser();
		this.setTime();
		if(!StringUtil.IsEmpty(cityId)){
			cityId = cityId.trim();
		}else{
			cityId = curUser.getCityId();
		}
		if(StringUtil.IsEmpty(completeStartTime)){
			this.completeStartTime = "0";
		}
		if(StringUtil.IsEmpty(completeEndTime)){
			this.completeEndTime = "0";
		}
		boolean tempNoChildCity = false;
		if("true".equals(noChildCity)){
			tempNoChildCity = true;
		}
		long areaId = curUser.getAreaId();
		this.deviceList = gwDeviceBio.getDeviceList(curPage_splitPage,num_splitPage,
				areaId, queryType,queryParam, queryField,cityId, startLastTime1,endLastTime1,
				vendorId,deviceModelId,	devicetypeId, bindType, deviceSerialnumber,
				deviceIp,username,servAccount,tempNoChildCity,completeStartTime,completeEndTime);
		int total = gwDeviceBio.getDeviceListCount(areaId, queryType,
				queryParam, queryField,cityId,  startLastTime1,endLastTime1,vendorId,deviceModelId,
				devicetypeId, bindType, deviceSerialnumber,deviceIp,username,
				servAccount,tempNoChildCity,completeStartTime,completeEndTime);
		if (total % num_splitPage == 0) {
			maxPage_splitPage = total / num_splitPage;
		} else {
			maxPage_splitPage = total / num_splitPage + 1;
		}
		if("none".equals(queryResultType)){
			return "list";
		}else{
			return "shareList";
		}
	}

	/**
	 * 设备导出
	 *
	 * @return
	 */
	public String DeviceQueryExcel() {
		UserRes curUser = WebUtil.getCurrentUser();
		this.setTime();
		if(!StringUtil.IsEmpty(cityId)){
			cityId = cityId.trim();
		}else{
			cityId = curUser.getCityId();
		}
		boolean tempNoChildCity = false;
		if("true".equals(noChildCity)){
			tempNoChildCity = true;
		}
		this.deviceList = gwDeviceBio.getZeroDeviceList(-1,num_splitPage,
				cityId,deviceSerialnumber,loopbackIp,username,servAccount,
				tempNoChildCity, startLastTime1,endLastTime1,addressingType,status,cpeMac, failReason,device_status,loopbackIpSix);

		if(Global.NXDX.equals(Global.instAreaShortName))
		{
			column = new String[]{"vendor_add", "device_model", "softwareversion", "city_name", "device_serialnumber",
					"loopback_ip", "addressing_type", "cpe_mac", "serv_account","pppoe_user", "complete_time"};
			title = new String[] { "设备厂商","设备型号", "软件版本", "属地", "设备序列号",
					"设备IP", "接入类型", "MAC", "业务账号", "接入账号","首次上报时间" };
		}
		else if(Global.JXDX.equals(Global.instAreaShortName))
		{
			column = new String[]{"vendor_add", "device_model", "softwareversion", "city_name", "device_serialnumber",
					"loopback_ip", "cpe_mac", "serv_account", "complete_time", "status","loopback_ipSix" };
			title = new String[] { "设备厂商","设备型号", "软件版本", "属地", "设备序列号",
					"设备IP(IPV4)", "MAC", "业务账号", "首次上报时间", "设备状态","设备IP(IPV6)" };
		}
		else
		{
			if(!Global.HNLT.equals(instAreaName)){
				column = new String[]{"vendor_add", "device_model", "softwareversion", "city_name", "device_serialnumber",
						"loopback_ip", "addressing_type", "cpe_mac", "serv_account", "complete_time", "status" };
				title = new String[] { "设备厂商","设备型号", "软件版本", "属地", "设备序列号",
						"设备IP", "接入类型", "MAC", "业务账号", "首次上报时间", "设备状态" };
			}else{
				column = new String[]{"vendor_add", "device_model", "softwareversion", "city_name", "device_serialnumber",
						"loopback_ip", "cpe_mac", "serv_account", "complete_time", "status" };
				title = new String[] { "设备厂商","设备型号", "软件版本", "属地", "设备序列号",
						"设备IP", "MAC", "业务账号", "首次上报时间", "设备状态" };
			}
		}

		fileName = "机顶盒查询统计";
		data = deviceList;
		return "excel";
	}

	/**
	 * 设备导出
	 *
	 * @return
	 */
	public String toExcel() {
		logger.debug("GwDeviceQueryACT=>toExcel()");
		this.startQuery = "true";
		UserRes curUser = WebUtil.getCurrentUser();
		this.setTime();
		if(!"".equals(cityId) && null!=cityId){
			cityId = cityId.trim();
		}else{
			cityId = curUser.getCityId();
		}
		if(StringUtil.IsEmpty(completeStartTime)){
			this.completeStartTime = "0";
		}
		if(StringUtil.IsEmpty(this.completeEndTime)){
			this.completeEndTime = "0";
		}
		boolean tempNoChildCity = false;
		if("true".equals(noChildCity)){
			tempNoChildCity = true;
		}

		long areaId = curUser.getAreaId();
		gwDeviceBio.deviceExport(response,"ITV_DEV_"+StringUtil.getStringValue(curUser.getCityId()),
				areaId, queryType,queryParam, queryField,cityId, startLastTime1,endLastTime1,
				vendorId,deviceModelId,	devicetypeId, bindType, deviceSerialnumber,
				deviceIp,username,servAccount,tempNoChildCity,completeStartTime,completeEndTime);
		return null;
	}

	/**
	 * 零配置设备导出
	 *
	 * @return
	 */
	public String zerotoExcel() {
		this.startQuery = "true";
		UserRes curUser = WebUtil.getCurrentUser();
		this.setTime();
		if(!StringUtil.IsEmpty(cityId)){
			cityId = cityId.trim();
		}else{
			cityId = curUser.getCityId();
		}
		boolean tempNoChildCity = false;
		if("true".equals(noChildCity)){
			tempNoChildCity = true;
		}

		gwDeviceBio.zeroDeviceExport(response,"ITV_DEV_"+StringUtil.getStringValue(curUser.getCityId()),
				cityId,deviceSerialnumber,
				loopbackIp,username,servAccount,tempNoChildCity,startLastTime1,endLastTime1,addressingType,
				status,cpeMac, failReason,device_status,loopbackIpSix);
		return null;
	}

	/**
	 * 分页查询
	 * @return
	 * @throws Exception
	 */
	public String goPage() throws Exception {
		logger.debug("GwDeviceQueryACT=>getDeviceList()");
		this.startQuery = "true";
		UserRes curUser = WebUtil.getCurrentUser();
		this.setTime();
		if(!StringUtil.IsEmpty(cityId)){
			cityId = cityId.trim();
		}else{
			cityId = curUser.getCityId();
		}
		if(StringUtil.IsEmpty(this.completeStartTime)){
			this.completeStartTime = "0";
		}
		if(StringUtil.IsEmpty(this.completeEndTime)){
			this.completeEndTime = "0";
		}
		boolean tempNoChildCity = false;
		if("true".equals(noChildCity)){
			tempNoChildCity = true;
		}
		long areaId = curUser.getAreaId();
		this.deviceList = gwDeviceBio.getDeviceList(curPage_splitPage,num_splitPage,
				areaId, queryType,queryParam, queryField,cityId, startLastTime1,endLastTime1,
				vendorId,deviceModelId,	devicetypeId, bindType, deviceSerialnumber,
				deviceIp,username,servAccount,tempNoChildCity,completeStartTime,completeEndTime);
		int total = gwDeviceBio.getDeviceListCount(areaId, queryType,
				queryParam, queryField,cityId, startLastTime1,endLastTime1,vendorId,deviceModelId,
				devicetypeId, bindType, deviceSerialnumber,deviceIp,username,
				servAccount,tempNoChildCity,completeStartTime,completeEndTime);
		if (total % num_splitPage == 0) {
			maxPage_splitPage = total / num_splitPage;
		} else {
			maxPage_splitPage = total / num_splitPage + 1;
		}
		if("none".equals(queryResultType)){
			return "list";
		}else{
			return "shareList";
		}
	}

	/**
	 * 测试在线状态
	 * @return
	 */
	public String testOnlineStatus()
	{
		ajax = String.valueOf(new ACSCorba(Global.GW_TYPE_STB).testConnection(deviceId, Global.GW_TYPE_STB));
		return "ajax";
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
	 * @return the cityId
	 */
	public String getCityId() {
		return cityId;
	}

	/**
	 * @param cityId
	 *            the cityId to set
	 */
	public void setCityId(String cityId) {
		if(null != cityId)
		{
			this.cityId = cityId.trim();
		}
	}

	/**
	 * @return the deviceModelId
	 */
	public String getDeviceModelId() {
		return deviceModelId;
	}

	/**
	 * @param deviceModelId
	 *            the deviceModelId to set
	 */
	public void setDeviceModelId(String deviceModelId) {
		if(null != deviceModelId)
		{
			this.deviceModelId = deviceModelId.trim();
		}
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

	/**
	 * @return the vendorId
	 */
	public String getVendorId() {
		return vendorId;
	}

	/**
	 * @param vendorId
	 *            the vendorId to set
	 */
	public void setVendorId(String vendorId) {
		if(null != vendorId)
		{
			this.vendorId = vendorId.trim();
		}
	}

	public String getQueryResultType() {
		return queryResultType;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	/**
	 * @param queryResultType
	 *            the queryResultType to set
	 */
	public void setQueryResultType(String queryResultType) {
		if(null != queryResultType)
		{
			this.queryResultType = queryResultType.trim();
		}
	}

	/**
	 * @return the queryType
	 */
	public String getQueryType() {
		return queryType;
	}

	/**
	 * @param queryType
	 *            the queryType to set
	 */
	public void setQueryType(String queryType) {
		if(null != queryType)
		{
			this.queryType = queryType.trim();
		}
	}

	/**
	 * @return the bindType
	 */
	public String getBindType() {
		return bindType;
	}

	/**
	 * @param bindType the bindType to set
	 */
	public void setBindType(String bindType) {
		if(null != bindType)
		{
			this.bindType = bindType.trim();
		}
	}

	/**
	 * @return the deviceSerialnumber
	 */
	public String getDeviceSerialnumber() {
		return deviceSerialnumber;
	}

	/**
	 * @param deviceSerialnumber the deviceSerialnumber to set
	 */
	public void setDeviceSerialnumber(String deviceSerialnumber) {
		if(null != deviceSerialnumber)
		{
			this.deviceSerialnumber = deviceSerialnumber.trim();
		}
	}

	/**
	 * @return the devicetypeId
	 */
	public String getDevicetypeId() {
		return devicetypeId;
	}

	/**
	 * @param devicetypeId the devicetypeId to set
	 */
	public void setDevicetypeId(String devicetypeId) {
		if(null != devicetypeId)
		{
			this.devicetypeId = devicetypeId;
		}
	}

	/**
	 * @return the onlineStatus
	 */
	public String getOnlineStatus() {
		return onlineStatus;
	}

	/**
	 * @param onlineStatus the onlineStatus to set
	 */
	public void setOnlineStatus(String onlineStatus) {
		if(null != onlineStatus)
		{
			this.onlineStatus = onlineStatus.trim();
		}
	}

	/**
	 * @return the queryField
	 */
	public String getQueryField() {
		return queryField;
	}

	/**
	 * @param queryField the queryField to set
	 */
	public void setQueryField(String queryField) {
		if(null != queryField)
		{
			this.queryField = queryField.trim();
		}
	}

	/**
	 * @return the queryParam
	 */
	public String getQueryParam() {
		return queryParam;
	}

	/**
	 * @param queryParam the queryParam to set
	 */
	public void setQueryParam(String queryParam) {
		if(null != queryParam)
		{
			this.queryParam = queryParam.trim();
		}
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
	 * @return the startQuery
	 */
	public String getStartQuery() {
		return startQuery;
	}

	/**
	 * @param startQuery the startQuery to set
	 */
	public void setStartQuery(String startQuery) {
		if(null != startQuery)
		{
			this.startQuery = startQuery.trim();
		}
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
		if(null != deviceId)
		{
			this.deviceId = deviceId.trim();
		}
	}

	/**
	 * @return the deviceIp
	 */
	public String getDeviceIp() {
		return deviceIp;
	}

	/**
	 * @param deviceIp the deviceIp to set
	 */
	public void setDeviceIp(String deviceIp) {
		if(null != deviceIp)
		{
			this.deviceIp = deviceIp.trim();
		}
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		if(null != username)
		{
			this.username = username.trim();
		}
	}

	/**
	 * @return the cityList
	 */
	public List<Map<String, String>> getCityList() {
		return cityList;
	}

	/**
	 * @param cityList the cityList to set
	 */
	public void setCityList(List<Map<String, String>> cityList) {
		this.cityList = cityList;
	}

	/**
	 * @return the cpeMac
	 */
	public String getCpeMac() {
		return cpeMac;
	}

	/**
	 * @param cpeMac the cpeMac to set
	 */
	public void setCpeMac(String cpeMac) {
		if(null != cpeMac)
		{
			this.cpeMac = cpeMac.trim();
		}
	}

	/**
	 * @return the certusDataBio
	 */
	public CertusDataBio getCertusDataBio() {
		return certusDataBio;
	}

	/**
	 * @param certusDataBio the certusDataBio to set
	 */
	public void setCertusDataBio(CertusDataBio certusDataBio) {
		this.certusDataBio = certusDataBio;
	}

	/**
	 * @return the deviceEpgMap
	 */
	public Map<String, String> getDeviceEpgMap() {
		return deviceEpgMap;
	}

	/**
	 * @param deviceEpgMap the deviceEpgMap to set
	 */
	public void setDeviceEpgMap(Map<String, String> deviceEpgMap) {
		this.deviceEpgMap = deviceEpgMap;
	}

	/**
	 * @return the deviceNetMap
	 */
	public Map<String, String> getDeviceNetMap() {
		return deviceNetMap;
	}

	/**
	 * @param deviceNetMap the deviceNetMap to set
	 */
	public void setDeviceNetMap(Map<String, String> deviceNetMap) {
		this.deviceNetMap = deviceNetMap;
	}

	/**
	 * @return the xinnengBio
	 */
	public DeviceXinnengBIO getXinnengBio() {
		return xinnengBio;
	}

	/**
	 * @param xinnengBio the xinnengBio to set
	 */
	public void setXinnengBio(DeviceXinnengBIO xinnengBio) {
		this.xinnengBio = xinnengBio;
	}

	/**
	 * @return the servAccount
	 */
	public String getServAccount() {
		return servAccount;
	}

	/**
	 * @param servAccount the servAccount to set
	 */
	public void setServAccount(String servAccount) {
		if(null != servAccount)
		{
			this.servAccount = servAccount.trim();
		}
	}

	/**
	 * @return the noChildCity
	 */
	public String getNoChildCity() {
		return noChildCity;
	}

	/**
	 * @param noChildCity the noChildCity to set
	 */
	public void setNoChildCity(String noChildCity) {
		if(null != noChildCity)
		{
			this.noChildCity = noChildCity.trim();
		}
	}
	public String getStartLastTime() {
		return startLastTime;
	}

	public void setStartLastTime(String startLastTime) {
		if(null != startLastTime)
		{
			this.startLastTime = startLastTime.trim();
		}
	}

	public String getEndLastTime() {
		return endLastTime;
	}

	public void setEndLastTime(String endLastTime) {
		if(null != endLastTime)
		{
			this.endLastTime = endLastTime.trim();
		}
	}

	public String getStartLastTime1() {
		return startLastTime1;
	}

	public void setStartLastTime1(String startLastTime1) {
		if(null != startLastTime1)
		{
			this.startLastTime1 = startLastTime1.trim();
		}
	}

	public String getEndLastTime1() {
		return endLastTime1;
	}

	public void setEndLastTime1(String endLastTime1) {
		if(null != endLastTime1)
		{
			this.endLastTime1 = endLastTime1.trim();
		}
	}
	/**
	 * @return the completeEndTime
	 */
	public String getCompleteEndTime() {
		return completeEndTime;
	}

	/**
	 * @param completeEndTime the completeEndTime to set
	 */
	public void setCompleteEndTime(String completeEndTime) {
		if(null != completeEndTime)
		{
			this.completeEndTime = completeEndTime.trim();
		}
	}

	/**
	 * @return the completeStartTime
	 */
	public String getCompleteStartTime() {
		return completeStartTime;
	}

	/**
	 * @param completeStartTime the completeStartTime to set
	 */
	public void setCompleteStartTime(String completeStartTime) {
		if(null != completeStartTime)
		{
			this.completeStartTime = completeStartTime.trim();
		}
	}
	public HttpServletResponse getResponse() {
		return response;
	}
	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}
	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}

	/**
	 * @return the statusList
	 */
	public List<Map<String, String>> getStatusList() {
		return statusList;
	}

	/**
	 * @param statusList the statusList to set
	 */
	public void setStatusList(List<Map<String, String>> statusList) {
		this.statusList = statusList;
	}

	/**
	 * @return the addressingType
	 */
	public String getAddressingType() {
		return addressingType;
	}

	/**
	 * @param addressingType the addressingType to set
	 */
	public void setAddressingType(String addressingType) {
		if(null != addressingType)
		{
			this.addressingType = addressingType.trim();
		}
	}

	/**
	 * @return the loopbackIp
	 */
	public String getLoopbackIp() {
		return loopbackIp;
	}

	/**
	 * @param loopbackIp the loopbackIp to set
	 */
	public void setLoopbackIp(String loopbackIp) {
		if(null != loopbackIp)
		{
			this.loopbackIp = loopbackIp.trim();
		}
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		if(null != status)
		{
			this.status = status.trim();
		}
	}

	public String getInfoQueryType() {
		return infoQueryType;
	}

	public void setInfoQueryType(String infoQueryType) {
		this.infoQueryType = infoQueryType;
	}


	public List<Map> getZerogwInfoList()
	{
		return zerogwInfoList;
	}


	public void setZerogwInfoList(List<Map> zerogwInfoList)
	{
		this.zerogwInfoList = zerogwInfoList;
	}


	public List<Map> getFailReasonList()
	{
		return failReasonList;
	}


	public void setFailReasonList(List<Map> failReasonList)
	{
		this.failReasonList = failReasonList;
	}


	public String getFailReason()
	{
		return failReason;
	}


	public void setFailReason(String failReason)
	{
		this.failReason = failReason;
	}

	public String getInstAreaName()
	{
		return instAreaName;
	}

	public void setInstAreaName(String instAreaName)
	{
		this.instAreaName = instAreaName;
	}


	public String getDevice_status()
	{
		return device_status;
	}


	public void setDevice_status(String device_status)
	{
		this.device_status = device_status;
	}

	public String getInstArea() {
		return instArea;
	}

	public void setInstArea(String instArea) {
		this.instArea = instArea;
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

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public String getLoopbackIpSix()
	{
		return loopbackIpSix;
	}

	public void setLoopbackIpSix(String loopbackIpSix)
	{
		this.loopbackIpSix = loopbackIpSix;
	}

}
