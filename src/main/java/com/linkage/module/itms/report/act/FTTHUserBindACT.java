package com.linkage.module.itms.report.act;

import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.itms.report.bio.FTTHUserBindBIO;

import action.splitpage.splitPageAction;


public class FTTHUserBindACT extends splitPageAction implements SessionAware {

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
	
	
	private FTTHUserBindBIO ftthUserBindBIO;

	
	
	
	/**
	 * 初始化统计查询页面
	 * @return
	 */
	public String init() {
		
		logger.debug("inti()");
		
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
		
		logger.warn("FTTH用户绑定情况统计   操作人ID："+curUser.getUser().getId()+
				    "   统计开始时间："+starttime+"   统计结束时间："+endtime+
				    "   属地："+cityMap.get(cityId));
		
		this.setTime();
		
		data = ftthUserBindBIO.countAll(cityId, starttime1, endtime1);
		
		return "allList";
	}
	
	
	/**
	 * 获取用户列表
	 * 
	 * @return
	 */
	public String getUserList() {
		FTTHUserList = ftthUserBindBIO.getUserList(bindFlag, cityId, starttime1, endtime1, curPage_splitPage, num_splitPage);
		maxPage_splitPage = ftthUserBindBIO.getUserCount(bindFlag, cityId, starttime1, endtime1, curPage_splitPage, num_splitPage);
		
		return "FTTHList";
	}
	
	
	public String getAllBindWayExcel() {
		
		fileName = "FTTHBindWay";
		
		title = new String[5];
		column = new String[5];
		
		title[0] = "属地";
		title[1] = "全量FTTH用户";
		title[2] = "已绑定用户";
		title[3] = "未绑定用户";
		title[4] = "绑定率";
		
		column[0] = "city_name";
		column[1] = "allBingNum";
		column[2] = "bindNum";
		column[3] = "notBindNum";
		column[4] = "percent";
		
		data = ftthUserBindBIO.countAll(cityId, starttime1, endtime1);
		
		return "excel";
	}
	
	
	public String getUserExcel() {
		
		fileName = "FTTHUserList";
		
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
		
		data = ftthUserBindBIO.getUserExcel(bindFlag, cityId, starttime1, endtime1);
		
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

	public void setFTTHUserList(List<Map> userList) {
		FTTHUserList = userList;
	}

	public List<Map<String, String>> getCityList() {
		return cityList;
	}

	public void setCityList(List<Map<String, String>> cityList) {
		this.cityList = cityList;
	}


	public FTTHUserBindBIO getFtthUserBindBIO() {
		return ftthUserBindBIO;
	}


	public void setFtthUserBindBIO(FTTHUserBindBIO ftthUserBindBIO) {
		this.ftthUserBindBIO = ftthUserBindBIO;
	}


	public String getBindFlag() {
		return bindFlag;
	}


	public void setBindFlag(String bindFlag) {
		this.bindFlag = bindFlag;
	}
}
