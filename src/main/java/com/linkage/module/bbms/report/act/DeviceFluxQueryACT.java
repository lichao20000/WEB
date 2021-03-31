package com.linkage.module.bbms.report.act;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.DateTimeUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.bbms.report.bio.DeviceFluxQueryBIO;

/**
 * 端口流量按设备查询
 * 
 * @author onelinesky
 * @since 2011-1-15
 */
public class DeviceFluxQueryACT implements SessionAware {

	// 日志记录
	private static Logger logger = LoggerFactory
			.getLogger(DeviceFluxQueryACT.class);

	// session
	private Map session;

	/** 查询时间 */
	private String stat_time = "";

	private String end_time = "";

	// 导出数据
	private List<Map<String, String>> data;

	// 导出文件列标题
	private String[] title;

	// 导出文件列
	private String[] column;

	// 导出文件名
	private String fileName;

	private DeviceFluxQueryBIO deviceFluxQueryBio;

	/** 查询方式 */
	private String queryType;

	/** 设备SN */
	private String deviceSN;

	/** 设备IP */
	private String loopbackIp;

	/** 用户账号 */
	private String userName;

	/**
	 * 初始化页面
	 * 
	 * @author wangsenbo
	 * @date Mar 18, 2010
	 * @param
	 * @return String
	 */
	public String init() {
		logger.debug("init()");
		DateTimeUtil dt = new DateTimeUtil();
		this.end_time = dt.getYYYY_MM_DD();
		dt.set(Calendar.DAY_OF_MONTH, 1);
		this.stat_time = dt.getYYYY_MM_DD();
		return "init";
	}

	/**
	 * 统计
	 * 
	 * @author onelinesky
	 * @date 2011-1-15
	 * @param
	 * @return String
	 */
	public String execute() {
		logger.debug("execute()");
		UserRes curUser = (UserRes) session.get("curUser");
		data = deviceFluxQueryBio.deviceFluxQuery(queryType, curUser
				.getCityId(), stat_time, stat_time, deviceSN, loopbackIp,
				userName);
		return "list";
	}

	/**
	 * 导出Excel
	 * 
	 * @author onelinesky
	 * @date 2011-1-15
	 * @param
	 * @return String
	 */
	public String getExcel() {
		logger.debug("getExcel()");
		UserRes curUser = (UserRes) session.get("curUser");
		fileName = "设备流量统计";
		title = new String[] { "设备序列号", "客户名称", "用户帐号", "采集时间", "流入字节数",
				"流出字节数" };
		column = new String[] { "device_serialnumber", "customer_name",
				"username", "collecttime", "ifinoctets", "ifoutoctets" };
		data = deviceFluxQueryBio.deviceFluxQuery(queryType, curUser
				.getCityId(), stat_time, stat_time, deviceSN, loopbackIp,
				userName);
		return "excel";
	}

	/**
	 * @return the session
	 */
	public Map getSession() {
		return session;
	}

	/**
	 * @param session
	 *            the session to set
	 */
	public void setSession(Map session) {
		this.session = session;
	}

	/**
	 * @return the stat_time
	 */
	public String getStat_time() {
		return stat_time;
	}

	/**
	 * @param stat_time
	 *            the stat_time to set
	 */
	public void setStat_time(String stat_time) {
		this.stat_time = stat_time;
	}

	/**
	 * @return the title
	 */
	public String[] getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String[] title) {
		this.title = title;
	}

	/**
	 * @return the column
	 */
	public String[] getColumn() {
		return column;
	}

	/**
	 * @param column
	 *            the column to set
	 */
	public void setColumn(String[] column) {
		this.column = column;
	}

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName
	 *            the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return the queryType
	 */
	public String getQueryType() {
		return queryType;
	}

	/**
	 * @param queryType
	 *            the queryType to set
	 */
	public void setQueryType(String queryType) {
		this.queryType = queryType;
	}

	/**
	 * @return the loopbackIp
	 */
	public String getLoopbackIp() {
		return loopbackIp;
	}

	/**
	 * @param loopbackIp
	 *            the loopbackIp to set
	 */
	public void setLoopbackIp(String loopbackIp) {
		this.loopbackIp = loopbackIp;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName
	 *            the userName to set
	 */
	public void setUserName(String userName) {
		try {
			this.userName = java.net.URLDecoder.decode(userName, "UTF-8");
		} catch (Exception e) {
			this.userName = userName;
		}
	}

	/**
	 * @return the deviceSN
	 */
	public String getDeviceSN() {
		return deviceSN;
	}

	/**
	 * @param deviceSN
	 *            the deviceSN to set
	 */
	public void setDeviceSN(String deviceSN) {
		this.deviceSN = deviceSN;
	}

	/**
	 * @return the data
	 */
	public List<Map<String, String>> getData() {
		return data;
	}

	/**
	 * @param data
	 *            the data to set
	 */
	public void setData(List<Map<String, String>> data) {
		this.data = data;
	}

	/**
	 * @return the deviceFluxQueryBio
	 */
	public DeviceFluxQueryBIO getDeviceFluxQueryBio() {
		return deviceFluxQueryBio;
	}

	/**
	 * @param deviceFluxQueryBio
	 *            the deviceFluxQueryBio to set
	 */
	public void setDeviceFluxQueryBio(DeviceFluxQueryBIO deviceFluxQueryBio) {
		this.deviceFluxQueryBio = deviceFluxQueryBio;
	}

	/**
	 * @return the end_time
	 */
	public String getEnd_time() {
		return end_time;
	}

	/**
	 * @param end_time
	 *            the end_time to set
	 */
	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}

}
