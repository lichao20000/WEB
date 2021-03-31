package com.linkage.module.gwms.config.act;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.config.bio.CountMulticastBatchBIO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

/**
 * 批量下发组播参数统计
 * @author wangyan10(Ailk NO.76091)
 * @version 1.0
 * @since 2018-12-26
 */
public class CountMulticastBatchACT extends splitPageAction implements SessionAware
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(CountMulticastBatchACT.class);
	// bio
	private CountMulticastBatchBIO bio;
	private Map session;
	private List<Map> MulticastBatchList;
	private List<Map> multicastDownwardsList;
	private List<Map> multicastDownwardsDetail;
	
	/**
	 * 当前操作的用户
	 */
	private UserRes current_user;
	private String ajax = null;
	private String device_serialnumber = null;
	private String cpe_allocatedstatus = null;
	
	private String vendorId = null;
	private String deviceModelId = null;
	private String deviceTypeId = null;
	/**区分ITMS和BBMS的功能*/
	private String gw_type ="";
	private int total;
	private List<Map> data; // 需要导出的结果集，必须是处理过的可以直接显示的结果
	private String[] column; // 需要导出的列名，对应data中的键值
	private String[] title; // 显示在导出文档上的列名
	private String fileName; // 导出的文件名（不包括后缀名）
	private String type;  //组播下移参数详细也页面的类型:开启，未开启,....
	
	
	/** 属地列表 */
	private List<Map<String, String>> cityList = null;
	

	/** 属地ID */
	private String cityId = "-1";


	public void setSession(Map session)
	{
		this.session = session;
	}

	public Map getSession()
	{
		return this.session;
	}

	/**
	 * 查询页面
	 * @param
	 * @return
	 */
	public String queryAll()
	{
		logger.debug("queryAll()");
		UserRes curUser = (UserRes) session.get("curUser");
		cityList = CityDAO.getNextCityListByCityPid(curUser.getCityId());
		return "query";
	}
	
	 /**
	  * 组播下移参数页面
	  * @return
	  */
     public String mcDownwards(){
    	 logger.debug("queryMCDownwards");
    	 UserRes curUser = (UserRes) session.get("curUser");
 		 cityList = CityDAO.getNextCityListByCityPid(curUser.getCityId());
 		return "queryMCDownwards";
     }
     
     /**
      * 组播下移参数页面查询
      * @return
      */
     public String queryMCDownwardsList(){
    	 logger.debug("queryMCDownwardsList");
    	 UserRes curUser = (UserRes) session.get("curUser");
    	 multicastDownwardsList = bio.queryMulticastDownwardsList(curUser,cityId, vendorId, deviceModelId);
    	 return "mcDownwardsList";
     }
	
     /**
      * 组播下移详细页面
      * @return
      */
     public String mcDownwardsDetail(){
    	 UserRes curUser = (UserRes) session.get("curUser");
    	 multicastDownwardsDetail = bio.multicastDownwardsDetail(curUser,type,cityId,vendorId, deviceModelId,curPage_splitPage,num_splitPage);
    	 maxPage_splitPage = bio.getMaxPage_splitPage();
    	 return "mcDownwardsdetail";
     }
     
     /**
      * 组播参数下移页面导出
      * @return
      */
     public String mcDownwardsExcel(){
    	logger.debug("mcDownwardsExcel()");
 		current_user = (UserRes) session.get("curUser");
 		multicastDownwardsList = bio.queryMulticastDownwardsListExcel(current_user,cityId, vendorId, deviceModelId);
 		String excelCol = null;
 		String excelTitle = null;
 		excelCol = "cityName#total#openCounts#closeCounts#failCounts";
 		excelTitle = "属地#采集数量#开启数量#未开启数量#采集失败";
 		column = excelCol.split("#");
 		title = excelTitle.split("#");
 		fileName = "组播下移参数统计";
 		data = multicastDownwardsList;
    	 return "excel";
     }
     
     /**
      * 组播下移详细页面导出
      * @return
      */
     public String mcDownwardsDetailExcel(){
    	logger.debug("mcDownwardsDetailExcel()");
  		current_user = (UserRes) session.get("curUser");
   	    multicastDownwardsDetail = bio.multicastDownwardsDetailExcel(current_user,type,cityId,vendorId,deviceModelId);
  		String excelCol = null;
  		String excelTitle = null;
  		excelCol = "vendor_name#device_model#software#city_name#device_serialnumber#gathertime#gatherRes";
  		excelTitle = "厂商#型号#软件版本#属地#设备序列号#采集时间#采集结果";
  		column = excelCol.split("#");
  		title = excelTitle.split("#");
  		fileName = "组播下移参数统计详细";
  		data = multicastDownwardsDetail;
     	 return "excel";
     }
	/**
	 * 查询所有数据  列表页面
	 * 
	 * @return
	 */
	public String queryList()
	{
		logger.debug("queryList()");
		current_user = (UserRes) session.get("curUser");
		MulticastBatchList = bio.queryMulticastBatchList(current_user, device_serialnumber, cityId, vendorId, 
				deviceModelId, deviceTypeId,curPage_splitPage, num_splitPage,cpe_allocatedstatus);
		maxPage_splitPage = bio.getMaxPage_splitPage();
		return "queryList";
	}
	
	/**
	 * 在导出之前，要先统计一下当前查询条件下的数量
	 * @param
	 * @return
	 */
	public String getMulticastBatchCount() {
		logger.debug("getMulticastBatchCount()");
		current_user = (UserRes) session.get("curUser");
		total = bio.countMulticastBatchList(current_user, device_serialnumber, cityId, vendorId, deviceModelId, deviceTypeId,cpe_allocatedstatus);
		ajax = total + "";
		return "ajax";
	}

	/**
	 * 导出功能
	 * @param
	 * @return
	 */
	public String getMulticastBatchExcel() {
		logger.debug("getMulticastBatchExcel()");
		current_user = (UserRes) session.get("curUser");
		MulticastBatchList = bio.getMulticastBatchExcel(current_user, device_serialnumber, cityId, vendorId, 
				deviceModelId, deviceTypeId,cpe_allocatedstatus);
		String excelCol = null;
		String excelTitle = null;
		excelCol = "loid#city_name#setTime#vendor_add#device_model#softwareversion#device_serialnumber#status";
		excelTitle = "LOID#属地#执行时间#设备厂商#设备型号#软件版本#设备序列号#开通状态";
		column = excelCol.split("#");
		title = excelTitle.split("#");
		fileName = "批量下发组播参数信息";
		data = MulticastBatchList;
		return "excel";
	}
	

	public CountMulticastBatchBIO getBio()
	{
		return bio;
	}

	public void setBio(CountMulticastBatchBIO bio)
	{
		this.bio = bio;
	}

	public UserRes getCurrent_user()
	{
		return current_user;
	}

	public void setCurrent_user(UserRes current_user)
	{
		this.current_user = current_user;
	}

	public String getAjax()
	{
		return ajax;
	}

	public void setAjax(String ajax)
	{
		this.ajax = ajax;
	}

	/**
	 * @return the cityList
	 */
	public List<Map<String, String>> getCityList()
	{
		return cityList;
	}

	/**
	 * @param cityList
	 *            the cityList to set
	 */
	public void setCityList(List<Map<String, String>> cityList)
	{
		this.cityList = cityList;
	}

	/**
	 * @return the cityId
	 */
	public String getCityId()
	{
		return cityId;
	}

	/**
	 * @param cityId
	 *            the cityId to set
	 */
	public void setCityId(String cityId)
	{
		this.cityId = cityId;
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

	public String getDeviceTypeId() {
		return deviceTypeId;
	}

	public void setDeviceTypeId(String deviceTypeId) {
		this.deviceTypeId = deviceTypeId;
	}

	public String getGw_type()
	{
		return gw_type;
	}

	public void setGw_type(String gw_type)
	{
		this.gw_type = gw_type;
	}

	public List<Map> getMulticastBatchList() {
		return MulticastBatchList;
	}

	public void setMulticastBatchList(List<Map> multicastBatchList) {
		MulticastBatchList = multicastBatchList;
	}

	public String getDevice_serialnumber() {
		return device_serialnumber;
	}

	public void setDevice_serialnumber(String device_serialnumber) {
		this.device_serialnumber = device_serialnumber;
	}
	
	public List<Map> getMulticastDownwardsList()
	{
		return multicastDownwardsList;
	}
	
	public void setMulticastDownwardsList(List<Map> multicastDownwardsList)
	{
		this.multicastDownwardsList = multicastDownwardsList;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
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

	public String getCpe_allocatedstatus() {
		return cpe_allocatedstatus;
	}

	public void setCpe_allocatedstatus(String cpe_allocatedstatus) {
		this.cpe_allocatedstatus = cpe_allocatedstatus;
	}

	public List<Map> getMulticastDownwardsDetail()
	{
		return multicastDownwardsDetail;
	}

	public void setMulticastDownwardsDetail(List<Map> multicastDownwardsDetail)
	{
		this.multicastDownwardsDetail = multicastDownwardsDetail;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}
     
}
