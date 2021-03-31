/**
 * 
 */
package com.linkage.module.gwms.resource.act;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.DateTimeUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.resource.bio.BindLogCountBIO;
import com.opensymphony.xwork2.ActionSupport;

/**
 * @author ASUS E-mail：qixq@lianchuang.com
 * @version $Revision$
 * @since 2008-12-18
 * @category action.confTaskView
 * 
 */
public class BindLogCountACT extends ActionSupport implements ServletRequestAware {

	//日志记录
	private static Logger logger = LoggerFactory.getLogger(BindLogCountACT.class);
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	BindLogCountBIO bindLogCountBIO = null;

	// 定制起始时间
	private String bindStartTime = null;

	// 定制截止时间
	private String bindEndTime = null;
	
	//属地
	private String cityId = null;
	
	//属地列表
	private List<Map<String,String>> cityIdList = null;
	
	//数据集
	private List<Map<String, String>> rsList = null;
	
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

	public String startQuery() throws Exception {

		if(null==this.cityId || "-1".equals(this.cityId)){
			HttpSession session = request.getSession();
			UserRes curUser = (UserRes) session.getAttribute("curUser");
			cityId = curUser.getCityId();
		}

		long startTime = new DateTimeUtil(bindStartTime,DateTimeUtil.yyyy_MM_dd_HH_mm_ss).getLongTime();
		long endTime = new DateTimeUtil(bindEndTime,DateTimeUtil.yyyy_MM_dd_HH_mm_ss).getLongTime();
		
		this.rsList = bindLogCountBIO.getBindCountMap(startTime, endTime, cityId);
		
		return "list";
		
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
	 * @return the bindLogCountBIO
	 */
	public BindLogCountBIO getBindLogCountBIO() {
		return bindLogCountBIO;
	}

	/**
	 * @param bindLogCountBIO the bindLogCountBIO to set
	 */
	public void setBindLogCountBIO(BindLogCountBIO bindLogCountBIO) {
		this.bindLogCountBIO = bindLogCountBIO;
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
	 * @return the rsList
	 */
	public List<Map<String, String>> getRsList() {
		return rsList;
	}

	/**
	 * @param rsList the rsList to set
	 */
	public void setRsList(List<Map<String, String>> rsList) {
		this.rsList = rsList;
	}
	
}
