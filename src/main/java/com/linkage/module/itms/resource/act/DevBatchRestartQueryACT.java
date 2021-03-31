package com.linkage.module.itms.resource.act;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.WebUtil;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.litms.system.UserRes;
import com.linkage.litms.uss.DateUtil;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.itms.resource.bio.DevBatchRestartQueryBIO;
import com.sun.tools.hat.internal.server.HttpReader;

/**
 * @author Administrator (Ailk No.)
 * @version 1.0
 * @since 2013-10-31
 * @category com.linkage.module.itms.resource.act
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
@SuppressWarnings({"rawtypes","unused"})
public class DevBatchRestartQueryACT extends splitPageAction 
implements ServletRequestAware, SessionAware
{
	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory.getLogger(DevBatchRestartQueryACT.class);
	
	private HttpServletRequest request;
	private Map session;
	// 开始时间
	private String startOpenDate = "";
	private String startOpenDate1 = "";
	// 结束时间
	private String endOpenDate = "";
	// 结束时间
	private String endOpenDate1 = "";
	// 属地
	private String city_id = null;
	// 属地
	private String gwShare_cityId = null;
	// 厂商
	private String gwShare_vendorId = null;
	// 型号
	private String gwShare_deviceModelId = null;
	// 版本
	private String gwShare_devicetypeId = null;
	/**loid */
	private String loid=null;
	private String device_serialnumber=null;
	private String serv_account=null;

	private List<Map> data; // 需要导出的结果集，必须是处理过的可以直接显示的结果
	private String[] column; // 需要导出的列名，对应data中的键值
	private String[] title; // 显示在导出文档上的列名
	private String fileName; // 导出的文件名（不包括后缀名）

	// 查询出批量重启信息列表
	private List<Map> restartDevMap;
	// 厂商版本型号大小
	private Map<String,Integer> sizeMap;

	private String gw_type;
	private DevBatchRestartQueryBIO bio;
	private String instArea=Global.instAreaShortName;

	@Override
	public String execute() throws Exception 
	{
		UserRes curUser = WebUtil.getCurrentUser();
		city_id = curUser.getCityId();
		
		if(Global.NXDX.equals(instArea)){
			startOpenDate=DateUtil.firstDayOfCurrentMonth("yyyy-MM-dd");
		}else{
			startOpenDate=DateUtil.getNowTime("yyyy-MM-dd");
		}
		endOpenDate = DateUtil.getNowTime("yyyy-MM-dd");
		
		return "init";
	}

	public String devBatchRestartQueryInfo() 
	{
		logger.debug("devBatchRestartQueryInfo({},{},{},{},{},{})",
				city_id,gwShare_vendorId, gwShare_deviceModelId, gwShare_devicetypeId,
				loid,device_serialnumber);
		if(!StringUtil.IsEmpty(loid)){
			loid=loid.trim();
		}
		if(!StringUtil.IsEmpty(device_serialnumber)){
			device_serialnumber=device_serialnumber.trim();
		}
		if(!StringUtil.IsEmpty(serv_account)){
			serv_account=serv_account.trim();
		}
		
		setTime();
		restartDevMap = bio.devBatchRestartQueryInfo(city_id, startOpenDate1,
				endOpenDate1, curPage_splitPage, num_splitPage, gw_type, gwShare_cityId,
				gwShare_vendorId, gwShare_deviceModelId, gwShare_devicetypeId,
				loid,device_serialnumber,serv_account);
		maxPage_splitPage = bio.countDevBatchRestartQueryInfo(city_id,
				startOpenDate1, endOpenDate1, curPage_splitPage, num_splitPage,
				gw_type,gwShare_cityId,gwShare_vendorId,gwShare_deviceModelId,
				gwShare_devicetypeId,loid,device_serialnumber,serv_account);

		return "list";
	}

	public String devBatchRestartQueryExcel() 
	{
		logger.debug("devBatchRestartQueryExcel({},{},{},{},{},{})",
				city_id,gwShare_vendorId, gwShare_deviceModelId, gwShare_devicetypeId,
				loid,device_serialnumber,serv_account);
		if(!StringUtil.IsEmpty(loid)){
			loid=loid.trim();
		}
		if(!StringUtil.IsEmpty(device_serialnumber)){
			device_serialnumber=device_serialnumber.trim();
		}
		if(!StringUtil.IsEmpty(serv_account)){
			serv_account=serv_account.trim();
		}
		
		setTime();
		restartDevMap = bio.devBatchRestartQueryExcel(city_id, startOpenDate1,
				endOpenDate1, gw_type,gwShare_cityId,gwShare_vendorId,
				gwShare_deviceModelId,gwShare_devicetypeId,loid,device_serialnumber,serv_account);
		
		String excelCol = "task_id#vendor_name#device_model#softwareversion" +
				"#city_name#device_serialnumber#add_time#restart_status#restart_time";
		String excelTitle = "任务ID#厂家#型号#版本#属地#设备序列号#任务制定时间#重启结果#重启时间";
		if(Global.NXDX.equals(Global.instAreaShortName)){
			if (!"1".equals(gw_type)) {
				excelCol = "serv_account#vendor_name#device_model#softwareversion#city_name" +
						"#device_serialnumber#restart_status#restart_time";
				excelTitle = "业务账号#厂家#型号#版本#属地#设备序列号#重启结果#重启时间";
			}else {
				excelCol = "loid#vendor_name#device_model#softwareversion#city_name" +
						"#device_serialnumber#restart_status#restart_time";
				excelTitle = "LOID#厂家#型号#版本#属地#设备序列号#重启结果#重启时间";
			}
		}
		
		column = excelCol.split("#");
		title = excelTitle.split("#");
		fileName = "devBatchRestartMap";
		data = restartDevMap;
		
		return "excel";
	}
	
	/**
	 * 初始化分组统计厂家、型号、版本、光猫/机顶盒、数量
	 */
	public String initStat() 
	{
		if(Global.NXDX.equals(instArea)){
			city_id=null;
			startOpenDate=DateUtil.firstDayOfCurrentMonth("yyyy-MM-dd");
		}else{
			UserRes curUser = WebUtil.getCurrentUser();
			city_id = curUser.getCityId();
			startOpenDate=DateUtil.getNowTime("yyyy-MM-dd");
		}
		endOpenDate = DateUtil.getNowTime("yyyy-MM-dd");
		return "initStat";
	}
	
	/**
	 * 创建分组统计结果
	 */
	public String createResult(HttpServletRequest request)
	{
		gw_type = (String) request.getParameter("gw_type");
		city_id = (String) request.getParameter("city_id");
		startOpenDate = (String) request.getParameter("startOpenDate");
		endOpenDate = (String) request.getParameter("endOpenDate");
		//opera 操作 toExcel:导出excel
		String opera=(String) request.getParameter("opera");
		
		if(Global.NXDX.equals(instArea)){
			gwShare_vendorId = (String) request.getParameter("gwShare_vendorId");
			gwShare_deviceModelId = (String) request.getParameter("gwShare_deviceModelId");
			gwShare_devicetypeId = (String) request.getParameter("gwShare_devicetypeId");
		}else{
			gwShare_vendorId=null;
			gwShare_deviceModelId=null;
			gwShare_devicetypeId=null;
		}
		
		setTime();
		if(bio==null){
			bio=new DevBatchRestartQueryBIO();
		}
		
		return bio.getTableString(city_id,startOpenDate1,endOpenDate1,gw_type,gwShare_vendorId,
				gwShare_deviceModelId,gwShare_devicetypeId,startOpenDate,endOpenDate,opera);
	}

	public String detail()
	{
		logger.debug("detail  "+gw_type+","+startOpenDate1+","+endOpenDate1
						+","+gwShare_vendorId+","+gwShare_deviceModelId+","+gwShare_devicetypeId);
		if(startOpenDate1!=null && "null".equals(startOpenDate1.toLowerCase())){
			startOpenDate1=null;
		}
		if(endOpenDate1!=null && "null".equals(endOpenDate1.toLowerCase())){
			endOpenDate1=null;
		}
		if(Global.NXDX.equals(instArea)){
			data=bio.getDetail4NX(gw_type,startOpenDate1,endOpenDate1,gwShare_vendorId,
					gwShare_deviceModelId,gwShare_devicetypeId,curPage_splitPage,num_splitPage,city_id);
			maxPage_splitPage=bio.count4NX(gw_type, startOpenDate1, endOpenDate1, gwShare_vendorId,
					gwShare_deviceModelId, gwShare_devicetypeId, num_splitPage,city_id);
		}else {
			data=bio.getDetail(gw_type,startOpenDate1,endOpenDate1,gwShare_vendorId,
					gwShare_deviceModelId,gwShare_devicetypeId,curPage_splitPage,num_splitPage);
			maxPage_splitPage=bio.count(gw_type, startOpenDate1, endOpenDate1, gwShare_vendorId,
					gwShare_deviceModelId, gwShare_devicetypeId, num_splitPage);
		}

		return "detail";
	}
	
	public String toDetailExcel()
	{
		logger.debug("toDetailExcel:"+gw_type+","+startOpenDate1+","+endOpenDate1
						+","+gwShare_vendorId+","+gwShare_deviceModelId+","+gwShare_devicetypeId);
		if(startOpenDate1!=null && "null".equals(startOpenDate1.toLowerCase())){
			startOpenDate1=null;
		}
		if(endOpenDate1!=null && "null".equals(endOpenDate1.toLowerCase())){
			endOpenDate1=null;
		}
		if(Global.NXDX.equals(instArea)){
			data=bio.getDetail4NX(gw_type,startOpenDate1,endOpenDate1,gwShare_vendorId,
					gwShare_deviceModelId,gwShare_devicetypeId,-1,-1,city_id);
		}else {
			data=bio.getDetail(gw_type,startOpenDate1,endOpenDate1,gwShare_vendorId,
					gwShare_deviceModelId,gwShare_devicetypeId,-1,-1);
		}

		column = new String[]{"vendor_name","device_model","softwareversion","city_name",
								"device_serialnumber","loid","restart_status"};
		if ("4".equals(gw_type)) {
			title = new String[]{"厂商","型号","软件版本","属地","设备序列号","业务账号","重启结果"};
		}else {
			title = new String[]{"厂商","型号","软件版本","属地","设备序列号","LOID","重启结果"};
		}
		fileName = "devBatchRestartDeatil";
		
		return "excel";
	}

	/**
	 * 初始化分组统计厂家、型号、版本、光猫/机顶盒、数量
	 */
	public String initNotStat()
	{
		city_id=null;
		startOpenDate=DateUtil.firstDayOfCurrentMonth("yyyy-MM-dd");
		endOpenDate = DateUtil.getNowTime("yyyy-MM-dd");
		return "initNoRestartStat";
	}

	/**
	 * NXDX-REQ-ITMS-20201104-LX-001 家庭网关持续在线30天未重启的终端进行重启优化
	 * 宁夏读取大数据分析不可重启设备统计列表
	 *
	 */
	public String createNotRestartResult(HttpServletRequest request)
	{
		logger.warn("begin createNotRestartResult...");
		gw_type = (String) request.getParameter("gw_type");
		city_id = (String) request.getParameter("city_id");
		startOpenDate = (String) request.getParameter("startOpenDate");
		endOpenDate = (String) request.getParameter("endOpenDate");
		//opera 操作 toExcel:导出excel
		String opera=(String) request.getParameter("opera");

		gwShare_vendorId = (String) request.getParameter("gwShare_vendorId");
		gwShare_deviceModelId = (String) request.getParameter("gwShare_deviceModelId");
		gwShare_devicetypeId = (String) request.getParameter("gwShare_devicetypeId");

		setTime();
		if(bio==null){
			bio=new DevBatchRestartQueryBIO();
		}

		return bio.getTableString4NX(city_id,startOpenDate1,endOpenDate1,gw_type,gwShare_vendorId,
				gwShare_deviceModelId,gwShare_devicetypeId,startOpenDate,endOpenDate,opera);
	}

	/**
	 * 宁夏获取未重启设备详情
	 * @return
	 */
	public String detail4NoResult()
	{
		logger.debug("detail4NoResult  "+gw_type+","+startOpenDate1+","+endOpenDate1
				+","+gwShare_vendorId+","+gwShare_deviceModelId+","+gwShare_devicetypeId);
		if(startOpenDate1!=null && "null".equals(startOpenDate1.toLowerCase())){
			startOpenDate1=null;
		}
		if(endOpenDate1!=null && "null".equals(endOpenDate1.toLowerCase())){
			endOpenDate1=null;
		}
		data=bio.getDetailNoRestart(gw_type,startOpenDate1,endOpenDate1,gwShare_vendorId,
				gwShare_deviceModelId,gwShare_devicetypeId,curPage_splitPage,num_splitPage,city_id);
		maxPage_splitPage=bio.countNoRestart(gw_type, startOpenDate1, endOpenDate1, gwShare_vendorId,
				gwShare_deviceModelId, gwShare_devicetypeId, num_splitPage,city_id);

		return "noRestartDetail";
	}

	public String toNotRestartDetailExcel()
	{
		logger.debug("toNotRestartDetailExcel:"+gw_type+","+startOpenDate1+","+endOpenDate1
				+","+gwShare_vendorId+","+gwShare_deviceModelId+","+gwShare_devicetypeId);
		if(startOpenDate1!=null && "null".equals(startOpenDate1.toLowerCase())){
			startOpenDate1=null;
		}
		if(endOpenDate1!=null && "null".equals(endOpenDate1.toLowerCase())){
			endOpenDate1=null;
		}
		data=bio.getDetailNoRestart(gw_type,startOpenDate1,endOpenDate1,gwShare_vendorId,
				gwShare_deviceModelId,gwShare_devicetypeId,-1,-1,city_id);

		column = new String[]{"vendor_name","device_model","softwareversion","city_name",
				"device_serialnumber","loid"};
		if ("4".equals(gw_type)) {
			title = new String[]{"厂商","型号","软件版本","属地","设备序列号","业务账号"};
		}else {
			title = new String[]{"厂商","型号","软件版本","属地","设备序列号","LOID"};
		}
		fileName = "devNotRestartDeatil";

		return "excel";
	}

	

	/**
	 * 时间转化
	 */
	private void setTime() 
	{
		if (StringUtil.IsEmpty(startOpenDate) || "null".equals(startOpenDate.toLowerCase())) {
			startOpenDate1 = null;
		} else {
			String start = startOpenDate + " 00:00:00";
			DateTimeUtil st = new DateTimeUtil(start);
			startOpenDate1 = String.valueOf(st.getLongTime());
		}
		if (StringUtil.IsEmpty(endOpenDate) || "null".equals(endOpenDate.toLowerCase())) {
			endOpenDate1 = null;
		} else {
			String end = endOpenDate + " 23:59:59";
			DateTimeUtil et = new DateTimeUtil(end);
			endOpenDate1 = String.valueOf(et.getLongTime());
		}
	}

	
	
	public List<Map> getRestartDevMap() {
		return restartDevMap;
	}

	public void setRestartDevMap(List<Map> restartDevMap) {
		this.restartDevMap = restartDevMap;
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

	public DevBatchRestartQueryBIO getBio() {
		return bio;
	}

	public void setBio(DevBatchRestartQueryBIO bio) {
		this.bio = bio;
	}

	public String getGw_type() {
		return gw_type;
	}

	public void setGw_type(String gw_type) {
		this.gw_type = gw_type;
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

	public String getStartOpenDate() {
		return startOpenDate;
	}

	public void setStartOpenDate(String startOpenDate) {
		this.startOpenDate = startOpenDate;
	}

	public String getStartOpenDate1() {
		return startOpenDate1;
	}

	public void setStartOpenDate1(String startOpenDate1) {
		this.startOpenDate1 = startOpenDate1;
	}

	public String getEndOpenDate1() {
		return endOpenDate1;
	}

	public void setEndOpenDate1(String endOpenDate1) {
		this.endOpenDate1 = endOpenDate1;
	}

	public String getGwShare_cityId(){
		return gwShare_cityId;
	}

	public void setGwShare_cityId(String gwShare_cityId){
		this.gwShare_cityId = gwShare_cityId;
	}

	public String getGwShare_vendorId(){
		return gwShare_vendorId;
	}

	public void setGwShare_vendorId(String gwShare_vendorId){
		this.gwShare_vendorId = gwShare_vendorId;
	}

	public String getGwShare_deviceModelId(){
		return gwShare_deviceModelId;
	}

	public void setGwShare_deviceModelId(String gwShare_deviceModelId){
		this.gwShare_deviceModelId = gwShare_deviceModelId;
	}

	public String getGwShare_devicetypeId(){
		return gwShare_devicetypeId;
	}

	public void setGwShare_devicetypeId(String gwShare_devicetypeId){
		this.gwShare_devicetypeId = gwShare_devicetypeId;
	}

	public Map<String, Integer> getSizeMap(){
		return sizeMap;
	}

	public void setSizeMap(Map<String, Integer> sizeMap){
		this.sizeMap = sizeMap;
	}

	@Override
	public void setSession(Map<String, Object> session){
		this.session = session;	
	}

	@Override
	public void setServletRequest(HttpServletRequest req){
		this.request = req;		
	}

	public String getLoid() {
		return loid;
	}

	public void setLoid(String loid) {
		this.loid = loid;
	}

	public String getDevice_serialnumber() {
		return device_serialnumber;
	}

	public void setDevice_serialnumber(String device_serialnumber) {
		this.device_serialnumber = device_serialnumber;
	}

	public String getServ_account() {
		return serv_account;
	}

	public void setServ_account(String serv_account) {
		this.serv_account = serv_account;
	}

	

}
