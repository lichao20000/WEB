package com.linkage.module.ids.act;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.ids.bio.PPPoECountBIO;

public class PPPoECountACT extends splitPageAction implements SessionAware {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	// 日志记录
	private static Logger logger = LoggerFactory
			.getLogger(PPPoECountACT.class);
	// session
	private Map<String, Object> session;
	/** 开始时间 */
	private String starttime = "";
	/** 结束时间 */
	private String endtime = "";
	/** 属地列表 */
	private List<Map<String, String>> cityList = null;
	/** 属地ID */
	private String cityId = "-1";
	/** 统计类型 */
	private String operType;
	/** 导出数据 */
	private List<Map<String, Object>> data;
	private String[] column; // 需要导出的列名，对应data中的键值

	private String[] title; // 显示在导出文档上的列名

	private String fileName; // 导出的文件名（不包括后缀名）
	private PPPoECountBIO bio;
	/* 统计列表 */
	private List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	/* 详细列表 */
	private List<Map<String, Object>> rlist = new ArrayList<Map<String, Object>>();

	/**
	 * 初始化页面
	 * 
	 * @author SONGJJ
	 * @date 2015.05.07
	 * @param
	 * @return String
	 */
	public String init() {
		UserRes curUser = (UserRes) session.get("curUser");
		cityId = curUser.getCityId();
		cityList = CityDAO.getNextCityListByCityPid(curUser.getCityId());
		setTime();
		return "init";
	}

	/**
	 * 统计查询页面
	 * 
	 * @author SONGJJ
	 * @date 2015.05.15
	 * @param
	 * @return String
	 */
	public String execute() {
		String indexName = "ids";
		String indexType = "netparam";
		list = bio.countPPPoE(indexName, indexType, starttime, endtime, cityId,
				curPage_splitPage, num_splitPage);
		return "list";
	}

	/**
	 * 导出统计列表页面
	 * 
	 * @author SONGJJ
	 * @date 2015.05.08
	 * @param
	 * @return String
	 */
	public String queryByPPPoEExcel() {
		String indexName = "ids";
		String indexType = "netparam";
		column = new String[] { "cityName", "timeoutNum", "failureNum",
				"disConnectNum" };
		title = new String[] { "属地", "ERROR_ISP_TIME_OUT",
				"ERROR_AUTHENTICATION_FAILURE", "ERROR_ISP_DISCONNECT" };
		fileName = "PPPoE失败原因统计";
		int total = bio.queryByPPPoETotal(indexName, indexType, starttime,
				endtime, cityId, operType, curPage_splitPage, num_splitPage);
		data = bio.queryByPPPoEExcel(indexName, indexType, starttime, endtime,
				cityId, operType, curPage_splitPage, num_splitPage, total);
		return "excel";
	}

	/**
	 * 详细列表页面
	 * 
	 * @author SONGJJ
	 * @date 2015.05.15
	 * @param
	 * @return String
	 */
	public String queryByPPPoEList() {
		String indexName = "ids";
		String indexType = "netparam";
		rlist = bio.queryByPPPoEList(indexName, indexType, starttime, endtime,
				cityId, operType, curPage_splitPage, num_splitPage);
		maxPage_splitPage = bio.queryByPPPoECount(indexName, indexType,
				starttime, endtime, cityId, operType, curPage_splitPage,
				num_splitPage);
		return "rlist";
	}

	/**
	 * 时间转化
	 */
	private void setTime() {
		DateTimeUtil dt = new DateTimeUtil();
		endtime = dt.getDate(); // 获取当前时间

		dt = new DateTimeUtil(endtime);
		long end_time = dt.getLongTime();
		dt = new DateTimeUtil((end_time + 24 * 3600 - 1) * 1000); // 精确到当前时间的23点59分59秒
		endtime = dt.getLongDate();

		starttime = dt.getFirtDayOfMonth(); // 获取开始时间，为当月时间的第一天
		dt = new DateTimeUtil(starttime);
		long start_time = dt.getLongTime();
		dt = new DateTimeUtil((start_time) * 1000);
		starttime = dt.getLongDate();
	}

	public Map<String, Object> getSession() {
		return session;
	}

	public void setSession(Map<String, Object> session) {
		this.session = session;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public static Logger getLogger() {
		return logger;
	}

	public PPPoECountBIO getBio() {
		return bio;
	}

	public void setBio(PPPoECountBIO bio) {
		this.bio = bio;
	}

	public List<Map<String, Object>> getList() {
		return list;
	}

	public void setList(List<Map<String, Object>> list) {
		this.list = list;
	}

	public String getOperType() {
		return operType;
	}

	public void setOperType(String operType) {
		this.operType = operType;
	}

	public List<Map<String, Object>> getRlist() {
		return rlist;
	}

	public void setRlist(List<Map<String, Object>> rlist) {
		this.rlist = rlist;
	}

	public List<Map<String, Object>> getData() {
		return data;
	}

	public void setData(List<Map<String, Object>> data) {
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
}
