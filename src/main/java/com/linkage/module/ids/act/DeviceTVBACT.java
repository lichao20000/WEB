package com.linkage.module.ids.act;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.ids.bio.DeviceTVBBIO;

import action.splitpage.splitPageAction;
/**
 * DeviceTVB
 * @author os_hanzz
 * Date 2015-05-04
 */
public class DeviceTVBACT extends splitPageAction implements SessionAware,ServletRequestAware {
	
	//日志操作
	private Logger logger = LoggerFactory.getLogger(DeviceTVBACT.class);
	
	//开始时间
	private String starttime;
	//结束时间
	private String endtime;
	//属地id
	private String cityId;
	//温度
	private String temperature;
	//电压
	private String vottage;
	//电流
	private String bais_current;
	private HttpServletRequest request;
	private String tvbTotal;
	private Map<String, Object> session;
	private List<Map> data; // 需要导出的结果集，必须是处理过的可以直接显示的结果
	private String[] column; // 需要导出的列名，对应data中的键值
	private String[] title; // 显示在导出文档上的列名
	private String fileName; // 导出的文件名（不包括后缀名）
	// 属地列表
	private List<Map<String, String>> cityList = null; 
	
	private List<Map<String, Object>> rlist=new ArrayList<Map<String,Object>>();
	
	private List<Map> list = new ArrayList<Map>();
	
	private DeviceTVBBIO bio;
	@Override
	public String execute() throws Exception {
		UserRes curUser = (UserRes) session.get("curUser");
		cityId = curUser.getCityId();
		cityList = CityDAO.getNextCityListByCityPid(curUser.getCityId());
		initTime();
		return "success";
	}
	
	public String queryByTVB(){
//		logger.warn("DeviceTVBACT=>queryByTVB:{}",starttime);
		String indexName = "ids";
		String indexType = "ponstatus";
		list = bio.queryByTVB(curPage_splitPage,num_splitPage,indexName, indexType, starttime, endtime, cityId, temperature, bais_current, vottage);
		return "list";
	}
	public String queryByTVBList(){
		String indexName = "ids";
		String indexType = "ponstatus";
		int num = bio.queryByTVBTotal(indexName, indexType, starttime, endtime, cityId, temperature, bais_current, vottage, curPage_splitPage, num_splitPage);
//		logger.warn("DeviceTVBACT.total={}",num);
		rlist=bio.queryByTVBList(indexName, indexType, starttime, endtime, cityId, temperature, bais_current, vottage, curPage_splitPage, num_splitPage);
		maxPage_splitPage=bio.queryByTVBCount(indexName, indexType, starttime, endtime, cityId, temperature, bais_current, vottage, curPage_splitPage, num_splitPage);
		return "rlist";
	}
	
	public String queryByTVBExcel(){
		String indexName = "ids";
		String indexType = "ponstatus";
		column = new String[] { "cityName", "total"};
		title = new String[] { "区域", "终端数"};
		fileName = "地市设备环境指标";
		data=bio.queryByTVB(curPage_splitPage,num_splitPage,indexName, indexType, starttime, endtime, cityId, temperature, bais_current, vottage);
		return "excel";
	}
	
	public String queryByTVBListExcel(){
		String indexName = "ids";
		String indexType = "ponstatus";
		column = new String[] { "loid", "device_serialnumber", "upload_time", "temperature", "bais_current", "vottage"};
		title = new String[] { "逻辑SN", "设备序列号", "时间", "温度", "电流", "电压"};
		fileName = "地市设备环境指标";
		int num = bio.queryByTVBTotal(indexName, indexType, starttime, endtime, cityId, temperature, bais_current, vottage, curPage_splitPage, num_splitPage);
		data=bio.queryByTVBExcel(indexName, indexType, starttime, endtime, cityId, temperature, bais_current, vottage, curPage_splitPage, num_splitPage, num);
		return "excel";
	}
	
	/**
	 * 初始化时间
	 */
	public void initTime(){
		DateTimeUtil dt = new DateTimeUtil();
		endtime = dt.getDate();  // 获取当前时间
		
		dt = new DateTimeUtil(endtime);
		long end_time = dt.getLongTime();
		dt = new DateTimeUtil((end_time + 24 * 3600 - 1) * 1000); // 精确到当前时间的23点59分59秒
		endtime = dt.getLongDate();
		
		starttime = dt.getFirtDayOfMonth();   //获取开始时间，为当月时间的第一天
		dt = new DateTimeUtil(starttime);
		long start_time = dt.getLongTime();
		dt = new DateTimeUtil((start_time) * 1000);
		starttime = dt.getLongDate();
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


	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public String getTemperature() {
		return temperature;
	}

	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}

	public String getVottage() {
		return vottage;
	}


	public void setVottage(String vottage) {
		this.vottage = vottage;
	}


	public String getBais_current() {
		return bais_current;
	}

	public void setBais_current(String bais_current) {
		this.bais_current = bais_current;
	}

	@Override
	public void setSession(Map<String, Object> session) {
		this.session=session;

	}

	public List<Map<String, String>> getCityList() {
		return cityList;
	}

	public void setCityList(List<Map<String, String>> cityList) {
		this.cityList = cityList;
	}

	public List<Map> getList() {
		return list;
	}

	public void setList(List<Map> list) {
		this.list = list;
	}

	public DeviceTVBBIO getBio() {
		return bio;
	}

	public void setBio(DeviceTVBBIO bio) {
		this.bio = bio;
	}

	public List<Map<String, Object>> getRlist() {
		return rlist;
	}

	public void setRlist(List<Map<String, Object>> rlist) {
		this.rlist = rlist;
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

	public String getTvbTotal() {
		return tvbTotal;
	}

	public void setTvbTotal(String tvbTotal) {
		this.tvbTotal = tvbTotal;
	}

	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request=request;
		
	}

	



}
