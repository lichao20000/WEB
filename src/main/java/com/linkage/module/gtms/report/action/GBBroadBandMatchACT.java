
package com.linkage.module.gtms.report.action;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.gtms.report.serv.GBBroadBandMatchBIO;

import jxl.read.biff.BiffException;

/**
 * JXDX-REQ-ITMS-20190227-WWF-001(ITMS+家庭网关页面匹配终端百兆千兆信息需求)-批注
 * 
 */
public class GBBroadBandMatchACT implements SessionAware
{
	// 日志记录
	private static Logger logger = LoggerFactory
			.getLogger(GBBroadBandMatchACT.class);
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
	private String ajax;
	//传入文件名
	private String gwShare_fileName;
	// 导出文件列标题
	private String[] title;
	// 导出文件列
	private String[] column;
	// 导出文件名
	private String fileName;
	private GBBroadBandMatchBIO bio;
	private List<Map> devList = null;
	private String status;
	private String resultId;
	
	public String execute()
	{
		logger.debug("GBBroadBandMatchACT.execute()");
		return "init";
	}
	
	/** 解析文件*/ 
	public String analysis(){
		logger.debug("GBBroadBandMatchACT.analysis()");
		int count = 0;
		try {
			count = bio.getCount(gwShare_fileName);
		} catch (FileNotFoundException e) {
			logger.warn("{}文件没找到！", gwShare_fileName);
			ajax = "1#文件没找到！";
			return "ajax";
		} catch (IOException e) {
			logger.warn("{}文件解析出错！", gwShare_fileName);
			ajax = "1#文件解析出错！";
			return "ajax";
		} catch (Exception e) {
			logger.warn("{}文件解析出错！", gwShare_fileName);
			ajax = "1#文件解析出错！";
			return "ajax";
		}

		if (count > 500){
			ajax = "1#文件行数不要超过500行";
			return "ajax";
		}
		ajax = bio.getFileName(gwShare_fileName)+".xls"; ;
		return "ajax";
	}
	
	/** 获取设备信息 */
	public String devInfo() throws FileNotFoundException, BiffException, IOException {
		logger.debug("GBBroadBandMatchACT.devInfo()");
		data = bio.devInfo(gwShare_fileName);
		String excelCol = null;
		String excelTitle = null;
		excelTitle = "oui-串码#厂家#型号#硬件版本#软件版本#百兆/千兆";
		excelCol = "oui-sn#vendor_name#device_model#hardwareversion#softwareversion#gbbroadband";
		fileName = bio.getFileName(gwShare_fileName);
		title = excelTitle.split("#");
		column = excelCol.split("#");
		return "excel";
	}
	
	/** 下载模板*/
	public String downloadTemplate(){
		logger.debug("downloadTemplate()");
		data = bio.downloadTemplate();
		String excelCol = null;
		String excelTitle = null;
		excelTitle = "设备串码#请把设备串码保存在工作表的第一列，第一列的第一行不读取";
		excelCol = "oui-sn";
		fileName = "设备串码模板";
		title = excelTitle.split("#");
		column = excelCol.split("#");
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

	public GBBroadBandMatchBIO getBio() {
		return bio;
	}


	public void setBio(GBBroadBandMatchBIO bio) {
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

	public String getGwShare_fileName() {
		return gwShare_fileName;
	}

	public void setGwShare_fileName(String gwShare_fileName) {
		this.gwShare_fileName = gwShare_fileName;
	}

	public String getAjax() {
		return ajax;
	}

	public void setAjax(String ajax) {
		this.ajax = ajax;
	}
	

}
