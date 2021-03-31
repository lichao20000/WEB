package com.linkage.module.gwms.resource.act;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.resource.bio.BatchVoipSheetBIO;

@SuppressWarnings("rawtypes")
public class BatchVoipSheetAction extends splitPageAction implements ServletRequestAware, SessionAware {

	private static final long serialVersionUID = 1L;
	/** log */
	private static Logger logger = LoggerFactory.getLogger(BatchVoipSheetAction.class);
	// request取登陆帐号使用
	private HttpServletRequest request;
	private BatchVoipSheetBIO bio;
	// 导入文件,根据此文件解析用户账号
	private File file;
	private Map session;
	private String ajax;

	// 是否显示结果
	private String isResult;

	// 总数
	private int totalNum = 0;
	// 成功数
	private int successNum = 0;
	// 发送失败
	private int failNum = 0;
	// 格式错误
	private int formatErrorNum = 0;
	
	private List<Map<String, String>> cityList = null;
	private String starttime;
	private String endtime;
	private String cityId;
	private List<Map> data;
	/**
	 * 下发工单
	 * @return
	 */
	public String analyticSheet() {
		logger.warn("analyticSheet");
		// 解析excel文件
		List<String[]> arr = bio.analyticFile(this.file);
		// 拼装并发送工单
		bio.simulateSheet(arr);

		successNum = bio.getSuccessNum();
		totalNum = bio.getTotalNum();
		failNum = bio.getFailNum();
		formatErrorNum = bio.getFormatErrorNum();
		isResult = "1";
		return "sheetResult";
	}

	/**
	 * 统计信息
	 * @return
	 */
	public String getListInfo() {
		logger.debug("execute()");
		data = bio.getListInfo(starttime, endtime, cityId);
		return "list";
	}
	
	/**
	 * 初始化report页面
	 * 
	 */
	public String initReport() {
		logger.debug("init()");
		UserRes curUser = (UserRes) session.get("curUser");
		cityId = curUser.getCityId();
		cityList = CityDAO.getNextCityListByCityPid(curUser.getCityId());

		DateTimeUtil dt = new DateTimeUtil();
		starttime = dt.getDate();
		dt = new DateTimeUtil(starttime);
		long start_time = dt.getLongTime();
		dt = new DateTimeUtil(start_time * 1000);
		starttime = dt.getLongDate();
		dt = new DateTimeUtil((start_time + 24 * 3600 - 1) * 1000);
		endtime = dt.getLongDate();

		return "init";
	}
	
	private List<Map> devList = null;
	private String result;

	/**
	 * 获取设备
	 * @return
	 */
	public String getDev() {
		logger.debug("getDev()");
		devList = bio.getDevList(starttime, endtime, cityId, result, curPage_splitPage, num_splitPage);
		maxPage_splitPage = bio.getDevCount(starttime, endtime, cityId, result, curPage_splitPage, num_splitPage);
		return "devlist";
	}

	public List<Map> getDevList() {
		return devList;
	}

	public void setDevList(List<Map> devList) {
		this.devList = devList;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	/**
	 * 模板下载
	 * 
	 * @return
	 */
	public String downloadTemplate() {
		logger.warn("downloadTemplate");
		return "toExport";
	}

	@SuppressWarnings("unused")
	private InputStream exportExcelStream;

	public InputStream getExportExcelStream() {
		FileInputStream fio = null;
		String path = ServletActionContext.getServletContext().getRealPath("/");
		String separa = System.getProperty("file.separator");
		String filePath = path + "gwms" + separa + "resource" + separa
				+ "batchImportVoipSheet.xls";
		try
		{
			fio = new FileInputStream(filePath);
		}
		catch (FileNotFoundException e)
		{
			logger.warn("读取文件异常", e);
		}
		return fio;
	}

	public void setExportExcelStream(InputStream exportExcelStream) {
		this.exportExcelStream = exportExcelStream;
	}

	public void setServletRequest(HttpServletRequest arg0) {
		this.request = arg0;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public void setSession(Map session) {
		this.session = session;
	}

	public Map getSession() {
		return session;
	}

	public String getAjax() {
		return ajax;
	}

	public void setAjax(String ajax) {
		this.ajax = ajax;
	}

	public int getSuccessNum() {
		return successNum;
	}

	public void setSuccessNum(int successNum) {
		this.successNum = successNum;
	}

	public String getIsResult() {
		return isResult;
	}

	public void setIsResult(String isResult) {
		this.isResult = isResult;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public BatchVoipSheetBIO getBio() {
		return bio;
	}

	public void setBio(BatchVoipSheetBIO bio) {
		this.bio = bio;
	}

	public int getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
	}

	public int getFailNum() {
		return failNum;
	}

	public void setFailNum(int failNum) {
		this.failNum = failNum;
	}

	public int getFormatErrorNum() {
		return formatErrorNum;
	}

	public void setFormatErrorNum(int formatErrorNum) {
		this.formatErrorNum = formatErrorNum;
	}

	public List<Map<String, String>> getCityList() {
		return cityList;
	}

	public void setCityList(List<Map<String, String>> cityList) {
		this.cityList = cityList;
	}

	public String getStarttime() {
		return starttime;
	}

	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}

	public String getEndtime() {
		return endtime;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
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
}
