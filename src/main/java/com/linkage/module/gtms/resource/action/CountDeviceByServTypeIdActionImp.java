package com.linkage.module.gtms.resource.action;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.litms.uss.DateUtil;
import com.linkage.module.gtms.resource.serv.CountDeviceByServTypeIdServ;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

@SuppressWarnings("rawtypes")
public class CountDeviceByServTypeIdActionImp extends splitPageAction implements SessionAware 
{
	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory.getLogger(CountDeviceByServTypeIdActionImp.class);
	
	private Map session = null;
	private List<Map<String,String>> cityList=null;
	private String cityId = null;
	/** 设备类型 */
	private String gw_type = null;
	/** 业务类型  10：宽带业务；11：IPTV业务；14：VOIP业务     */
	private String servTypeId = null;
	private String startTime = "-1";
	private String endTime = "-1";
	/** 1：展示已开通业务详细信息    0：未开通业务详细信息  */
	private String isOpen = null;
	/** 已开通业务统计 */
	private List<Map> haveResultList = null;
	/** 未开通业务统计 */
	private List<Map> haveNotResultList = null;
	/** 详细信息展示 */
	private List<Map> detailResultList = null;
	/** 导出Excel文件名 */
	private String fileName = null;
	/** 导出Excel的标题 */
	private String [] title = null;
	/** 导出Excel的列 */
	private String [] column = null;
	/** 导出Excel的数据*/
	private List<Map> data =null;
	
	private CountDeviceByServTypeIdServ bio;
	
	
	/**
	 * 统计已开通业务量
	 */
	public String countHaveOpenningService()
	{
		logger.debug("action==>countHaveOpenningService()");
		UserRes curUser = (UserRes) session.get("curUser");
		cityId = curUser.getCityId();
		haveResultList = bio.countHaveOpenningService(gw_type, cityId);
		
		return "haveOpen";
	}
	
	/**
	 * 统计家庭网关宽带业务或VOIP业务已开通业务量 查询页
	 * 宁夏特制
	 */
	public String queryCountHaveOpenService()
	{
		cityList = CityDAO.getNextCityListByCityPid("00");
		startTime = setTime(0);
		endTime = setTime(1);
		
		return "queryCountHaveOpenService";
	}
	
	/**
	 * 统计家庭网关宽带业务或VOIP业务已开通业务量
	 * 宁夏特制
	 */
	public String countHaveOpenService()
	{
		logger.debug("countHaveOpenService({},{},{},{},{})",
				gw_type,cityId,servTypeId,startTime,endTime);
		if(StringUtil.IsEmpty(cityId) || "-1".equals(cityId)){
			cityId="00";
		}
		
		haveResultList = bio.countHaveOpenService(gw_type,cityId,servTypeId,
									getLongTime(startTime),getLongTime(endTime),"count");
		
		return "queryCountHaveOpenServiceResult";
	}
	
	/**
	 * 统计家庭网关宽带业务或VOIP业务已开通业务量数据导出
	 * 宁夏特制
	 */
	public String toExcel() 
	{
		fileName = "家庭网关已开通业务统计";
		if("10,14".equals(servTypeId)){
			title = new String[] {"属地","家庭网关宽带业务","家庭网关VOIP业务"};
			column = new String[] {"city_name", "internetValue", "voipValue" };
		}else if("10".equals(servTypeId)){
			title = new String[] {"属地","家庭网关宽带业务"};
			column = new String[] {"city_name", "internetValue"};
		}else if("14".equals(servTypeId)){
			title = new String[] {"属地","家庭网关VOIP业务"};
			column = new String[] {"city_name","voipValue" };
		}if("11,14".equals(servTypeId)){
			title = new String[] {"属地","家庭网关单VOIP业务"};
			column = new String[] {"city_name","voipValue" };
		}
		
		data = bio.countHaveOpenService(gw_type,cityId,servTypeId,
											getLongTime(startTime),getLongTime(endTime),"toExcel");
		
		return "excel";
	}
	
	/**
	 * 统计未开通业务量
	 */
	public String countHaveNotOpenningService()
	{
		logger.debug("action==>countHaveNotOpenningService()");
		UserRes curUser = (UserRes) session.get("curUser");
		cityId = curUser.getCityId();
		haveNotResultList = bio.countHaveNotOpenningService(gw_type, cityId);
		
		return "haveNotOpen";
	}
	
	/**
	 * 已开通业务统计数据导出到Excel
	 */
	public String outputHaveOpenServiceToExcel() 
	{
		logger.debug("action==>outputHaveOpenServiceToExcel()");
		
		if ("1".equals(gw_type)) {
			fileName = "家庭网关已开通业务统计";
			title = new String[] { "属地", "家庭网关宽带业务", "家庭网关IPTV业务", "家庭网关VOIP业务"};
		}else {
			fileName = "企业网关已开通业务统计";
			title = new String[] { "属地", "企业网关宽带业务", "企业网关IPTV业务", "企业网关VOIP业务"};
		}
		
		column = new String[] { "city_name", "internetValue", "iptvValue", "voipValue" };
		data = bio.countHaveOpenningService(gw_type, cityId);
		
		return "excel";
	}
	
