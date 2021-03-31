/**
 * 
 */
package com.linkage.module.gtms.stb.resource.action;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.commons.util.DateTimeUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gtms.stb.resource.serv.BindLogViewBIO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

/**
 * @author ASUS E-mail：qixq@lianchuang.com
 * @version $Revision$
 * @since 2008-12-18
 * @category action.confTaskView
 * 
 */
public class BindLogViewACT extends splitPageAction implements ServletRequestAware {

	//日志记录
	private static Logger logger = LoggerFactory.getLogger(BindLogViewACT.class);
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	BindLogViewBIO bindLogViewBIO = null;

	// 定制起始时间
	private String bindStartTime = null;

	// 定制截止时间
	private String bindEndTime = null;

	// 用户账号
	private String username = null;

	//设备序列号
	private String deviceSn = null;
	
	//属地
	private String cityId = null;
	
	//绑定类型
	private String operType = null;
	
	//属地列表
	private List<Map<String,String>> cityIdList = null;
	
	//数据集
	private List rsList = null;
	
	//request取登陆帐号使用
	private HttpServletRequest request;
	
	public String execute() throws Exception {
		
		logger.debug("execute()");
		
		if(null==this.cityId){
			HttpSession session = request.getSession();
			UserRes curUser = (UserRes) session.getAttribute("curUser");
			this.cityId = curUser.getCityId();
		}
		this.cityIdList = CityDAO.getNextCityListByCityPid(this.cityId);
		
		return SUCCESS;
	}

	public String goPage() throws Exception {

		if(null==this.cityId || "-1".equals(this.cityId)){
			HttpSession session = request.getSession();
			UserRes curUser = (UserRes) session.getAttribute("curUser");
			this.cityId = curUser.getCityId();
		}
		
		long startTime = new DateTimeUtil(bindStartTime,DateTimeUtil.yyyy_MM_dd_HH_mm_ss).getLongTime();
		long endTime = new DateTimeUtil(bindEndTime,DateTimeUtil.yyyy_MM_dd_HH_mm_ss).getLongTime();
		
		this.rsList = bindLogViewBIO.getBindLogList(curPage_splitPage, num_splitPage, startTime, endTime, username, deviceSn, cityId,operType);

		maxPage_splitPage = bindLogViewBIO.getBindLogCount(num_splitPage, startTime, endTime, username, deviceSn, cityId,operType);

		return "list";
	}

	public String startQuery() throws Exception {

		if(null==this.cityId || "-1".equals(this.cityId)){
			HttpSession session = request.getSession();
			UserRes curUser = (UserRes) session.getAttribute("curUser");
			cityId = curUser.getCityId();
		}

		long startTime = new DateTimeUtil(bindStartTime,DateTimeUtil.yyyy_MM_dd_HH_mm_ss).getLongTime();
		long endTime = new DateTimeUtil(bindEndTime,DateTimeUtil.yyyy_MM_dd_HH_mm_ss).getLongTime();
		
		this.rsList = bindLogViewBIO.getBindLogList(curPage_splitPage, num_splitPage, startTime, endTime, username, deviceSn, cityId,operType);

		maxPage_splitPage = bindLogViewBIO.getBindLogCount(num_splitPage, startTime, endTime, username, deviceSn, cityId,operType);
		
		return "list";
		
	}
	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username
	 *            the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	public void setServletRequest(HttpServletRequest arg0) {
		this.request = arg0;
	}

	/**
	 * @return the request
	 */
	public HttpServletRequest getRequest() {
		return request;
	}

	/**
	 * @param request the request to set
	 */
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	/**
	 * @return the bindEndtTime
	 */
	public String getBindEndTime() {
		return bindEndTime;
	}

	/**
	 * @param bindEndtTime the bindEndtTime to set
	 */
	public void setBindEndTime(String bindEndTime) {
		this.bindEndTime = bindEndTime;
	}

	/**
	 * @return the bindLogViewBIO
	 */
	public BindLogViewBIO getBindLogViewBIO() {
		return bindLogViewBIO;
	}

	/**
	 * @param bindLogViewBIO the bindLogViewBIO to set
	 */
	public void setBindLogViewBIO(BindLogViewBIO bindLogViewBIO) {
		this.bindLogViewBIO = bindLogViewBIO;
	}

	/**
	 * @return the bindStartTime
	 */
	public String getBindStartTime() {
		return bindStartTime;
	}

	/**
	 * @param bindStartTime the bindStartTime to set
	 */
	public void setBindStartTime(String bindStartTime) {
		this.bindStartTime = bindStartTime;
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
	 * @return the cityIdList
	 */
	public List<Map<String, String>> getCityIdList() {
		return cityIdList;
	}

	/**
	 * @param cityIdList the cityIdList to set
	 */
	public void setCityIdList(List<Map<String, String>> cityIdList) {
		this.cityIdList = cityIdList;
	}

	/**
	 * @return the deviceSn
	 */
	public String getDeviceSn() {
		return deviceSn;
	}

	/**
	 * @param deviceSn the deviceSn to set
	 */
	public void setDeviceSn(String deviceSn) {
		this.deviceSn = deviceSn;
	}

	/**
	 * @return the rsList
	 */
	public List getRsList() {
		return rsList;
	}

	/**
	 * @param rsList the rsList to set
	 */
	public void setRsList(List rsList) {
		this.rsList = rsList;
	}

	/**
	 * @return the operType
	 */
	public String getOperType() {
		return operType;
	}

	/**
	 * @param operType the operType to set
	 */
	public void setOperType(String operType) {
		this.operType = operType;
	}
	
}
