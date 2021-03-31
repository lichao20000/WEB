/*
 * ASIAINFO-LINKAGE TECHNOLOGY (NANJING) CO.,LTD. Copyright 1996-2012,  All rights reserved.
 * 文件名     :MonitorBIO.java
 * 创建人     :ZhangCong(zhangcong@asiainfo-linkage.com)
 * 创建日期:2015年12月1日
 */
package com.linkage.module.liposs.monitor.bio;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * [一句话功能简述]
 * </p>
 * <p>
 * [功能详细描述]
 * </p>
 * @author ZhangCong(zhangcong@asiainfo-linkage.com)
 * @version [revision], 2015年12月1日
 * @sinse webapp
 */
public interface MonitorBIO
{
    public String getAllMonitorHostListJson();
    
    public String getMonitorTypeListByHostIDJson(String host_id);
    
    public String paintMRTG(int rptType,String rptTime,String monitor_host,String monitor_type,HttpServletRequest request,String phaseStart,String phaseEnd,int width, int height,String tableWidth,String tableStyle);
    
    public String paintMRTGAll(int rptType,String rptTime,String monitor_host,String monitor_type,HttpServletRequest request,String phaseStart,String phaseEnd,int width, int height,String tableWidth,String tableStyle);

	public List<Map> getAllMonitorHostList();

	public List<Map<String, String>> getHostProgressList(String monitor_host);

	public List<Map<String, String>> getHostMonitorList(long currTime);

	public int getHourValue(long currTime);

	public List<Map> getProHistoryList(String monitor_host,
			String progress_type, String starttime, String endtime,
			int curPage_splitPage, int num_splitPage);

	public int getProHistoryCount(String monitor_host, String progress_type,
			String starttime, String endtime);

	public String getHostProgressType(String monitor_host);
	
	public List<Map<String, String>> getHostLog(String monitor_host,String progress_type);

}
