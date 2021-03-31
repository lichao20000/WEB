package com.linkage.module.gtms.report.action;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gtms.report.serv.BusinessOpenCountServ;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.StringUtil;

/**
 * 
 * @author Administrator(工号) Tel:
 * @version 1.0
 * @since 2013年1月15日 13:59:21
 * @category com.linkage.module.gtms.report.action
 * @copyright 南京联创科技 网管科技部
 *
 */
public class BusinessOpenCountActionImpl extends splitPageAction implements SessionAware,BusinessOpenCountAction {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static Logger logger = LoggerFactory.getLogger(BusinessOpenCountActionImpl.class);
	
	/** session */
	private Map session = null;
	
	/**  用户属地 */
	private String cityId = null;
	
	/** 开始时间 */
	private String starttime = null;
	
	/** 结束时间 */
	private String endtime = null;
	/**月份*/
	private String monthDate = null;
	private String start ;
	private String end;
	/**开通状态[{失败:-1},{成功:1},{未下发:0},{下发:-1&&1},{总数:0&&-1&&1}]
	 * 其中下发用2来表示，总数用3来表示，到sql转换
	 * */
	private String openStatus ="0";
	
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
	
	/** 开通用户列表 */
	private List<Map> businessOpenUserList = null;

	private BusinessOpenCountServ bio ;
	
	private String parentCityId = null;
	// 统计口径: -1:全部; 0:新装; 1:维护;
	private String selectTypeId;
	
	/**
	 * 初始化统计查询页面
	 * @return
	 */
	public String init() {
		
		logger.debug("BusinessOpenCountActionImpl==>init()");
		UserRes curUser = (UserRes) session.get("curUser");
		cityId = curUser.getCityId();
		
		cityList = CityDAO.getNextCityListByCityPid(curUser.getCityId());
		DateTimeUtil dt = new DateTimeUtil();
		
		start = dt.getFirtDayOfMonth() + " 00:00:00";
		end = new DateTimeUtil().getLongDate();
		return "init";
	}
	
	
	/**
	 * 统计结果页面
	 * @return
	 */
	public String countAll() {
		
		logger.debug("BusinessOpenCountActionImpl==>countAll()");
		
		UserRes curUser = (UserRes) session.get("curUser");
		Map<String,String> cityMap = CityDAO.getCityIdCityNameMap();
		
		logger.warn("业务开通率统计   操作人ID："+curUser.getUser().getId()+
				    "   属地："+cityMap.get(cityId));
		this.setTime();
		
		data = bio.countAll(cityId, starttime,endtime,selectTypeId);
		parentCityId = cityId;
		return "showList";
	}
	/**
	 * 下载各地区的统计情况
	 */
	public String getBusinessOpenCountExcel() {
		
		logger.debug("BusinessOpenCountActionImpl==>getBusinessOpenCountExcel()");
		this.setTime();
		fileName = "BusinessOpenCountList";
		
		title = new String[7];
		column = new String[7];
		
		title[0] = "本地网";
		title[1] = "工单总数";
		title[2] = "未下发工单总数";
		title[3] = "下发工单总数";
		title[4] = "下发成功数";
		title[5] = "下发失败数";
		title[6] = "总下发成功率";
		
		column[0] = "cityName";
		column[1] = "totalNum";
		column[2] = "notOpenNum";
		column[3] = "openNum";
		column[4] = "successOpenNum";
		column[5] = "failOpenNum";
		column[6] = "successRate";
		
		data = bio.countAll(cityId, starttime,endtime,selectTypeId);
		
		return "excel";
	}
	
	/**
	 * 获取用户列表
	 *
	 * @return
	 */
	public String getUserList() {
		
		logger.debug("BusinessOpenCountActionImpl==>getUserList()");
		logger.warn("BusinessOpenCountActionImpl==>getUserList({},{},{},{},{},{},{})",
				new Object[]{openStatus,cityId, starttime,endtime, 
				curPage_splitPage, num_splitPage,monthDate});
		this.setTime();
		businessOpenUserList = bio.getUserList(openStatus,cityId,parentCityId, starttime,endtime, 
				curPage_splitPage, num_splitPage,selectTypeId);
		maxPage_splitPage = bio.getUserCount(openStatus,cityId,parentCityId, starttime,endtime,
				curPage_splitPage, num_splitPage,selectTypeId);
		
		return "userList";
	}
	/**
	 * 下载各用户的情况统计情况
	 */
	public String getUserExcel() {
		
		logger.debug("BusinessOpenCountActionImpl==>getUserExcel()");
		this.setTime();
		fileName = "UserCountList";
		
		title = new String[4];
		column = new String[4];
		
		title[0] = "逻辑SN";
		title[1] = "属地";
		title[2] = "业务";
		title[3] = "业务开通状态";
		
		column[0] = "username";
		column[1] = "cityName";
		column[2] = "servTypeName";
		column[3] = "openStatus";
		
		data = bio.getUserListExcel(openStatus,cityId, parentCityId,starttime,endtime,selectTypeId);
		
		return "excel";
	}
	
	/**
	 * 时间转化
	 */
	private void setTime(){
		logger.debug("BusinessOpenCountActionImpl==>setTime()" + starttime);
		//该月第一天
		if(StringUtil.IsEmpty(start) && StringUtil.IsEmpty(end))
		{
			starttime = String.valueOf(new DateTimeUtil(new DateTimeUtil().getFirtDayOfMonth()).getLongTime());
			endtime   = String.valueOf(new DateTimeUtil().getLongTime());
		}
		else
		{
			starttime = String.valueOf(new DateTimeUtil(start).getLongTime());
			endtime   = String.valueOf(new DateTimeUtil(end).getLongTime());
		}
		
	}
	public Map getSession() {
		return session;
	}
	
	public void setSession(Map session) {
		this.session = session;
	}

	public void setStarttime(String starttime) {
		this.starttime = starttime;
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
	
	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public List<Map> getBusinessOpenUserList() {
		return businessOpenUserList;
	}

	public void setBusinessOpenUserList(List<Map> businessOpenUserList) {
		this.businessOpenUserList = businessOpenUserList;
	}

	public void setBio(BusinessOpenCountServ bio) {
		this.bio = bio;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}

	public String getMonthDate() {
		return monthDate;
	}
	public void setMonthDate(String monthDate) {
		this.monthDate = monthDate;
	}


	public String getStarttime() {
		return starttime;
	}


	public String getEndtime() {
		return endtime;
	}


	public BusinessOpenCountServ getBio() {
		return bio;
	}


	public String getOpenStatus() {
		return openStatus;
	}


	public void setOpenStatus(String openStatus) {
		this.openStatus = openStatus;
	}


	public String getParentCityId() {
		return parentCityId;
	}


	public void setParentCityId(String parentCityId) {
		this.parentCityId = parentCityId;
	}


	
	public String getStart()
	{
		return start;
	}


	
	public void setStart(String start)
	{
		this.start = start;
	}


	
	public String getEnd()
	{
		return end;
	}


	
	public void setEnd(String end)
	{
		this.end = end;
	}


	public String getSelectTypeId() {
		return selectTypeId;
	}


	public void setSelectTypeId(String selectTypeId) {
		this.selectTypeId = selectTypeId;
	}
	
	
}
