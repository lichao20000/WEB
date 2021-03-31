package com.linkage.module.itms.resource.act;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.itms.resource.bio.VersionBIO;

public class VersionACT extends splitPageAction implements SessionAware,
		ServletRequestAware {

	private static final long serialVersionUID = -7787526862261263533L;
	private static Logger logger = LoggerFactory
			.getLogger(VersionACT.class);
	private VersionBIO bio;
	@SuppressWarnings("unused")
	private HttpServletRequest request;
	@SuppressWarnings({ "rawtypes", "unused" })
	private Map session;

	private String vendor = "";

	private String devicemodel = "";

	private String versionSpecification = "";

	private String specName = "";

	private String deviceType = "";

	private String accessWay = "";

	private String voiceAgreement = "";

	private String zeroconf = "";

	private String mbbndwidth = "";

	private String temval = "";

	private String ipvsix = "";

	private String softwareversion = "";

	@SuppressWarnings("rawtypes")
	private List<Map> data; // 需要导出的结果集，必须是处理过的可以直接显示的结果
	private String[] column; // 需要导出的列名，对应data中的键值
	private String[] title; // 显示在导出文档上的列名
	private String fileName; // 导出的文件名（不包括后缀名）
	@SuppressWarnings("rawtypes")
	private List<Map> deployList = null;
	@SuppressWarnings("rawtypes")
	private List<Map> deployDevList = null;

	private String starttime = "";
	private String endtime = "";
	private String cityId = "";

	private List<Map<String, String>> cityList;

	// 厂商名称集合
	private List<Map<String, Object>> vendorList;

	// 操作类型 1.编辑 2.新增
	private String operType = "";
	// 厂商id
	private String vendor_id = "";
	// 厂商名称
	private String vendor_name = "";
	// 发送光功率
	private String tx_power = "";
	// 接收光功率
	private String rx_power = "";

	// 光功率采集列表
	@SuppressWarnings("rawtypes")
	private List<Map> rlist = null;
	// 用户账号
	private String username = "";
	// 设备序列号
	private String device_serialnumber = "";
	// 软件版本
	private String soft_version = "";
	// 指标阀值
	private String powerVal = "";
	// 阀值范围
	private String powerScope = "";

	private String ajax = "";

	// 采集类型
	private String colType;

	private List<Map<String, Object>> list = null;

	/**
	 * 初始化页面数据
	 */
	@Override
	public String execute() throws Exception {
		return "init";
	}

	/*
	 * 页面初始化
	 */
	public String init() {
		list = bio.getDroopList();
		vendorList = bio.getVendors();
		return "init";
	}

	/*
	 * 操作光功率设定值 1.编辑2.保存
	 */
	public String operDroop() {
		ajax = bio.operDroop(operType, vendor_id, vendor_name, tx_power,
				rx_power);
		return "operPoewer";
	}

	/**
	 * 删除光功率设定值
	 * 
	 * @param vendor_id
	 * @return
	 */
	public String delDroop() {
		ajax = bio.delDroop(vendor_id);
		return "operPoewer";
	}

	/*
	 * 光功率配置页面初始化
	 */
	public String colinit() {
		UserRes curUser = (UserRes) session.get("curUser");
		cityId = curUser.getCityId();
		cityList = CityDAO.getNextCityListByCityPid(cityId);
		return "colinit";
	}

	/*
	 * 光功率定制
	 */
	public String orderPower() {
		ajax = bio.orderPower(cityId, colType);
		return "ajax";
	}

	public String queryVersionDev() {
		if ("2".equals(temval)) {
			voiceAgreement = "";
			zeroconf = "";
			mbbndwidth = "";
			ipvsix = "";
		}
		deployDevList = bio.queryVersionList(vendor, devicemodel,
				versionSpecification, specName, deviceType, accessWay,
				voiceAgreement, zeroconf, mbbndwidth, ipvsix, temval,
				curPage_splitPage, num_splitPage);
		maxPage_splitPage = bio.countQueryVersionList(vendor, devicemodel,
				versionSpecification, specName, deviceType, accessWay,
				voiceAgreement, zeroconf, mbbndwidth, ipvsix, temval,
				curPage_splitPage, num_splitPage);
		return "devlist";
	}

	/*
	 * 查询软件版本
	 */
	public String querySoftVersion() {
		ajax = bio.getSoftVersion(vendor_id);
		return "ajax";
	}

	/*
	 * 光功率采集查询初始化
	 */
	public String queryinit() {
		DateTimeUtil dt = new DateTimeUtil();
		endtime = dt.getDate();
		starttime = dt.getFirtDayOfMonth();
		dt = new DateTimeUtil(endtime);
		long end_time = dt.getLongTime();
		dt = new DateTimeUtil((end_time + 24 * 3600 - 1) * 1000);
		endtime = dt.getLongDate();
		dt = new DateTimeUtil(starttime);
		starttime = dt.getLongDate();
		UserRes curUser = (UserRes) session.get("curUser");
		cityId = curUser.getCityId();
		List<Map<String,String>> city_list = CityDAO.getNextCityListByCityPid(curUser.getCityId());

	    cityList = new ArrayList<Map<String,String>>();
	    for (Map<String,String> map : city_list) {
	      Map<String,String> m = new HashMap<String,String>();
	      String city_id = (String)map.get("city_id");
	      String city_name = (String)map.get("city_name");
	      if ("00".equals(city_id)) {
	        city_name = "全区";
	      }
	      m.put("city_id", city_id);
	      m.put("city_name", city_name);

	      cityList.add(m);
	      m = null;
	      map = null;
	    }
		vendorList = bio.getVendorList();
		return "queryinit";
	}

	/*
	 * 光功率采集结果查询
	 */
	public String powerByQuery() {
		rlist = bio.getpowerList(curPage_splitPage, num_splitPage, starttime,
				endtime, username, device_serialnumber, cityId, vendor_id,
				soft_version, powerVal, powerScope);
		maxPage_splitPage = bio.getpowerCount(num_splitPage, starttime,
				endtime, username, device_serialnumber, cityId, vendor_id,
				soft_version, powerVal, powerScope);

		return "list";
	}

	/*
	 * 导出光功率采集查询结果
	 */
	public String getPowerExcel() throws Exception {
		fileName = "光功率采集结果";
		title = new String[] { "序号","属地", "逻辑ID", "厂家", "终端型号","设备序列号", "发送光功率", "接收光功率",
				"采集时间"};
		column = new String[] { "index","cityName", "username", "vendorName","device_model",
				"deviceSn", "txPower", "rxPower", "colDate"};
		data = bio.getPowerExcel(starttime, endtime, username,
				device_serialnumber, cityId, vendor_id, soft_version, powerVal,
				powerScope);
		return "excel";
	}

	public String queryVersionDevExcel() {
		if ("2".equals(temval)) {
			voiceAgreement = "";
			zeroconf = "";
			mbbndwidth = "";
			ipvsix = "";
		}
		deployDevList = bio.excelQueryVersionList(vendor, devicemodel,
				versionSpecification, specName, deviceType, accessWay,
				voiceAgreement, zeroconf, mbbndwidth, ipvsix, temval);
		String excelCol = "vendor_id#device_model_id#hardwareversion#softwareversion#complete_time#versionttime";
		String excelTitle = "厂商#型号#硬件版本#软件版本#注册时间#定版时间";
		column = excelCol.split("#");
		title = excelTitle.split("#");
		fileName = "设备统计查询信息";
		data = deployDevList;
		return "excel";
	}

	public String queryDetail() {
		if ("2".equals(temval)) {
			voiceAgreement = "";
			zeroconf = "";
			mbbndwidth = "";
			ipvsix = "";
		}
		deployDevList = bio.queryDetail(vendor, devicemodel,
				versionSpecification, specName, deviceType, accessWay,
				voiceAgreement, zeroconf, mbbndwidth, ipvsix, temval,
				softwareversion, curPage_splitPage, num_splitPage);
		maxPage_splitPage = bio.countQueryDetail(vendor, devicemodel,
				versionSpecification, specName, deviceType, accessWay,
				voiceAgreement, zeroconf, mbbndwidth, ipvsix, temval,
				softwareversion, curPage_splitPage, num_splitPage);
		return "detaillist";
	}

	public String queryDetailExcel() {
		if ("2".equals(temval)) {
			voiceAgreement = "";
			zeroconf = "";
			mbbndwidth = "";
			ipvsix = "";
		}
		deployDevList = bio.excelqueryDetail(vendor, devicemodel,
				versionSpecification, specName, deviceType, accessWay,
				voiceAgreement, zeroconf, mbbndwidth, ipvsix, temval,
				softwareversion);
		String excelCol = "device_serialnumber#username#complete_time#city_name";
		String excelTitle = "设备序列号#绑定LOID#注册时间#属地";
		column = excelCol.split("#");
		title = excelTitle.split("#");
		fileName = "设备统计查询详细信息";
		data = deployDevList;
		return "excel";
	}

	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public VersionBIO getBio() {
		return bio;
	}

	public void setBio(VersionBIO bio) {
		this.bio = bio;
	}

	@SuppressWarnings("rawtypes")
	public List<Map> getData() {
		return data;
	}

	@SuppressWarnings("rawtypes")
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

	@SuppressWarnings("rawtypes")
	public List<Map> getDeployList() {
		return deployList;
	}

	@SuppressWarnings("rawtypes")
	public void setDeployList(List<Map> deployList) {
		this.deployList = deployList;
	}

	@SuppressWarnings("rawtypes")
	public List<Map> getDeployDevList() {
		return deployDevList;
	}

	@SuppressWarnings("rawtypes")
	public void setDeployDevList(List<Map> deployDevList) {
		this.deployDevList = deployDevList;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public String getSpecName() {
		return specName;
	}

	public void setSpecName(String specName) {
		this.specName = specName;
	}

	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	public String getDevicemodel() {
		return devicemodel;
	}

	public void setDevicemodel(String devicemodel) {
		this.devicemodel = devicemodel;
	}

	public String getTemval() {
		return temval;
	}

	public void setTemval(String temval) {
		this.temval = temval;
	}

	public String getVersionSpecification() {
		return versionSpecification;
	}

	public void setVersionSpecification(String versionSpecification) {
		this.versionSpecification = versionSpecification;
	}

	public String getAccessWay() {
		return accessWay;
	}

	public void setAccessWay(String accessWay) {
		this.accessWay = accessWay;
	}

	public String getVoiceAgreement() {
		return voiceAgreement;
	}

	public void setVoiceAgreement(String voiceAgreement) {
		this.voiceAgreement = voiceAgreement;
	}

	public String getZeroconf() {
		return zeroconf;
	}

	public void setZeroconf(String zeroconf) {
		this.zeroconf = zeroconf;
	}

	public String getMbbndwidth() {
		return mbbndwidth;
	}

	public void setMbbndwidth(String mbbndwidth) {
		this.mbbndwidth = mbbndwidth;
	}

	public String getIpvsix() {
		return ipvsix;
	}

	public void setIpvsix(String ipvsix) {
		this.ipvsix = ipvsix;
	}

	public String getSoftwareversion() {
		return softwareversion;
	}

	public void setSoftwareversion(String softwareversion) {
		this.softwareversion = softwareversion;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public String getStarttime() {
		return starttime;
	}

	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}

	public String getEndtime() {
		return endtime;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public List<Map<String, String>> getCityList() {
		return cityList;
	}

	public void setCityList(List<Map<String, String>> cityList) {
		this.cityList = cityList;
	}

	public List<Map<String, Object>> getVendorList() {
		return vendorList;
	}

	public void setVendorList(List<Map<String, Object>> vendorList) {
		this.vendorList = vendorList;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getOperType() {
		return operType;
	}

	public void setOperType(String operType) {
		this.operType = operType;
	}

	public String getVendor_id() {
		return vendor_id;
	}

	public void setVendor_id(String vendor_id) {
		this.vendor_id = vendor_id;
	}

	public String getVendor_name() {
		return vendor_name;
	}

	public void setVendor_name(String vendor_name) {
		this.vendor_name = vendor_name;
	}

	public String getTx_power() {
		return tx_power;
	}

	public void setTx_power(String tx_power) {
		this.tx_power = tx_power;
	}

	public String getRx_power() {
		return rx_power;
	}

	public void setRx_power(String rx_power) {
		this.rx_power = rx_power;
	}

	public List<Map> getRlist() {
		return rlist;
	}

	public void setRlist(List<Map> rlist) {
		this.rlist = rlist;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getDevice_serialnumber() {
		return device_serialnumber;
	}

	public void setDevice_serialnumber(String device_serialnumber) {
		this.device_serialnumber = device_serialnumber;
	}

	public String getSoft_version() {
		return soft_version;
	}

	public void setSoft_version(String soft_version) {
		this.soft_version = soft_version;
	}

	public String getPowerVal() {
		return powerVal;
	}

	public void setPowerVal(String powerVal) {
		this.powerVal = powerVal;
	}

	public String getPowerScope() {
		return powerScope;
	}

	public void setPowerScope(String powerScope) {
		this.powerScope = powerScope;
	}

	public String getAjax() {
		return ajax;
	}

	public void setAjax(String ajax) {
		this.ajax = ajax;
	}

	public String getColType() {
		return colType;
	}

	public void setColType(String colType) {
		this.colType = colType;
	}

	public List<Map<String, Object>> getList() {
		return list;
	}

	public void setList(List<Map<String, Object>> list) {
		this.list = list;
	}

}
