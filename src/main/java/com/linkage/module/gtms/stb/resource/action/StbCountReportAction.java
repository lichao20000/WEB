package com.linkage.module.gtms.stb.resource.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.module.gtms.stb.resource.serv.StbCountReportBIO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.StringUtil;

/**
 * 湖南联通机顶盒按EPG或APK版本统计
 */
@SuppressWarnings("rawtypes")
public class StbCountReportAction extends splitPageAction implements SessionAware 
{
	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory.getLogger(StbCountReportAction.class);
	private Map session;
	
	/** 属地列表 */
	private List<Map<String, String>> cityList = null;
	
	/** 属地列表（有序） */
	private List<HashMap<String, String>> cityListOrder = null;
	
	/** 属地ID */
	private String city_id = "-1";
	/**属地in或=标志，0为=，1为in*/
	private String cityType="0";
	/**统计类型，queryType：epg,apk*/
	private String queryType="";
	/**EPG版本/APK版本*/
	private String version = "";
	
	/**存储统计后的数据文件，供导出时用，无需再次统计*/
	private String queryTime;
	// 导出数据
	private List<Map<String,String>> data;
	private int queryCount;
	// 导出文件列标题
	private String[] title;
	// 导出文件列
	private String[] column;
	// 导出文件名
	private String fileName;
	private StbCountReportBIO bio;
	private List<Map> devList = null;
	
	private String ajax;

	/**
	 * 初始化页面
	 */
	public String init() 
	{
		cityList = CityDAO.getNextCityListByCityPid("00");

		return "init";
	}

	/**
	 * 按属地、版本统计
	 */
	public String execute() 
	{
		logger.warn("execute({},{})",city_id,queryType);
		
		if(StringUtil.IsEmpty(city_id) || "-1".equals(city_id)){
			city_id="00";
		}
		cityList = CityDAO.getNextCityListByCityPid(city_id);
		data = bio.countResultByCity(queryType,city_id,cityList);
		cityListOrder = bio.getNextCityListOrderByCityPid(city_id);
		ajax=bio.toConversion(queryType,data,city_id,cityListOrder);
		
		return "ajax";
	}
	
	/**
	 * 按属地、版本展示设备详细
	 */
	public String getStbDeviceList() 
	{
		logger.warn("getStbDeviceList({},{},{},{})",
				new Object[]{city_id,version,curPage_splitPage,num_splitPage});
		
		if(StringUtil.IsEmpty(city_id) || "-1".equals(city_id)){
			city_id="00";
		}
		
		data = bio.countResultByCityVersion(queryType,city_id,cityType,version,curPage_splitPage, num_splitPage);
		queryCount=bio.getCountResultByCityVersion(queryType,city_id,cityType,version);
		if (queryCount % num_splitPage == 0){
			maxPage_splitPage = queryCount / num_splitPage;
		}else{
			maxPage_splitPage = queryCount / num_splitPage + 1;
		}
		
		return "stb_dev_list";
	}
	
	/**
	 * 导出统计数据
	 */
	public String getExcel() 
	{
		logger.warn("getExcel({},{},{})",city_id,queryType,queryTime);
		
		if (StringUtil.IsEmpty(queryTime)) {
			data = bio.countResultByCity(queryType,city_id,cityList);
		}else{
			data=bio.stringToList(queryTime,queryType);
		}
		
		if(StringUtil.IsEmpty(city_id) || "-1".equals(city_id)){
			city_id="00";
		}
		cityList = CityDAO.getNextCityListByCityPid(city_id);
		
		title = new String[cityList.size()+2];
		if("apk".equals(queryType)){
			fileName="机顶盒按APK版本统计";
			title[0]="APK版本";
		}else if("epg".equals(queryType)){
			fileName="机顶盒按EPG版本统计";
			title[0]="EPG版本";
		}
		
		for(int i=0;i<cityList.size();i++){
			title[i+1]=StringUtil.getStringValue(cityList.get(i),"city_name");
		}
		title[cityList.size()+1]="小计";
		
		column = new String[cityList.size()+2];
		column[0]="version";
		for(int i=0;i<cityList.size();i++){
			column[i+1]=StringUtil.getStringValue(cityList.get(i),"city_id");
		}
		column[cityList.size()+1]="total_num";
		
		return "excel";
	}
	
	

	public Map getSession() {
		return session;
	}

	public void setSession(Map session) {
		this.session = session;
	}

	public List<Map<String, String>> getCityList() {
		return cityList;
	}

	public void setCityList(List<Map<String, String>> cityList) {
		this.cityList = cityList;
	}

	public List<Map<String,String>> getData() {
		return data;
	}

	public void setData(List<Map<String,String>> data) {
		this.data = data;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public int getQueryCount() {
		return queryCount;
	}

	public void setQueryCount(int queryCount) {
		this.queryCount = queryCount;
	}

	public StbCountReportBIO getBio() {
		return bio;
	}

	public void setBio(StbCountReportBIO bio) {
		this.bio = bio;
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

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public List<Map> getDevList() {
		return devList;
	}

	public void setDevList(List<Map> devList) {
		this.devList = devList;
	}

	public String getCity_id() {
		return city_id;
	}

	public void setCity_id(String city_id) {
		this.city_id = city_id;
	}

	public String getAjax() {
		return ajax;
	}

	public void setAjax(String ajax) {
		this.ajax = ajax;
	}

	public String getQueryType() {
		return queryType;
	}

	public void setQueryType(String queryType) {
		this.queryType = queryType;
	}

	public String getCityType() {
		return cityType;
	}

	public void setCityType(String cityType) {
		this.cityType = cityType;
	}

	public String getQueryTime() {
		return queryTime;
	}

	public void setQueryTime(String queryTime) {
		this.queryTime = queryTime;
	}

	
	public List<HashMap<String, String>> getCityListOrder()
	{
		return cityListOrder;
	}

	
	public void setCityListOrder(List<HashMap<String, String>> cityListOrder)
	{
		this.cityListOrder = cityListOrder;
	}

	

	
	
}
