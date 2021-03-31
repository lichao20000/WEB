
package com.linkage.module.itms.report.act;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.uss.DateUtil;
import com.linkage.module.itms.report.bio.ActiveUserReportBIO;

/**
 * @author zszhao6 (Ailk No.78987)
 * @version 1.0
 * @since 2018-8-13
 * @category action.report
 * @copyright Ailk NBS-Network Mgt. RD Dept. 机顶盒每月活跃用户统计
 */
public class ActiveUserReportACT extends splitPageAction
{

	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory.getLogger(ActiveUserReportACT.class);
	private String ajax;
	private List<Map> data; // 需要导出的结果集，必须是处理过的可以直接显示的结果
	private String[] column; // 需要导出的列名，对应data中的键值
	private String[] title; // 显示在导出文档上的列名
	private String fileName; // 导出的文件名（不包括后缀名）
	private List<Map> activeUserList = null;
	private ActiveUserReportBIO bio = null;
	// add0918修改查询条件新增
	// 属地
	private String gwShare_cityId = null;
	// 厂商
	private String gwShare_vendorId = null;
	// 型号
	private String gwShare_deviceModelId = null;
	// 版本
	private String gwShare_devicetypeId = null;
	// 开始时间
	private String startOpenDate = "";
	private String startOpenDate1 = "";
	// 结束时间
	private String endOpenDate = "";
	// 结束时间
	private String endOpenDate1 = "";

	public String init(){
		//获取系统当前时间
		String dateTime = DateUtil.getNowTime("yyyy-MM-dd");
		endOpenDate = dateTime;
		DateTimeUtil befDateTime = new DateTimeUtil(dateTime);
		befDateTime.getNextMonth(-1);// 获取上个月时间
		//获取一个月之内新装设备
		startOpenDate = DateUtil.format(befDateTime.getDateTime(), "yyyy-MM-dd");
		return "init";
	}
	
	/**
	 * @return
	 */
	public String getActiveUserInfo()
	{
		logger.warn("开始统计机顶盒活跃用户");
		this.setTime();
		activeUserList = bio.getActiveUserInfo(curPage_splitPage, num_splitPage,startOpenDate1,
				endOpenDate1,gwShare_cityId,gwShare_vendorId,gwShare_deviceModelId,gwShare_devicetypeId);
		maxPage_splitPage = bio.getActiveUserCount(num_splitPage,startOpenDate1,
				endOpenDate1,gwShare_cityId,gwShare_vendorId,gwShare_deviceModelId,gwShare_devicetypeId);
		logger.warn("统计完毕，活跃用户数：" + activeUserList.size());
		return "list";
	}

	public String getActiveUserInfoExcel()
	{
		this.setTime();
		logger.debug("getActiveUserInfo()");
		fileName = "机顶盒活跃用户统计";
		title = new String[] { "机顶盒序列号", "IPTV账号", "厂家", "型号", "属地", "硬件版本", "软件版本",
				"最近一次在线时间" };
		column = new String[] { "device_serialnumber", "serv_account", "vendorName",
				"deviceModel", "cityName", "hardwareversion", "softwareversion",
				"lasttime" };
		data = bio.getActiveUserInfo4Excel(startOpenDate1,endOpenDate1,gwShare_cityId,gwShare_vendorId,gwShare_deviceModelId,gwShare_devicetypeId);
		return "excel";
	}

	public String goPage() throws Exception
	{
		this.setTime();
		activeUserList = bio.getActiveUserInfo(curPage_splitPage, num_splitPage,startOpenDate1,
				endOpenDate1,gwShare_cityId,gwShare_vendorId,gwShare_deviceModelId,gwShare_devicetypeId);
		maxPage_splitPage = bio.getActiveUserCount(num_splitPage,startOpenDate1,
				endOpenDate1,gwShare_cityId,gwShare_vendorId,gwShare_deviceModelId,gwShare_devicetypeId);
		return "list";
	}

	/**
	 * 时间转化
	 */
	private void setTime() {
		// start time
		if (StringUtil.IsEmpty(startOpenDate)) {
			startOpenDate1 = null;
		} else {
			String start = startOpenDate + " 00:00:00";
			DateTimeUtil st = new DateTimeUtil(start);
			startOpenDate1 = String.valueOf(st.getLongTime());
		}
		// end time
		if (StringUtil.IsEmpty(endOpenDate)) {
			endOpenDate1 = null;
		} else {
			String end = endOpenDate + " 23:59:59";
			DateTimeUtil et = new DateTimeUtil(end);
			endOpenDate1 = String.valueOf(et.getLongTime());
		}
	}
	/**
	 * @return the ajax
	 */
	public String getAjax()
	{
		return ajax;
	}

	/**
	 * @param ajax
	 *            the ajax to set
	 */
	public void setAjax(String ajax)
	{
		this.ajax = ajax;
	}

	/**
	 * @return the activeUserList
	 */
	public List<Map> getActiveUserList()
	{
		return activeUserList;
	}

	/**
	 * @param activeUserList
	 *            the activeUserList to set
	 */
	public void setActiveUserList(List<Map> activeUserList)
	{
		this.activeUserList = activeUserList;
	}

	/**
	 * @return the bio
	 */
	public ActiveUserReportBIO getBio()
	{
		return bio;
	}

	/**
	 * @param bio
	 *            the bio to set
	 */
	public void setBio(ActiveUserReportBIO bio)
	{
		this.bio = bio;
	}

	/**
	 * @return the column
	 */
	public String[] getColumn()
	{
		return column;
	}

	/**
	 * @param column
	 *            the column to set
	 */
	public void setColumn(String[] column)
	{
		this.column = column;
	}

	/**
	 * @return the title
	 */
	public String[] getTitle()
	{
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String[] title)
	{
		this.title = title;
	}

	/**
	 * @return the fileName
	 */
	public String getFileName()
	{
		return fileName;
	}

	/**
	 * @param fileName
	 *            the fileName to set
	 */
	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}

	/**
	 * @return the data
	 */
	public List<Map> getData()
	{
		return data;
	}

	/**
	 * @param data
	 *            the data to set
	 */
	public void setData(List<Map> data)
	{
		this.data = data;
	}

	public String getGwShare_cityId() {
		return gwShare_cityId;
	}

	public void setGwShare_cityId(String gwShare_cityId) {
		this.gwShare_cityId = gwShare_cityId;
	}

	public String getGwShare_vendorId() {
		return gwShare_vendorId;
	}

	public void setGwShare_vendorId(String gwShare_vendorId) {
		this.gwShare_vendorId = gwShare_vendorId;
	}

	public String getGwShare_deviceModelId() {
		return gwShare_deviceModelId;
	}

	public void setGwShare_deviceModelId(String gwShare_deviceModelId) {
		this.gwShare_deviceModelId = gwShare_deviceModelId;
	}

	public String getGwShare_devicetypeId() {
		return gwShare_devicetypeId;
	}

	public void setGwShare_devicetypeId(String gwShare_devicetypeId) {
		this.gwShare_devicetypeId = gwShare_devicetypeId;
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

	public String getEndOpenDate() {
		return endOpenDate;
	}

	public void setEndOpenDate(String endOpenDate) {
		this.endOpenDate = endOpenDate;
	}

	public String getEndOpenDate1() {
		return endOpenDate1;
	}

	public void setEndOpenDate1(String endOpenDate1) {
		this.endOpenDate1 = endOpenDate1;
	}
	
}
