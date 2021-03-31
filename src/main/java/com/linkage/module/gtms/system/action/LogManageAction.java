
package com.linkage.module.gtms.system.action;

import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import action.splitpage.splitPageAction;
import com.linkage.module.gtms.system.obj.LogOBJ;
import com.linkage.module.gtms.system.serv.LogManageServ;

/**
 * 日志管理
 * 
 * @author Jason(3412)
 * @date 2010-12-4
 */
public class LogManageAction extends splitPageAction implements SessionAware
{

	//
	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory.getLogger(LogManageAction.class);
	
	// session
	private Map session;
	
	
	// 日志对象
	LogOBJ logObj;
	// 模块列表
	private List itemList;
	// serv
	LogManageServ logManageServ;
	// 日志查询结果List
	private List logList;

	//属地信息
	private List cityList;
	
	//导出数据data
	private List<Map> data;
	private String[] column = null;
	private String[] title = null;
	private String fileName = "userusedSystemSum";
	
	private String area_name;
	private String gr_name;
	
	public List<Map> getData() {
		return data;
	}


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


	public List getCityList() {
		return cityList;
	}


	public void setCityList(List cityList) {
		this.cityList = cityList;
	}


	/**
	 * 初始化方法
	 * 
	 */
	public String execute()
	{
		return initPage();
	}
	
	
	/**
	 * 初始化方法
	 */
	public String initPage()
	{
		logger.debug("initPage()");
//		itemList = logManageServ.queryItemList();
		queryLog();
		return "initPage";
	}

	/**
	 * 查询日志
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2010-12-4
	 * @return String
	 */
	public String queryLog()
	{
		logger.debug("queryLog()");
		itemList = logManageServ.queryItemList();
		logList = logManageServ.queryLog(curPage_splitPage, num_splitPage, logObj);
		maxPage_splitPage = logManageServ.queryLogCount(curPage_splitPage, num_splitPage, logObj);
		return "queryLog";
	}
	

	/**
	 * 查询日志,导出Excel
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2010-12-4
	 * @return String
	 */
	public String excelLog()
	{
		column=new String[]{"acc_loginname","oper_cont","usedCount","area_name","role_name"};
		title=new String[]{"用户名","功能名称","使用次数","登录域","角色"};
		logger.debug("excelLog()");
		data = logManageServ.excelLog(logObj);
		
		return "excel";
	}
	
	public String queryLogSAS()
	{
		logger.debug("queryLogSAS");
		itemList = logManageServ.queryItemList();
		logList = logManageServ.queryLogSAS(curPage_splitPage, num_splitPage, logObj);
		maxPage_splitPage = logManageServ.querySASLogCount(num_splitPage, logObj);
		return "saslog";
	}
	
	public List getItemList()
	{
		return itemList;
	}

	public void setItemList(List itemList)
	{
		this.itemList = itemList;
	}

	public void setLogManageServ(LogManageServ logManageServ)
	{
		this.logManageServ = logManageServ;
	}

	public List getLogList()
	{
		return logList;
	}

	public void setLogList(List logList)
	{
		this.logList = logList;
	}

	public LogOBJ getLogObj()
	{
		return logObj;
	}

	public void setLogObj(LogOBJ logObj)
	{
		this.logObj = logObj;
	}

	public String getArea_name() {
		return area_name;
	}


	public void setArea_name(String area_name) {
		this.area_name = area_name;
	}


	public String getGr_name() {
		return gr_name;
	}


	public void setGr_name(String gr_name) {
		this.gr_name = gr_name;
	}


	/* (non-Javadoc)
	 * @see org.apache.struts2.interceptor.SessionAware#setSession(java.util.Map)
	 */
	@Override
	public void setSession(Map arg0)
	{
		session = arg0;
		
	}
}
