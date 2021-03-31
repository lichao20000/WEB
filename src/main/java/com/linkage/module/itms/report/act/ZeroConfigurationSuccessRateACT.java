package com.linkage.module.itms.report.act;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.litms.common.WebUtil;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.itms.report.bio.ZeroConfigurationSuccessRateBIO;

public class ZeroConfigurationSuccessRateACT extends splitPageAction {
	private static final long serialVersionUID = 1L;

	// 日志记录
	private static Logger logger = LoggerFactory
			.getLogger(ZeroConfigurationSuccessRateACT.class);

	// 开始时间 日期型
	private String startTime = "";
	// 开始时间 转换成 秒
	private String starttime1 = "";
	// 属地ID
	private String cityId = "-1";

	// 用户列表展示
	private List<Map> hgwList = null;

	// Excel文件列表标题
	private String[] title = null;
	// Excel文件列名
	private String[] column = null;
	// 数据集
	private ArrayList<HashMap<String,String>> data = null;
	// Excel文件名
	private String fileName = "";

	/** 属地列表 */
	private List<Map<String, String>> cityList = null;

	private ZeroConfigurationSuccessRateBIO bio;

	public String ZeroConfigurationSuccessRatePage() {
		logger.debug("ZeroConfigurationSuccessRatePage()");
		if ("00".equals(WebUtil.getCurrentUser().getCityId()))
		{
	//		cityList = CityDAO.getAllNextCityListByCityPid(WebUtil.getCurrentUser().getCityId());
			cityList = CityDAO.getNextCityListByCityPid(WebUtil.getCurrentUser().getCityId());
		}
		else
		{
			cityList = new ArrayList<Map<String,String>>();
			HashMap<String,String> map = new HashMap<String,String>();
			String localId = CityDAO.getLocationCityIdByCityId(WebUtil.getCurrentUser().getCityId());
			String localName = CityDAO.getCityName(localId);
			map.put("city_name", localName);
			map.put("city_id", localId);
			cityList.add(map);
		}
		DateTimeUtil dateTimeUtil = new DateTimeUtil();
		startTime = dateTimeUtil.getDate().substring(0,7);
		return "page";
	}
	
	public String getZeroConfigurationSuccessRateByCityid() {
		logger.debug("getZeroConfigurationSuccessRateByCityid()");
		setTime();
		data = bio.getZeroConfigurationSuccessRateByCityid(startTime, cityId, "1");
		return "list";
	}

	public String getZeroConfigurationSuccessRateByCityidExcel() {
		logger.debug("getZeroConfigurationSuccessRateByCityidExcel()");
		fileName = "零配置开通成功率统计";
		title = new String[] { "属地", "新装工单数", "失败工单数", "零配置开通成功率" };
		column = new String[] { "city_name", "detotal", "nototal", "percent" };
		data = bio.getZeroConfigurationSuccessRateByCityid(startTime, cityId, "0");
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
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getStarttime1() {
		return starttime1;
	}

	public void setStarttime1(String starttime1) {
		this.starttime1 = starttime1;
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
	public ArrayList<HashMap<String, String>> getData()
	{
		return data;
	}
	public void setData(ArrayList<HashMap<String, String>> data)
	{
		this.data = data;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public ZeroConfigurationSuccessRateBIO getBio() {
		return bio;
	}

	public void setBio(ZeroConfigurationSuccessRateBIO bio) {
		this.bio = bio;
	}

	public List<Map<String, String>> getCityList() {
		return cityList;
	}

	public void setCityList(List<Map<String, String>> cityList) {
		this.cityList = cityList;
	}
}
