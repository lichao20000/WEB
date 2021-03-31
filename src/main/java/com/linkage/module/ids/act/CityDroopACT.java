package com.linkage.module.ids.act;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.ids.bio.CityDroopBIO;

public class CityDroopACT extends splitPageAction implements SessionAware {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 2389555267415599412L;
	// 日志记录
	private static Logger logger = LoggerFactory
			.getLogger(CityDroopACT.class);
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
	private String operType = "";
	/** 用户详细展示查询方式 */
	private String query_type;
	/** 查询参数值 */
	private String queryVal;
	private String ajax = "";

	private HttpServletRequest request;
	// 导出数据
	private List<Map<String, Object>> data;

	private String[] column; // 需要导出的列名，对应data中的键值

	private String[] title; // 显示在导出文档上的列名

	private String fileName; // 导出的文件名（不包括后缀名）

	private CityDroopBIO bio;

	private List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

	private String droopTotal;

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
	 * @date 2015.05.07
	 * @param
	 * @return String
	 */
	public String execute() {
		String indexName = "ids";
		String indexType = "ponstatus";
		list = bio.countCityDroop(indexName, indexType, starttime, endtime,
				cityId, curPage_splitPage, num_splitPage);
		return "list";
	}

	/**
	 * 详细列表页面
	 * 
	 * @author SONGJJ
	 * @date 2015.05.07
	 * @param
	 * @return String
	 */
	public String queryByDroopList() {
		String indexName = "ids";
		String indexType = "ponstatus";
		rlist = bio.queryByDroopList(indexName, indexType, starttime, endtime,
				cityId, operType, curPage_splitPage, num_splitPage);
		maxPage_splitPage = bio.queryByDroopCount(indexName, indexType,
				starttime, endtime, cityId, operType, curPage_splitPage,
				num_splitPage);
		return "rlist";
	}

	/**
	 * 导出详细列表页面
	 * 
	 * @author SONGJJ
	 * @date 2015.05.08
	 * @param
	 * @return String
	 */
	public String queryByDroopExcel() {
		String indexName = "ids";
		String indexType = "ponstatus";
		column = new String[] { "city_name", "loid", "device_serialnumber",
				"upload_time", "add_time", "droop" };
		title = new String[] { "属 地", "逻辑SN", "设备序列号", "上报时间", "入库时间", "光衰" };
		fileName = "地市设备光衰用过户列表";
		int total = bio.queryByDroopTotal(indexName, indexType, starttime,
				endtime, cityId, curPage_splitPage, num_splitPage);
		data = bio.queryByDroopExcel(indexName, indexType, starttime, endtime,
				cityId, operType, curPage_splitPage, num_splitPage, total);
		return "excel";
	}

	/**
	 * 查看用户详细信息页面
	 * 
	 * @author SONGJJ
	 * @date 2015.05.08
	 * @param
	 * @return String
	 */
	public String getDetail() {
		ajax = bio.getDetail(query_type, queryVal);
		return "ajax";
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

	public static long getSerialversionuid() {
		return serialVersionUID;
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

	public static Logger getLogger() {
		return logger;
	}

	public List<Map<String, Object>> getData() {
		return data;
	}

	public void setData(List<Map<String, Object>> data) {
		this.data = data;
	}

	public CityDroopBIO getBio() {
		return bio;
	}

	public void setBio(CityDroopBIO bio) {
		this.bio = bio;
	}

	public List<Map<String, Object>> getList() {
		return list;
	}

	public void setList(List<Map<String, Object>> list) {
		this.list = list;
	}

	public String getDroopTotal() {
		return droopTotal;
	}

	public void setDroopTotal(String droopTotal) {
		this.droopTotal = droopTotal;
	}

	public List<Map<String, Object>> getRlist() {
		return rlist;
	}

	public void setRlist(List<Map<String, Object>> rlist) {
		this.rlist = rlist;
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

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public String getOperType() {
		return operType;
	}

	public void setOperType(String operType) {
		this.operType = operType;
	}

	public String getAjax() {
		return ajax;
	}

	public void setAjax(String ajax) {
		this.ajax = ajax;
	}

	public String getQuery_type() {
		return query_type;
	}

	public void setQuery_type(String query_type) {
		this.query_type = query_type;
	}

	public String getQueryVal() {
		return queryVal;
	}

	public void setQueryVal(String queryVal) {
		this.queryVal = queryVal;
	}

}