	public String outputHaveNotOpenServiceToExcel() 
	{
		logger.debug("action==>outputHaveNotOpenServiceToExcel()");
		
		if ("1".equals(gw_type)) {
			fileName = "家庭网关未开通业务统计";
			title = new String[] { "属地", "家庭网关宽带业务", "家庭网关IPTV业务", "家庭网关VOIP业务"};
		}else {
			fileName = "企业网关未开通业务统计";
			title = new String[] { "属地", "企业网关宽带业务", "企业网关IPTV业务", "企业网关VOIP业务"};
		}
		
		column = new String[] { "city_name", "internetValue", "iptvValue", "voipValue" };
		data = bio.countHaveNotOpenningService(gw_type, cityId);
		
		return "excel";
	}
	
	/**
	 * 详细信息展示
	 */
	public String getDetail()
	{
		logger.debug("getDetail({},{},{},{},{},{})",
				cityId,gw_type,servTypeId,isOpen,startTime,endTime);
		
		detailResultList = bio.getDetail(cityId,gw_type,servTypeId.replaceAll(" ",""),isOpen,curPage_splitPage,num_splitPage,
									getLongTime(startTime),getLongTime(endTime));
		maxPage_splitPage = bio.getCount(cityId,gw_type,servTypeId.replaceAll(" ",""),isOpen,curPage_splitPage,num_splitPage,
									getLongTime(startTime),getLongTime(endTime));
		
		return "detail";
	}
	
	/**
	 * 详细信息导出
	 */
	public String getDetailExcel()
	{
		logger.debug("getDetailExcel({},{},{},{},{},{})",
				cityId,gw_type,servTypeId,isOpen,startTime,endTime);
		
		if ("1".equals(gw_type)) {
			fileName = "家庭网关设备信息导出";
		}else {
			fileName = "企业网关设备信息导出";
		}
		
		title = new String[] { "设备厂商", "型号", "软件版本", "属地", "设备序列号"};
		column = new String[] { "vendor_name", "device_model", "softwareversion",
				"city_name", "device_serialnumber" };
		data = bio.getDetailExcel(cityId, gw_type, servTypeId, isOpen,
							getLongTime(startTime),getLongTime(endTime));
		
		return "excel";
	}
	
	/**
	 * 时间转化
	 */
	private long getLongTime(String time)
	{
		if (StringUtil.IsEmpty(time) || "-1".equals(time)){
			return -1;
		}
		
		DateTimeUtil dt = new DateTimeUtil(time);
		return dt.getLongTime();
	}
	
	/**
	 * 获取当前时间或本月的初始时间
	 */
	private String setTime(int i)
	{
		if(i==0){
			return DateUtil.firstDayOfCurrentMonth("yyyy-MM-dd HH:mm:ss");
		}else{
			Calendar now = Calendar.getInstance();
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(now.getTime());
		}
	}
	
	
	public String getGw_type() {
		return gw_type;
	}
	
	public void setGw_type(String gw_type) {
		this.gw_type = gw_type;
	}
	
	public List<Map> getHaveResultList() {
		return haveResultList;
	}

	public void setHaveResultList(List<Map> haveResultList) {
		this.haveResultList = haveResultList;
	}
	
	public List<Map> getHaveNotResultList() {
		return haveNotResultList;
	}
	
	public void setHaveNotResultList(List<Map> haveNotResultList) {
		this.haveNotResultList = haveNotResultList;
	}
	
	public Map getSession() {
		return session;
	}
	
	public void setSession(Map session) {
		this.session = session;
	}
	
	public String getCityId() {
		return cityId;
	}
	
	public void setCityId(String cityId) {
		this.cityId = cityId;
	}
	
	public String getServTypeId() {
		return servTypeId;
	}
	
	public void setServTypeId(String servTypeId) {
		this.servTypeId = servTypeId;
	}
	
	public String getIsOpen() {
		return isOpen;
	}
	
	public void setIsOpen(String isOpen) {
		this.isOpen = isOpen;
	}
	
	public List<Map> getDetailResultList() {
		return detailResultList;
	}
	
	public void setDetailResultList(List<Map> detailResultList) {
		this.detailResultList = detailResultList;
	}
	
	public String getFileName() {
		return fileName;
	}
	
	public void setFileName(String fileName) {
		this.fileName = fileName;
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

	public CountDeviceByServTypeIdServ getBio() {
		return bio;
	}
	
	public void setBio(CountDeviceByServTypeIdServ bio) {
		this.bio = bio;
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

	public List<Map<String, String>> getCityList() {
		return cityList;
	}

	public void setCityList(List<Map<String, String>> cityList) {
		this.cityList = cityList;
	}
	
}
