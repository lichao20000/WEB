package com.linkage.module.itms.report.act;

import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.itms.report.bio.FTTHUserBindBIO;
import com.linkage.module.itms.report.bio.UserOpenBindBIO;

import action.splitpage.splitPageAction;
/**
 * 新开用户绑定终端情况 Action
 * @author zhangyu25
 *
 */
@SuppressWarnings("all")
public class UserOpenBindACT extends splitPageAction implements SessionAware {

	/** 日志记录 */
	private static Logger logger = LoggerFactory.getLogger(UserOpenBindACT.class);
	
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
	
	private UserOpenBindBIO userOpenBindBIO;

	
	/**
	 * 初始化
	 * @return
	 */
	public String init() {
		
		logger.debug("init()");
		
		UserRes curUser = (UserRes) session.get("curUser");
		long area_id = curUser.getAreaId();
		List<Map> citys = userOpenBindBIO.queryCityIdByAreaId(String.valueOf(area_id));
		cityId = "00";
		if(citys != null && citys.size() > 0) {
			cityId = StringUtil.getStringValue(citys.get(0), "city_id");
		}
		cityList = CityDAO.getNextCityListByCityPid(cityId);
		
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
	 * 新开用户绑定终端情况列表
	 * @return
	 */
	public String userOpenBindList() {
		logger.debug("userOpenBindList()");
		
		this.setTime();
		
		data = userOpenBindBIO.userOpenBindList(cityId, starttime1, endtime1, curPage_splitPage, num_splitPage);
		int count = userOpenBindBIO.userOpenBindCount(cityId, starttime1, endtime1);
		
		if (count % num_splitPage == 0){
			maxPage_splitPage = count / num_splitPage;
		}else{
			maxPage_splitPage = count / num_splitPage + 1;
		}
		
		return "list";
	}
	
	/**
	 * 新开用户绑定终端情况列表 页面跳转使用
	 * @return
	 */
	public String userOpenBindListForPage() {
		logger.debug("userOpenBindListForPage()");
		
		UserRes curUser = (UserRes) session.get("curUser");
		long area_id = curUser.getAreaId();
		
		// 页面属地下拉框 start
		List<Map> citys = userOpenBindBIO.queryCityIdByAreaId(String.valueOf(area_id));
		String city_id = "00";
		if(citys != null && citys.size() > 0) {
			city_id = StringUtil.getStringValue(citys.get(0), "city_id");
		}
		cityList = CityDAO.getNextCityListByCityPid(city_id);
		// 页面属地下拉框 end
		
		this.setTime();
		
		data = userOpenBindBIO.userOpenBindList(cityId, starttime1, endtime1, curPage_splitPage, num_splitPage);
		int count = userOpenBindBIO.userOpenBindCount(cityId, starttime1, endtime1);
		
		if (count % num_splitPage == 0){
			maxPage_splitPage = count / num_splitPage;
		}else{
			maxPage_splitPage = count / num_splitPage + 1;
		}
		
		return "listForPage";
	}
	
	/**
	 * 导出Excel
	 * @return
	 */
	public String userOpenBindListToExcel() {
		
		fileName = "新开用户绑定终端情况";
		
		title = new String[8];
		column = new String[8];
		
		title[0] = "LOID";
		title[1] = "宽带账号";
		title[2] = "属地";
		title[3] = "厂商";
		title[4] = "型号";
		title[5] = "业务开通时间";
		title[6] = "绑定时间";
		title[7] = "最近在线时间";
		
		column[0] = "loid";
		column[1] = "broadband_account";
		column[2] = "city_name";
		column[3] = "vendor_name";
		column[4] = "device_model";
		column[5] = "opendate";
		column[6] = "binddate";
		column[7] = "last_time";
		
		this.setTime();
		data = userOpenBindBIO.userOpenBindList(cityId, starttime1, endtime1, -1, -1);
		
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

	public List<Map<String, String>> getCityList() {
		return cityList;
	}

	public void setCityList(List<Map<String, String>> cityList) {
		this.cityList = cityList;
	}


	public UserOpenBindBIO getUserOpenBindBIO() {
		return userOpenBindBIO;
	}


	public void setUserOpenBindBIO(UserOpenBindBIO userOpenBindBIO) {
		this.userOpenBindBIO = userOpenBindBIO;
	}


}
