/**
 * 
 */
package com.linkage.module.gwms.service.act;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.service.bio.ServStrategyConfigBIO;
import com.linkage.module.ids.act.IdsDeviceQueryACT;

/**
 * @author ASUS E-mail：qixq@lianchuang.com
 * @version $Revision$
 * @since 2008-12-18
 * @category action.confTaskView
 * 
 */
public class ServStrategyConfigACT extends splitPageAction implements ServletRequestAware {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory.getLogger(ServStrategyConfigACT.class);

	ServStrategyConfigBIO bio = null;

	private String ajax = null;
	
	//属地
	private String cityId = null;
	
	// 定制起始时间
	private String time_start = null;

	// 定制截止时间
	private String time_end = null;

	// 设备序列号
	private String device_serialnumber = null;

	// 用户账号
	private String deviceId = null;
	
	// 用户账号
	private String username = null;
	
	private String taskId = null;
	
	private String id = null;
	
	private List servStrategyList = null;
	
	private String gw_type;
	
	private Integer strategyType=6;
	
	//request取登陆帐号使用
	private HttpServletRequest request;
	
	public String init(){
		return "init";
	}
	
	public String execute() throws Exception {
		
		if(null==cityId){
			HttpSession session = request.getSession();
			UserRes curUser = (UserRes) session.getAttribute("curUser");
			cityId = curUser.getCityId();
		}
		logger.warn("strategyType is:"+strategyType);
		servStrategyList = bio.getServStrategy(curPage_splitPage, num_splitPage,
				cityId, time_start, time_end, device_serialnumber, username,taskId,strategyType);

		maxPage_splitPage = bio.getServStrategyCount(curPage_splitPage,
				num_splitPage, cityId, time_start, time_end, device_serialnumber, username,taskId,strategyType);

		return "list";
	}

	public String goPage() throws Exception {

		if(null==cityId){
			HttpSession session = request.getSession();
			UserRes curUser = (UserRes) session.getAttribute("curUser");
			cityId = curUser.getCityId();
		}
		logger.warn("strategyType is:"+strategyType);
		servStrategyList = bio.getServStrategy(curPage_splitPage, num_splitPage,
				cityId, time_start, time_end, device_serialnumber, username,taskId,strategyType);

		maxPage_splitPage = bio.getServStrategyCount(curPage_splitPage,
				num_splitPage, cityId, time_start, time_end, device_serialnumber, username,taskId,strategyType);

		return "list";
	}

	public String resetData(){
		bio.resetData(deviceId, taskId,id,strategyType);
		this.ajax = "重置成功！";
		return "ajax";
	}
	
	public String cancelData(){
		bio.cancelData(deviceId, taskId,id,strategyType);
		this.ajax = "取消成功！";
		return "ajax";
	}
	
	public void setServletRequest(HttpServletRequest arg0) {
		this.request = arg0 ;
	}

	/**
	 * @return the bio
	 */
	public ServStrategyConfigBIO getBio() {
		return bio;
	}

	/**
	 * @param bio the bio to set
	 */
	public void setBio(ServStrategyConfigBIO bio) {
		this.bio = bio;
	}

	/**
	 * @return the cityId
	 */
	public String getCityId() {
		return cityId;
	}

	/**
	 * @param cityId the cityId to set
	 */
	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	/**
	 * @return the device_serialnumber
	 */
	public String getDevice_serialnumber() {
		return device_serialnumber;
	}

	/**
	 * @param device_serialnumber the device_serialnumber to set
	 */
	public void setDevice_serialnumber(String device_serialnumber) {
		this.device_serialnumber = device_serialnumber;
	}

	/**
	 * @return the servStrategyList
	 */
	public List getServStrategyList() {
		return servStrategyList;
	}

	/**
	 * @param servStrategyList the servStrategyList to set
	 */
	public void setServStrategyList(List servStrategyList) {
		this.servStrategyList = servStrategyList;
	}

	/**
	 * @return the time_end
	 */
	public String getTime_end() {
		return time_end;
	}

	/**
	 * @param time_end the time_end to set
	 */
	public void setTime_end(String time_end) {
		this.time_end = time_end;
	}

	/**
	 * @return the time_start
	 */
	public String getTime_start() {
		return time_start;
	}

	/**
	 * @param time_start the time_start to set
	 */
	public void setTime_start(String time_start) {
		this.time_start = time_start;
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
		this.username = username;
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
	 * @return the ajax
	 */
	public String getAjax() {
		return ajax;
	}

	/**
	 * @param ajax the ajax to set
	 */
	public void setAjax(String ajax) {
		this.ajax = ajax;
	}

	/**
	 * @return the taskId
	 */
	public String getTaskId() {
		return taskId;
	}

	/**
	 * @param taskId the taskId to set
	 */
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	public String getGw_type() {
		return gw_type;
	}

	public void setGw_type(String gw_type) {
		this.gw_type = gw_type;
	}

	
	public Integer getStrategyType()
	{
		return strategyType;
	}

	
	public void setStrategyType(Integer strategyType)
	{
		this.strategyType = strategyType;
	}
	
}
