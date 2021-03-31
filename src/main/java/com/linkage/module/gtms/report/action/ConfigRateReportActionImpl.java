package com.linkage.module.gtms.report.action;

import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gtms.report.serv.ConfigRateReportServ;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

public class ConfigRateReportActionImpl  extends splitPageAction implements SessionAware,ConfigRateReportAction{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory.getLogger(ConfigRateReportActionImpl.class);

	// session
	private Map session;
	private String endtime = "";
	private String endtime1 = "";
	private List<Map<String, String>> cityList = null;
	private String cityId = "-1";
	private List<Map> data;

	private ConfigRateReportServ configRateReportBio;
	
	private String gw_type;
	private String[] title;
	private String[] column;
	private String fileName;
	

	/**
	 * 
	 * @author chendong
	 * @date Fer 5, 2013
	 * @param
	 * @return String
	 */
	public String init()
	{
		logger.debug("init()");
		UserRes curUser = (UserRes) session.get("curUser");
		cityId = curUser.getCityId();
		cityList = CityDAO.getNextCityListByCityPid(curUser.getCityId());
		DateTimeUtil dt = new DateTimeUtil();
//		long end_time = dt.getLongTime();
//		dt = new DateTimeUtil((end_time - 24 * 3600 + 1) * 1000);
		endtime = dt.getNextDate("day", -1);
		
		return "init";
	}
	
	/**
	 * 
	 * @author chendong
	 * @date Fer 5, 2013
	 * @param
	 * @return String
	 */
	public String execute()
	{
		logger.debug("execute()");
		this.setTime();
		data = configRateReportBio.countSuccessRate(endtime1, cityId);
		return "list";
	}
	
	
	/**
	 * @author chendong
	 * @date Fer 5, 2013
	 * @param
	 * @return String
	 */
	public String getExcel()
	{
		logger.debug("getExcel()");
//		this.setTime();
		fileName = "CONFIGRATE";
		title = new String[] { "属地", "全量", "成功量","成功率"};
		column = new String[] { "city_name", "total", "succ","rate"};
		data = configRateReportBio.countSuccessRate(endtime1, cityId);

		return "excel";
	}
	
	/**
	 */
	private void setTime()
	{
		DateTimeUtil dt = null;
		if (endtime == null || "".equals(endtime))
		{
			endtime1 = null;
		}
		else
		{
			dt = new DateTimeUtil(endtime);
			long end_time =  dt.getLongTime();
			dt = new DateTimeUtil((end_time + 24 * 3600) * 1000);
			endtime1 = String.valueOf(dt.getLongTime());
		}
	}
	
	
	public Map getSession() {
		return session;
	}
	
	public void setSession(Map session) {
		this.session = session;
	}
	public List<Map<String, String>> getCityList() {
		return cityList;
	}
	public void setCityList(List<Map<String, String>> cityList) {
		this.cityList = cityList;
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
	public String getCityId() {
		return cityId;
	}
	public void setCityId(String cityId) {
		this.cityId = cityId;
	}
	public List<Map> getData() {
		return data;
	}
	public void setData(List<Map> data) {
		this.data = data;
	}
	public ConfigRateReportServ getConfigRateReportBio() {
		return configRateReportBio;
	}
	public void setConfigRateReportBio(ConfigRateReportServ configRateReportBio) {
		this.configRateReportBio = configRateReportBio;
	}
	public String getGw_type() {
		return gw_type;
	}
	public void setGw_type(String gwType) {
		gw_type = gwType;
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
}
