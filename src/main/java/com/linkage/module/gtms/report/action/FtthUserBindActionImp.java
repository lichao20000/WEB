package com.linkage.module.gtms.report.action;

import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gtms.report.serv.FtthUserBindServ;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

/**
 * 
 * @author Administrator(工号) Tel:
 * @version 1.0
 * @since Apr 25, 2012 3:02:17 PM
 * @category com.linkage.module.gtms.report.action
 * @copyright 南京联创科技 网管科技部
 *
 */
public class FtthUserBindActionImp extends splitPageAction implements SessionAware,
		FtthUserBindAction {
	
	private static Logger logger = LoggerFactory.getLogger(FtthUserBindActionImp.class);
	
	/** session */
	private Map session = null;
	
	/**  用户属地 */
	private String cityId = null;
	
	/** 开始时间 */
	private String starttime = null;
	
	private String starttime1 = null;
	
	/** 截至时间 */
	private String endtime = null;
	
	private String endtime1 = null;
	
	private String bindFlag = null;
	/**是否只统计为光纤接入的用户*/
	private String isFiber = null;
	
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
	private List<Map> FtthUserList = null;

	private FtthUserBindServ bio ;
	
	
	/**
	 * 初始化统计查询页面
	 * @return
	 */
	public String init() {
		
		logger.debug("FtthUserBindActionImp==>init()");
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
		if("1".equals(isFiber)){
			return "initFiber";
		}
		return "init";
	}
	
	
	/**
	 * 统计结果页面
	 * @return
	 */
	public String countAll() {
		
		logger.debug("FtthUserBindActionImp==>countAll()");
		
		UserRes curUser = (UserRes) session.get("curUser");
		Map<String,String> cityMap = CityDAO.getCityIdCityNameMap();
		
		logger.warn("FTTH用户绑定情况统计   操作人ID："+curUser.getUser().getId()+
				    "   统计开始时间："+starttime+"   统计结束时间："+endtime+
				    "   属地："+cityMap.get(cityId));
		
		this.setTime();
		data = bio.countAll(cityId, starttime1, endtime1,isFiber);
		
		return "allList";
	}
	
	
	/**
	 * 获取用户列表
	 * 
	 * @return
	 */
	public String getUserList() {
		
		logger.debug("FtthUserBindActionImp==>getUserList()");
		
		FtthUserList = bio.getUserList(bindFlag, cityId, starttime1, endtime1,
				curPage_splitPage, num_splitPage,isFiber);
		maxPage_splitPage = bio.getUserCount(bindFlag, cityId, starttime1, endtime1,
				curPage_splitPage, num_splitPage,isFiber);
		
		return "FtthList";
	}
	

	public String getAllBindWayExcel() {
		
		logger.debug("FtthUserBindActionImp==>getAllBindWayExcel()");
		
		fileName = "FtthBindWay";
		
		title = new String[4];
		column = new String[4];
		
		title[0] = "属地";
		title[1] = "绑定用户数";
		title[2] = "工单用户数";
		title[3] = "绑定率";
		
		column[0] = "city_name";
		column[1] = "bindNum";
		column[2] = "allBingNum";
		column[3] = "percent";
		
		data = bio.countAll(cityId, starttime1, endtime1,isFiber);
		
		return "excel";
	}
	
	
	public String getUserExcel() {
		
		logger.debug("FtthUserBindActionImp==>getUserExcel()");
		
		fileName = "FtthUserList";
		
		title = new String[8];
		column = new String[8];
		
		title[0] = "用户帐号";
		title[1] = "设备厂商";
		title[2] = "设备型号";
		title[3] = "属地";
		title[4] = "用户来源";
		title[5] = "绑定设备";
		title[6] = "开户时间";
		title[7] = "绑定方式";
		
		column[0] = "username";
		column[1] = "vendor_add";
		column[2] = "device_model";
		column[3] = "city_name";
		column[4] = "user_type";
		column[5] = "device";
		column[6] = "opendate";
		column[7] = "bindtype";
		
		data = bio.getUserExcel(bindFlag, cityId, starttime1, endtime1,isFiber);
		
		return "excel";
	}
	
	
	
	/**
	 * 时间转化
	 */
	private void setTime(){
		logger.debug("FtthUserBindActionImp==>setTime()" + starttime);
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
	
	public List<Map> getFtthUserList() {
		return FtthUserList;
	}
	
	public void setFtthUserList(List<Map> userList) {
		FtthUserList = userList;
	}
	
	public FtthUserBindServ getBio() {
		return bio;
	}

	public void setBio(FtthUserBindServ bio) {
		this.bio = bio;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}


	public String getIsFiber() {
		return isFiber;
	}


	public void setIsFiber(String isFiber) {
		this.isFiber = isFiber;
	}  
	
}
