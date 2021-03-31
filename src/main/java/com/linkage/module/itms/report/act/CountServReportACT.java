
package com.linkage.module.itms.report.act;

import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.itms.report.bio.CountServReportBIO;

/**
 * 按开始时间、结束时间、设备属地、用户业务类型、是否活跃统计
 * 
 * @author wanghong5 2015-02-03
 */
@SuppressWarnings("unchecked")
public class CountServReportACT extends splitPageAction implements SessionAware{
	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(CountServReportACT.class);
	// session
	private Map session;
	/** 开始时间 */
	private String starttime = "";
	/** 结束时间 */
	private String endtime = "";
	/** 属地列表 */
	private List<Map<String, String>> cityList = null;
	/** 属地ID(城市) */
	private String cityId = "";
	/** 属地ID（城市及下属地） */
	private String city_id = "";
	/** 业务ID */
	private String specId = "";
	/** 是否活跃 */
	private String is_active = "";
	/** 规格列表 */
	private List<Map<String, String>> specList = null;
	
	// 导出数据
	private List<Map> data;
	// 导出文件列标题
	private String[] title;
	// 导出文件列
	private String[] column;
	// 导出文件名
	private String fileName;
	
	String serv_type_id;
	private CountServReportBIO countServReportBio;
	/*
	 * 按城市、始末时间、业务类型和活跃度统计用户详细报表
	 */
	private List<Map> details_list = null;
	private List<Map<String, String>> titleList;
	private List<Map<String, String>> countList;
	private List<Map<String, String>> countListByType;
	
	/*
	 * 访问首页，获取限制参数（时间段，属地，业务类型，活跃度）
	 */
	public String init(){
		logger.warn("CountServReportACT.init()--start");
		UserRes curUser = (UserRes) session.get("curUser");
		cityId = curUser.getCityId();
		DateTimeUtil dt = new DateTimeUtil();
		endtime = dt.getDate();
		starttime = dt.getFirtDayOfMonth();
		dt = new DateTimeUtil(endtime);
		long end_time = dt.getLongTime();
		dt = new DateTimeUtil((end_time + 24 * 3600 - 1) * 1000);
		endtime = dt.getLongDate();
		dt = new DateTimeUtil(starttime);
		starttime = dt.getLongDate();
		cityList = CityDAO.getNextCityListByCityPid(curUser.getCityId());
		specList=countServReportBio.getCountServReportDao().getSpecList();
		
		return "init";
	}
	
	/*
	 * 按城市、始末时间、业务类型和活跃度统计用户报表
	 */
	public String getcountAll(){
		logger.warn("CountServReportACT.getcountAll()--start");
		data = countServReportBio.countAll(cityId,starttime,endtime,specId,is_active);
		return "list";
	}
	
	/*
	 * 按城市、始末时间、业务类型和活跃度统计用户详细报表
	 */
	public String getDetails(){
		logger.warn("CountServReportACT.getDetails()--start");
		details_list = countServReportBio.getDetailsForPage(city_id,starttime, endtime,specId,is_active,serv_type_id, curPage_splitPage, num_splitPage);
		maxPage_splitPage = countServReportBio.getDetailsCount(city_id,starttime, endtime, 
				specId,is_active, serv_type_id, num_splitPage);
		return "details_list";
	}
	
	/*
	 * 分页功能
	 */
	public String goPage() throws Exception {
		logger.warn("CountServReportACT.goPage()--start");
		details_list = countServReportBio.getDetailsForPage(city_id,starttime, endtime,specId,is_active,serv_type_id, curPage_splitPage, num_splitPage);
		maxPage_splitPage = countServReportBio.getDetailsCount(city_id,starttime, endtime, 
				specId,is_active, serv_type_id, num_splitPage);
		return "details_list";
	}
	
