/**
 * 
 */

package com.linkage.module.itms.config.act;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.itms.config.bio.DigitMapConfigBIO;

/**
 * @author chenjie(67371)
 * @date 2011.4.7
 */
public class DigitMapConfigACT extends splitPageAction implements SessionAware
{

	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(DigitMapConfigACT.class);
	// bio
	private DigitMapConfigBIO bio;
	private Map session;
	/**
	 * 所有树图配置列表
	 */
	private List<Map> digitMapList;
	/**
	 * 当前操作的用户
	 */
	private UserRes current_user;
	private String ajax = null;
	private Map digitMap = new HashMap();
	private String map_id = null;
	private String map_name = null;
	private String map_content = null;
	private String device_id = null;
	
	private String vendorId = null;
	private String deviceModelId = null;
	private String deviceTypeId = null;
	/**区分ITMS和BBMS的功能*/
	private String gw_type ="";
	
	
	/** 属地列表 */
	private List<Map<String, String>> cityList = null;
	private List<Map<String, String>> vendorList = null;
	private List<Map<String, String>> deviceModelList = null;
	private List<Map<String, String>> deviceTypeList = null;
	
	// 用于查询设备 add by zhangchy 2011-09-21 
	private String gwShare_cityId = null; 
	private String gwShare_onlineStatus = null;
	private String gwShare_vendorId = null;
	private String gwShare_deviceModelId = null;
	private String gwShare_devicetypeId = null;
	private String gwShare_bindType = null;
	private String gwShare_deviceSerialnumber = null;
	
	// 任务名称
	private String taskName = null; 
	private String is_default = null;
	
	public String getIs_default() {
		return is_default;
	}

	public void setIs_default(String is_default) {
		this.is_default = is_default;
	}


	/** 属地ID */
	private String cityId = "-1";

	public String getDevice_id()
	{
		return device_id;
	}

	public void setDevice_id(String device_id)
	{
		this.device_id = device_id;
	}

	public void setSession(Map session)
	{
		this.session = session;
	}

	public Map getSession()
	{
		return this.session;
	}

	/**
	 * 查询页面 add by zhangchy 新增查询页面
	 * 
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
	 * 查询所有数据  列表页面
	 * 
	 * @return
	 */
	public String queryList()
	{
		logger.debug("queryList()");
		current_user = (UserRes) session.get("curUser");
		digitMapList = bio.queryAllDigitMap(current_user, map_name, cityId, vendorId, deviceModelId, deviceTypeId);
		return "queryList";
	}

	public String queryForConfig()
	{
		logger.debug("queryForConfig()");
		current_user = (UserRes) session.get("curUser");
		digitMapList = bio.queryForConfigMap(device_id,cityId);
		return "queryForConfig";
	}
	
	
	
	/**
	 * 数图配置下发 加载设备查询页面
	 * 
	 * @return
	 */
	public String queryForConfigAll(){
		logger.debug("queryForConfigAll()");
		current_user = (UserRes) session.get("curUser");
		digitMapList = bio.queryForConfigAllMap(cityId, vendorId, deviceModelId, deviceTypeId);
		return "queryForConfigAll";
	}
	
	
	/**
	 * 数图配置下发  加载数图模板
	 * 
	 * @return
	 */
	public String queryDigitMap(){
		logger.debug("queryDigitMap()");
		current_user = (UserRes) session.get("curUser");
		digitMapList = bio.queryForConfigAllMap(cityId, vendorId, deviceModelId, deviceTypeId);
		return "queryForConfig";
	}
	

	/**
	 * 添加数据
	 * 
	 * @return
	 */
	public String add(){
		logger.debug("add()");
		current_user = (UserRes) session.get("curUser");
		int result = bio.queryIsDefault( vendorId,deviceModelId,is_default);
		logger.warn("add result="+result);
		if (result > 0)
		{
			ajax = "-2";
			logger.warn("add ajax="+ajax);
			return "ajax";
		}
		try	{
			bio.addDigitMap(map_name, map_content, current_user,cityId, vendorId, deviceModelId, deviceTypeId,is_default);
			ajax = "1";
		}catch (Exception e){
			ajax = "-1";
		}
		logger.warn("add ajax="+ajax);

		return "ajax";
	}

	/**
	 * 修改展示数据
	 * 
	 * @return
	 */
	public String updateQuery()
	{
		logger.debug("updateQuery()");
		try{
			ajax = "1";
			digitMap = bio.getDigitMapById(map_id);
			UserRes curUser = (UserRes) session.get("curUser");
			cityList = CityDAO.getNextCityListByCityPid(curUser.getCityId());
		}catch (Exception e){
			ajax = "-1";
		}
		return "updateQuery";
	}

	/**
	 * 修改展示数据
	 * 
	 * @return
	 */
	public String addInit()
	{
		logger.debug("addInit()");
		UserRes curUser = (UserRes) session.get("curUser");
		cityList = CityDAO.getNextCityListByCityPid(curUser.getCityId());
		return "updateQuery";
	}

	/**
	 * 修改数据
	 * 
	 * @return
	 */
	public String update()
	{
		logger.debug("update()");
		int result = bio.queryIsDefault( vendorId,deviceModelId,is_default);
		if (result > 0)
		{
			ajax = "-2";
			return "ajax";
		}
		try{
			bio.update(map_id, map_name, map_content, cityId, vendorId, deviceModelId, deviceTypeId,is_default);
			ajax = "1";
		}catch (Exception e){
			ajax = "-1";
		}
		return "ajax";
	}

