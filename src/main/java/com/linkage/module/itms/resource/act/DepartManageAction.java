package com.linkage.module.itms.resource.act;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.litms.system.UserRes;
import com.linkage.module.itms.resource.bio.DepartManageBIO;

@SuppressWarnings("rawtypes")
public class DepartManageAction extends splitPageAction implements SessionAware, ServletRequestAware {

	private static Logger logger = LoggerFactory.getLogger(DepartManageAction.class);

	private static final long serialVersionUID = 1L;
	// 日志记录

	private DepartManageBIO bio;
	
	/** 特定编号 */
	private String ajax;
	
	/** 开始时间 */
	private String startTime;
	/** 结束时间 */
	private String endTime;
	private String nameSearch;

	private HttpServletRequest request;
	
	private List<Map> departList = null;

	public String queryList() {
		logger.debug("DepartManageAction queryList");
		departList = bio.queryDepartList(curPage_splitPage, num_splitPage, startTime, endTime, nameSearch);
		maxPage_splitPage = bio.getMaxPage_splitPage();
		return "queryList";
		
	}

	private String deptname;
	private String deptdesc;
	private Long departid; 
	public String addDepartInfo() {
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		// 定制人
		long acc_oid = curUser.getUser().getId();
		
		ajax = bio.addDepartInfo(deptname, deptdesc, acc_oid);
		return "ajax";
	}

	public String deleteDepart() {
		ajax = bio.deleteDepart(departid);
		return "ajax";
	}

	public String editDepartInfo() {
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		// 定制人
		long acc_oid = curUser.getUser().getId();
		
		ajax = bio.editDepartInfo(deptname, deptdesc, acc_oid, departid);
		return "ajax";
	}

	public DepartManageBIO getBio() {
		return bio;
	}

	public void setBio(DepartManageBIO bio) {
		this.bio = bio;
	}

	public String getAjax() {
		return ajax;
	}

	public void setAjax(String ajax) {
		this.ajax = ajax;
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

	public String getNameSearch() {
		return nameSearch;
	}

	public void setNameSearch(String nameSearch) {
		this.nameSearch = nameSearch;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public List<Map> getDepartList() {
		return departList;
	}

	public void setDepartList(List<Map> departList) {
		this.departList = departList;
	}

	public String getDeptname() {
		return deptname;
	}

	public void setDeptname(String deptname) {
		this.deptname = deptname;
	}

	public String getDeptdesc() {
		return deptdesc;
	}

	public void setDeptdesc(String deptdesc) {
		this.deptdesc = deptdesc;
	}

	public Long getDepartid() {
		return departid;
	}

	public void setDepartid(Long departid) {
		this.departid = departid;
	}

	@Override
	public void setServletRequest(HttpServletRequest arg0) {
		this.request = arg0;

	}
	@Override
	public void setSession(Map<String, Object> arg0) {
	}

//	public String getSpeversion() {
//		return speversion;
//	}
//
//	public void setSpeversion(String speversion) {
//		try {
//			this.speversion = java.net.URLDecoder.decode(speversion, "UTF-8");
//		} catch (Exception e) {
//			this.speversion = speversion;
//		}
//	}

//	public void setPortInfo(String portInfo) {
//		try {
//			this.portInfo = java.net.URLDecoder.decode(portInfo, "UTF-8");
//		} catch (Exception e) {
//			this.portInfo = portInfo;
//		}
//	}
}