	/*
	 * 导出属地报表
	 */
	public String getCountExcel(){
		logger.warn("CountServReportACT.getCountExcel()--start");
		is_active=is_active+0;
		fileName = "属地列表";
		title = new String[]{ "本地网", "总用户数", "宽带用户", "IPTV用户", "VOIP用户","宽带占比","IPTV占比","VOIP占比"};;
		column = new String[]{ "city_name", "totalNum", "kuandaiNum", "IPTVNum",
				"VOIPNum","kuandai_scale","IPTV_scale","VOIP_scale"};
		data = countServReportBio.countAll(cityId,starttime,endtime,specId,is_active);
		return "excel";
	}
	
	/*
	 * 导出用户详细报表
	 */
	public String getDetailsExcel(){
		logger.warn("CountServReportACT.getDetailsExcel()--start");
		fileName = "用户列表";
		title = new String[] { "设备序列号", "LOID", "受理时间", "业务名称", "业务账号"};
		column = new String[] { "deviceNum", "LOID", "dealdate", "operationName",
				"operationNum"};
		data = countServReportBio.getDetailsExcel(city_id,starttime, endtime,  specId,
				is_active,serv_type_id);
		return "excel";
	}
	
	public CountServReportACT(){
		
	}
	
	public Map getSession(){
		return session;
	}

	public void setSession(Map session){
		this.session = session;
	}

	public String getStarttime(){
		return starttime;
	}

	public void setStarttime(String starttime){
		this.starttime = starttime;
	}

	public List<Map<String, String>> getCityList(){
		return cityList;
	}

	public void setCityList(List<Map<String, String>> cityList){
		this.cityList = cityList;
	}

	public String getCityId(){
		return cityId;
	}

	public void setCityId(String cityId){
		this.cityId = cityId;
	}

	public String getSpecId() {
		return specId;
	}
	
	public void setSpecId(String specId) {
		this.specId = specId;
	}
	
	public String getIs_active() {
		return is_active;
	}
	
	public void setIs_active(String is_active) {
		this.is_active = is_active;
	}
	 
	public List<Map> getData(){
		return data;
	}

	public void setData(List<Map> data){
		this.data = data;
	}

	public String[] getTitle(){
		return title;
	}

	public void setTitle(String[] title){
		this.title = title;
	}

	public String[] getColumn(){
		return column;
	}

	public void setColumn(String[] column){
		this.column = column;
	}

	public String getFileName(){
		return fileName;
	}

	public void setFileName(String fileName){
		this.fileName = fileName;
	}

	public List<Map> getDetails_list() {
		return details_list;
	}
	
	public void setDetails_list(List<Map> details_list) {
		this.details_list = details_list;
	}
	
	public String getEndtime(){
		return endtime;
	}

	public void setEndtime(String endtime){
		this.endtime = endtime;
	}

	public List<Map<String, String>> getTitleList(){
		return titleList;
	}

	public void setTitleList(List<Map<String, String>> titleList){
		this.titleList = titleList;
	}
	
	public List<Map<String, String>> getSpecList() {
		return specList;
	}
	
	public void setSpecList(List<Map<String, String>> specList) {
		this.specList = specList;
	}	
	
	public CountServReportBIO getCountServReportBio() {
		return countServReportBio;
	}
	
	public void setCountServReportBio(CountServReportBIO countServReportBio) {
		this.countServReportBio = countServReportBio;
	}
	
	public List<Map<String, String>> getCountList() {
		return countList;
	}
	
	public void setCountList(List<Map<String, String>> countList) {
		this.countList = countList;
	}
	
	public List<Map<String, String>> getCountListByType() {
		return countListByType;
	}
	
	public void setCountListByType(List<Map<String, String>> countListByType) {
		this.countListByType = countListByType;
	}
	
	public String getCity_id() {
		return city_id;
	}
	
	public void setCity_id(String city_id) {
		this.city_id = city_id;
	}
	
	public String getServ_type_id() {
		return serv_type_id;
	}
	
	public void setServ_type_id(String serv_type_id) {
		this.serv_type_id = serv_type_id;
	}

	

}
