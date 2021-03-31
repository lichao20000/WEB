package com.linkage.module.itms.report.act;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.module.itms.report.bio.BandwidthDeviceReportBIO;

@SuppressWarnings("rawtypes")
public class BandwidthDeviceReportACT extends splitPageAction {

	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory.getLogger(BandwidthDeviceReportACT.class);
	private List<Map> data; // 需要导出的结果集，必须是处理过的可以直接显示的结果
	private String[] column; // 需要导出的列名，对应data中的键值
	private String[] title; // 显示在导出文档上的列名
	private String fileName; // 导出的文件名（不包括后缀名）
	private List<Map> deviceList = null;
	private BandwidthDeviceReportBIO bio = null;
	// add0918修改查询条件新增
	// 属地
	private String gwShare_cityId = null;

	private String bandwidth = null;

	private String isSpeedUp = null;
	
	private static HashMap<String, String> modelMap = null;
	private static HashMap<String, String> vendorMap = null;
	static {
		initModel(); 
		initVendor();
	}
	public static String getModel(String modelId) {
		return StringUtil.getStringValue(modelMap, modelId, "");
	}
	
	public static String getVendor(String vendorId) {
		return StringUtil.getStringValue(vendorMap, vendorId, "");
	}
	
	
	public String getDeviceInfo() {
		logger.warn("开始统计带宽对应终端");
		deviceList = bio.getDeviceInfo(curPage_splitPage, num_splitPage,
				gwShare_cityId, bandwidth, isSpeedUp);
		maxPage_splitPage = bio.getDeviceInfoCount(num_splitPage,
				gwShare_cityId, bandwidth, isSpeedUp);
		logger.warn("统计完毕，计带宽对应终端数：" + deviceList.size());
		return "list";
	}

	public String getDeviceInfoExcel() {
		logger.debug("getDeviceInfoExcel()");
		fileName = "宽对应终端统计";
		title = new String[] { "属地", "区域", "LOID", "设备标识码", "宽带账号", "厂家", "型号",
				"当前带宽", "是否支持提速" };
		column = new String[] { "parentCityName", "cityName", "loid",
				"device_name", "username", "vendorName", "deviceModel",
				"down_bandwidth", "isSpeedUp" };
		data = bio.getDeviceInfoExcel(gwShare_cityId, bandwidth, isSpeedUp);
		return "excel";
	}

	public String goPage() throws Exception {
		deviceList = bio.getDeviceInfo(curPage_splitPage, num_splitPage,
				gwShare_cityId, bandwidth, isSpeedUp);
		maxPage_splitPage = bio.getDeviceInfoCount(num_splitPage,
				gwShare_cityId, bandwidth, isSpeedUp);
		return "list";
	}

	/**
	 * 初始化设备型号与型号名称之间的对应关系
	 */
	private static void initModel() {
       	String mysql = "select device_model_id, device_model from gw_device_model";
       	PrepareSQL psql = new PrepareSQL(mysql);
       	Cursor cursor = DataSetBean.getCursor(psql.getSQL());
       	modelMap = new HashMap<String,String>(cursor.getRecordSize());
       	Map map = cursor.getNext();
       	while (map != null) {
       		modelMap.put((String) map.get("device_model_id"), (String) map.get("device_model"));
       	    map = cursor.getNext();
       	}
       	cursor = null;
	}
	
	private static void initVendor() {
       	String mysql = "select vendor_id, vendor_name from tab_vendor";
       	PrepareSQL psql = new PrepareSQL(mysql);
       	Cursor cursor = DataSetBean.getCursor(psql.getSQL());
       	vendorMap = new HashMap<String,String>(cursor.getRecordSize());
       	Map map = cursor.getNext();
       	while (map != null) {
       		vendorMap.put((String) map.get("vendor_id"), (String) map.get("vendor_name"));
       	    map = cursor.getNext();
       	}
       	cursor = null;
	}
	
	public BandwidthDeviceReportBIO getBio() {
		return bio;
	}

	public void setBio(BandwidthDeviceReportBIO bio) {
		this.bio = bio;
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

	public List<Map> getData() {
		return data;
	}

	public void setData(List<Map> data) {
		this.data = data;
	}

	public String getGwShare_cityId() {
		return gwShare_cityId;
	}

	public void setGwShare_cityId(String gwShare_cityId) {
		this.gwShare_cityId = gwShare_cityId;
	}

	public List<Map> getDeviceList() {
		return deviceList;
	}

	public void setDeviceList(List<Map> deviceList) {
		this.deviceList = deviceList;
	}

	public String getBandwidth() {
		return bandwidth;
	}

	public void setBandwidth(String bandwidth) {
		this.bandwidth = bandwidth;
	}

	public String getIsSpeedUp() {
		return isSpeedUp;
	}

	public void setIsSpeedUp(String isSpeedUp) {
		this.isSpeedUp = isSpeedUp;
	}
}
