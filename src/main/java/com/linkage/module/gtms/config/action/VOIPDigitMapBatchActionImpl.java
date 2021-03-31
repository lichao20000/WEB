package com.linkage.module.gtms.config.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletResponseAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gtms.config.serv.VOIPDigitMapBatchServ;

public class VOIPDigitMapBatchActionImpl extends splitPageAction implements VOIPDigitMapBatchAction, ServletResponseAware {

		// 日志记录
		private static Logger logger = LoggerFactory.getLogger(VOIPDigitMapBatchActionImpl.class);
		// bio
		private VOIPDigitMapBatchServ bio;
		private Map session;
		/**
		 * 所有树图配置列表
		 */
		private List<Map> digitMapList;
		/**
		 * 当前操作的用户
		 */
		private UserRes current_user;
		private String ajax = null;
		private Map digitMap = new HashMap();
		private String map_id = null;
		private String map_name = null;
		private String map_content = null;
		private String device_id = null;
		/**区分ITMS和BBMS的功能*/
		private String gw_type ="";
		/**区分数图模板*/
		private String flag ="1";
		// 任务名称
		private String taskName = null; 
		
		/** 属地ID */
		private String cityId = "-1";
		private String caseDownload;
		private HttpServletResponse response;
		private String starttime;
		private String endtime;
		/**条件*/
		private String con="";
		/**条件内容*/
		private String condition="";
		/**开通状态*/
		private String openState="";
		/**获取device_id*/
		private List<Map> device_idList=null;
		/**绑定数据*/
		private List<Map> list=null;
		
		/**
		 * 数图配置下发  加载数图模板
		 * 
		 * @return
		 */
		public String init(){
				digitMapList = bio.queryAllDigitMap(flag);
			return "init";
		}
		/**
		 * 批量设备 下发数图配置
		 * 
		 * @return
		 */
		public String doConfigAll(){
			logger.debug("addToStrategy({},{})", new Object[] { this.device_id, this.map_id });
		    String[] paramArr = new String[1];
		    String serviceId = "7";
		    String[] deviceIds = this.device_id.split(",");

		    this.map_content = this.bio.queryDigitMapById(this.map_id, this.flag);
		    paramArr[0] = this.map_content;

		    this.ajax = this.bio.doConfigAll(deviceIds, serviceId, paramArr, this.gw_type);
		    return "ajax";
		}
		/**
		 * 下载模板文件
		 * 
		 * @return
		 */
		
		public String download()
		{
			if (!StringUtil.IsEmpty(caseDownload))
			{
				// 文件路径
				String storePath = LipossGlobals.getLipossProperty("uploaddir");
				// 下载xls模板
				if ("0".equals(caseDownload))
				{
					bio.download(storePath + "/数图业务XLS模板.xls",
							response);
				}
				// 下载txt模板
				else
				{
					bio.download(storePath + "/数图业务TXT模板.txt",
							response);
				}
				return null;
			}
			return null;
		}
		/*
		 * 访问首页，获取限制参数（条件，时间段，开通状态）
		 */
		public String execute(){
			DateTimeUtil dt = new DateTimeUtil();
			endtime = dt.getDate();
			starttime = dt.getFirtDayOfMonth();
			dt = new DateTimeUtil(endtime);
			long end_time = dt.getLongTime();
			dt = new DateTimeUtil((end_time + 24 * 3600 - 1) * 1000);
			endtime = dt.getLongDate();
			dt = new DateTimeUtil(starttime);
			starttime = dt.getLongDate();
			return "digitQuery";
		}
		
		/*
		 * 按限制条件查询
		 */
		public String getCountAll(){
			device_idList=bio.getDao().getDevice_id(con, condition.trim());
			if(device_idList.size()==1){
				list = bio.getDetailsForPage(device_idList,starttime,endtime,openState,curPage_splitPage,num_splitPage);
				maxPage_splitPage = bio.getDetailsCount(device_idList,starttime,endtime,openState,num_splitPage);
			}
			return "list";
		}
		
		
		public VOIPDigitMapBatchServ getBio() {
			return bio;
		}
		public void setBio(VOIPDigitMapBatchServ bio) {
			this.bio = bio;
		}
		public Map getSession() {
			return session;
		}
		public void setSession(Map session) {
			this.session = session;
		}
		public List<Map> getDigitMapList() {
			return digitMapList;
		}
		public void setDigitMapList(List<Map> digitMapList) {
			this.digitMapList = digitMapList;
		}
		public UserRes getCurrent_user() {
			return current_user;
		}
		public void setCurrent_user(UserRes currentUser) {
			current_user = currentUser;
		}
		public String getAjax() {
			return ajax;
		}
		public void setAjax(String ajax) {
			this.ajax = ajax;
		}
		public Map getDigitMap() {
			return digitMap;
		}
		public void setDigitMap(Map digitMap) {
			this.digitMap = digitMap;
		}
		public String getMap_id() {
			return map_id;
		}
		public void setMap_id(String mapId) {
			map_id = mapId;
		}
		public String getMap_name() {
			return map_name;
		}
		public void setMap_name(String mapName) {
			map_name = mapName;
		}
		public String getMap_content() {
			return map_content;
		}
		public void setMap_content(String mapContent) {
			map_content = mapContent;
		}
		public String getDevice_id() {
			return device_id;
		}
		public void setDevice_id(String deviceId) {
			device_id = deviceId;
		}
		public String getGw_type() {
			return gw_type;
		}
		public void setGw_type(String gwType) {
			gw_type = gwType;
		}
		public String getTaskName() {
			return taskName;
		}
		public void setTaskName(String taskName) {
			this.taskName = taskName;
		}
		public String getCityId() {
			return cityId;
		}
		public void setCityId(String cityId) {
			this.cityId = cityId;
		}
		
		public String getFlag()
		{
			return flag;
		}
		
		public void setFlag(String flag)
		{
			this.flag = flag;
		}
		
		public String getCaseDownload()
		{
			return caseDownload;
		}
		
		public void setCaseDownload(String caseDownload)
		{
			this.caseDownload = caseDownload;
		}
		
		  public void setServletResponse(HttpServletResponse response)
		  {
		    this.response = response;
		  }

		  public void setServletRequest(HttpServletRequest request)
		  {
		  }
		
		public HttpServletResponse getResponse()
		{
			return response;
		}
		
		public void setResponse(HttpServletResponse response)
		{
			this.response = response;
		}
		
		public String getStarttime()
		{
			return starttime;
		}
		
		public void setStarttime(String starttime)
		{
			this.starttime = starttime;
		}
		
		public String getEndtime()
		{
			return endtime;
		}
		
		public void setEndtime(String endtime)
		{
			this.endtime = endtime;
		}
		
		public String getCon()
		{
			return con;
		}
		
		public void setCon(String con)
		{
			this.con = con;
		}
		
		public String getCondition()
		{
			return condition;
		}
		
		public void setCondition(String condition)
		{
			this.condition = condition;
		}
		
		public String getOpenState()
		{
			return openState;
		}
		
		public void setOpenState(String openState)
		{
			this.openState = openState;
		}
		
		public List<Map> getDevice_idList()
		{
			return device_idList;
		}
		
		public void setDevice_idList(List<Map> device_idList)
		{
			this.device_idList = device_idList;
		}
		
		public List<Map> getList()
		{
			return list;
		}
		
		public void setList(List<Map> list)
		{
			this.list = list;
		}
		  
	}
