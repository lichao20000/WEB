package com.linkage.module.gwms.report.act;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.linkage.module.gwms.report.bio.PingResultBIO;
import com.opensymphony.xwork2.ActionSupport;

public class PingResultACT extends ActionSupport implements SessionAware {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// 日期
	private long startDate = 0;
	// 日期
	private long endDate = 0;
	
	// 属地
	private String cityId;
	// 属地下拉列表
	private List cityList;
	
	//设备序列号
	private String devSn;
	
	/**执行状态
	 * 1表示执行成功，0表示未执行，-1表示执行失败，100全部
	 * */
	@SuppressWarnings("unused")
	private String exeStatus;

	// BIO
	private PingResultBIO pingResultBio;
	
	// session用户取登陆用户属地信息
	private Map session;
	
	//统计数据
	private String ajax;

	private String startDateSrc;
	
	private String endDateSrc;
	
	private	String deviceId;
	
	private List<Map<String, String>> pingResultList;
	
	
	/**
	 * isReport
	 */
	private String isReport = null;
	
	//excel导出相关字段
	private String[] title;
	private String[] column;
	private ArrayList<Map> data;
	private String fileName;
	
	//下载文件名
	private String pdfFileName = null;
	//标题
	private String pdfTitle = null;
	//table tile
	private String[] tbTitle = null;
	//需要取的列
	private String[] tbName = null;
	//数据
	private List<Map<String,String>> pdfListData = null;
	
	
	public String execute() {
		
		// 属地下拉列表
		pingResultList = pingResultBio.getPingResult(deviceId);
		// 返回页面
		return "success";
	}

	

	public long getStartDate() {
		return startDate;
	}

	public void setStartDate(long startDate) {
		this.startDate = startDate;
	}

	public long getEndDate() {
		return endDate;
	}

	public void setEndDate(long endDate) {
		this.endDate = endDate;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public List getCityList() {
		return cityList;
	}

	public void setCityList(List cityList) {
		this.cityList = cityList;
	}

	public String getDevSn() {
		return devSn;
	}

	public void setDevSn(String devSn) {
		this.devSn = devSn;
	}

	public String getExeStatus() {
		return exeStatus;
	}

	public void setExeStatus(String exeStatus) {
		this.exeStatus = exeStatus;
	}

	public PingResultBIO getPingResultBio() {
		return pingResultBio;
	}

	public void setPingResultBio(PingResultBIO pingResultBio) {
		this.pingResultBio = pingResultBio;
	}

	public Map getSession() {
		return session;
	}

	public void setSession(Map session) {
		this.session = session;
	}

	public String getAjax() {
		return ajax;
	}

	public void setAjax(String ajax) {
		this.ajax = ajax;
	}

	public String getStartDateSrc() {
		return startDateSrc;
	}

	public void setStartDateSrc(String startDateSrc) {
		this.startDateSrc = startDateSrc;
	}

	public String getEndDateSrc() {
		return endDateSrc;
	}

	public void setEndDateSrc(String endDateSrc) {
		this.endDateSrc = endDateSrc;
	}

	public String getIsReport() {
		return isReport;
	}

	public void setIsReport(String isReport) {
		this.isReport = isReport;
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

	public ArrayList<Map> getData() {
		return data;
	}

	public void setData(ArrayList<Map> data) {
		this.data = data;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getPdfFileName() {
		return pdfFileName;
	}

	public void setPdfFileName(String pdfFileName) {
		this.pdfFileName = pdfFileName;
	}

	public String getPdfTitle() {
		return pdfTitle;
	}

	public void setPdfTitle(String pdfTitle) {
		this.pdfTitle = pdfTitle;
	}

	public String[] getTbTitle() {
		return tbTitle;
	}

	public void setTbTitle(String[] tbTitle) {
		this.tbTitle = tbTitle;
	}

	public String[] getTbName() {
		return tbName;
	}

	public void setTbName(String[] tbName) {
		this.tbName = tbName;
	}

	public List<Map<String, String>> getPdfListData() {
		return pdfListData;
	}

	public void setPdfListData(List<Map<String, String>> pdfListData) {
		this.pdfListData = pdfListData;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}



	public List<Map<String, String>> getPingResultList() {
		return pingResultList;
	}



	public void setPingResultList(List<Map<String, String>> pingResultList) {
		this.pingResultList = pingResultList;
	}
	
	
	
}
