package com.linkage.module.gwms.blocTest.act;

import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.blocTest.bio.QueryDeviceStatusBIO;
import com.linkage.module.gwms.blocTest.bio.QueryDeviceStatusServiceBIO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;



public class QueryDeviceStatusACT extends splitPageAction implements SessionAware{
	
	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(QueryDeviceStatusACT.class);
	
	// session
	private Map session;
	
	// 导出数据
	private List<Map> data;
	// 导出文件列标题
	private String[] title;
	// 导出文件列
	private String[] column;
	//设备属地列表
	private List<Map<String, String>> CityList = null;
	
	private String vendor = "";         // 设备厂商
	private String device_model = "";   // 设备型号
	private String specversion = "";    // 设备版本
	private String device_serialnumber = "";  // 设备序列号
	private String city_id = "";        // 设备属地
	private String loopback_ip = "";    // 设备IP
	
	private String ajax = "";
	private String deviceIds = "";       // 批量设备ID
	
	/** BIO */
	private QueryDeviceStatusBIO queryDeviceStatusBio;

	
	
	/**
	 * gw_type = 1 表示家庭网关    2 表示企业网关
	 */
	private String gw_type;

	public String getGw_type() {
		return gw_type;
	}

	public void setGw_type(String gw_type) {
		this.gw_type = gw_type;
	}
	
	/**
	 * @return the session
	 */
	public Map getSession()
	{
		return session;
	}

	/**
	 * @param session
	 *            the session to set
	 */
	public void setSession(Map session)
	{
		this.session = session;
	}
	
	
	/**
	 * 查询页面
	 * @return
	 */
	public String queryDeviceStutusList() throws Exception{
		logger.debug("queryDeviceStutusList()");
		
		UserRes curUser = (UserRes) session.get("curUser");
		
		CityList = CityDAO.getAllNextCityListByCityPid(curUser.getCityId());  // 设备属地列表
		
		return "query";
	}
	
	
	/**
	 * 设备列表页面
	 *
	 */
	public String deviceStatusOperate() throws Exception{
		logger.debug("deviceStatusOperate()");

		title = new String[5];
		title[0] = "设备OUI";
		title[1] = "设备序列号";
		title[2] = "设备IP";
		title[3] = "设备属地";
		title[4] = "在线状态";

		data = queryDeviceStatusBio.getDeviceList(getGw_type(), vendor, device_model, specversion, device_serialnumber, city_id, loopback_ip);
		return "deviceStatusOperate";
	}

	
	/**
	 * 批量获取设备状态
	 * 
	 * @param curUser
	 * @param deviceIds
	 * @return ajax
	 */
	public String getDeviceStatusByBat() throws Exception {
		logger.debug("getDeviceListByBat({})", new Object[]{deviceIds});
		
		String deviceIdArr[] = deviceIds.split(",");
		
		for (int i = 0; i < deviceIdArr.length; i++) {
			QueryDeviceStatusServiceBIO myConn = new QueryDeviceStatusServiceBIO();
			int intFlag = myConn.getConnectionFlag(deviceIdArr[i]);
			
			if ("".equals(ajax)) {
				ajax = deviceIdArr[i]+","+intFlag;
			} else {
				ajax += "|" + deviceIdArr[i]+","+intFlag;
			}
		}
		return "ajax";
	}


	/**
	 * @return the data
	 */
	public List<Map> getData()
	{
		return data;
	}

	/**
	 * @param data
	 *            the data to set
	 */
	public void setData(List<Map> data)
	{
		this.data = data;
	}

	/**
	 * @return the title
	 */
	public String[] getTitle()
	{
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String[] title)
	{
		this.title = title;
	}

	/**
	 * @return the column
	 */
	public String[] getColumn()
	{
		return column;
	}

	/**
	 * @param column
	 *            the column to set
	 */
	public void setColumn(String[] column)
	{
		this.column = column;
	}

	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	public String getDevice_model() {
		return device_model;
	}

	public void setDevice_model(String device_model) {
		this.device_model = device_model;
	}

	public String getSpecversion() {
		return specversion;
	}

	public void setSpecversion(String specversion) {
		this.specversion = specversion;
	}

	public String getDevice_serialnumber() {
		return device_serialnumber;
	}

	public void setDevice_serialnumber(String device_serialnumber) {
		this.device_serialnumber = device_serialnumber;
	}

	public String getCity_id() {
		return city_id;
	}

	public void setCity_id(String city_id) {
		this.city_id = city_id;
	}

	public String getLoopback_ip() {
		return loopback_ip;
	}

	public void setLoopback_ip(String loopback_ip) {
		this.loopback_ip = loopback_ip;
	}


	public List<Map<String, String>> getCityList() {
		return CityList;
	}

	public void setCityList(List<Map<String, String>> cityList) {
		CityList = cityList;
	}

	public QueryDeviceStatusBIO getQueryDeviceStatusBio() {
		return queryDeviceStatusBio;
	}

	public void setQueryDeviceStatusBio(QueryDeviceStatusBIO queryDeviceStatusBio) {
		this.queryDeviceStatusBio = queryDeviceStatusBio;
	}

	public String getDeviceIds() {
		return deviceIds;
	}

	public void setDeviceIds(String deviceIds) {
		this.deviceIds = deviceIds;
	}

	public String getAjax() {
		return ajax;
	}

	public void setAjax(String ajax) {
		this.ajax = ajax;
	}


}
