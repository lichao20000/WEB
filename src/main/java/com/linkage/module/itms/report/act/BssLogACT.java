package com.linkage.module.itms.report.act;

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
import com.linkage.module.itms.report.bio.BssLogBIO;

@SuppressWarnings("serial")
public class BssLogACT extends splitPageAction implements SessionAware,
		ServletRequestAware {

	private static Logger logger = LoggerFactory
			.getLogger(BssLogACT.class);
	private BssLogBIO bio;
	// session
	@SuppressWarnings({ "rawtypes", "unused" })
	private Map session = null;
	@SuppressWarnings("unused")
	private HttpServletRequest request;
	@SuppressWarnings("rawtypes")
	private List<Map> bsslogList;
	private String ajax;
	private String startOpenDate = "";
	private String startOpenDate1 = "";
	private String loid = "";
	private String bussinessacount = "";
	private String bssaccount = "";
	private String operationuser = "";

	// ********Export All Data To Excel****************
	@SuppressWarnings("rawtypes")
	private List<Map> data; // 需要导出的结果集，必须是处理过的可以直接显示的结果
	private String[] column; // 需要导出的列名，对应data中的键值
	private String[] title; // 显示在导出文档上的列名
	private String fileName; // 导出的文件名（不包括后缀名）

	public String init() {
		startOpenDate = getStartDate();
		return "init";
	}

	public String bsslogQuery() {
		setTime();
		bsslogList = bio.bsslogList(loid, bussinessacount, startOpenDate1,
				operationuser,bssaccount, curPage_splitPage, num_splitPage);
		maxPage_splitPage = bio
				.countbsslogList(loid, bussinessacount, startOpenDate1,
						operationuser,bssaccount, curPage_splitPage, num_splitPage);
		return "list";
	}

	public String getExcel() {
		setTime();
		bsslogList = bio.getcountbsslogExcel(loid, bussinessacount,
				startOpenDate1, operationuser,bssaccount);
		String excelCol = "username#serv_type_id#serv_account#oper_type#acc_loginname#occ_ip#oper_time";
		String excelTitle = "LOID#业务类型#业务账号#操作类型#操作人#操作IP#操作时间";
		column = excelCol.split("#");
		title = excelTitle.split("#");
		fileName = "模拟工单操作日志";
		data = bsslogList;
		return "excel";
	}

	/**
	 * 时间转化
	 */
	private void setTime() {
		DateTimeUtil dt = null;
		if (startOpenDate == null || "".equals(startOpenDate)) {
			startOpenDate1 = null;
		} else {
			dt = new DateTimeUtil(startOpenDate);
			startOpenDate1 = String.valueOf(dt.getLongTime());
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

	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
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

	public BssLogBIO getBio() {
		return bio;
	}

	public void setBio(BssLogBIO bio) {
		this.bio = bio;
	}

	public String getAjax() {
		return ajax;
	}

	public void setAjax(String ajax) {
		this.ajax = ajax;
	}

	public List<Map> getBsslogList() {
		return bsslogList;
	}

	public void setBsslogList(List<Map> bsslogList) {
		this.bsslogList = bsslogList;
	}

	public String getStartOpenDate() {
		return startOpenDate;
	}

	public void setStartOpenDate(String startOpenDate) {
		this.startOpenDate = startOpenDate;
	}

	public String getStartOpenDate1() {
		return startOpenDate1;
	}

	public void setStartOpenDate1(String startOpenDate1) {
		this.startOpenDate1 = startOpenDate1;
	}

	public String getLoid() {
		return loid;
	}

	public void setLoid(String loid) {
		this.loid = loid;
	}

	public String getBussinessacount() {
		return bussinessacount;
	}

	public void setBussinessacount(String bussinessacount) {
		this.bussinessacount = bussinessacount;
	}

	public String getOperationuser() {
		return operationuser;
	}

	public void setOperationuser(String operationuser) {
		this.operationuser = operationuser;
	}

	public String getBssaccount() {
		return bssaccount;
	}

	public void setBssaccount(String bssaccount) {
		this.bssaccount = bssaccount;
	}

}
