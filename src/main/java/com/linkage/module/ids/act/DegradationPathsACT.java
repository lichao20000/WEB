package com.linkage.module.ids.act;

import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.litms.common.WebUtil;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.ids.bio.DegradationPathsBIO;

public class DegradationPathsACT extends splitPageAction implements
		SessionAware {

	private static final long serialVersionUID = 1L;

	private static Logger logger = LoggerFactory
			.getLogger(DegradationPathsACT.class);

	@SuppressWarnings("rawtypes")
	private Map session;

	private String startTime = "";
	private String endTime = "";
	private String starttime1 = "";
	private String endtime1 = "";

	@SuppressWarnings("rawtypes")
	private List dataList;

	private List<Map<String, String>> cityList = null;

	private String cityId = "-1";

	private String oltip = "";

	private String ponid = "";

	@SuppressWarnings("rawtypes")
	private List infoList;

	private DegradationPathsBIO bio;

	public String execute() {
		cityList = CityDAO.getAllNextCityListByCityPid(WebUtil.getCurrentUser()
				.getCityId());
		DateTimeUtil dateTimeUtil = new DateTimeUtil();
		endTime = dateTimeUtil.getDate();
		dateTimeUtil.getNextDate(-1);
		startTime = dateTimeUtil.getDate();
		return "init";
	}

	public String getDegradationPaths() {
		logger.warn("doDegradationPaths()");
		dataList = bio.getDegradationPathsList(curPage_splitPage,
				num_splitPage, startTime, endTime, cityId, oltip);
		maxPage_splitPage = bio.getDegradationPathsCount(num_splitPage,
				startTime, endTime, cityId, oltip);
		return "list";
	}

	public String getDegradationPathsInfo() {
		logger.warn("doDegradationPaths()");
		infoList = bio.getDegradationPathsInfoList(curPage_splitPage,
				num_splitPage, oltip, ponid,startTime, endTime);
		maxPage_splitPage = bio.getDegradationPathsInfoCount(num_splitPage,
				oltip, ponid,startTime, endTime);
		return "info";
	}


	@SuppressWarnings("rawtypes")
	public Map getSession() {
		return session;
	}

	@SuppressWarnings("rawtypes")
	public void setSession(Map session) {
		this.session = session;
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

	public String getStarttime1() {
		return starttime1;
	}

	public void setStarttime1(String starttime1) {
		this.starttime1 = starttime1;
	}

	public String getEndtime1() {
		return endtime1;
	}

	public void setEndtime1(String endtime1) {
		this.endtime1 = endtime1;
	}

	@SuppressWarnings("rawtypes")
	public List getDataList() {
		return dataList;
	}

	@SuppressWarnings("rawtypes")
	public void setDataList(List dataList) {
		this.dataList = dataList;
	}

	public List<Map<String, String>> getCityList() {
		return cityList;
	}

	public void setCityList(List<Map<String, String>> cityList) {
		this.cityList = cityList;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public String getOltip() {
		return oltip;
	}

	public void setOltip(String oltip) {
		this.oltip = oltip;
	}

	public DegradationPathsBIO getBio() {
		return bio;
	}

	public void setBio(DegradationPathsBIO bio) {
		this.bio = bio;
	}

	@SuppressWarnings("rawtypes")
	public List getInfoList() {
		return infoList;
	}

	@SuppressWarnings("rawtypes")
	public void setInfoList(List infoList) {
		this.infoList = infoList;
	}

	public String getPonid() {
		return ponid;
	}

	public void setPonid(String ponid) {
		this.ponid = ponid;
	}
}
