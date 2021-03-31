package com.linkage.module.itms.resource.act;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.itms.resource.bio.UserSpecTerminalsBIO;

/**
 * 
 * @author hanzezheng (Ailk No.)
 * @version 1.0
 * @since 2015-2-10
 * @category com.linkage.module.itms.resource.act
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 * 
 */
public class UserSpecTerminalsACT extends splitPageAction implements
		ServletRequestAware, SessionAware {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory
			.getLogger(UserSpecTerminalsACT.class);
	private HttpServletRequest requst;
	private Map<String, Object> session=new HashMap<String, Object>();
	// 开始时间
	private String startOpenDate = "";
	// 开始时间
	private String startOpenDate1 = "";
	// 结束时间
	private String endOpenDate = "";
	// 结束时间
	private String endOpenDate1 = "";
	// 属地
	private String city_id;
	private String spec_id;
	@SuppressWarnings("rawtypes")
	private List<Map> data; // 需要导出的结果集，必须是处理过的可以直接显示的结果
	private String[] column; // 需要导出的列名，对应data中的键值
	private String[] title; // 显示在导出文档上的列名
	private String fileName; // 导出的文件名（不包括后缀名）
	private List<Map> list = new ArrayList<Map>();
	private Map<String, String> map = new HashMap<String, String>();
	// 属地列表
	private List<Map<String, String>> cityList = null;
	private UserSpecTerminalsBIO bio;

	private String ajax;
	private String specTitle="";

	@Override
	public String execute() throws Exception {
		logger.debug("init()");
		UserRes curUser = (UserRes) session.get("curUser");
		city_id = curUser.getCityId();
		cityList = CityDAO.getNextCityListByCityPid(curUser.getCityId());
		startOpenDate = getStartDate();
		endOpenDate = getEndDate();
		return "init";
	}

	public String getUserSpecTerminals() {
		this.setTime();
		if(spec_id.equals("4221")){
			specTitle="用户规格4+2，终端规格2+1";
		}else if(spec_id.equals("4220")){
			specTitle="用户规格4+2，终端规格2+0";
		}else if(spec_id.equals("2142")){
			specTitle="用户规格2+1，终端规格4+2";
		}else if(spec_id.equals("2042")){
			specTitle="用户规格2+0，终端规格4+2";
		}
		list = bio.getUserSpecTerminals(startOpenDate1, endOpenDate1, city_id,
				spec_id);

		return "searchdata";
	}

	public String getTerminalSpecList() {
		list = bio.getTerminalSpecList(startOpenDate, endOpenDate, city_id,spec_id,curPage_splitPage, num_splitPage);
		maxPage_splitPage = bio.getTerminalSpecListCount(startOpenDate, endOpenDate, city_id,spec_id, curPage_splitPage,num_splitPage);
		return "list";
	}

	public String getCityExcel(){
		this.setTime();
		if(spec_id.equals("4221")){
			specTitle="用户规格4+2，终端规格2+1";
		}else if(spec_id.equals("4220")){
			specTitle="用户规格4+2，终端规格2+0";
		}else if(spec_id.equals("2142")){
			specTitle="用户规格2+1，终端规格4+2";
		}else if(spec_id.equals("2042")){
			specTitle="用户规格2+0，终端规格4+2";
		}
		list=bio.getCityExcel(startOpenDate1, endOpenDate1, city_id, spec_id);
		String excelCol = "city_name#totals#total#pert";
		String excelTitle = "属地#总开户数#"+specTitle+"#占比";
		column = excelCol.split("#");
		title = excelTitle.split("#");
		fileName = "终端规格与用户规格不一致比对";
		data = list;
		return "excel";
	}
	
	public String getExcel() {
		list = bio.getExcle(startOpenDate, endOpenDate, city_id, spec_id);
		String excelCol = "loid#serial#cusSpec#terSpec";
		String excelTitle = "用户LOID#设备序列号#用户规格#设备规格";
		column = excelCol.split("#");
		title = excelTitle.split("#");
		fileName = "终端规格与用户规格不一致比对";
		data = list;
		return "excel";
	}

	// 当前年的1月1号
	private String getStartDate() {
		GregorianCalendar now = new GregorianCalendar();
		SimpleDateFormat fmtrq = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.US);
		now.set(GregorianCalendar.DATE, 1);
		now.set(GregorianCalendar.MONTH, 0);
		now.set(Calendar.HOUR_OF_DAY, 0);
		now.set(Calendar.MINUTE, 0);
		now.set(Calendar.SECOND, 0);
		String time = fmtrq.format(now.getTime());
		return time;
	}

	// 当前时间的23:59:59,如 2011-05-11 23:59:59
	private String getEndDate() {
		GregorianCalendar now = new GregorianCalendar();
		SimpleDateFormat fmtrq = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.US);
		now.set(Calendar.HOUR_OF_DAY, 23);
		now.set(Calendar.MINUTE, 59);
		now.set(Calendar.SECOND, 59);
		String time = fmtrq.format(now.getTime());
		return time;
	}

	/**
	 * 时间转化
	 */
	private void setTime() {
		logger.debug("setTime()" + startOpenDate);
		DateTimeUtil dt = null;// 定义DateTimeUtil
		if (startOpenDate == null || "".equals(startOpenDate)) {
			startOpenDate1 = null;
		} else {
			dt = new DateTimeUtil(startOpenDate);
			startOpenDate1 = String.valueOf(dt.getLongTime());
		}
		if (endOpenDate == null || "".equals(endOpenDate)) {
			endOpenDate1 = null;
		} else {
			dt = new DateTimeUtil(endOpenDate);
			endOpenDate1 = String.valueOf(dt.getLongTime());
		}
	}

	public String getStartOpenDate() {
		return startOpenDate;
	}

	public void setStartOpenDate(String startOpenDate) {
		this.startOpenDate = startOpenDate;
	}

	public String getEndOpenDate() {
		return endOpenDate;
	}

	public void setEndOpenDate(String endOpenDate) {
		this.endOpenDate = endOpenDate;
	}

	public String getCity_id() {
		return city_id;
	}

	public void setCity_id(String city_id) {
		this.city_id = city_id;
	}

	public List<Map<String, String>> getCityList() {
		return cityList;
	}

	public void setCityList(List<Map<String, String>> cityList) {
		this.cityList = cityList;
	}

	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.requst = request;

	}

	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	public UserSpecTerminalsBIO getBio() {
		return bio;
	}

	public void setBio(UserSpecTerminalsBIO bio) {
		this.bio = bio;
	}

	public String getSpec_id() {
		return spec_id;
	}

	public void setSpec_id(String spec_id) {
		this.spec_id = spec_id;
	}

	public Map<String, String> getMap() {
		return map;
	}

	public void setMap(Map<String, String> map) {
		this.map = map;
	}

	public String getStartOpenDate1() {
		return startOpenDate1;
	}

	public void setStartOpenDate1(String startOpenDate1) {
		this.startOpenDate1 = startOpenDate1;
	}

	public String getAjax() {
		return ajax;
	}

	public void setAjax(String ajax) {
		this.ajax = ajax;
	}

	public List<Map> getList() {
		return list;
	}

	public void setList(List<Map> list) {
		this.list = list;
	}

	public String getEndOpenDate1() {
		return endOpenDate1;
	}

	public void setEndOpenDate1(String endOpenDate1) {
		this.endOpenDate1 = endOpenDate1;
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

	public Map<String, Object> getSession() {
		return session;
	}

	public String getSpecTitle() {
		return specTitle;
	}

	public void setSpecTitle(String specTitle) {
		this.specTitle = specTitle;
	}
	

}
