package com.linkage.module.gwms.report.act;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.report.bio.TerminalNoMatchReportBIO;

import action.splitpage.splitPageAction;



public class TerminalNoMatchReportACT extends splitPageAction implements
		ServletRequestAware {

	private static final long serialVersionUID = 1891651756165L;
	Logger logger = LoggerFactory.getLogger(TerminalNoMatchReportACT.class);

	private TerminalNoMatchReportBIO bio;
	private HttpServletRequest request;
	
	/** 地市集合 **/
	private List<Map<String,String>> cityList;
	private String cityId = "";
	private String gwType = "";
	private String fileName = "";
	private String[] title ;
	private String[] column ;
	private List<Map> data;
	private List<Map> devList = null;
	private List<Map<String,String>> oldDevDetailList = null;
	private List<Map<String,String>> pt921gDevDetailList = null;
	private List<Map<String,String>> thrdDevDetailList = null;
	private int needCount = 0;
	/**
	 * 老版需求
	 * @return
	 */
	public String init(){
		
		if(null==this.cityId || "".equals(this.cityId)){
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
		
		this.data = bio.getData(cityId);
		return "init";
	}
	
	/**
	 * 终端不匹配init
	 * @return
	 */
	public String initDevMisMath(){
		if(StringUtil.IsEmpty(cityId)){
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
		cityList = CityDAO.getNextCityListByCityPidCore(cityId);
		this.data = bio.getDevMisMtch(cityId);
		return "devMisMatch";
	}
	
	/**
	 * 终端不匹配
	 * @return
	 */
	public String devMisMtch(){
		this.data = bio.getDevMisMtch(cityId);
		return "devMisMatch";
	}
	
	/**
	 * 老旧终端更换init
	 * @return
	 */
	public String initOldChage(){
		if(StringUtil.IsEmpty(cityId)){
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
		cityList = CityDAO.getNextCityListByCityPidCore(cityId);
		this.data = bio.getOldDev(cityId);
		return "devOld";
	}	
	
	/**
	 * 老旧终端更换
	 * @return
	 */
	public String devOldChage(){
		this.data = bio.getOldDev(cityId);
		return "devOld";
	}
	
	
	/**
	 * 第一批终端不匹配详情
	 * @return
	 */
	public String queryNoMatchDetailList(){
		devList = bio.queryNoMatchDetailList(cityId,curPage_splitPage, num_splitPage);
		maxPage_splitPage = bio.queryNoMatchCountDetailList(cityId,curPage_splitPage, num_splitPage);
		return "devMisMathDetail";
	}
	
	/**
	 * 终端能力与套餐不匹配当日完成量
	 * @return
	 */
	public String getNoMatchDayDetailList(){
		devList = bio.getNoMatchDayDetailList(cityId,curPage_splitPage, num_splitPage);
		//maxPage_splitPage = bio.queryNoMatchDayDetailCount(cityId,curPage_splitPage, num_splitPage);
		logger.warn("应要求保存数据一致：needCount : {}",needCount);
		totalRowCount_splitPage = needCount;
		maxPage_splitPage = getMaxPage_splitPage();
		return "devMisMathDayDetail";
	}
	
	/**
	 * 终端不匹配详情导出
	 * @return
	 */
	public String getNoMatchDevForAllExcel(){
		fileName = "不匹配终端详单";
		title = new String[] { "营业区", "LOID", "宽带账号", "厂家", "型号", "是否支持千兆","是否是天翼网关","备注"};
		column = new String[] { "city_name", "loid", "pppoe_name","vendor_add", "device_model", "gigabit_port","device_version_type","remark"};
		data = bio.queryNoMatchDetailListExcel(cityId);
		return "excel";
	}
	
	/**
	 * 终端不匹配日完成量详情导出
	 * @return
	 */
	public String getNoMatchDayCompltExcel(){
		fileName = "不匹配终端日完成量详单";
		title = new String[] { "营业区", "LOID", "宽带账号", "厂家", "型号", "是否支持千兆","是否是天翼网关","备注"};
		column = new String[] { "city_name", "loid", "pppoe_name","vendor_add", "device_model", "gigabit_port","device_version_type","remark"};
		data = bio.getNoMatchDayCompltExcel(cityId);
		return "excel";
	}
	
	/**
	 * 第一批新增终端详情
	 * @return
	 */
	public String queryDetailList(){
		devList = bio.queryDetailList(cityId,curPage_splitPage, num_splitPage);
		maxPage_splitPage = bio.queryCountDetailList(cityId,curPage_splitPage, num_splitPage);
		return "detailList";
	}
	
	public String getDevForAllExcel(){
		fileName = "设备列表";
		title = new String[] { "营业区", "LOID", "宽带账号", "设备标识码", "厂家", "型号", "绑定时间"};
		column = new String[] { "city_name", "loid", "username","device_name", "vendor_add", "device_model", "binddate"};
		data = bio.queryDetailListExcel(cityId);
		return "excel";
	}
	
	
	public String querySrcapDevDetailList(){
		devList = bio.querySrcapDevDetailList(cityId,curPage_splitPage, num_splitPage);
		maxPage_splitPage = bio.querySrcapDevCountDetailList(cityId,curPage_splitPage, num_splitPage);
		return "detailList2";
	}
	
	public String getSrcapDevForAllExcel(){
		fileName = "报废终端再次上线详单";
		title = new String[] { "营业区", "LOID", "宽带账号", "厂家", "型号", "是否老旧终端","绑定时间"};
		column = new String[] { "city_name", "loid", "username","vendor_add", "device_model", "isold","binddate"};
		data = bio.querySrcapDetailListExcel(cityId);
		return "excel";
	}

	/**
	 * 第一批获取老旧终端使用情况详情，未完成量
	 * @return
	 */
	public String queryOldDevDetailList(){
		Map<String,Object> result = bio.queryOldDevDetailList(cityId,curPage_splitPage,num_splitPage);
		totalRowCount_splitPage = (Integer) (result.get("total"));
		maxPage_splitPage = getMaxPage_splitPage();
		if(result.get("list") == null){
			oldDevDetailList = null;
		}else {
			oldDevDetailList = (List<Map<String, String>>) result.get("list");
		}
		return "oldDetail";
	}

	/**
	 * 第一批获取老旧终端使用情况详情，未完成量导出
	 * @return
	 */
	public String getOldDevDetailExcel(){
		fileName = "老旧终端更换未完成详情单";
		title = new String[] { "营业区", "LOID", "宽带账号", "厂家", "型号", "最近上线时间"};
		column = new String[] { "cityName", "loid", "username","vendorName", "deviceModelName", "lastOnlineTime"};
		data = bio.queryOldDevDetailExcel(cityId);
		return "excel";
	}
	
	/**
	 * 第一批完成量总量详情
	 * @return
	 */
	public String queryOldCompleteDevDetailList(){
		Map<String,Object> result = bio.queryOldCompleteDevDetailList(cityId,curPage_splitPage,num_splitPage);
		totalRowCount_splitPage = (Integer) (result.get("total"));
		//应现场要求强制要求数量一致
		maxPage_splitPage = getMaxPage_splitPage();
		if(result.get("list") == null){
			oldDevDetailList = null;
		}else {
			oldDevDetailList = (List<Map<String, String>>) result.get("list");
		}
		return "oldDetail1";
	}
	
	/**
	 * 第一批日完成量详情
	 * @return
	 */
	public String queryOldDayCompleteDevDetailList(){
		Map<String,Object> result = bio.queryOldDayCompleteDevDetailList(cityId,curPage_splitPage,num_splitPage);
		//totalRowCount_splitPage = (Integer) (result.get("total"));
		logger.warn("应要求保存数据一致：needCount : {}",needCount);
		totalRowCount_splitPage = needCount;
		maxPage_splitPage = getMaxPage_splitPage();
		if(result.get("list") == null){
			oldDevDetailList = null;
		}else {
			oldDevDetailList = (List<Map<String, String>>) result.get("list");
		}
		return "oldDayDetail1";
	}

	/**
	 * Pt921g 终端未完成量详情
	 * @return
	 */
	public String queryPt921gDetail(){
		Map<String,Object> result = bio.queryPt921gDetail(cityId,curPage_splitPage,num_splitPage);
		totalRowCount_splitPage = (Integer) (result.get("total"));
		maxPage_splitPage = getMaxPage_splitPage();
		if(null == result.get("list")){
			pt921gDevDetailList = null;
		}else {
			pt921gDevDetailList = (List<Map<String, String>>) result.get("list");
		}
		return "pt921gDetail";
	}
	
	/**
	 * 第三批 终端未完成量详情
	 * @return
	 */
	public String queryThrdDetail(){
		Map<String,Object> result = bio.queryThrdDetail(cityId,curPage_splitPage,num_splitPage);
		totalRowCount_splitPage = (Integer) (result.get("total"));
		maxPage_splitPage = getMaxPage_splitPage();
		if(null == result.get("list")){
			thrdDevDetailList = null;
		}else {
			thrdDevDetailList = (List<Map<String, String>>) result.get("list");
		}
		return "thrdDetail";
	}
	
	/**
	 * 第三批 终端未完成量详情导出
	 * @return
	 */
	public String getThrdDetailExcel(){
		fileName = "老旧终端更换完成详情单";
		title = new String[] { "营业区", "LOID", "宽带账号", "厂家", "型号", "最近上线时间","备注"};
		column = new String[] { "cityName", "loid", "username","vendorName", "deviceModelName", "lastOnlineTime","remark"};
		data = bio.getThrdDetailExcel(cityId);
		return "excel";
	}
	
	/**
	 * pt921g 终端未完成量详情导出
	 * @return
	 */
	public String getPt921gDetailExcel(){
		fileName = "pt921g老旧终端总量详情单";
		title = new String[] { "营业区", "LOID", "宽带账号", "厂家", "型号", "最近上线时间","备注"};
		column = new String[] { "cityName", "loid", "username","vendorName", "deviceModelName", "lastOnlineTime","remark"};
		data = bio.getPt921gDetailExcel(cityId);
		return "excel";
	}
	
	/**
	 * 第一批完成量总量详情导出
	 * @return
	 */
	public String getOldCompleteDevDetailExcel(){
		fileName = "老旧终端更换完成详情单";
		title = new String[] { "营业区", "LOID", "宽带账号", "厂家", "型号", "最近上线时间","备注"};
		column = new String[] { "cityName", "loid", "username","vendorName", "deviceModelName", "lastOnlineTime","remark"};
		data = bio.queryOldCompleteDevDetailExcel(cityId);
		return "excel";
	}
	
	/**
	 * 第一批日完成量详情导出
	 * @return
	 */
	public String getOldDayCompleteDevDetailExcel(){
		fileName = "老旧终端更换日完成量详情单";
		title = new String[] { "营业区", "LOID", "宽带账号", "厂家", "型号", "最近上线时间","备注"};
		column = new String[] { "cityName", "loid", "username","vendorName", "deviceModelName", "lastOnlineTime","remark"};
		data = bio.getOldDayCompleteDevDetailExcel(cityId);
		return "excel";
	}
	
	public String getTyDetailCountList(){
		return "detailList";
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

	public TerminalNoMatchReportBIO getBio() {
		return bio;
	}

	public void setBio(TerminalNoMatchReportBIO bio) {
		this.bio = bio;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}


	public String getGwType() {
		return gwType;
	}

	public void setGwType(String gwType) {
		this.gwType = gwType;
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

	public List<Map> getDevList() {
		return devList;
	}

	public void setDevList(List<Map> devList) {
		this.devList = devList;
	}

	public List<Map<String, String>> getOldDevDetailList() {
		return oldDevDetailList;
	}

	public void setOldDevDetailList(List<Map<String, String>> oldDevDetailList) {
		this.oldDevDetailList = oldDevDetailList;
	}

	public List<Map<String,String>> getCityList()
	{
		return cityList;
	}

	public void setCityList(List<Map<String,String>> cityList)
	{
		this.cityList = cityList;
	}

	public List<Map<String,String>> getPt921gDevDetailList()
	{
		return pt921gDevDetailList;
	}

	public void setPt921gDevDetailList(List<Map<String,String>> pt921gDevDetailList)
	{
		this.pt921gDevDetailList = pt921gDevDetailList;
	}

	public List<Map<String,String>> getThrdDevDetailList()
	{
		return thrdDevDetailList;
	}

	public void setThrdDevDetailList(List<Map<String,String>> thrdDevDetailList)
	{
		this.thrdDevDetailList = thrdDevDetailList;
	}

	public int getNeedCount()
	{
		return needCount;
	}

	public void setNeedCount(int needCount)
	{
		this.needCount = needCount;
	}
}
