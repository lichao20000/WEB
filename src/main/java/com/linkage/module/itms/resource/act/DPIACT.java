package com.linkage.module.itms.resource.act;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.itms.resource.bio.DPIBIO;

@SuppressWarnings("serial")
public class DPIACT extends splitPageAction implements SessionAware,
		ServletRequestAware {

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(DPIACT.class);
	private DPIBIO bio;
	@SuppressWarnings("unused")
	private HttpServletRequest request;

	@SuppressWarnings("rawtypes")
	private Map session;

	private String vendor = "";

	private String devicemodel = "";

	private String starttime = "";

	private String starttime1 = "";

	private String endtime = "";

	private String endtime1 = "";

	private String cityId = "";

	private String status = "";

	private String resultId = "";

	private List<Map<String, String>> cityList = null;

	@SuppressWarnings("rawtypes")
	private List<Map> data; // 需要导出的结果集，必须是处理过的可以直接显示的结果
	private String[] column; // 需要导出的列名，对应data中的键值
	private String[] title; // 显示在导出文档上的列名
	private String fileName; // 导出的文件名（不包括后缀名）

	/**
	 * 初始化页面数据
	 */
	@Override
	public String execute() throws Exception {
		UserRes curUser = (UserRes) session.get("curUser");
		cityId = curUser.getCityId();
		starttime = getStartDate();
		endtime = getEndDate();
		cityList = CityDAO.getNextCityListByCityPid(curUser.getCityId());
		return "init";
	}

	public String queryDPIDev() {
		setTime();
		data = bio.queryDPIList(starttime1, endtime1, cityId, vendor,
				devicemodel);
		return "list";
	}

	public String queryDPIDevExcel() {
		setTime();
		data = bio.queryDPIList(starttime1, endtime1, cityId, vendor,
				devicemodel);
		String excelCol = "city_name#vendor_id#device_model_id#allup#successnum#failnum#percent";
		String excelTitle = "本地网#厂家#设备型号#部署总数#部署成功数#失败数#部署成功率";
		column = excelCol.split("#");
		title = excelTitle.split("#");
		fileName = "部署情况统计";
		return "excel";
	}

	public String queryDetail() {
		setTime();
		data = bio
				.queryDetail(starttime1, endtime1, cityId, vendor, devicemodel,
						status, resultId, curPage_splitPage, num_splitPage);
		maxPage_splitPage = bio.countQueryDetail(starttime1, endtime1, cityId,
				vendor, devicemodel, status, resultId, curPage_splitPage,
				num_splitPage);
		return "devlist";
	}

	public String queryDetailExcel() {
		setTime();
		data = bio.excelqueryDetail(starttime1, endtime1, cityId, vendor,
				devicemodel, status, resultId);
		String excelCol ="vendor_id#device_model_id#softwareversion#city_name#device_serialnumber#fault_desc";
		String excelTitle = "设备厂商#型号#软件版本#属地#设备序列号#配置结果";
		column = excelCol.split("#");
		title = excelTitle.split("#");
		fileName = "部署情况统计详细信息";
		return "excel";
	}

	private void setTime() {
		DateTimeUtil dt = null;
		if (starttime == null || "".equals(starttime)) {
			starttime1 = null;
		} else {
			dt = new DateTimeUtil(starttime);
			starttime1 = String.valueOf(dt.getLongTime());
		}
		if (endtime == null || "".equals(endtime)) {
			endtime1 = null;
		} else {
			dt = new DateTimeUtil(endtime);
			endtime1 = String.valueOf(dt.getLongTime());
		}
	}

	// 当前年的1月1号
	private String getStartDate() {
		GregorianCalendar now = new GregorianCalendar();
		SimpleDateFormat fmtrq = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.US);
		now.set(GregorianCalendar.DATE, 1);
		now.set(GregorianCalendar.MONTH, 0);
		now.set(Calendar.HOUR_OF_DAY, 0);
		now.set(Calendar.MINUTE, 0);
		now.set(Calendar.SECOND, 0);
		String time = fmtrq.format(now.getTime());
		return time;
	}

	// 当前时间的23:59:59,如 2011-05-11 23:59:59
	private String getEndDate() {
		GregorianCalendar now = new GregorianCalendar();
		SimpleDateFormat fmtrq = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.US);
		now.set(Calendar.HOUR_OF_DAY, 23);
		now.set(Calendar.MINUTE, 59);
		now.set(Calendar.SECOND, 59);
		String time = fmtrq.format(now.getTime());
		return time;
	}

	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public DPIBIO getBio() {
		return bio;
	}

	public void setBio(DPIBIO bio) {
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

	public String[] getColumn() {
		return column;
	}

	public void setColumn(String[] column) {
		this.column = column;
	}

	public String[] getTitle() {
		return title;
	}

	public void setTitle(String[] title) {
		this.title = title;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	public String getDevicemodel() {
		return devicemodel;
	}

	public void setDevicemodel(String devicemodel) {
		this.devicemodel = devicemodel;
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

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public List<Map<String, String>> getCityList() {
		return cityList;
	}

	public void setCityList(List<Map<String, String>> cityList) {
		this.cityList = cityList;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getResultId() {
		return resultId;
	}

	public void setResultId(String resultId) {
		this.resultId = resultId;
	}

}
