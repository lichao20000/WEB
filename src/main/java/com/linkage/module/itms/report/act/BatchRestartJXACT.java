package com.linkage.module.itms.report.act;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.itms.report.bio.BatchRestartJXBIO;

import action.splitpage.splitPageAction;

/**
 * 
 * @author yaoli (Ailk No.)
 * @version 1.0
 * @since 2019年10月12日
 * @category com.linkage.module.itms.report.act
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class BatchRestartJXACT extends splitPageAction
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5311051406702778413L;
	private static Logger logger = LoggerFactory.getLogger(BatchRestartJXACT.class);

	private BatchRestartJXBIO bio;
	private String startTime = "";
	private String endTime = "";
	private List<Map> restartDevList = null;
	private List restartDetailList = null;
	/** 导出数据 */
	private List<HashMap<String,String>> data;
	/** 导出文件列标题 */
	private String[] title;
	/** 导出文件列 */
	private String[] column;
	/** 导出文件名 */
	private String fileName;
	 
	private String type;
	private String taskId;
	
	public String init(){
		return "init";
	}
	
	/**
	 * 查询
	 * @return
	 */
	public String qryList(){
		setTime();
		setRestartDevList(bio.qryBatchStartTask(startTime, endTime, curPage_splitPage, num_splitPage));
		int total = bio.qryBatchStartTaskCount(startTime, endTime);
		if (total % num_splitPage == 0) {
			maxPage_splitPage = total / num_splitPage;
		} else {
			maxPage_splitPage = total / num_splitPage + 1;
		}
		return "list";
	}
	
	/**
	 * 查询导出
	 * @return
	 */
	public String qryExcel(){
		setTime(); 
		fileName = "集团光宽批量重启";
		title = new String[] { "日期", "重启成功数量", "重启失败数量", "重启总数" };
		column = new String[] { "time", "succ", "fail", "total"};
		setData(bio.qryBatchStartTaskExcel(startTime, endTime));
		return "excel";
	}
	
	/**
	 * 详细信息
	 * @return
	 */
	public String qryDetail(){
		setTime();
		restartDetailList = bio.qryDetail(startTime, endTime, taskId, type, curPage_splitPage, num_splitPage); 
		int total = bio.qryDetailCount(startTime, endTime, taskId, type);
		if (total % num_splitPage == 0) {
			maxPage_splitPage = total / num_splitPage;
		} else {
			maxPage_splitPage = total / num_splitPage + 1;
		}
		return "detail";
	}
	
	
	/**
	 * 详细导出
	 * @return
	 */
	public String getDetailExcel(){
		setTime();
		fileName = "集团光宽批量重启详细";
		title = new String[] { "设备厂商", "设备型号", "软件版本","属地", "设备序列号", "设备IP","业务账号"};
		column = new String[] { "vendor_name", "device_model", "softwareversion", "city_name", "device_serialnumber","loopback_ip",
				"serv_account"};
		setData(bio.qryDetailExcel(startTime, endTime, taskId, type));
		return "excel";
	}
	
	private void setTime(){
		DateTimeUtil dt = null;// 定义DateTimeUtil
		if (startTime == null || "".equals(startTime)){
			startTime = null;
		}else{
			dt = new DateTimeUtil(startTime);
			startTime = String.valueOf(dt.getLongTime());
		}
		if (endTime == null || "".equals(endTime)){
			endTime = null;
		}else{
			dt = new DateTimeUtil(endTime);
			endTime = String.valueOf(dt.getLongTime());
		}
	}
	 
 	
	public String getEndTime()
	{
		return endTime;
	}
 
	public void setEndTime(String endTime)
	{
		this.endTime = endTime;
	}

	public String getStartTime()
	{
		return startTime;
	}

	public void setStartTime(String startTime)
	{
		this.startTime = startTime;
	}

	public String[] getTitle()
	{
		return title;
	}

	public void setTitle(String[] title)
	{
		this.title = title;
	}

	public String[] getColumn()
	{
		return column;
	}

	public void setColumn(String[] column)
	{
		this.column = column;
	}

	public String getFileName()
	{
		return fileName;
	}

	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}

	
	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public List getRestartDetailList()
	{
		return restartDetailList;
	}

	public void setRestartDetailList(List restartDetailList)
	{
		this.restartDetailList = restartDetailList;
	}
	public BatchRestartJXBIO getBio()
	{
		return bio;
	}

	
	public void setBio(BatchRestartJXBIO bio)
	{
		this.bio = bio;
	}

	public List<Map> getRestartDevList()
	{
		return restartDevList;
	}

	public void setRestartDevList(List<Map> restartDevList)
	{
		this.restartDevList = restartDevList;
	}

	public String getTaskId()
	{
		return taskId;
	}

	public void setTaskId(String taskId)
	{
		this.taskId = taskId;
	}

	public List<HashMap<String,String>> getData()
	{
		return data;
	}

	public void setData(List<HashMap<String,String>> data)
	{
		this.data = data;
	}

}
