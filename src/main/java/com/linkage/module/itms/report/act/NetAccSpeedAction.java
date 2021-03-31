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
import com.linkage.module.itms.report.bio.NetAccSpeedBIO;

public class NetAccSpeedAction  extends splitPageAction implements SessionAware {

	/** 日志记录 */
	private static Logger logger = LoggerFactory.getLogger(FTTHUserBindACT.class);
	
	/** session */
	private Map session = null;
	
	/** 设备属地 */
	private String cityId = null;
	
	/** 注册时间 */
	private String starttime = null;
	
	private String starttime1 = null;
	
	/** 注册时间 */
	private String endtime = null;
	
	private String endtime1 = null;
	
	private String bindFlag = null;
	
	/** 属地列表 */
	private List<Map<String, String>> cityList = null;
	
	/** 导出数据 */
	private List<Map> data = null;
	
	/** 导出文件列标题 */
	private String[] title = null;
	
	/** 导出文件列 */
	private String[] column = null;
	
	/** 导出文件名 */
	private String fileName = null;
	
	/** 绑定用户列表 */
	private List<Map> FTTHUserList = null;  
	
	
	private NetAccSpeedBIO bio;

	
	
	
	/**
	 * 初始化统计查询页面
	 * @return
	 */
	public String init() {
		
		logger.debug("init()");
		
		UserRes curUser = (UserRes) session.get("curUser");
		cityId = curUser.getCityId();
		cityList = CityDAO.getNextCityListByCityPid(curUser.getCityId());
		
		DateTimeUtil dt = new DateTimeUtil();
		endtime = dt.getDate();  // 获取当前时间
		starttime = dt.getFirtDayOfMonth();
		
		dt = new DateTimeUtil(endtime);
		long end_time = dt.getLongTime();
		dt = new DateTimeUtil((end_time + 24 * 3600 - 1) * 1000);
		endtime = dt.getLongDate();
		
		dt = new DateTimeUtil(starttime);
		starttime = dt.getLongDate();
		
		return "init";
	}
	
	
	/**
	 * 统计结果页面
	 * @return
	 */
	public String countAll() {
		
		logger.debug("countAll()");
		
		UserRes curUser = (UserRes) session.get("curUser");
		Map<String,String> cityMap = CityDAO.getCityIdCityNameMap();
		
		logger.warn("互联网专线用户测速情况统计   操作人ID："+curUser.getUser().getId()+
				    "   统计开始时间："+starttime+"   统计结束时间："+endtime+
				    "   属地："+cityMap.get(cityId));
		
		this.setTime();
		
		data = bio.countAll(cityId, starttime1, endtime1);
		
		return "allList";
	}
	
	
	/**
	 * 获取用户列表
	 * 
	 * @return
	 */
	public String getList() {
		FTTHUserList = bio.getList(bindFlag, cityId, starttime1, endtime1, curPage_splitPage, num_splitPage);
		maxPage_splitPage = bio.getListCount(bindFlag, cityId, starttime1, endtime1, curPage_splitPage, num_splitPage);
		
		return "detailList";
	}
	
	
	public String getAllExcel() {
		
		fileName = "NetAccSpeed";
		
		title = new String[5];
		column = new String[5];
		
		title[0] = "属地";
		title[1] = "总计";
		title[2] = "达标";
		title[3] = "未达标";
		title[4] = "达标率";
		
		column[0] = "city_name";
		column[1] = "allSpeedtNum";
		column[2] = "succSpeedNum";
		column[3] = "failSpeedNum";
		column[4] = "percent";
		
		data = bio.countAll(cityId, starttime1, endtime1);
		
		return "excel";
	}
	
	
	public String getExcel() {
		
		fileName = "NetAccSpeedList";
		
		title = new String[9];
		column = new String[9];
		
		title[0] = "用户帐号";
		title[1] = "设备厂商";
		title[2] = "设备型号";
		title[3] = "软件型号";
		title[4] = "硬件型号";
		title[5] = "属地";
		title[6] = "测速时间";
		title[7] = "签约速率";
		title[8] = "测速速率";
		
		column[0] = "username";
		column[1] = "vendor_add";
		column[2] = "device_model";
		column[3] = "softversion";
		column[4] = "hardwareversion";
		column[5] = "city_name";
		column[6] = "test_time";
		column[7] = "downlink";
		column[8] = "realspeed";
		
		data = bio.getdetailExcel(bindFlag, cityId, starttime1, endtime1);
		return "excel";
	}
	
	
	
	/**
	 * 时间转化
	 */
	private void setTime(){
		logger.debug("setTime()" + starttime);
		DateTimeUtil dt = null;// 定义DateTimeUtil
		if (starttime == null || "".equals(starttime)){
			starttime1 = null;
		}else{
			dt = new DateTimeUtil(starttime);
			starttime1 = String.valueOf(dt.getLongTime());
		}
		if (endtime == null || "".equals(endtime)){
			endtime1 = null;
		}else{
			dt = new DateTimeUtil(endtime);
			endtime1 = String.valueOf(dt.getLongTime());
		}
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


	public String getBindFlag() {
		return bindFlag;
	}


	public void setBindFlag(String bindFlag) {
		this.bindFlag = bindFlag;
	}


	public List<Map<String, String>> getCityList() {
		return cityList;
	}


	public void setCityList(List<Map<String, String>> cityList) {
		this.cityList = cityList;
	}


	public List<Map> getData() {
		return data;
	}


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


	public List<Map> getFTTHUserList() {
		return FTTHUserList;
	}


	public void setFTTHUserList(List<Map> fTTHUserList) {
		FTTHUserList = fTTHUserList;
	}


	public NetAccSpeedBIO getBio() {
		return bio;
	}


	public void setBio(NetAccSpeedBIO bio) {
		this.bio = bio;
	}
	

}
