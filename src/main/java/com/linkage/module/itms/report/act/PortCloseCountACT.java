
package com.linkage.module.itms.report.act;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.LipossGlobals;
import com.linkage.module.itms.report.bio.PortCloseCountBIO;
import com.linkage.module.itms.report.bio.SoftUpResultReportBIO;

import action.splitpage.splitPageAction;

/**
 * 重庆电信21和23号端口关闭统计
 * 
 */
@SuppressWarnings("unchecked")
public class PortCloseCountACT extends splitPageAction implements SessionAware
{
	// 日志记录
	private static Logger logger = LoggerFactory
			.getLogger(PortCloseCountACT.class);
	// session
	private Map session;
	/** 属地列表 */
	private List<Map<String, String>> cityList = null;
	/** 属地ID */
	private String city_id = "-1";
	private String type;//查询的什么类型的设备详情 未作/失败/成功
	private String countNum;//查询详情时直接将数量传过来
	private List<Map> countList;
	private List<Map> data = new ArrayList<Map>();; // 需要导出的结果集，必须是处理过的可以直接显示的结果
	// 导出文件列标题
	private String[] title;
	// 导出文件列
	private String[] column;
	// 导出文件名
	private String fileName;
	private PortCloseCountBIO bio;
	private List<Map> devList = null;
	private String status;
	private String resultId;
	
	/**
	 * 根据属地展示21和23端口关闭结果统计
	 */
	public String execute()
	{
		logger.debug("execute()");
		countList = bio.portCloseListByCity();
		return "countList";
	}
	
	/** 导出统计列表*/
	public String reportCountExcel() {
		logger.debug("queryDevListExcel()");
		data = bio.reportCountExcel();
		String excelCol = null;
		String excelTitle = null;
		excelTitle = "属地#总配置数#成功#未触发#失败";
		excelCol = "city_name#totalNum#succNum#unDoneNum#failNum";
		fileName = "21和23端口关闭统计表";
		title = excelTitle.split("#");
		column = excelCol.split("#");
		logger.warn("....."+data.toString());
		return "excel";
	}
	
	
	/** 详情 */
	public String queryDevList() {
		devList = bio.queryDevList(city_id, type, curPage_splitPage, num_splitPage);
		maxPage_splitPage = bio.queryDevCount(countNum, curPage_splitPage, num_splitPage);
		return "devList";
	}
	
	/** 导出设备列表*/
	public String reportDevListExcel() {
		logger.debug("queryDevListExcel()");
		data = bio.queryDevList(city_id, type, 0, 0);
		String excelCol = null;
		String excelTitle = null;
		excelTitle = "设备序列号#厂商#型号#区域#版本#执行时间#执行结果#结果描述";
		excelCol = "device_serialnumber#vendor_id#device_model_id#city_name#version#settime#result_id#result_desc";
		fileName = "21和23端口关闭统计-设备列表";
		title = excelTitle.split("#");
		column = excelCol.split("#");
		logger.warn("....."+data.toString());
		return "excel";
	}
	
	/**
	 * @return the session
	 */
	public Map getSession()
	{
		return session;
	}

	/**
	 * @param session
	 *            the session to set
	 */
	public void setSession(Map session)
	{
		this.session = session;
	}


	/**
	 * @return the cityList
	 */
	public List<Map<String, String>> getCityList()
	{
		return cityList;
	}

	/**
	 * @param cityList
	 *            the cityList to set
	 */
	public void setCityList(List<Map<String, String>> cityList)
	{
		this.cityList = cityList;
	}

	/**
	 * @return the data
	 */
	public List<Map> getCountList()
	{
		return countList;
	}

	/**
	 * @param data
	 *            the data to set
	 */
	public void setCountList(List<Map> countList)
	{
		this.countList = countList;
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
	 * @return the devList
	 */
	public List<Map> getDevList()
	{
		return devList;
	}

	/**
	 * @param devList
	 *            the devList to set
	 */
	public void setDevList(List<Map> devList)
	{
		this.devList = devList;
	}

	/**
	 * @return the status
	 */
	public String getStatus()
	{
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(String status)
	{
		this.status = status;
	}

	/**
	 * @return the resultId
	 */
	public String getResultId()
	{
		return resultId;
	}

	/**
	 * @param resultId
	 *            the resultId to set
	 */
	public void setResultId(String resultId)
	{
		this.resultId = resultId;
	}

	public PortCloseCountBIO getBio() {
		return bio;
	}


	public void setBio(PortCloseCountBIO bio) {
		this.bio = bio;
	}



	public String getCity_id() {
		return city_id;
	}



	public void setCity_id(String city_id) {
		this.city_id = city_id;
	}



	public String getType() {
		return type;
	}



	public void setType(String type) {
		this.type = type;
	}


	public String getCountNum() {
		return countNum;
	}



	public void setCountNum(String countNum) {
		this.countNum = countNum;
	}

	public List<Map> getData() {
		return data;
	}

	public void setData(List<Map> data) {
		this.data = data;
	}

}