	/**
	 * 删除数据
	 * 
	 * @return
	 */
	public String delete()
	{
		logger.debug("delete()");
		try
		{
			bio.deleteDigitMapById(map_id);
			ajax = "1";
		}
		catch (Exception e)
		{
			ajax = "-1";
		}
		return "ajax";
	}

	/**
	 * 下发模板数据
	 * 
	 * @return
	 */
	public String doConfig()
	{
		logger.debug("doConfig");
		current_user = (UserRes) session.get("curUser");
		ajax = bio.doConfig(current_user.getUser().getId(), device_id, map_id,gw_type);
		return "ajax";
	}
	
	
	/**
	 * 批量设备 下发数图配置
	 * 
	 * @return
	 */
	public String doConfigAll(){
		logger.debug("doConfigAll()");
		logger.warn("Digite Map Config DeviceIds:"+device_id);
		current_user = (UserRes) session.get("curUser");
		ajax = bio.doConfigAll(current_user.getUser().getId(), device_id, map_id, taskName);
		return "ajax";
	}
	
	
	/**
	 * 查询设备（带列表）
	 * 
	 * @return
	 */
	public String queryDeviceList() {
		logger.debug("queryDeviceList()");
		UserRes curUser = (UserRes) session.get("curUser");
		long areaId = curUser.getAreaId();

		if(null!=gwShare_cityId){
			gwShare_cityId.trim();
		}else{
			this.gwShare_cityId = curUser.getCityId();
		}
			if(null!=gwShare_deviceSerialnumber && !"".equals(gwShare_deviceSerialnumber)){
				gwShare_deviceSerialnumber.trim();
			}
			ajax = bio.getDeviceList(areaId,gwShare_cityId, gwShare_onlineStatus,
					gwShare_vendorId,gwShare_deviceModelId,gwShare_devicetypeId, 
					gwShare_bindType, gwShare_deviceSerialnumber);
			
			return "ajax";
	}

	public DigitMapConfigBIO getBio()
	{
		return bio;
	}

	public void setBio(DigitMapConfigBIO bio)
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

	public List<Map> getDigitMapList()
	{
		return digitMapList;
	}

	public void setDigitMapList(List<Map> digitMapList)
	{
		this.digitMapList = digitMapList;
	}

	public String getAjax()
	{
		return ajax;
	}

	public void setAjax(String ajax)
	{
		this.ajax = ajax;
	}

	public Map getDigitMap()
	{
		return digitMap;
	}

	public void setDigitMap(Map digitMap)
	{
		this.digitMap = digitMap;
	}

	public String getMap_content()
	{
		return map_content;
	}

	public void setMap_content(String map_content)
	{
		this.map_content = map_content;
	}

	public String getMap_name()
	{
		return map_name;
	}

	public void setMap_name(String map_name)
	{
		try
		{
			this.map_name = java.net.URLDecoder.decode(map_name, "UTF-8");
		}
		catch (Exception e)
		{
			this.map_name = map_name;
		}
	}

	public String getMap_id()
	{
		return map_id;
	}

	public void setMap_id(String map_id)
	{
		this.map_id = map_id;
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

	public List<Map<String, String>> getVendorList() {
		return vendorList;
	}

	public void setVendorList(List<Map<String, String>> vendorList) {
		this.vendorList = vendorList;
	}

	public List<Map<String, String>> getDeviceModelList() {
		return deviceModelList;
	}

	public void setDeviceModelList(List<Map<String, String>> deviceModelList) {
		this.deviceModelList = deviceModelList;
	}

	public List<Map<String, String>> getDeviceTypeList() {
		return deviceTypeList;
	}

	public void setDeviceTypeList(List<Map<String, String>> deviceTypeList) {
		this.deviceTypeList = deviceTypeList;
	}

	public String getGwShare_cityId() {
		return gwShare_cityId;
	}

	public void setGwShare_cityId(String gwShare_cityId) {
		this.gwShare_cityId = gwShare_cityId;
	}

	public String getGwShare_onlineStatus() {
		return gwShare_onlineStatus;
	}

	public void setGwShare_onlineStatus(String gwShare_onlineStatus) {
		this.gwShare_onlineStatus = gwShare_onlineStatus;
	}

	public String getGwShare_vendorId() {
		return gwShare_vendorId;
	}

	public void setGwShare_vendorId(String gwShare_vendorId) {
		this.gwShare_vendorId = gwShare_vendorId;
	}

	public String getGwShare_deviceModelId() {
		return gwShare_deviceModelId;
	}

	public void setGwShare_deviceModelId(String gwShare_deviceModelId) {
		this.gwShare_deviceModelId = gwShare_deviceModelId;
	}

	public String getGwShare_devicetypeId() {
		return gwShare_devicetypeId;
	}

	public void setGwShare_devicetypeId(String gwShare_devicetypeId) {
		this.gwShare_devicetypeId = gwShare_devicetypeId;
	}

	public String getGwShare_bindType() {
		return gwShare_bindType;
	}

	public void setGwShare_bindType(String gwShare_bindType) {
		this.gwShare_bindType = gwShare_bindType;
	}

	public String getGwShare_deviceSerialnumber() {
		return gwShare_deviceSerialnumber;
	}

	public void setGwShare_deviceSerialnumber(String gwShare_deviceSerialnumber) {
		this.gwShare_deviceSerialnumber = gwShare_deviceSerialnumber;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		try {
			this.taskName = java.net.URLDecoder.decode(taskName, "UTF-8");
		} catch (Exception e) {
			this.taskName = taskName;
		}
	}
	
	public String getGw_type()
	{
		return gw_type;
	}

	
	public void setGw_type(String gw_type)
	{
		this.gw_type = gw_type;
	}
     
}
