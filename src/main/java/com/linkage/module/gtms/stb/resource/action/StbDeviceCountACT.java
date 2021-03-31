package com.linkage.module.gtms.stb.resource.action;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.module.gtms.stb.resource.serv.StbDeviceCountBIO;
import com.linkage.module.gwms.Global;

/**
 * 江西机顶盒报表统计
 */
public class StbDeviceCountACT 
{
	private static Logger logger = LoggerFactory.getLogger(StbDeviceCountACT.class);

	private String ajax;
	private String data_type;
	private List<Map<String,String>> data;
	private String[] title;
	private String[] column;
	private String fileName;
	/**存储统计后的数据文件，供导出时用，无需再次统计*/
	private String queryTime;
	
	private StbDeviceCountBIO bio;

	//开始时间
	private String startTime;
	
	//结束时间
	private String endTime;
	
	/**
	 * 全省IPTV用户终端分布及活跃率情况
	 */
	public String stbCount()
	{
		logger.warn("StbDeviceCountACT.stbCount() 全省IPTV用户终端分布及活跃率情况");
		
		if (StringUtil.IsEmpty(queryTime))
		{
		/*	if (("jx_dx".equals(Global.instAreaShortName)
					&& startTime != null && !"".equals(startTime) && endTime != null
					&& !"".equals(endTime)))*/
			if (Global.JXDX.equals(Global.instAreaShortName)
						&& startTime != null && !"".equals(startTime))
			{
				
					data = bio.stbCount(startTime, endTime);
				
			}
			else
			{
				data = bio.stbCount();
				queryTime = bio.listToString(data, "stbCount");
				logger.warn("stbCount [{}]toQuery", queryTime);
			}
			
		}
		else
		{
			data=bio.stringToList(queryTime,"stbCount");
			logger.warn("stbCount [{}]toExcel",queryTime);
		}
		
		if("exp".equals(data_type))
		{
			fileName = "全省IPTV用户终端分布及活跃率情况";
		    title = new String[] {"单位","终端总数","其中4K机顶盒","其中非4K机顶盒","已部署探针","探针部署率","月活跃数","月活跃率"};
		    column = new String[] { "city_name", "all_num", "Y_4K_num", "N_4K_num", 
		      "is_probe_num", "probe_rate", "month_active_num", "month_active_rate" };

		    return "excel";
		}
		
		return "stb_count_list";
	}
	
	/**
	 * 全省非4K机顶盒分布报表
	 */
	public String stbN4KCount()
	{
		logger.warn("StbDeviceCountACT.stbN4KCount() 全省非4K机顶盒分布报表");
		
		if(StringUtil.IsEmpty(queryTime)){
			data=bio.stbN4KCount();
			queryTime=bio.listToString(data,"stbN4KCount");
			logger.warn("stbN4KCount [{}]toQueryTime",queryTime);
		}else{
			data=bio.stringToList(queryTime,"stbN4KCount");
			logger.warn("stbN4KCount [{}]toExcel",queryTime);
		}
		
		if("exp".equals(data_type))
		{
			fileName = "全省非4K机顶盒分布报表";
		    title = new String[] {"单位","华为","创维","中兴","烽火","长虹","百事通","杰赛","其他","小计"};
		    column = new String[] { "city_name", "hw_num", "cw_num", "zx_num", 
		      "fh_num", "ch_num", "bst_num", "js_num","other_num","city_all_num" };

		    return "excel";
		}
		
		return "stb_n4k_count_list";
	}
	
	/**
	 * 全省4K机顶盒分布报表
	 */
	public String stb4KCount()
	{
		logger.warn("StbDeviceCountACT.stb4KCount() 全省4K机顶盒分布报表");
		if(StringUtil.IsEmpty(queryTime)){
			data=bio.stb4KCount();
			queryTime=bio.listToString(data,"stb4KCount");
			logger.warn("stb4KCount [{}]toQueryTime",queryTime);
		}else{
			data=bio.stringToList(queryTime,"stb4KCount");
			logger.warn("stb4KCount [{}]toExcel",queryTime);
		}
		
		if("exp".equals(data_type))
		{
			fileName = "全省4K机顶盒分布报表";
		    title = new String[] {"单位","华为4K机顶盒数量","华为部署软探针数量","创维4K机顶盒数量","创维部署软探针数量",
		    							"中兴4K机顶盒数量","中兴部署软探针数量","烽火4K机顶盒数量","烽火部署软探针数量",
		    							"长虹4K机顶盒数量","长虹部署软探针数量","百事通4K机顶盒数量","百事通部署软探针数量",
		    							"杰赛4K机顶盒数量","杰赛部署软探针数量","其他厂家4K机顶盒数量","其他厂家部署软探针数量",
		    							"4K机顶盒数量合计","部署软探针数量合计"};
		    column = new String[] {"city_name","hw_4k_num","hw_probe_num","cw_4k_num","cw_probe_num",
		    						"zx_4k_num","zx_probe_num","fh_4k_num","fh_probe_num","ch_4k_num",
		    						"ch_probe_num","bst_4k_num","bst_probe_num","js_4k_num","js_probe_num",
		    						"other_4k_num","other_probe_num","sum_4k_num","sum_probe_num"};
			
			return "excel";
		}
		
		return "stb_4k_count_list";
	}
	

	public String getAjax() {
		return ajax;
	}

	public void setAjax(String ajax) {
		this.ajax = ajax;
	}

	public List<Map<String,String>> getData() {
		return data;
	}

	public void setData(List<Map<String,String>> data) {
		this.data = data;
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

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public StbDeviceCountBIO getBio() {
		return bio;
	}

	public void setBio(StbDeviceCountBIO bio) {
		this.bio = bio;
	}

	public String getData_type() {
		return data_type;
	}

	public void setData_type(String data_type) {
		this.data_type = data_type;
	}

	public String getQueryTime() {
		return queryTime;
	}

	public void setQueryTime(String queryTime) {
		this.queryTime = queryTime;
	}
	

	public String getStartTime()
	{
		return startTime;
	}

	
	public void setStartTime(String startTime)
	{
		this.startTime = startTime;
	}

	
	public String getEndTime()
	{
		return endTime;
	}

	
	public void setEndTime(String endTime)
	{
		this.endTime = endTime;
	}

}
