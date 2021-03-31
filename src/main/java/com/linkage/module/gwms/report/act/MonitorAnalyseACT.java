package com.linkage.module.gwms.report.act;

import java.util.List;
import java.util.Map;

import action.splitpage.splitPageAction;

import com.linkage.litms.LipossGlobals;
import com.linkage.module.gwms.report.bio.MonitorAnalyseBIO;


/**
 * 
 * @author zzd (Ailk No.)
 * @version 1.0
 * @since 2016-12-26
 * @category com.linkage.module.gwms.report.act
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public class MonitorAnalyseACT extends splitPageAction
{
	private MonitorAnalyseBIO bio;
	private String startDate;
	private String endDate;
	private String indexType;
	private String instance;
	private String conType;
	private String ipSelect;
	private List<Map> monitorInfoList;
	private List<Map> ipMsgList;
	private List<Map> conList;
	private List<Map> data;
	// 导出文件列标题
	private String[] title;
	// 导出文件列
	private String[] column;
	// 导出文件名
	private String fileName;
	public String queryMonitorAnalyse(){
		monitorInfoList = bio.queryMonitorAnalyse(curPage_splitPage, num_splitPage, startDate, endDate, indexType, instance, ipSelect);
		data = monitorInfoList;
		maxPage_splitPage = bio.queryMonitorAnalyseCount(curPage_splitPage, num_splitPage, startDate, endDate, indexType, instance, ipSelect);
		return "list";
	}

	public String download()
	{
		data = bio.queryMonitorAnalyseForExcel(startDate, endDate, indexType, instance, ipSelect);
		if ("1".equals(indexType))
		{
			title = new String[3];
			title[0] = "设备信息";
			title[1] = "次数";
			title[2] = "日期";
			column = new String[3];
			column[0] = "device_message";
			column[1] = "report_times";
			column[2] = "count_day";
		}else if("4".equals(indexType)){
			title = new String[3];
			title[0] = "进程名";
			title[1] = "连接数";
			title[2] = "时间";
			column = new String[3];
			column[0] = "instance";
			column[1] = "value1";
			column[2] = "gettime";
		}
		else
		{
			title = new String[6];
			title[0] = "设备信息";
			title[1] = "设备序列号";
			title[2] = "厂商";
			title[3] = "型号";
			title[4] = "次数";
			title[5] = "日期";
			column = new String[6];
			
			column[0] = "device_message";
			column[1] = "device_serialnumber";
			column[2] = "vendor_add";
			column[3] = "device_model";
			column[4] = "report_times";
			column[5] = "count_day";
		}
		
		fileName = "ITMS监控分析";
		return "excel";
	}
	public String init(){
		ipMsgList = bio.queryIpMsg();
		conList = bio.getDao().queryConMsg();
		return "monitorAnalyse";
	}
	public String getStartDate()
	{
		return startDate;
	}

	public void setStartDate(String startDate)
	{
		this.startDate = startDate;
	}

	

	
	public String getEndDate()
	{
		return endDate;
	}

	
	public void setEndDate(String endDate)
	{
		this.endDate = endDate;
	}

	public String getIndexType()
	{
		return indexType;
	}

	public void setIndexType(String indexType)
	{
		this.indexType = indexType;
	}


	public MonitorAnalyseBIO getBio()
	{
		return bio;
	}


	public void setBio(MonitorAnalyseBIO bio)
	{
		this.bio = bio;
	}


	public List<Map> getMonitorInfoList()
	{
		return monitorInfoList;
	}


	public void setMonitorInfoList(List<Map> monitorInfoList)
	{
		this.monitorInfoList = monitorInfoList;
	}


	public List<Map> getData()
	{
		return data;
	}


	public void setData(List<Map> data)
	{
		this.data = data;
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

	
	public String getConType()
	{
		return conType;
	}

	
	public void setConType(String conType)
	{
		this.conType = conType;
	}

	
	

	
	
	public List<Map> getIpMsgList()
	{
		return ipMsgList;
	}

	
	public void setIpMsgList(List<Map> ipMsgList)
	{
		this.ipMsgList = ipMsgList;
	}

	public String getInstance()
	{
		return instance;
	}

	
	public void setInstance(String instance)
	{
		this.instance = instance;
	}

	
	public String getIpSelect()
	{
		return ipSelect;
	}

	
	public void setIpSelect(String ipSelect)
	{
		this.ipSelect = ipSelect;
	}

	
	public List<Map> getConList()
	{
		return conList;
	}

	
	public void setConList(List<Map> conList)
	{
		this.conList = conList;
	}

}
