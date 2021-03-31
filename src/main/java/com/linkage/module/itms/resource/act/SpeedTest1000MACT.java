package com.linkage.module.itms.resource.act;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.itms.resource.bio.SpeedTest1000MBIO;

import action.splitpage.splitPageAction;



public class SpeedTest1000MACT extends splitPageAction implements
		ServletRequestAware {

	private static final long serialVersionUID = 1891651756165L;
	Logger logger = LoggerFactory.getLogger(SpeedTest1000MACT.class);

	private SpeedTest1000MBIO bio;
	private HttpServletRequest request;
	
	/** 地市集合 **/
	private List<Map<String,String>> cityList;
	private String cityId = "";
	private String speedRet = "";
	private String bandwidth = "";
	private String startTime = "";
	private String endTime = "";
	
	//导出
	private String fileName;
	private String[] title ;
	private String[] column ;
	
	private List<Map> data;
	
	//列表
	private List<Map> speedRetList = null;
	
	/**
	 * 老版需求
	 * @return
	 */
	public String init(){
		initCity();
		initTime();
		cityList = CityDAO.getNextCityListByCityPid(cityId);
		return "init";
	}
	
	public String query(){
		setTime();
		initCity();
		logger.warn("query speedRet={},bandwidth={},startTime={},endTime={},cityId={},",speedRet, bandwidth, startTime, endTime, cityId);
		speedRetList = bio.query(speedRet, bandwidth, startTime, endTime, cityId, curPage_splitPage, num_splitPage);
		maxPage_splitPage = bio.queryCount(speedRet, bandwidth, startTime, endTime, cityId, curPage_splitPage, num_splitPage);
		return "list";
	}
	
	/**
	 * 终端不匹配详情导出
	 * @return
	 */
	public String toExcel(){
		logger.warn("toExcel speedRet={},bandwidth={},startTime={},endTime={},cityId={},",speedRet, bandwidth, startTime, endTime, cityId);
		initCity();
		setTime();
		setFileName("宽带测速结果统计");
		title = new String[] { "属地", "营业区", "LOID","宽带账号", "用户宽带带宽", "厂家","型号", "测速结果","测速结果"};
		column = new String[] { "city_name", "county_name", "loid","username", "bandwidth", "vendor_name","devicemodel","speed_ret","result"};
		data = bio.toExcel(speedRet, bandwidth, startTime, endTime, cityId, curPage_splitPage, num_splitPage);
		return "excel";
	}
	
	public void initCity(){
		if(null == this.cityId || "".equals(this.cityId) || "-1".equals(cityId)){
			HttpSession session = request.getSession();
			UserRes curUser = (UserRes) session.getAttribute("curUser");
			long area_id = curUser.getAreaId();
			List<Map> list = bio.queryCityId(StringUtil.getStringValue(area_id));
			Map map=list.get(0);
			cityId=StringUtil.getStringValue(map.get("city_id"));
			if (StringUtil.IsEmpty(cityId)) {
				cityId=curUser.getCityId();
			}
		}
	}
	public void initTime(){
		DateTimeUtil dt = new DateTimeUtil();
		endTime = dt.getDate();  // 获取当前时间
		
		dt = new DateTimeUtil(endTime);
		long end_time = dt.getLongTime();
		dt = new DateTimeUtil((end_time + 24 * 3600 - 1) * 1000); // 精确到当前时间的23点59分59秒
		endTime = dt.getLongDate();
		
		int year = dt.getYear();
		int month = dt.getMonth()-1;
		int day = dt.getDay();
		
		dt = new DateTimeUtil(year+"-"+month+"-"+day);
		long start_time = dt.getLongTime();
		dt = new DateTimeUtil((start_time) * 1000);
		startTime = dt.getLongDate();
	} 
	
	private void setTime(){
		DateTimeUtil dt = null;// 定义DateTimeUtil
		if (startTime == null || "".equals(startTime)){
			startTime = null;
		}else{
			dt = new DateTimeUtil(startTime);
			startTime = String.valueOf(dt.getLongTime());
		}
		if (endTime == null || "".equals(endTime)){
			endTime = null;
		}else{
			dt = new DateTimeUtil(endTime);
			endTime = String.valueOf(dt.getLongTime());
		}
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	@Override
	public void setServletRequest(HttpServletRequest arg0) {
		this.request = arg0;
	}
 

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public String[] getTitle() {
		return title;
	}

	public void setTitle(String[] title) {
		this.title = title;
	}

	public String[] getColumn() {
		return column;
	}

	public void setColumn(String[] column) {
		this.column = column;
	}

	public List<Map> getData() {
		return data;
	}

	public void setData(List<Map> data) {
		this.data = data;
	}

	public List<Map<String,String>> getCityList()
	{
		return cityList;
	}

	public void setCityList(List<Map<String,String>> cityList)
	{
		this.cityList = cityList;
	}
 
	public String getSpeedRet()
	{
		return speedRet;
	}

	
	public void setSpeedRet(String speedRet)
	{
		this.speedRet = speedRet;
	}

	
	public String getBandwidth()
	{
		return bandwidth;
	}

	
	public void setBandwidth(String bandwidth)
	{
		this.bandwidth = bandwidth;
	}

	
	public String getStartTime()
	{
		return startTime;
	}

	
	public void setStartTime(String startTime)
	{
		this.startTime = startTime;
	}

	
	public String getEndTime()
	{
		return endTime;
	}

	
	public void setEndTime(String endTime)
	{
		this.endTime = endTime;
	}

	
	public void setBio(SpeedTest1000MBIO bio)
	{
		this.bio = bio;
	}

	public SpeedTest1000MBIO getBio()
	{
		return bio;
	}

	public String getFileName()
	{
		return fileName;
	}



	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}

	public List<Map> getSpeedRetList()
	{
		return speedRetList;
	}

	public void setSpeedRetList(List<Map> speedRetList)
	{
		this.speedRetList = speedRetList;
	}
}
