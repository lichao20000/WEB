package com.linkage.module.itms.report.act;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.litms.common.WebUtil;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.itms.report.bio.BindReportBIO;

public class BindReportAction extends splitPageAction {
	// 日志记录
	private static Logger logger = LoggerFactory
			.getLogger(BindReportAction.class);

	private static final long serialVersionUID = 2008053052050001L;

	// 开始时间 日期型
	private String startTime = "";
	// 结束时间 日期型
	private String endTime = "";
	// 开始时间 转换成 秒
	private String starttime1 = "";
	// 结束时间 转换成 秒
	private String endtime1 = "";
	// 用户终端类型
	private String usertype = "";
	// 属地ID
	private String cityId = "-1";
	// 是否已绑定 1：表示已绑定 0：表示未绑定
	private String bind_flag = "";

	// 用户列表展示
	private List<Map> hgwList = null;

	// Excel文件列表标题
	private String[] title = null;
	// Excel文件列名
	private String[] column = null;
	// 数据集
	private List<Map> data = null;
	// Excel文件名
	private String fileName = "";

	private String gw_type = "1";
	/** 属地列表 */
	private List<Map<String, String>> cityList = null;

	private BindReportBIO bio;

	/**
	 * 绑定率统计查询页面展示
	 * 
	 * @return
	 */
	public String bindPage() {
		logger.debug("bindPage()");
		cityList = CityDAO.getAllNextCityListByCityPid(WebUtil.getCurrentUser().getCityId());
		DateTimeUtil dateTimeUtil = new DateTimeUtil();
		endTime = dateTimeUtil.getDate();
		dateTimeUtil.getNextDate(-1);
		startTime = dateTimeUtil.getDate();
		return "page";
	}

	/**
	 * 绑定率统计列表页面展示
	 * 
	 * @return
	 */
	public String getBindRateByCityid() {
		logger.debug("getBindRateByCityid()");
		Map<String, String> cityMap = CityDAO.getCityIdCityNameMap();
		String userTypeName = "";
		if ("1".equals(usertype)) {
			userTypeName = "E8-B";
		} else if ("2".equals(usertype)) {
			userTypeName = "E8-C";
		}
		logger.warn("家庭网关按绑定率统计   操作人ID：" + WebUtil.getCurrentUser().getUser().getId()
				+ "   统计开始时间：" + startTime + "   统计结束时间：" + endTime + "   属地："
				+ cityMap.get(cityId) + "   用户终端类型：" + userTypeName);
		setTime();
		data = bio.getBindRateByCityid(starttime1, endtime1, cityId, usertype,
				gw_type);
		return "list";
	}

	/**
	 * 获得用户列表
	 * 
	 * @param
	 * @return String
	 */
	public String getUser() {
		logger.debug("getUser()");
		hgwList = bio.getUsersList(starttime1, endtime1, cityId, bind_flag,
				curPage_splitPage, num_splitPage, usertype, gw_type);
		maxPage_splitPage = bio.getUsersCount(starttime1, endtime1, cityId,
				bind_flag, curPage_splitPage, num_splitPage, usertype, gw_type);
		return "hgwlist";
	}

	// 绑定率统计 Excel导出
	public String getBindRateByCityidExcel() {
		logger.debug("getBindRateByCityidExcel()");
		fileName = "绑定状态统计";
		title = new String[] { "属地", "已绑定数", "未绑定数", "绑定率" };
		column = new String[] { "city_name", "detotal", "nototal", "percent" };

		data = bio.getBindRateByCityid(starttime1, endtime1, cityId, usertype,gw_type);

		return "excel";
	}

	/**
	 * 时间转化
	 */
	private void setTime() {
		logger.debug("setTime()" + startTime);
		DateTimeUtil dt = null;// 定义DateTimeUtil
		if (startTime == null || "".equals(startTime)) {
			starttime1 = null;
		} else {
			dt = new DateTimeUtil(startTime);
			starttime1 = String.valueOf(dt.getLongTime());
		}
		if (endTime == null || "".equals(endTime)) {
			endtime1 = null;
		} else {
			dt = new DateTimeUtil(endTime);
			endtime1 = String.valueOf(dt.getLongTime());
		}
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getStarttime1() {
		return starttime1;
	}

	public void setStarttime1(String starttime1) {
		this.starttime1 = starttime1;
	}

	public String getEndtime1() {
		return endtime1;
	}

	public void setEndtime1(String endtime1) {
		this.endtime1 = endtime1;
	}

	public List<Map> getHgwList() {
		return hgwList;
	}

	public void setHgwList(List<Map> hgwList) {
		this.hgwList = hgwList;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public String getBind_flag() {
		return bind_flag;
	}

	public void setBind_flag(String bind_flag) {
		this.bind_flag = bind_flag;
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

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public BindReportBIO getBio() {
		return bio;
	}

	public void setBio(BindReportBIO bio) {
		this.bio = bio;
	}

	public List<Map<String, String>> getCityList() {
		return cityList;
	}

	public void setCityList(List<Map<String, String>> cityList) {
		this.cityList = cityList;
	}

	public String getUsertype() {
		return usertype;
	}

	public void setUsertype(String usertype) {
		this.usertype = usertype;
	}

	public String getGw_type() {
		return gw_type;
	}

	public void setGw_type(String gwType) {
		gw_type = gwType;
	}

}
