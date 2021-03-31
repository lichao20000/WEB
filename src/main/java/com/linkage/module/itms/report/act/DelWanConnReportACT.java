package com.linkage.module.itms.report.act;

import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.itms.report.bio.DelWanConnReportBIO;

/**
 * 湖北电信ITMS+批量删除光猫内业务数据报表
 *
 */
public class DelWanConnReportACT extends splitPageAction implements SessionAware {
	 	private static final long serialVersionUID = 1L;
	 	private static Logger logger = LoggerFactory.getLogger(DelWanConnReportACT.class);
	
	 	private DelWanConnReportBIO bio;
	 	@SuppressWarnings("rawtypes")
		private Map session;
		/** 开始时间 */
		private String starttime = "";
		/** 开始时间 */
		private String starttime1 = "";
		/** 结束时间 */
		private String endtime = "";
		/** 结束时间 */
		private String endtime1 = "";
		/** 属地列表 */
		private List<Map<String, String>> cityList = null;
		/** 属地ID */
		private String cityId = "-1";
		
		// 导出数据
		@SuppressWarnings("rawtypes")
		private List<Map> data;
		// 导出文件列标题
		private String[] title;
		// 导出文件列
		private String[] column;
		// 导出文件名
		private String fileName;
		
		@SuppressWarnings("rawtypes")
		private List<Map> devList = null;
		private String status;
		
		/**
		 * 初始化页面
		 */
		public String init()
		{
			logger.debug("init()");
			UserRes curUser = (UserRes) session.get("curUser");
			cityId = curUser.getCityId();
			cityList = CityDAO.getNextCityListByCityPid(curUser.getCityId());
			DateTimeUtil dt = new DateTimeUtil();
			starttime = dt.getDate();
			dt = new DateTimeUtil(starttime);
			long start_time = dt.getLongTime();
			dt = new DateTimeUtil(start_time * 1000);
			starttime = dt.getLongDate();
			dt = new DateTimeUtil((start_time+24*3600-1) * 1000);
			endtime = dt.getLongDate();
			return "init";
		}
	
		/**
		 * 结果统计
		 */
		public String execute()
		{
			logger.debug("execute()");
			this.setTime();
			data = bio.countDelWanConnResult(starttime1,endtime1,cityId);
			return "list";
		}
	
		/**
		 * 导出excel
		 */
		public String getExcel()
		{
			logger.debug("getExcel()");
			fileName = "删除光猫内业务数据报表";
			data = bio.countDelWanConnResult(starttime1,endtime1,cityId);
			title = new String[] { "属地", "总配置数", "成功","未做", "失败", "成功率" };
			column = new String[] { "city_name", "allup", "successnum","noupnum", "failnum", "percent" };
			return "excel";
		}

		
		
		
		/**
		 * 获取设备列表
		 */
		public String getDev()
		{
			logger.debug("getDev()");
			devList = bio.getDevList(starttime1,endtime1,cityId,status,curPage_splitPage, num_splitPage);
			maxPage_splitPage = bio.getDevCount(starttime1,endtime1,cityId,status,curPage_splitPage,num_splitPage);
			
			return "devlist";
		}

		/**
		 * 设备列表导出
		 */
		public String getDevExcel()
		{
			logger.debug("getDevExcel()");
			fileName = "删除光猫内业务详细信息";
			data = bio.getDevExcel(starttime1,endtime1,cityId,status);
				title = new String[] { "属 地", "LOID", "业务类型", "vlan ID", "设备序列号", "结果描述" };
				column = new String[] { "city_name", "loid", "serv_type","vlanid","device_serialnumber","res_desc" };
			return "excel";
		}
		
		
		
		/**
		 * 时间转化
		 */
		private void setTime()
		{
			logger.debug("setTime()" + starttime);
			if(StringUtil.IsEmpty(starttime)){
				starttime1 = null;
			}else{
				DateTimeUtil dt = new DateTimeUtil(starttime);
				starttime1 = StringUtil.getStringValue(dt.getLongTime());
			}
			
			if(StringUtil.IsEmpty(endtime)){
				endtime1 = null;
			}else{
				DateTimeUtil dt = new DateTimeUtil(endtime);
				endtime1 = StringUtil.getStringValue(dt.getLongTime());
			}
		}
		@SuppressWarnings("rawtypes")
		public Map getSession() {
			return session;
		}
		@SuppressWarnings("rawtypes")
		public void setSession(Map session) {
			this.session = session;
		}
		public String getStarttime() {
			return starttime;
		}
		public void setStarttime(String starttime) {
			this.starttime = starttime;
		}
		public String getStarttime1() {
			return starttime1;
		}
		public void setStarttime1(String starttime1) {
			this.starttime1 = starttime1;
		}
		public String getEndtime() {
			return endtime;
		}
		public void setEndtime(String endtime) {
			this.endtime = endtime;
		}
		public String getEndtime1() {
			return endtime1;
		}
		public void setEndtime1(String endtime1) {
			this.endtime1 = endtime1;
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
		public static long getSerialversionuid() {
			return serialVersionUID;
		}
		public static Logger getLogger() {
			return logger;
		}

		public DelWanConnReportBIO getBio() {
			return bio;
		}

		public void setBio(DelWanConnReportBIO bio) {
			this.bio = bio;
		}

		@SuppressWarnings("rawtypes")
		public List<Map> getData() {
			return data;
		}

		@SuppressWarnings("rawtypes")
		public void setData(List<Map> data) {
			this.data = data;
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

		@SuppressWarnings("rawtypes")
		public List<Map> getDevList() {
			return devList;
		}

		@SuppressWarnings("rawtypes")
		public void setDevList(List<Map> devList) {
			this.devList = devList;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}
	
}
